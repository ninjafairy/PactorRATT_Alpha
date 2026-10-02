package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.config.CommitMode;
import com.pactorratt.alpha.config.MacroFile;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.Scrollable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Listen or ARQ connection window: transcript, App TX buffer, Compose, controls, status.
 */
public final class ConnectionWindow extends JFrame {

    public enum Kind {
        LISTEN,
        ARQ
    }

    private static final char BACKSPACE = 0x08;
    private static final Color MAILBOX_RED = Color.RED;
    private static final Color MAILBOX_RED_DIM = new Color(0x8B0000);
    private static final Color ABORT_FAINT_RED = new Color(255, 220, 220);
    private static final Color MAILBOX_PURPLE = new Color(0xD8, 0xB4, 0xFE);
    private static final int MAILBOX_FLASH_MS = 500;
    private static final int MAILBOX_HIDE_MS = 3000;

    private final AppController app;
    private final Kind kind;
    private final String titleCall;

    private final JTextPane transcript = new JTextPane();
    private final JTextArea appTxBuffer = new JTextArea();
    private final JTextArea compose = new JTextArea(3, 40);
    private final JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private final JLabel statusPrefix = new JLabel();
    private final JLabel statusSuffix = new JLabel();
    private final TxChip txChip = new TxChip();
    private final JLabel noticeLabel = new JLabel(" ");
    private final JButton sendButton = new JButton("Send");
    /** ARQ only: TMail warning under Send. Null on Listen windows. */
    private JButton mailboxButton;
    private Timer mailboxFlashTimer;
    private Timer mailboxHideTimer;
    private boolean mailboxFlashLit;
    private boolean mailboxUiClosed;
    private final List<JButton> controlButtons = new ArrayList<>();
    private JButton abortButton;
    /** Listen only. Chosen on the window; not saved. */
    private JRadioButton fecFast;
    private JRadioButton fecNormal;
    private JRadioButton fecBaud200;
    private final List<JButton> handoverButtons = new ArrayList<>();
    private JPanel macroPanel;
    private JSplitPane contentSplit;
    private JPanel bottomPane;
    /** After the first fit, later layouts only grow the button area so a user-dragged divider is kept. */
    private boolean buttonAreasFitted;

    private volatile boolean sessionActive = true;
    /** Phase 1 offline default: hold commits in App TX buffer (IRS). */
    private boolean localIsIrs = true;
    /** Last Pactor OPMODE {@code u} baud (100 or 200); null until a decoded reply. */
    private Integer opmodeBaud;
    /** True after a non-Standby OPMODE so later Standby can mark the link dead. */
    private boolean opmodeWasLive;
    /**
     * HO / HO after TX clear / HO with text locked after sending CTRL-Z until OPMODE shows
     * IRS (consumed) then ISS again — prevents stacking extra {@code $1A} in the TNC buffer.
     * {@code handoverSeenIssSinceLock} ignores IRS that was already true at press (HO after
     * TX clear from IRS while flushing App TX).
     */
    private volatile boolean handoverLocked;
    private boolean handoverSawIrs;
    private boolean handoverSeenIssSinceLock;
    /** Current inbound line (after {@code $08}); Listen newline scan for Heard/Mentioned/Connect. */
    private final StringBuilder inboundLine = new StringBuilder();
    private Consumer<String> inboundLineListener;
    /** Demo ARQ window: cycles every link-status chip state. Ignores live updates. */
    private Timer txPreviewTimer;
    private int txPreviewStep;
    private boolean statusPreview;
    /** Dead ARQ chip stays DEAD and ignores later status bytes. */
    private boolean linkStatusFrozen;

    public ConnectionWindow(AppController app, Kind kind, String titleCall) {
        super(kind == Kind.LISTEN ? "PtR FEC" : "PtR ARQ — " + titleCall);
        this.app = app;
        this.kind = kind;
        this.titleCall = titleCall;
        buildUi();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                scheduleFitButtonAreas();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                attemptClose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                mailboxUiClosed = true;
                stopMailboxUi();
                stopArqTxPreview();
            }
        });
        WindowPlacement.apply(this,
                kind == Kind.LISTEN ? app.config().getWindowFec() : app.config().getWindowArq(),
                WindowPlacement.CONNECTION_WIDTH,
                WindowPlacement.CONNECTION_HEIGHT);
        if (kind == Kind.ARQ) {
            beginTmailMailboxCheck();
        }
    }

    /**
     * Apply optional OPMODE {@code x} (ISS/IRS) and optional Pactor {@code u} baud
     * (100/200) for the ARQ status-bar speed slot.
     * {@code x} uses the same S=Tx/ISS R=Rx/IRS table for every mode that includes *x*.
     * IRS→ISS drains App TX to Host ({@link #flushIss}).
     */
    public void applyOpmodeLink(Boolean transmit, Integer pactorBaud) {
        if (pactorBaud != null && (pactorBaud == 100 || pactorBaud == 200)) {
            this.opmodeBaud = pactorBaud;
        }
        if (transmit != null && sessionActive && kind == Kind.ARQ) {
            boolean wantIrs = !transmit;
            if (wantIrs) {
                if (handoverLocked && handoverSeenIssSinceLock) {
                    handoverSawIrs = true;
                }
                localIsIrs = true;
            } else if (localIsIrs) {
                if (handoverLocked) {
                    handoverSeenIssSinceLock = true;
                    if (handoverSawIrs) {
                        unlockHandoverControls();
                    }
                }
                flushIss();
                return;
            } else if (handoverLocked) {
                handoverSeenIssSinceLock = true;
            }
        }
        refreshStatus();
    }

    public void markOpmodeLive() {
        this.opmodeWasLive = true;
    }

    public boolean hasSeenLiveOpmode() {
        return opmodeWasLive;
    }

    public Kind kind() {
        return kind;
    }

    public boolean isLocalIrs() {
        return localIsIrs;
    }

    public boolean isHandoverLocked() {
        return handoverLocked;
    }

    /**
     * Disable HO / HO after TX clear / HO with text. EDT. Returns false if already locked
     * or the session is dead.
     */
    public boolean lockHandoverControls() {
        if (!sessionActive || handoverLocked) {
            return false;
        }
        handoverLocked = true;
        handoverSawIrs = false;
        handoverSeenIssSinceLock = !localIsIrs;
        setHandoverButtonsEnabled(false);
        return true;
    }

    /** Re-enable HO buttons if the session is still active. EDT. */
    public void unlockHandoverControls() {
        handoverLocked = false;
        handoverSawIrs = false;
        handoverSeenIssSinceLock = false;
        if (sessionActive) {
            setHandoverButtonsEnabled(true);
        }
    }

    private void setHandoverButtonsEnabled(boolean enabled) {
        for (JButton b : handoverButtons) {
            b.setEnabled(enabled);
        }
    }

    public boolean isSessionActive() {
        return sessionActive;
    }

    public void setSessionActive(boolean active) {
        this.sessionActive = active;
        if (!active) {
            handoverLocked = false;
            handoverSawIrs = false;
            handoverSeenIssSinceLock = false;
        }
        compose.setEditable(active);
        sendButton.setEnabled(active);
        for (JButton b : controlButtons) {
            b.setEnabled(active);
        }
        if (kind == Kind.LISTEN && abortButton != null) {
            abortButton.setEnabled(true);
        }
        if (active && handoverLocked) {
            setHandoverButtonsEnabled(false);
        }
        refreshStatus();
    }

    public void showNotice(String text) {
        noticeLabel.setText(text == null || text.isBlank() ? " " : text);
    }

    /** Listen only: completed inbound lines (no trailing newline), after {@code $08}. */
    public void setInboundLineListener(Consumer<String> listener) {
        this.inboundLineListener = listener;
    }

    /** Append remote (inbound Host) text to the transcript. EDT-safe. */
    public void appendRemoteText(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        String normalized = text.replace("\r\n", "\n").replace('\r', '\n');
        Runnable r = () -> applyInboundTranscript(normalized);
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    /**
     * Inbound only: print text, interpret {@code $08} as backspace on the current line.
     * Does not delete a newline or walk onto the previous line; extra BS is consumed.
     */
    private void applyInboundTranscript(String normalized) {
        if (normalized == null || normalized.isEmpty()) {
            return;
        }
        StringBuilder pending = new StringBuilder();
        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            if (c == BACKSPACE) {
                if (inboundLine.length() > 0) {
                    inboundLine.deleteCharAt(inboundLine.length() - 1);
                }
                if (pending.length() > 0) {
                    if (pending.charAt(pending.length() - 1) != '\n') {
                        pending.deleteCharAt(pending.length() - 1);
                    }
                } else {
                    backspaceCurrentTranscriptLine();
                }
            } else if (c == '\n') {
                finishInboundLine();
                pending.append(c);
            } else {
                inboundLine.append(c);
                pending.append(c);
            }
        }
        if (pending.length() > 0) {
            appendTranscript(pending.toString(), UiColors.REMOTE_TEXT);
        }
    }

    private void finishInboundLine() {
        if (kind != Kind.LISTEN) {
            inboundLine.setLength(0);
            return;
        }
        String line = inboundLine.toString();
        inboundLine.setLength(0);
        Consumer<String> listener = inboundLineListener;
        if (listener != null) {
            listener.accept(line);
        }
    }

    /**
     * Delete one character from the current transcript line (after the last {@code \n}).
     * No-op if the line is empty. Does not delete local grey text if it is somehow last.
     */
    private void backspaceCurrentTranscriptLine() {
        StyledDocument doc = transcript.getStyledDocument();
        int len = doc.getLength();
        if (len <= 0) {
            return;
        }
        try {
            String existing = doc.getText(0, len);
            if (existing.endsWith("\n")) {
                return;
            }
            AttributeSet attrs = doc.getCharacterElement(len - 1).getAttributes();
            Color fg = StyleConstants.getForeground(attrs);
            if (UiColors.LOCAL_PENDING.equals(fg)) {
                return;
            }
            doc.remove(len - 1, 1);
            transcript.setCaretPosition(doc.getLength());
        } catch (BadLocationException ignored) {
        }
    }

    private void buildUi() {
        getContentPane().setBackground(UiColors.WINDOW_BG);
        setLayout(new BorderLayout(4, 4));

        transcript.setEditable(false);
        transcript.setBackground(UiColors.TRANSCRIPT_BG);
        transcript.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JPopupMenu transcriptMenu = new JPopupMenu();
        JMenuItem clearTranscript = new JMenuItem("Clear");
        clearTranscript.addActionListener(e -> clearTranscript());
        transcriptMenu.add(clearTranscript);
        transcript.setComponentPopupMenu(transcriptMenu);
        JScrollPane transcriptScroll = new JScrollPane(transcript);
        transcriptScroll.setBorder(BorderFactory.createTitledBorder("Transcript"));

        appTxBuffer.setEditable(false);
        appTxBuffer.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        appTxBuffer.setRows(4);
        JScrollPane bufferScroll = new JScrollPane(appTxBuffer);
        bufferScroll.setBorder(BorderFactory.createTitledBorder("App TX buffer (IRS hold)"));
        JPopupMenu bufferMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(e -> editAppTxBuffer());
        JMenuItem clearBuffer = new JMenuItem("Clear");
        clearBuffer.addActionListener(e -> appTxBuffer.setText(""));
        bufferMenu.add(editItem);
        bufferMenu.add(clearBuffer);
        appTxBuffer.setComponentPopupMenu(bufferMenu);

        compose.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        compose.setLineWrap(true);
        compose.setWrapStyleWord(true);
        compose.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER
                        && app.config().getCommitMode() == CommitMode.LINE
                        && !e.isShiftDown()) {
                    e.consume();
                    commitComposeLines(true);
                }
            }
        });
        JPopupMenu composeMenu = new JPopupMenu();
        JMenuItem clearCompose = new JMenuItem("Clear");
        clearCompose.addActionListener(e -> compose.setText(""));
        composeMenu.add(clearCompose);
        compose.setComponentPopupMenu(composeMenu);
        JScrollPane composeScroll = new JScrollPane(compose);
        composeScroll.setBorder(BorderFactory.createTitledBorder("Compose"));

        sendButton.addActionListener(e ->
                commitComposeLines(app.config().getCommitMode() == CommitMode.LINE));

        JPanel composeRow = new JPanel(new BorderLayout(4, 4));
        composeRow.setBackground(UiColors.PANEL_BG);
        composeRow.add(composeScroll, BorderLayout.CENTER);
        JPanel sendCol = new JPanel();
        sendCol.setLayout(new BoxLayout(sendCol, BoxLayout.Y_AXIS));
        sendCol.setOpaque(false);
        sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sendCol.add(sendButton);
        if (kind == Kind.ARQ) {
            mailboxButton = new MailboxAlertButton();
            mailboxButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            mailboxButton.setFont(sendButton.getFont().deriveFont(Font.BOLD));
            mailboxButton.setVisible(false);
            mailboxButton.addActionListener(e -> disableTmailMailbox());
            sendCol.add(Box.createVerticalStrut(4));
            sendCol.add(mailboxButton);
        }
        JPanel sendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sendPanel.setBackground(UiColors.PANEL_BG);
        sendPanel.add(sendCol);
        composeRow.add(sendPanel, BorderLayout.EAST);

        JPanel southCenter = new JPanel(new BorderLayout(4, 4));
        southCenter.setBackground(UiColors.PANEL_BG);
        southCenter.add(bufferScroll, BorderLayout.NORTH);
        southCenter.add(composeRow, BorderLayout.CENTER);

        JPanel chatPane = new JPanel(new BorderLayout(4, 4));
        chatPane.setBackground(UiColors.PANEL_BG);
        chatPane.add(transcriptScroll, BorderLayout.CENTER);
        chatPane.add(southCenter, BorderLayout.SOUTH);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(UiColors.STATUS_BG);
        noticeLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        statusRow.setOpaque(true);
        statusRow.setBackground(UiColors.STATUS_BG);
        statusRow.setBorder(BorderFactory.createEmptyBorder(2, 6, 4, 6));
        statusPrefix.setOpaque(false);
        statusSuffix.setOpaque(false);
        statusRow.add(statusPrefix);
        statusRow.add(txChip);
        statusRow.add(statusSuffix);
        JScrollPane controlsScroll = buildControlsScroll();
        JScrollPane macrosScroll = buildMacrosScroll();
        JPanel pinnedBars = new JPanel(new BorderLayout(0, 2));
        pinnedBars.setOpaque(false);
        pinnedBars.add(controlsScroll, BorderLayout.NORTH);
        pinnedBars.add(macrosScroll, BorderLayout.SOUTH);

        JPanel statusBlock = new JPanel(new BorderLayout());
        statusBlock.setOpaque(false);
        statusBlock.add(pinnedBars, BorderLayout.NORTH);
        statusBlock.add(statusRow, BorderLayout.SOUTH);

        JPanel filler = new JPanel();
        filler.setOpaque(false);
        bottom.add(noticeLabel, BorderLayout.NORTH);
        bottom.add(filler, BorderLayout.CENTER);
        bottom.add(statusBlock, BorderLayout.SOUTH);
        bottom.setMinimumSize(new Dimension(120, 140));
        bottomPane = bottom;

        contentSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chatPane, bottom);
        contentSplit.setResizeWeight(1.0);
        contentSplit.setOneTouchExpandable(true);
        contentSplit.setContinuousLayout(true);
        add(contentSplit, BorderLayout.CENTER);
        scheduleFitButtonAreas();
        refreshStatus();
    }

    /**
     * Give the bottom pane its preferred height so both button bars show without a scrollbar.
     * Extra space stays in the chat. After the first fit, only grow the bars when wrapping or
     * new macros need more room.
     */
    private void scheduleFitButtonAreas() {
        SwingUtilities.invokeLater(this::ensureButtonAreasFit);
    }

    private void ensureButtonAreasFit() {
        if (contentSplit == null || bottomPane == null || contentSplit.getHeight() <= 0) {
            return;
        }
        bottomPane.invalidate();
        int pref = bottomPane.getPreferredSize().height;
        int current = contentSplit.getHeight() - contentSplit.getDividerLocation() - contentSplit.getDividerSize();
        if (buttonAreasFitted && current >= pref - 1) {
            return;
        }
        int loc = contentSplit.getHeight() - contentSplit.getDividerSize() - pref;
        if (loc < 80) {
            loc = 80;
        }
        contentSplit.setDividerLocation(loc);
        buttonAreasFitted = true;
    }

    private JScrollPane buildControlsScroll() {
        JPanel p = new ControlsPanel();
        p.setBackground(UiColors.PANEL_BG);
        p.setBorder(BorderFactory.createTitledBorder("Controls"));

        if (kind == Kind.ARQ) {
            addControl(p, "Disc. after TX clear",
                    "Flush App TX, then ch0 CTRL-D $04 after TNC TX empty",
                    () -> app.arqDiscAfterTxClear(this));
            addControl(p, "Disconnect now", "TClear (TC) then ch0 CTRL-D $04",
                    () -> app.arqDisconnectNow(this));
            addAbortControl(p, "Abort link (PN if Listen on, else Pt)", this::abortSession);
            JButton hoNow = addControl(p, "Clear TX and Handover",
                    "TClear (TC) then ch0 CTRL-Z $1A",
                    () -> app.arqHandoverNow(this));
            JButton hoAfter = addControl(p, "HO after TX clear",
                    "Flush App TX, then ch0 CTRL-Z $1A after TNC TX empty",
                    () -> app.arqHoAfterTxClear(this));
            addControl(p, "Seize", "Seize link / ACHG (Host AG)",
                    () -> app.arqSeize(this));
            JButton hoText = addControl(p, "HO with text",
                    "Canned handover text + CTRL-Z $1A in the same ch0 block",
                    () -> app.arqHoWithText(this));
            addControl(p, "Disc. with text",
                    "Canned disconnect text + CTRL-D $04 in the same ch0 block",
                    () -> app.arqDiscWithText(this));
            handoverButtons.add(hoNow);
            handoverButtons.add(hoAfter);
            handoverButtons.add(hoText);
        } else {
            p.add(fecModeBox());
            addAbortControl(p,
                    "Abort FEC transmit (PN if Listen on, else Pt). Window stays open.",
                    this::abortFec);
            addControl(p, "FEC / End TX", "FEC mode command → buffer → CTRL-D end",
                    this::fecEndTx);
            addControl(p, "CQ", "Canned CQ text × CQ repeat (Program settings) → FEC mode command + CTRL-D",
                    this::sendCq);
        }

        JButton save = new JButton("Save chat");
        save.setToolTipText("Save transcript to a file");
        save.addActionListener(e -> saveChat());
        p.add(save);

        JScrollPane scroll = new JScrollPane(p,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setMinimumSize(new Dimension(80, 48));
        scroll.getViewport().addChangeListener(e -> {
            p.invalidate();
            p.revalidate();
            scheduleFitButtonAreas();
        });
        return scroll;
    }

    /**
     * Control buttons wrap to the viewport width. Extra rows scroll vertically
     * instead of being clipped when the window is narrowed.
     */
    private static final class ControlsPanel extends JPanel implements Scrollable {
        ControlsPanel() {
            super(new WrapLayout(FlowLayout.LEFT, 4, 2));
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return Math.max(visibleRect.height - 16, 16);
        }
    }

    /** Fast {@code PD1,1}, Normal {@code PD} (default), 200 baud {@code PD2,2}. */
    private JPanel fecModeBox() {
        JPanel box = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        box.setOpaque(false);
        box.setBorder(BorderFactory.createTitledBorder("FEC mode"));
        fecFast = new JRadioButton("Fast");
        fecNormal = new JRadioButton("Normal", true);
        fecBaud200 = new JRadioButton("200 baud");
        fecFast.setToolTipText("Host PD1,1");
        fecNormal.setToolTipText("Host PD");
        fecBaud200.setToolTipText("Host PD2,2");
        fecFast.setOpaque(false);
        fecNormal.setOpaque(false);
        fecBaud200.setOpaque(false);
        ButtonGroup group = new ButtonGroup();
        group.add(fecFast);
        group.add(fecNormal);
        group.add(fecBaud200);
        box.add(fecFast);
        box.add(fecNormal);
        box.add(fecBaud200);
        return box;
    }

    /** Command for the radio selected now. Normal ({@code PD}) when the box was not built. */
    private String selectedFecCommand() {
        if (fecFast != null && fecFast.isSelected()) {
            return "PD1,1";
        }
        if (fecBaud200 != null && fecBaud200.isSelected()) {
            return "PD2,2";
        }
        return "PD";
    }

    private JButton addControl(JPanel p, String label, String tooltip, Runnable action) {
        JButton b = new JButton(label);
        b.setToolTipText(tooltip);
        b.addActionListener(e -> action.run());
        controlButtons.add(b);
        p.add(b);
        return b;
    }

    private JButton addAbortControl(JPanel p, String tooltip, Runnable action) {
        JButton b = new FaintAbortButton("Abort");
        b.setToolTipText(tooltip);
        b.addActionListener(e -> action.run());
        controlButtons.add(b);
        abortButton = b;
        p.add(b);
        return b;
    }

    private JScrollPane buildMacrosScroll() {
        macroPanel = new ControlsPanel();
        macroPanel.setBackground(UiColors.PANEL_BG);
        macroPanel.setBorder(BorderFactory.createTitledBorder("Macros"));
        reloadMacros();
        JScrollPane scroll = new JScrollPane(macroPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setMinimumSize(new Dimension(80, 48));
        scroll.getViewport().addChangeListener(e -> {
            macroPanel.invalidate();
            macroPanel.revalidate();
            scheduleFitButtonAreas();
        });
        return scroll;
    }

    /** Rebuild macro buttons from {@code config/macros.ini}. EDT. */
    public void reloadMacros() {
        if (macroPanel == null) {
            return;
        }
        macroPanel.removeAll();
        for (MacroFile.Macro macro : app.loadMacros()) {
            JButton b = new JButton(macro.name());
            b.addActionListener(e -> {
                if (!sessionActive) {
                    showNotice(macro.name() + " — session is not active.");
                    return;
                }
                app.runMacro(this, macro);
            });
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        MacroEditDialog.open(ConnectionWindow.this, app, macro);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        MacroEditDialog.open(ConnectionWindow.this, app, macro);
                    }
                }
            });
            macroPanel.add(b);
        }
        JButton add = new JButton("+new");
        add.setToolTipText("Create a macro");
        add.addActionListener(e -> MacroEditDialog.openNew(this, app));
        macroPanel.add(add);
        macroPanel.revalidate();
        macroPanel.repaint();
        scheduleFitButtonAreas();
    }

    /**
     * Append one macro line to Compose. An empty line becomes a blank line.
     * Does not send. EDT.
     *
     * @return false when the session is not active
     */
    public boolean appendMacroComposeLine(String line) {
        if (!sessionActive) {
            return false;
        }
        String existing = compose.getText();
        if (existing == null) {
            existing = "";
        }
        if (!existing.isEmpty() && !existing.endsWith("\n")) {
            compose.append("\n");
        }
        if (line == null || line.isEmpty()) {
            compose.append("\n");
        } else {
            compose.append(line);
        }
        compose.setCaretPosition(compose.getDocument().getLength());
        return true;
    }

    /**
     * Stage one {@code >} macro line the same way as Send, without starting a Host send.
     * Listen and IRS queue the line in App TX. ARQ ISS paints the transcript and returns
     * the text the caller must transmit. EDT.
     */
    public MacroSendStage stageMacroSendLine(String line) {
        if (!sessionActive) {
            return MacroSendStage.stopped();
        }
        if (line == null || line.isEmpty()) {
            return MacroSendStage.held();
        }
        if (localIsIrs || kind != Kind.ARQ) {
            if (!appTxBuffer.getText().isEmpty()) {
                appTxBuffer.append("\n");
            }
            appTxBuffer.append(line);
            return MacroSendStage.held();
        }
        ensureTranscriptNewline();
        appendTranscript(line.endsWith("\n") ? line : line + "\n", UiColors.LOCAL_PENDING);
        return MacroSendStage.sending(line);
    }

    /** Outcome of staging one send-prefix macro line. */
    public record MacroSendStage(boolean inactive, String transmit) {
        static MacroSendStage stopped() {
            return new MacroSendStage(true, null);
        }

        static MacroSendStage held() {
            return new MacroSendStage(false, null);
        }

        static MacroSendStage sending(String line) {
            return new MacroSendStage(false, line);
        }
    }

    private void stubAction(String name) {
        showNotice(name + " — Host action not implemented yet.");
        app.debugLog().info(kind + " stub: " + name);
    }

    /**
     * @param lineMode if true, commit only the current single-line compose contents
     *                 (Enter in LINE mode). If false, commit all non-empty lines (MESSAGE Send).
     */
    private void commitComposeLines(boolean lineMode) {
        if (!sessionActive) {
            return;
        }
        String text = compose.getText();
        if (text == null) {
            return;
        }
        String normalized = text.replace("\r\n", "\n").replace('\r', '\n');
        if (lineMode) {
            while (normalized.endsWith("\n")) {
                normalized = normalized.substring(0, normalized.length() - 1);
            }
            int lastNl = normalized.lastIndexOf('\n');
            String line = lastNl >= 0 ? normalized.substring(lastNl + 1) : normalized;
            if (!line.isEmpty()) {
                enqueueOrFlush(line);
            }
            compose.setText("");
            return;
        }
        if (normalized.isBlank()) {
            return;
        }
        for (String line : normalized.split("\n")) {
            if (!line.isEmpty()) {
                enqueueOrFlush(line);
            }
        }
        compose.setText("");
    }

    private void enqueueOrFlush(String line) {
        if (localIsIrs) {
            if (!appTxBuffer.getText().isEmpty()) {
                appTxBuffer.append("\n");
            }
            appTxBuffer.append(line);
            return;
        }
        ensureTranscriptNewline();
        String forTranscript = line.endsWith("\n") ? line : line + "\n";
        appendTranscript(forTranscript, UiColors.LOCAL_PENDING);
        if (kind == Kind.ARQ) {
            app.sendOutboundChat(this, line);
        }
    }


    /**
     * Drain App TX to grey transcript and mark local ISS. Returns the drained text
     * (empty if the buffer was blank). Does not send Host data — caller does.
     * EDT.
     */
    public String drainAppTxBufferToTranscript() {
        if (!sessionActive || kind != Kind.ARQ) {
            return "";
        }
        String pending = appTxBuffer.getText();
        if (pending == null) {
            pending = "";
        }
        localIsIrs = false;
        ensureTranscriptNewline();
        if (pending.isBlank()) {
            refreshStatus();
            return "";
        }
        String forTranscript = pending.endsWith("\n") ? pending : pending + "\n";
        appendTranscript(forTranscript, UiColors.LOCAL_PENDING);
        appTxBuffer.setText("");
        refreshStatus();
        return pending;
    }

    public boolean isAppTxEmpty() {
        String pending = appTxBuffer.getText();
        return pending == null || pending.isBlank();
    }

    /**
     * OPMODE IRS→ISS: drain App TX to grey transcript and Host ch0.
     * Empty buffer still marks ISS so later commits go outbound.
     */
    private void flushIss() {
        if (!sessionActive || kind != Kind.ARQ) {
            return;
        }
        String pending = drainAppTxBufferToTranscript();
        if (pending.isBlank()) {
            return;
        }
        app.sendOutboundChat(this, pending);
    }

    /**
     * Paint canned HO / Disc text on the transcript once, as grey local outbound.
     * Does not touch Compose, the App TX buffer, or Host send. EDT.
     */
    public void paintCannedLocalOutbound(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        String normalized = text.replace("\r\n", "\n").replace('\r', '\n');
        if (normalized.isEmpty()) {
            return;
        }
        ensureTranscriptNewline();
        String forTranscript = normalized.endsWith("\n") ? normalized : normalized + "\n";
        appendTranscript(forTranscript, UiColors.LOCAL_PENDING);
    }

    /**
     * IRS→ISS (and any App TX drain): if the transcript has text that does not already end
     * on its own line, insert a newline so local outbound does not continue the last RX line.
     * No-op on an empty transcript (no leading blank line after connect). Does not send Host data.
     */
    private void ensureTranscriptNewline() {
        String existing = transcript.getText();
        if (existing == null || existing.isEmpty() || existing.endsWith("\n")) {
            return;
        }
        appendTranscript("\n", UiColors.LOCAL_PENDING);
    }

    /**
     * Listen: one-shot unproto — grey transcript, then Host {@code PD} + ch0 data + CTRL-D.
     * Does not flip IRS/ISS; further commits stay in App TX buffer until the next press.
     */
    private void fecEndTx() {
        if (!sessionActive || kind != Kind.LISTEN) {
            return;
        }
        String pending = appTxBuffer.getText();
        if (pending == null || pending.isBlank()) {
            showNotice("FEC / End TX — App TX buffer empty; commit text first.");
            return;
        }
        appTxBuffer.setText("");
        refreshStatus();
        paintAndSendListenFec(pending, "FEC / End TX");
    }

    /**
     * Listen CQ: canned text from Program settings, repeated CQ-repeat times (each on its
     * own line), then the same {@code PD} + data + CTRL-D path as FEC / End TX.
     * Does not use or clear App TX.
     */
    private void sendCq() {
        if (!sessionActive || kind != Kind.LISTEN) {
            return;
        }
        String pending = app.config().cqFecPayload();
        if (pending.isBlank()) {
            showNotice("CQ — set canned CQ text and CQ repeat in Program settings.");
            return;
        }
        paintAndSendListenFec(pending, "CQ");
    }

    private void paintAndSendListenFec(String pending, String actionName) {
        String forTranscript = pending.endsWith("\n") ? pending : pending + "\n";
        ensureTranscriptNewline();
        appendTranscript(forTranscript, UiColors.LOCAL_PENDING);
        refreshStatus();
        app.listenFecEndTx(this, pending, actionName, selectedFecCommand());
    }

    private void editAppTxBuffer() {
        if (!sessionActive || !localIsIrs) {
            showNotice("Edit only applies to IRS-queued App TX buffer lines.");
            return;
        }
        String composeText = compose.getText();
        String bufferText = appTxBuffer.getText();
        StringBuilder merged = new StringBuilder();
        if (!bufferText.isEmpty()) {
            merged.append(bufferText);
        }
        if (!composeText.isEmpty()) {
            if (!merged.isEmpty() && !merged.toString().endsWith("\n")) {
                merged.append('\n');
            }
            merged.append(composeText);
        }
        appTxBuffer.setText("");
        compose.setText(merged.toString());
        compose.setCaretPosition(compose.getDocument().getLength());
        compose.requestFocusInWindow();
    }

    private void clearTranscript() {
        StyledDocument doc = transcript.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException ignored) {
        }
    }

    private void appendTranscript(String text, Color color) {
        StyledDocument doc = transcript.getStyledDocument();
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, color);
        StyleConstants.setFontFamily(attrs, Font.MONOSPACED);
        try {
            doc.insertString(doc.getLength(), text, attrs);
            transcript.setCaretPosition(doc.getLength());
        } catch (BadLocationException ignored) {
        }
    }

    private void abortSession() {
        if (!sessionActive || kind != Kind.ARQ) {
            return;
        }
        app.arqAbort(this);
    }

    private void abortFec() {
        if (kind != Kind.LISTEN) {
            return;
        }
        app.fecAbort(this);
    }

    /** Write this window's bounds into the matching settings slot. Does not save the file. */
    public void rememberBounds() {
        String bounds = WindowPlacement.capture(this);
        if (kind == Kind.LISTEN) {
            app.config().setWindowFec(bounds);
        } else {
            app.config().setWindowArq(bounds);
        }
    }

    private void saveChat() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File(
                titleCall.replaceAll("[^A-Za-z0-9._-]", "_") + "-chat.txt"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            Files.writeString(chooser.getSelectedFile().toPath(), transcript.getText(), StandardCharsets.UTF_8);
            showNotice("Chat saved.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Save failed:\n" + e.getMessage(),
                    "PactorRATT_Alpha", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Show OPMODE *w* on the link-status chip. No-op while this window is the demo cycle,
     * frozen dead, or {@code n} is not {@code $30}–{@code $37}. EDT.
     */
    public void showLinkByte(int n) {
        if (statusPreview || linkStatusFrozen) {
            return;
        }
        TxChip.State state = TxChip.stateForByte(n);
        if (state != null) {
            txChip.setState(state);
        }
    }

    /** Listen window while a live ARQ link owns the indicator. EDT. */
    public void showInactive() {
        if (statusPreview || linkStatusFrozen) {
            return;
        }
        txChip.setState(TxChip.State.INACTIVE);
    }

    /** Plain STANDBY, no background. EDT. */
    public void showStandby() {
        if (statusPreview || linkStatusFrozen) {
            return;
        }
        txChip.setState(TxChip.State.STANDBY);
    }

    /**
     * Dead ARQ chip. Later {@link #showLinkByte} and {@link #showInactive} calls do nothing.
     * EDT.
     */
    public void showDead() {
        if (statusPreview) {
            return;
        }
        linkStatusFrozen = true;
        txChip.setState(TxChip.State.DEAD);
    }

    public boolean isLinkStatusFrozen() {
        return linkStatusFrozen;
    }

    /**
     * Demo ARQ window: cycle every chip state at 0.5 Hz.
     * Visual only — does not enable the session or send Host commands.
     */
    public void startArqTxPreview() {
        if (kind != Kind.ARQ) {
            return;
        }
        stopArqTxPreview();
        statusPreview = true;
        linkStatusFrozen = false;
        txPreviewStep = 0;
        txChip.setState(TxChip.PREVIEW_STATES[0]);
        txPreviewTimer = new Timer(2000, e -> {
            txPreviewStep = (txPreviewStep + 1) % TxChip.PREVIEW_STATES.length;
            txChip.setState(TxChip.PREVIEW_STATES[txPreviewStep]);
        });
        txPreviewTimer.start();
    }

    private void stopArqTxPreview() {
        if (txPreviewTimer != null) {
            txPreviewTimer.stop();
            txPreviewTimer = null;
        }
        statusPreview = false;
        txChip.stopMotion();
    }

    private void refreshStatus() {
        String role = localIsIrs ? "IRS" : "ISS";
        String speed = opmodeBaud != null ? String.valueOf(opmodeBaud) : "--";
        String tnc = app.isTncConnected() ? "connected" : "offline";
        statusPrefix.setText(String.format(" %s | ", role));
        statusSuffix.setText(String.format(
                " | speed %s | quality -- | retries -- | call %s | ticker: (stub) | TNC %s",
                speed, titleCall, tnc));
    }

    /**
     * Host {@code TL} query after every ARQ window create. TMail ON → flashing disable
     * button; query fail → purple unknown; OFF → no button.
     */
    private void beginTmailMailboxCheck() {
        if (mailboxButton == null) {
            return;
        }
        app.queryTmail(enabled -> {
            if (mailboxUiClosed || mailboxButton == null) {
                return;
            }
            if (enabled == null) {
                showMailboxUnknown();
            } else if (enabled) {
                showMailboxDisableWarn();
            }
        });
    }

    private void showMailboxDisableWarn() {
        stopMailboxTimers();
        applyMailboxHtml("disable", "mailbox");
        mailboxButton.setEnabled(true);
        mailboxFlashLit = true;
        applyMailboxFlashPaint();
        mailboxButton.setVisible(true);
        mailboxFlashTimer = new Timer(MAILBOX_FLASH_MS, e -> {
            mailboxFlashLit = !mailboxFlashLit;
            applyMailboxFlashPaint();
        });
        mailboxFlashTimer.start();
        syncMailboxButtonWidth();
    }

    private void showMailboxUnknown() {
        stopMailboxTimers();
        applyMailboxHtml("mailbox", "unknown");
        mailboxButton.setBackground(MAILBOX_PURPLE);
        mailboxButton.setEnabled(true);
        mailboxButton.setVisible(true);
        syncMailboxButtonWidth();
    }

    private void showMailboxDisabledSolid() {
        stopMailboxTimers();
        applyMailboxHtml("mailbox", "disabled");
        mailboxButton.setBackground(sendButton.getBackground());
        mailboxButton.setEnabled(false);
        mailboxButton.setVisible(true);
        mailboxButton.repaint();
        syncMailboxButtonWidth();
    }

    private void startMailboxHideTimer() {
        if (mailboxHideTimer != null) {
            mailboxHideTimer.stop();
        }
        mailboxHideTimer = new Timer(MAILBOX_HIDE_MS, e -> {
            if (mailboxButton != null) {
                mailboxButton.setVisible(false);
                revalidate();
            }
        });
        mailboxHideTimer.setRepeats(false);
        mailboxHideTimer.start();
    }

    private void disableTmailMailbox() {
        if (mailboxButton == null || !mailboxButton.isEnabled()) {
            return;
        }
        showMailboxDisabledSolid();
        app.disableTmail(error -> {
            if (mailboxUiClosed || mailboxButton == null) {
                return;
            }
            if (error == null) {
                startMailboxHideTimer();
            } else {
                showMailboxUnknown();
            }
        });
    }

    private void applyMailboxHtml(String line1, String line2) {
        mailboxButton.setText("<html><center><b>" + line1 + "<br>" + line2 + "</b></center></html>");
        mailboxButton.setForeground(Color.BLACK);
        mailboxButton.setFont(sendButton.getFont().deriveFont(Font.BOLD));
    }

    private void applyMailboxFlashPaint() {
        mailboxButton.setBackground(mailboxFlashLit ? MAILBOX_RED : MAILBOX_RED_DIM);
        mailboxButton.repaint();
    }

    private void syncMailboxButtonWidth() {
        if (mailboxButton == null) {
            return;
        }
        int w = Math.max(sendButton.getPreferredSize().width, mailboxButton.getPreferredSize().width);
        int sendH = sendButton.getPreferredSize().height;
        int mbH = Math.max(mailboxButton.getPreferredSize().height, sendH * 2);
        sendButton.setPreferredSize(new Dimension(w, sendH));
        sendButton.setMaximumSize(new Dimension(w, sendH));
        mailboxButton.setPreferredSize(new Dimension(w, mbH));
        mailboxButton.setMaximumSize(new Dimension(w, mbH));
        revalidate();
    }

    private void stopMailboxUi() {
        stopMailboxTimers();
    }

    private void stopMailboxTimers() {
        if (mailboxFlashTimer != null) {
            mailboxFlashTimer.stop();
            mailboxFlashTimer = null;
        }
        if (mailboxHideTimer != null) {
            mailboxHideTimer.stop();
            mailboxHideTimer = null;
        }
    }

    private void attemptClose() {
        if (kind == Kind.ARQ && sessionActive) {
            Object[] options = {"Abort", "Disconnect", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this,
                    "ARQ session is active. Abort, disconnect, or cancel?",
                    "Close connection",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[2]);
            if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) {
                return;
            }
            if (choice == 0) {
                abortSession();
            } else {
                stubAction("Disconnect");
                app.markArqDead(this);
            }
        }
        rememberBounds();
        app.saveConfig();
        app.onConnectionWindowClosed(this);
        dispose();
    }

    /**
     * Faint red fill that still shows under Windows system look-and-feel, which ignores
     * {@link JButton#setBackground}. Keeps the normal button border.
     */
    private static final class FaintAbortButton extends JButton {
        FaintAbortButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setOpaque(false);
            setBackground(ABORT_FAINT_RED);
            setForeground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    /**
     * Fills its own background so red/purple show under Windows system L&F.
     */
    private static final class MailboxAlertButton extends JButton {
        MailboxAlertButton() {
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setFocusPainted(false);
            setForeground(Color.BLACK);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    /**
     * Link-status chip. Width fits the widest label in the font that label uses.
     * Words are centered. Phasing sweeps a bright band once a second.
     * CHO pulses a bright edge once a second. Idle fades toward white once a second.
     */
    private static final class TxChip extends JComponent {
        enum State {
            STANDBY, PHASING, CHO, IDLE, TRAFFIC, ERROR, RQ, SYNC, INACTIVE, DEAD
        }

        static final State[] PREVIEW_STATES = {
                State.STANDBY, State.PHASING, State.CHO, State.IDLE, State.TRAFFIC,
                State.ERROR, State.RQ, State.SYNC, State.INACTIVE, State.DEAD
        };

        private static final Font PLAIN = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        private static final Font BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        private static final int PAD_X = 12;
        private static final int PAD_Y = 4;

        private State state = State.STANDBY;
        private Timer motion;
        private Dimension fixedSize;

        TxChip() {
            setOpaque(false);
            setFont(PLAIN);
        }

        static State stateForByte(int n) {
            return switch (n) {
                case 0x30 -> State.STANDBY;
                case 0x31 -> State.PHASING;
                case 0x32 -> State.CHO;
                case 0x33 -> State.IDLE;
                case 0x34 -> State.TRAFFIC;
                case 0x35 -> State.ERROR;
                case 0x36 -> State.RQ;
                case 0x37 -> State.SYNC;
                default -> null;
            };
        }

        void setState(State state) {
            this.state = state == null ? State.STANDBY : state;
            setFont(bold(this.state) ? BOLD : PLAIN);
            syncMotion();
            repaint();
        }

        void stopMotion() {
            if (motion != null) {
                motion.stop();
                motion = null;
            }
        }

        private void syncMotion() {
            boolean animate = state == State.PHASING || state == State.CHO || state == State.IDLE;
            if (!animate) {
                stopMotion();
                return;
            }
            if (motion == null) {
                motion = new Timer(40, e -> repaint());
                motion.start();
            }
        }

        private static boolean bold(State state) {
            return switch (state) {
                case PHASING, CHO, IDLE, TRAFFIC, ERROR, RQ -> true;
                default -> false;
            };
        }

        private String label() {
            return switch (state) {
                case STANDBY -> "STANDBY";
                case PHASING -> "PHASING";
                case CHO -> "CHO";
                case IDLE -> "IDLE";
                case TRAFFIC -> "TRAFFIC";
                case ERROR -> "ERROR";
                case RQ -> "RQ";
                case SYNC -> "SYNC";
                case INACTIVE -> "INACTIVE";
                case DEAD -> "DEAD";
            };
        }

        private Color fill() {
            return switch (state) {
                case PHASING -> UiColors.LINK_PHASING;
                case CHO -> UiColors.LINK_CHO;
                case IDLE -> throb(UiColors.LINK_IDLE);
                case TRAFFIC -> UiColors.LINK_TRAFFIC;
                case ERROR -> UiColors.LINK_ERROR;
                case RQ -> UiColors.LINK_RQ;
                default -> null;
            };
        }

        private Dimension fixedSize() {
            if (fixedSize == null) {
                int w = 0;
                int h = 0;
                for (State candidate : State.values()) {
                    Font font = bold(candidate) ? BOLD : PLAIN;
                    FontMetrics fm = getFontMetrics(font);
                    w = Math.max(w, fm.stringWidth(labelFor(candidate)));
                    h = Math.max(h, fm.getHeight());
                }
                fixedSize = new Dimension(w + PAD_X, Math.max(h + PAD_Y, 16));
            }
            return fixedSize;
        }

        private static String labelFor(State candidate) {
            return switch (candidate) {
                case STANDBY -> "STANDBY";
                case PHASING -> "PHASING";
                case CHO -> "CHO";
                case IDLE -> "IDLE";
                case TRAFFIC -> "TRAFFIC";
                case ERROR -> "ERROR";
                case RQ -> "RQ";
                case SYNC -> "SYNC";
                case INACTIVE -> "INACTIVE";
                case DEAD -> "DEAD";
            };
        }

        @Override
        public Dimension getPreferredSize() {
            return fixedSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return fixedSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return fixedSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();
            g2.setClip(new RoundRectangle2D.Float(0, 0, width, height, 4, 4));
            Color fill = fill();
            if (fill != null) {
                g2.setColor(fill);
                g2.fillRect(0, 0, width, height);
                if (state == State.PHASING) {
                    paintGlint(g2, width, height);
                } else if (state == State.CHO) {
                    paintEdgePulse(g2, width, height);
                }
            }
            g2.setComposite(AlphaComposite.SrcOver);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String text = label();
            int x = (width - fm.stringWidth(text)) / 2;
            int y = (height + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(Color.BLACK);
            g2.drawString(text, x, y);
            g2.dispose();
        }

        /** Border brightens toward white and back once per second. Fill stays put. */
        private static void paintEdgePulse(Graphics2D g2, int width, int height) {
            float t = (float) (0.5 - 0.5 * Math.cos(2 * Math.PI * cyclePhase()));
            float inset = 1.5f;
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(blend(UiColors.LINK_CHO, Color.WHITE, t));
            g2.draw(new RoundRectangle2D.Float(
                    inset, inset, width - inset * 2, height - inset * 2, 4, 4));
        }

        /** Bright band sweeps left to right once per second. */
        private static void paintGlint(Graphics2D g2, int width, int height) {
            int band = Math.max(10, width / 3);
            int x = Math.round((width + band) * cyclePhase()) - band;
            g2.setComposite(AlphaComposite.SrcOver.derive(0.55f));
            g2.setColor(Color.WHITE);
            g2.fillRect(x, 0, band, height);
        }

        /** Fade between {@code base} and white once per second. */
        private static Color throb(Color base) {
            float t = (float) (0.5 - 0.5 * Math.cos(2 * Math.PI * cyclePhase()));
            return blend(base, Color.WHITE, t);
        }

        private static float cyclePhase() {
            long period = 1_000_000_000L;
            return (System.nanoTime() % period) / (float) period;
        }

        private static Color blend(Color from, Color to, float t) {
            float u = 1f - t;
            return new Color(
                    Math.round(from.getRed() * u + to.getRed() * t),
                    Math.round(from.getGreen() * u + to.getGreen() * t),
                    Math.round(from.getBlue() * u + to.getBlue() * t));
        }
    }
}
