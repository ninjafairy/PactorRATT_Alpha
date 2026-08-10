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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class MainWindow extends JFrame {

    private static final String NODE_BUDDIES = "Buddies";
    private static final String NODE_HEARD = "Heard";
    private static final String NODE_MENTIONED = "Mentioned";

    private final AppController app;

    private final JLabel modeLabel = new JLabel();
    private final JLabel tncLabel = new JLabel();
    private final JTextField callsignField = new JTextField(12);
    private final JButton connectButton = new JButton("Connect");
    private final JToggleButton listenToggle = new JToggleButton("Listen");
    private JMenuItem tncConnectItem;
    private JMenuItem tncDisconnectItem;

    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Stations");
    private final DefaultMutableTreeNode buddiesNode = new DefaultMutableTreeNode(NODE_BUDDIES);
    private final DefaultMutableTreeNode heardNode = new DefaultMutableTreeNode(NODE_HEARD);
    private final DefaultMutableTreeNode mentionedNode = new DefaultMutableTreeNode(NODE_MENTIONED);
    private final DefaultTreeModel treeModel = new DefaultTreeModel(root);
    private final JTree stationTree = new JTree(treeModel);

    private boolean suppressListenCallback;
    private boolean suppressExpandPersist;

    public MainWindow(AppController app) {
        super("PactorRATT_Alpha");
        this.app = app;
        root.add(buddiesNode);
        root.add(heardNode);
        root.add(mentionedNode);
        buildMenu();
        buildUi();
        loadBuddies();
        loadPlaceholderBranches();
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
        top.add(modeLabel);
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
                if (e.getClickCount() != 2) {
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
                        && !NODE_MENTIONED.equals(category)) {
                    return;
                }
                String call = String.valueOf(node.getUserObject());
                if (call.startsWith("(")) {
                    return; // placeholder text
                }
                callsignField.setText(call);
                app.requestConnect(call);
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

    private void loadPlaceholderBranches() {
        heardNode.removeAllChildren();
        mentionedNode.removeAllChildren();
        heardNode.add(new DefaultMutableTreeNode("(heard list fills from Listen monitor)"));
        mentionedNode.add(new DefaultMutableTreeNode("(mentioned patterns TBD)"));
        treeModel.reload(heardNode);
        treeModel.reload(mentionedNode);
    }

    private void applyExpandState() {
        AppConfig c = app.config();
        suppressExpandPersist = true;
        setExpanded(buddiesNode, c.isBuddiesExpanded());
        setExpanded(heardNode, c.isHeardExpanded());
        setExpanded(mentionedNode, c.isMentionedExpanded());
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
    }

    private void loadBuddies() {
        buddiesNode.removeAllChildren();
        Path file = app.configStore().buddiesFile();
        List<String> calls = new ArrayList<>();
        try {
            app.configStore().ensureBuddiesFile();
        } catch (Exception e) {
            buddiesNode.add(new DefaultMutableTreeNode("(failed to create buddies.json)"));
            treeModel.reload(buddiesNode);
            return;
        }
        try {
            String text = Files.readString(file, StandardCharsets.UTF_8).trim();
            if (text.startsWith("[")) {
                String body = text.substring(1, text.endsWith("]") ? text.length() - 1 : text.length());
                for (String part : body.split(",")) {
                    String s = part.trim();
                    if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
                        calls.add(s.substring(1, s.length() - 1).toUpperCase());
                    }
                }
            } else {
                for (String line : text.split("\\R")) {
                    String s = line.trim();
                    if (!s.isEmpty() && !s.startsWith("#")) {
                        calls.add(s.toUpperCase());
                    }
                }
            }
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
