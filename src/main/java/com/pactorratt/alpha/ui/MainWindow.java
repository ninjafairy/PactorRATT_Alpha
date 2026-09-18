package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.app.AppMode;
import com.pactorratt.alpha.config.AppConfig;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class MainWindow extends JFrame {

    private static final String NODE_BUDDIES = "Buddies";
    private static final String NODE_HEARD = "Heard";
    private static final String NODE_MENTIONED = "Mentioned";
    private static final String NODE_CONNECT = "<C>onnect";
    private static final String CONNECT_LABEL = "Begin ARQ";
    private static final String LISTEN_LABEL = "FEC/Monitor";
    private static final String LISTEN_TIP = "Opens window for sending and monitoring FEC";
    private static final String CONNECT_TNC_FIRST_TIP = "Connect to TNC first";

    private final AppController app;

    private final JLabel modeLabel = new JLabel();
    private final JLabel tncLabel = new JLabel();
    private final JLabel callingLabel = new JLabel();
    private final JButton cancelCallingButton = new JButton("Cancel");
    private JDialog callingDialog;
    private JLabel callingDialogCall;
    private JLabel callingDialogLongpath;
    private final JTextField callsignField = new JTextField(12);
    private final JButton connectButton = new PassThroughWhenDisabledButton(CONNECT_LABEL);
    private final JToggleButton listenToggle = new PassThroughWhenDisabledToggle(LISTEN_LABEL);
    /** Hit-target while the button is disabled (disabled children do not get mouse events). */
    private final JPanel connectTipWrap = tooltipWrap(connectButton);
    private final JPanel listenTipWrap = tooltipWrap(listenToggle);
    private final List<HoverTip> hoverTips = new ArrayList<>();
    private final Timer tncPulseTimer = new Timer(500, e -> pulseTncOffline());
    private boolean tncPulseBright;
    /** Session-only long-path modifier for outbound {@code PG}. Does not persist. */
    private final JCheckBox longpathToggle = new JCheckBox();
    private final JLabel mycallLabel = new JLabel();
    private JMenuItem tncConnectItem;
    private JMenuItem tncDisconnectItem;

    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Stations");
    private final DefaultMutableTreeNode buddiesNode = new DefaultMutableTreeNode(NODE_BUDDIES);
    private final DefaultMutableTreeNode heardNode = new DefaultMutableTreeNode(NODE_HEARD);
    private final DefaultMutableTreeNode mentionedNode = new DefaultMutableTreeNode(NODE_MENTIONED);
    private final DefaultMutableTreeNode connectNode = new DefaultMutableTreeNode(NODE_CONNECT);
    private final DefaultTreeModel treeModel = new DefaultTreeModel(root);
    private final JTree stationTree = new JTree(treeModel);

    private boolean suppressListenCallback;
    private boolean suppressExpandPersist;
    /** Session-only; not saved in settings.json. */
    private boolean connectExpanded = true;

    public MainWindow(AppController app) {
        super("PactorRATT_Alpha");
        this.app = app;
        root.add(buddiesNode);
        root.add(heardNode);
        root.add(mentionedNode);
        root.add(connectNode);
        tncPulseTimer.setRepeats(true);
        ToolTipManager.sharedInstance().setEnabled(true);
        ToolTipManager.sharedInstance().setInitialDelay(400);
        buildMenu();
        buildUi();
        loadBuddies();
        refreshMonitorLists();
        applyExpandState();
        refreshConnectionState();
        refreshModeLabel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                attemptExit();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                hideHoverTips();
            }
        });
        setSize(640, 560);
        setLocationByPlatform(true);
    }

    public boolean isListenSelected() {
        return listenToggle.isSelected();
    }

    /** Session-only. When true, outbound {@code PG} gets a leading {@code !} unless already present. */
    public boolean isLongpathSelected() {
        return longpathToggle.isSelected();
    }

    public void setListenToggleSilently(boolean selected) {
        suppressListenCallback = true;
        listenToggle.setSelected(selected);
        suppressListenCallback = false;
    }

    public void refreshConnectionState() {
        boolean connected = app.isTncConnected();
        boolean busy = app.isTncBusy();
        boolean online = connected && !busy;
        if (busy) {
            tncLabel.setText("TNC: connecting…");
            startTncPulse();
        } else if (connected) {
            tncPulseTimer.stop();
            tncLabel.setText("TNC: connected");
            tncLabel.setForeground(UiColors.TNC_CONNECTED);
        } else {
            tncLabel.setText("TNC: offline");
            startTncPulse();
        }
        String mycall = app.tncMycall();
        if (online && mycall != null && !mycall.isBlank()) {
            mycallLabel.setText("MYCall: " + mycall);
        } else {
            mycallLabel.setText("MYCall: connect to tnc");
        }
        connectButton.setEnabled(online);
        listenToggle.setEnabled(online);
        if (tncConnectItem != null) {
            tncConnectItem.setEnabled(!connected && !busy);
        }
        if (tncDisconnectItem != null) {
            tncDisconnectItem.setEnabled(connected || busy);
        }
    }

    private void startTncPulse() {
        if (!tncPulseTimer.isRunning()) {
            tncPulseBright = true;
            tncLabel.setForeground(UiColors.TNC_OFFLINE);
            tncPulseTimer.restart();
        }
    }

    private void pulseTncOffline() {
        tncPulseBright = !tncPulseBright;
        tncLabel.setForeground(tncPulseBright ? UiColors.TNC_OFFLINE : UiColors.TNC_OFFLINE_DIM);
    }

    public void refreshModeLabel() {
        AppMode mode = app.mode();
        modeLabel.setText("Mode: " + mode.displayName());
    }

    /**
     * Show or hide {@code Calling <call>…} plus Cancel in the top status strip.
     * {@code callsign} null/blank hides the row.
     */
    public void setCallingDisplay(String callsign) {
        boolean show = callsign != null && !callsign.isBlank();
        callingLabel.setText(show ? "Calling " + callsign.trim() + "…" : "");
        callingLabel.setVisible(show);
        cancelCallingButton.setVisible(show);
        statusRowRevalidate();
        if (show) {
            showCallingDialog(callsign.trim());
        } else {
            hideCallingDialog();
        }
    }

    /**
     * {@code <call> no answer} after {@code $50 Timeout} while calling, or the 60 s
     * local fallback. Cancel is hidden; a later {@code $50} CONNECTED still opens ARQ.
     * Cleared by {@link #setCallingDisplay}.
     */
    public void setCallNoAnswerDisplay(String callsign) {
        String call = callsign == null ? "" : callsign.trim();
        if (call.isEmpty()) {
            setCallingDisplay(null);
            return;
        }
        callingLabel.setText(call + " no answer");
        callingLabel.setVisible(true);
        cancelCallingButton.setVisible(false);
        hideCallingDialog();
        statusRowRevalidate();
    }

    private void showCallingDialog(String pgCall) {
        boolean longpath = pgCall.startsWith("!");
        String display = longpath ? pgCall.substring(1) : pgCall;
        ensureCallingDialog();
        callingDialogCall.setText("Calling: " + display);
        callingDialogLongpath.setText(longpath ? "Longpath" : " ");
        callingDialogLongpath.setVisible(longpath);
        callingDialog.pack();
        if (!callingDialog.isVisible()) {
            callingDialog.setLocationRelativeTo(this);
            callingDialog.setVisible(true);
        }
    }

    public void hideCallingDialog() {
        JDialog dialog = callingDialog;
        callingDialog = null;
        callingDialogCall = null;
        callingDialogLongpath = null;
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
        }
    }

    private void ensureCallingDialog() {
        if (callingDialog != null) {
            return;
        }
        callingDialog = new JDialog(this, "Calling", false);
        callingDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        callingDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                app.cancelOutboundCall();
            }
        });

        callingDialogCall = new JLabel("Calling: ");
        callingDialogCall.setFont(callingDialogCall.getFont().deriveFont(Font.BOLD, 16f));
        callingDialogLongpath = new JLabel("Longpath");
        callingDialogLongpath.setVisible(false);

        JButton abort = new JButton("Abort");
        abort.setToolTipText("Stop the outbound ARQ call (PN if Listen on, else Pt)");
        abort.addActionListener(e -> app.cancelOutboundCall());

        JPanel text = new JPanel(new GridLayout(0, 1, 0, 6));
        text.setOpaque(false);
        text.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));
        text.add(callingDialogCall);
        text.add(callingDialogLongpath);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.setOpaque(false);
        buttons.setBorder(BorderFactory.createEmptyBorder(4, 16, 12, 16));
        buttons.add(abort);

        callingDialog.getContentPane().setBackground(UiColors.PANEL_BG);
        callingDialog.setLayout(new BorderLayout());
        callingDialog.add(text, BorderLayout.CENTER);
        callingDialog.add(buttons, BorderLayout.SOUTH);
        callingDialog.setResizable(false);
    }

    private void statusRowRevalidate() {
        if (callingLabel.getParent() != null) {
            callingLabel.getParent().revalidate();
            callingLabel.getParent().repaint();
        }
    }

    private void buildMenu() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> attemptExit());
        file.add(exit);

        JMenu settings = new JMenu("Settings");
        JMenuItem com = new JMenuItem("COM Port…");
        com.addActionListener(e -> {
            try {
                ComPortDialog dialog = new ComPortDialog(this, app);
                dialog.setVisible(true);
            } catch (Throwable t) {
                JOptionPane.showMessageDialog(this,
                        "Could not open COM Port settings:\n" + t.getMessage(),
                        "Settings — COM Port",
                        JOptionPane.ERROR_MESSAGE);
            }
            refreshConnectionState();
        });
        JMenuItem program = new JMenuItem("Program…");
        program.addActionListener(e -> {
            ProgramSettingsDialog dialog = new ProgramSettingsDialog(this, app);
            dialog.setVisible(true);
        });
        JMenuItem tnc = new JMenuItem("TNC…");
        tnc.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "TNC parameter editor is planned for a later phase.\n"
                        + "Alpha will use a coded init sequence after Host open.",
                "Settings — TNC",
                JOptionPane.INFORMATION_MESSAGE));
        settings.add(com);
        settings.add(program);
        settings.add(tnc);

        JMenu tncMenu = new JMenu("TNC");
        tncConnectItem = new JMenuItem("Connect");
        tncConnectItem.addActionListener(e -> app.connectTnc());
        tncDisconnectItem = new JMenuItem("Disconnect");
        tncDisconnectItem.addActionListener(e -> app.disconnectTnc());
        tncMenu.add(tncConnectItem);
        tncMenu.add(tncDisconnectItem);
        tncMenu.addSeparator();

        JMenu devTools = new JMenu("Dev Tools");
        JMenuItem debugMonitor = new JMenuItem("Debug Monitor…");
        debugMonitor.addActionListener(e -> app.openDebugMonitor());
        devTools.add(debugMonitor);
        JMenuItem statusMonitor = new JMenuItem("Status Monitor…");
        statusMonitor.addActionListener(e -> app.openStatusMonitor());
        devTools.add(statusMonitor);
        JMenuItem ubit10Monitor = new JMenuItem("UBIT 10…");
        ubit10Monitor.addActionListener(e -> app.openUbit10Monitor());
        devTools.add(ubit10Monitor);
        JMenuItem displayMonitor = new JMenuItem("Display…");
        displayMonitor.addActionListener(e -> app.openDisplayMonitor());
        devTools.add(displayMonitor);
        devTools.addSeparator();
        JMenuItem previewArq = new JMenuItem("Preview ARQ window");
        previewArq.addActionListener(e -> app.openPreviewArqWindow());
        devTools.add(previewArq);
        JMenuItem previewFec = new JMenuItem("Preview FEC window");
        previewFec.addActionListener(e -> app.openPreviewFecWindow());
        devTools.add(previewFec);
        tncMenu.add(devTools);
        openSubmenuOnHover(bar, tncMenu, devTools);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> AboutDialog.show(this));
        help.add(about);

        bar.add(file);
        bar.add(settings);
        bar.add(tncMenu);
        bar.add(help);
        setJMenuBar(bar);
    }

    /** Nested menus already hover-open after a delay; this opens Dev Tools immediately. */
    private static void openSubmenuOnHover(JMenuBar bar, JMenu parent, JMenu submenu) {
        submenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[] {
                        bar,
                        parent,
                        parent.getPopupMenu(),
                        submenu,
                        submenu.getPopupMenu()
                });
            }
        });
    }

    private void buildUi() {
        getContentPane().setBackground(UiColors.WINDOW_BG);
        setLayout(new BorderLayout(6, 6));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UiColors.PANEL_BG);
        modeLabel.setFont(modeLabel.getFont().deriveFont(Font.BOLD));
        mycallLabel.setFont(modeLabel.getFont());
        mycallLabel.setText("MYCall: connect to tnc");
        callingLabel.setVisible(false);
        cancelCallingButton.setVisible(false);
        cancelCallingButton.setToolTipText("Stop the outbound ARQ call (same as Abort: PN if Listen on, else Pt)");
        cancelCallingButton.addActionListener(e -> app.cancelOutboundCall());
        JPanel statusRow = new JPanel(new BorderLayout());
        statusRow.setBackground(UiColors.PANEL_BG);
        JPanel statusLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        statusLeft.setOpaque(false);
        statusLeft.add(modeLabel);
        statusLeft.add(callingLabel);
        statusLeft.add(cancelCallingButton);
        JPanel statusRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        statusRight.setOpaque(false);
        statusRight.add(mycallLabel);
        statusRow.add(statusLeft, BorderLayout.WEST);
        statusRow.add(statusRight, BorderLayout.EAST);
        top.add(statusRow, BorderLayout.CENTER);
        top.setBorder(BorderFactory.createEmptyBorder(6, 8, 4, 8));

        stationTree.setRootVisible(false);
        stationTree.setShowsRootHandles(true);
        stationTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        renderer.setLeafIcon(null);
        stationTree.setCellRenderer(renderer);
        stationTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2 || e.isPopupTrigger()) {
                    return;
                }
                TreePath path = stationTree.getPathForLocation(e.getX(), e.getY());
                if (path == null) {
                    return;
                }
                Object last = path.getLastPathComponent();
                if (!(last instanceof DefaultMutableTreeNode node) || !node.isLeaf()) {
                    return;
                }
                Object parent = node.getParent();
                if (!(parent instanceof DefaultMutableTreeNode parentNode)) {
                    return;
                }
                String category = String.valueOf(parentNode.getUserObject());
                if (!NODE_BUDDIES.equals(category)
                        && !NODE_HEARD.equals(category)
                        && !NODE_MENTIONED.equals(category)
                        && !NODE_CONNECT.equals(category)) {
                    return;
                }
                String call = String.valueOf(node.getUserObject());
                if (call.startsWith("(")) {
                    return; // placeholder text
                }
                callsignField.setText(call);
                app.requestConnect(call);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowStationPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowStationPopup(e);
            }
        });
        stationTree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                persistExpandState();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                persistExpandState();
            }
        });

        JScrollPane treeScroll = new JScrollPane(stationTree);
        treeScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 8, 4, 8),
                BorderFactory.createTitledBorder("Stations")));

        JPanel bottom = new JPanel(new BorderLayout(4, 4));
        bottom.setBackground(UiColors.PANEL_BG);
        bottom.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));

        JPanel callRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        callRow.setBackground(UiColors.PANEL_BG);
        callRow.add(new JLabel("Callsign:"));
        callsignField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        callRow.add(callsignField);
        callRow.add(connectTipWrap);
        callRow.add(listenTipWrap);
        JLabel lpLabel = new JLabel("Use Longpath");
        longpathToggle.setOpaque(false);
        longpathToggle.setToolTipText("Long path: prefix ! on PG unless already typed. Session only; does not change the callsign field.");
        callRow.add(lpLabel);
        callRow.add(longpathToggle);
        tncLabel.setFont(tncLabel.getFont().deriveFont(Font.BOLD));
        callRow.add(tncLabel);

        connectButton.addActionListener(e -> app.requestConnect(callsignField.getText()));
        listenToggle.addActionListener(e -> {
            if (suppressListenCallback) {
                return;
            }
            app.setListenEnabled(listenToggle.isSelected());
            refreshModeLabel();
        });
        installHoverTip(connectButton, this::beginArqTip);
        installHoverTip(connectTipWrap, this::beginArqTip);
        installHoverTip(listenToggle, this::fecMonitorTip);
        installHoverTip(listenTipWrap, this::fecMonitorTip);

        bottom.add(callRow, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(treeScroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    /** Heard / Mentioned / &lt;C&gt;onnect leaves; most recent first. Double-click still Connects. */
    public void refreshMonitorLists() {
        fillCallBranch(heardNode, app.heardCalls());
        fillCallBranch(mentionedNode, app.mentionedCalls());
        fillCallBranch(connectNode, app.connectCalls());
        SwingUtilities.invokeLater(this::applyExpandState);
    }

    public void refreshBuddies() {
        loadBuddies();
    }

    private void maybeShowStationPopup(MouseEvent e) {
        if (!e.isPopupTrigger()) {
            return;
        }
        TreePath path = stationTree.getPathForLocation(e.getX(), e.getY());
        if (path == null) {
            return;
        }
        stationTree.setSelectionPath(path);
        Object last = path.getLastPathComponent();
        if (!(last instanceof DefaultMutableTreeNode node)) {
            return;
        }
        JPopupMenu menu = stationPopupFor(node);
        if (menu != null) {
            menu.show(stationTree, e.getX(), e.getY());
        }
    }

    private JPopupMenu stationPopupFor(DefaultMutableTreeNode node) {
        String label = String.valueOf(node.getUserObject());
        if (NODE_HEARD.equals(label)) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem clear = new JMenuItem("Clear");
            clear.addActionListener(ev -> app.clearHeardList());
            menu.add(clear);
            return menu;
        }
        if (NODE_MENTIONED.equals(label)) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem clear = new JMenuItem("Clear");
            clear.addActionListener(ev -> app.clearMentionedList());
            menu.add(clear);
            return menu;
        }
        if (NODE_CONNECT.equals(label)) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem clear = new JMenuItem("Clear");
            clear.addActionListener(ev -> app.clearConnectList());
            menu.add(clear);
            return menu;
        }
        Object parent = node.getParent();
        if (!(parent instanceof DefaultMutableTreeNode parentNode) || !node.isLeaf()) {
            return null;
        }
        if (label.startsWith("(")) {
            return null;
        }
        String category = String.valueOf(parentNode.getUserObject());
        if (NODE_HEARD.equals(category)) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem addBuddy = new JMenuItem("Add buddy");
            addBuddy.addActionListener(ev -> app.addBuddy(label));
            JMenuItem clear = new JMenuItem("Clear");
            clear.addActionListener(ev -> app.clearHeardCall(label));
            menu.add(addBuddy);
            menu.add(clear);
            return menu;
        }
        if (NODE_MENTIONED.equals(category)) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem addBuddy = new JMenuItem("Add buddy");
            addBuddy.addActionListener(ev -> app.addBuddy(label));
            JMenuItem clear = new JMenuItem("Clear");
            clear.addActionListener(ev -> app.clearMentionedCall(label));
            menu.add(addBuddy);
            menu.add(clear);
            return menu;
        }
        if (NODE_CONNECT.equals(category)) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem addBuddy = new JMenuItem("Add buddy");
            addBuddy.addActionListener(ev -> app.addBuddy(label));
            JMenuItem clear = new JMenuItem("Clear");
            clear.addActionListener(ev -> app.clearConnectCall(label));
            menu.add(addBuddy);
            menu.add(clear);
            return menu;
        }
        if (NODE_BUDDIES.equals(category)) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem top = new JMenuItem("Move to top");
            top.addActionListener(ev -> app.moveBuddyToTop(label));
            JMenuItem remove = new JMenuItem("Remove");
            remove.addActionListener(ev -> app.removeBuddy(label));
            menu.add(top);
            menu.add(remove);
            return menu;
        }
        return null;
    }

    private void fillCallBranch(DefaultMutableTreeNode branch, List<String> calls) {
        branch.removeAllChildren();
        if (calls == null || calls.isEmpty()) {
            treeModel.reload(branch);
            return;
        }
        for (String call : calls) {
            branch.add(new DefaultMutableTreeNode(call));
        }
        treeModel.reload(branch);
    }

    private void applyExpandState() {
        AppConfig c = app.config();
        suppressExpandPersist = true;
        setExpanded(buddiesNode, c.isBuddiesExpanded());
        setExpanded(heardNode, c.isHeardExpanded());
        setExpanded(mentionedNode, c.isMentionedExpanded());
        setExpanded(connectNode, connectExpanded);
        suppressExpandPersist = false;
    }

    private void setExpanded(DefaultMutableTreeNode node, boolean expanded) {
        TreePath path = new TreePath(node.getPath());
        if (expanded) {
            stationTree.expandPath(path);
        } else {
            stationTree.collapsePath(path);
        }
    }

    private void persistExpandState() {
        if (suppressExpandPersist) {
            return;
        }
        AppConfig c = app.config();
        c.setBuddiesExpanded(stationTree.isExpanded(new TreePath(buddiesNode.getPath())));
        c.setHeardExpanded(stationTree.isExpanded(new TreePath(heardNode.getPath())));
        c.setMentionedExpanded(stationTree.isExpanded(new TreePath(mentionedNode.getPath())));
        connectExpanded = stationTree.isExpanded(new TreePath(connectNode.getPath()));
    }

    private void loadBuddies() {
        buddiesNode.removeAllChildren();
        try {
            app.configStore().ensureBuddiesFile();
            List<String> calls = app.configStore().loadBuddyList();
            if (calls.isEmpty()) {
                buddiesNode.add(new DefaultMutableTreeNode("(no buddies yet)"));
            } else {
                for (String call : calls) {
                    buddiesNode.add(new DefaultMutableTreeNode(call));
                }
            }
        } catch (Exception e) {
            buddiesNode.add(new DefaultMutableTreeNode("(failed to read buddies.json)"));
        }
        treeModel.reload(buddiesNode);
        SwingUtilities.invokeLater(this::applyExpandState);
    }

    private void attemptExit() {
        if (app.hasActiveArq()) {
            Object[] options = {"Abort", "Disconnect", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this,
                    "An ARQ link is active. Abort, disconnect, or cancel exit?",
                    "Exit PactorRATT_Alpha",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[2]);
            if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }
        persistExpandState();
        tncPulseTimer.stop();
        hideHoverTips();
        app.shutdown();
        dispose();
        System.exit(0);
    }

    private String beginArqTip() {
        return connectButton.isEnabled() ? null : CONNECT_TNC_FIRST_TIP;
    }

    private String fecMonitorTip() {
        return listenToggle.isEnabled() ? LISTEN_TIP : CONNECT_TNC_FIRST_TIP;
    }

    private void installHoverTip(JComponent host, Supplier<String> text) {
        hoverTips.add(new HoverTip(host, text));
    }

    private void hideHoverTips() {
        for (HoverTip tip : hoverTips) {
            tip.hide();
        }
    }

    private static JPanel tooltipWrap(JComponent child) {
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.setOpaque(false);
        wrap.add(child);
        return wrap;
    }

    /**
     * Disabled Swing buttons drop mouse events, so hover never reaches them.
     * Returning false from {@code contains} lets the wrap receive the hover instead.
     */
    private static final class PassThroughWhenDisabledButton extends JButton {
        PassThroughWhenDisabledButton(String text) {
            super(text);
        }

        @Override
        public boolean contains(int x, int y) {
            return isEnabled() && super.contains(x, y);
        }
    }

    private static final class PassThroughWhenDisabledToggle extends JToggleButton {
        PassThroughWhenDisabledToggle(String text) {
            super(text);
        }

        @Override
        public boolean contains(int x, int y) {
            return isEnabled() && super.contains(x, y);
        }
    }

    /** Shows a tooltip on mouse-enter without relying on ToolTipManager mouse delivery. */
    private static final class HoverTip {
        private static final int DELAY_MS = 400;
        private final JComponent host;
        private final Supplier<String> text;
        private final Timer delay;
        private Popup popup;

        HoverTip(JComponent host, Supplier<String> text) {
            this.host = host;
            this.text = text;
            delay = new Timer(DELAY_MS, e -> show());
            delay.setRepeats(false);
            host.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    delay.restart();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hide();
                }
            });
        }

        private void show() {
            hidePopup();
            String t = text.get();
            if (t == null || t.isBlank() || !host.isShowing()) {
                return;
            }
            JToolTip tip = host.createToolTip();
            tip.setTipText(t);
            Point loc = host.getLocationOnScreen();
            popup = PopupFactory.getSharedInstance().getPopup(
                    host, tip, loc.x, loc.y + host.getHeight() + 2);
            popup.show();
        }

        void hide() {
            delay.stop();
            hidePopup();
        }

        private void hidePopup() {
            if (popup != null) {
                popup.hide();
                popup = null;
            }
        }
    }
}
