package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.app.AppMode;
import com.pactorratt.alpha.config.AppConfig;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import javax.swing.JTree;
import javax.swing.SwingUtilities;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public final class MainWindow extends JFrame {

    private static final String NODE_BUDDIES = "Buddies";
    private static final String NODE_HEARD = "Heard";
    private static final String NODE_MENTIONED = "Mentioned";
    private static final String NODE_CONNECT = "<C>onnect";

    private final AppController app;

    private final JLabel modeLabel = new JLabel();
    private final JLabel tncLabel = new JLabel();
    private final JLabel callingLabel = new JLabel();
    private final JButton cancelCallingButton = new JButton("Cancel");
    private final JTextField callsignField = new JTextField(12);
    private final JButton connectButton = new JButton("Connect");
    private final JToggleButton listenToggle = new JToggleButton("Listen");
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
        });
        setSize(420, 560);
        setLocationByPlatform(true);
    }

    public boolean isListenSelected() {
        return listenToggle.isSelected();
    }

    public void setListenToggleSilently(boolean selected) {
        suppressListenCallback = true;
        listenToggle.setSelected(selected);
        suppressListenCallback = false;
    }

    public void refreshConnectionState() {
        boolean connected = app.isTncConnected();
        boolean busy = app.isTncBusy();
        tncLabel.setText(busy ? "TNC: connecting…" : (connected ? "TNC: connected" : "TNC: offline"));
        connectButton.setEnabled(connected && !busy);
        listenToggle.setEnabled(!busy);
        if (tncConnectItem != null) {
            tncConnectItem.setEnabled(!connected && !busy);
        }
        if (tncDisconnectItem != null) {
            tncDisconnectItem.setEnabled(connected || busy);
        }
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
        statusRowRevalidate();
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
        JMenuItem preview = new JMenuItem("Preview ARQ window");
        preview.addActionListener(e -> app.openPreviewArqWindow());
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> attemptExit());
        file.add(preview);
        file.addSeparator();
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
        JMenuItem debugMonitor = new JMenuItem("Debug Monitor…");
        debugMonitor.addActionListener(e -> app.openDebugMonitor());
        tncMenu.add(debugMonitor);
        JMenuItem statusMonitor = new JMenuItem("Status Monitor…");
        statusMonitor.addActionListener(e -> app.openStatusMonitor());
        tncMenu.add(statusMonitor);

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

    private void buildUi() {
        getContentPane().setBackground(UiColors.WINDOW_BG);
        setLayout(new BorderLayout(6, 6));

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.setBackground(UiColors.PANEL_BG);
        modeLabel.setFont(modeLabel.getFont().deriveFont(Font.BOLD));
        callingLabel.setVisible(false);
        cancelCallingButton.setVisible(false);
        cancelCallingButton.setToolTipText("Stop the outbound ARQ call (same as Abort: PN if Listen on, else Pt)");
        cancelCallingButton.addActionListener(e -> app.cancelOutboundCall());
        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        statusRow.setBackground(UiColors.PANEL_BG);
        statusRow.add(modeLabel);
        statusRow.add(callingLabel);
        statusRow.add(cancelCallingButton);
        top.add(statusRow);
        top.add(tncLabel);
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
        callRow.add(connectButton);
        callRow.add(listenToggle);

        connectButton.addActionListener(e -> app.requestConnect(callsignField.getText()));
        listenToggle.addActionListener(e -> {
            if (suppressListenCallback) {
                return;
            }
            app.setListenEnabled(listenToggle.isSelected());
            refreshModeLabel();
        });

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
        app.shutdown();
        dispose();
        System.exit(0);
    }
}
