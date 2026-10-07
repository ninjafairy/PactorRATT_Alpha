package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.config.CommitMode;
import com.pactorratt.alpha.config.MacroFile;
import com.pactorratt.alpha.hostmode.CallsignLineParser;

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
import javax.swing.MenuSelectionManager;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.Scrollable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.FontMetrics;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    /** Halfway between idle pink and pure red. The lighter step of the armed pulse. */
    private static final Color ABORT_BRIGHT_RED = new Color(255, 110, 110);
    /** Former lighter armed color. The darker step of the armed pulse. */
    private static final Color ABORT_PULSE_RED = new Color(255, 0, 0);
    private static final int ABORT_ARM_MS = 5000;
    private static final int ABORT_PRESS_MS = 400;
    private static final int ABORT_AGAIN_MS = 600;
    private static final String ABORT_COMPLETE_LINE =
            "[Abort sequence complete, unsent data cleared]";
    private static final DateTimeFormatter TRANSCRIPT_DATE = DateTimeFormatter.ofPattern("MM-dd-yy");
    private static final Color MAILBOX_PURPLE = new Color(0xD8, 0xB4, 0xFE);
    private static final int MAILBOX_FLASH_MS = 500;
    private static final int MAILBOX_HIDE_MS = 3000;
    private static final String APP_TX_TITLE = "App TX buffer (IRS hold)";
    private static final String APP_TX_QUEUED_TITLE = "App TX buffer (queued until next ISS)";

    private final AppController app;
    private final Kind kind;
    private final String titleCall;

    private final JTextPane transcript = new JTextPane();
    private final JTextArea appTxBuffer = new JTextArea();
    private TitledBorder appTxBorder;
    private JScrollPane appTxScroll;
    private final JTextArea compose = new JTextArea(3, 40);
    /**
     * Enter never activates a focused button on this window. Line mode commits Compose.
     * Message mode and Shift+Enter insert a newline.
     */
    private final KeyEventDispatcher enterKeyDispatcher = this::dispatchEnterKey;
    private final JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    private final JLabel statusPrefix = new JLabel();
    private final JLabel statusSuffix = new JLabel();
    /** ARQ only: text between the link chip and the callsign pair. */
    private JLabel statusBeforeCall;
    private JLabel mycallLabel;
    private JLabel peerLabel;
    private DirectionArrow directionArrow;
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
    /** ARQ only. Stays enabled while IRS or a handover hold is active. */
    private JButton seizeButton;
    /** Listen only. Chosen on the window; not saved. */
    private JRadioButton fecFast;
    private JRadioButton fecNormal;
    private JRadioButton fecBaud200;
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
     * Set on HO press, before the Host ack. Locks every Controls button except Abort, Seize,
     * and Save transcript, and holds chat in App TX, until OPMODE shows IRS (the {@code $1A}
     * was consumed) then ISS again. Presses happen while ISS, so the next IRS counts.
     */
    private volatile boolean handoverLocked;
    private boolean handoverSawIrs;
    private boolean handoverSeenIssSinceLock;
    /** Current inbound line (after {@code $08}); Listen newline scan for Heard/Mentioned/Connect. */
    private final StringBuilder inboundLine = new StringBuilder();
    private Consumer<String> inboundLineListener;
    /** Demo ARQ window: cycles every link-status chip state. Ignores live updates. */
    private Timer txPreviewTimer;
    private Timer arrowSpeedTimer;
    private int txPreviewStep;
    private boolean statusPreview;
    /** Preview only: chevrons point at Mycall when true, at the connected call when false. */
    private boolean previewTowardLocal = true;
    /** Dead ARQ chip stays DEAD and ignores later status bytes. */
    private boolean linkStatusFrozen;
    private enum AbortArm {
        IDLE, ARMED, SENDING
    }
    private AbortArm abortArm = AbortArm.IDLE;
    private Timer abortLabelTimer;
    private long abortArmDeadlineMs;
    /** Selection captured on mouse press, before a popup trigger can clear it. */
    private int popupSelStart;
    private int popupSelEnd;
    private int popupClickPos;

    public ConnectionWindow(AppController app, Kind kind, String titleCall) {
        super(kind == Kind.LISTEN ? "PtR FEC" : "PtR ARQ — " + titleCall);
        this.app = app;
        this.kind = kind;
        this.titleCall = titleCall;
        buildUi();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(enterKeyDispatcher);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                applyConnectionMinimumWidth();
                scheduleFitButtonAreas();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                attemptClose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(enterKeyDispatcher);
                mailboxUiClosed = true;
                stopMailboxUi();
                stopArqTxPreview();
                stopAbortArm();
                if (directionArrow != null) {
                    directionArrow.stop();
                }
            }
        });
        WindowPlacement.apply(this,
                kind == Kind.LISTEN ? app.config().getWindowFec() : app.config().getWindowArq(),
                WindowPlacement.CONNECTION_WIDTH,
                WindowPlacement.CONNECTION_HEIGHT);
        applyConnectionMinimumWidth();
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
     * Lock Controls (except Abort, Seize, and Save transcript) and hold chat in App TX.
     * EDT. Returns false if already locked or the session is dead.
     */
    public boolean lockHandoverControls() {
        if (!sessionActive || handoverLocked) {
            return false;
        }
        handoverLocked = true;
        handoverSawIrs = false;
        handoverSeenIssSinceLock = true;
        refreshStatus();
        return true;
    }

    /**
     * Clear the handover hold. EDT. If this was a failed send and we are still ISS,
     * queued App TX goes out now. The IRS→ISS path calls this while still IRS, then
     * {@link #flushIss()} once, so that path does not send twice.
     */
    public void unlockHandoverControls() {
        boolean flushQueued = handoverLocked && sessionActive && !localIsIrs;
        handoverLocked = false;
        handoverSawIrs = false;
        handoverSeenIssSinceLock = false;
        if (flushQueued) {
            flushIss();
        } else {
            refreshStatus();
        }
    }

    /**
     * ARQ: while IRS or a handover hold, only Abort and Seize stay enabled.
     * Save transcript is not in {@link #controlButtons}. Listen keeps its own rule.
     */
    private void applyControlLocks() {
        boolean holdControls = kind == Kind.ARQ && sessionActive && (localIsIrs || handoverLocked);
        for (JButton b : controlButtons) {
            if (b == abortButton || b == seizeButton) {
                boolean abortAlways = b == abortButton && (kind == Kind.LISTEN || statusPreview);
                b.setEnabled(abortAlways || sessionActive);
            } else {
                b.setEnabled(sessionActive && !holdControls);
            }
        }
    }

    /** IRS, or a handover that has not yet been consumed. Chat stays in App TX. */
    private boolean holdsOutboundChat() {
        return kind != Kind.ARQ || localIsIrs || handoverLocked;
    }

    public boolean isSessionActive() {
        return sessionActive;
    }

    public void setSessionActive(boolean active) {
        this.sessionActive = active;
        if (!active && kind == Kind.ARQ && !statusPreview && abortArm == AbortArm.ARMED) {
            restoreAbortIdle();
        }
        if (!active) {
            handoverLocked = false;
            handoverSawIrs = false;
            handoverSeenIssSinceLock = false;
        }
        compose.setEditable(active);
        sendButton.setEnabled(active);
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
            appendTranscript(pending.toString(), incomingColor());
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
            if (sameColor(fg, outgoingColor())) {
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
        transcript.setFont(chatFont());
        JPopupMenu transcriptMenu = new JPopupMenu();
        JMenuItem clearTranscript = new JMenuItem("Clear");
        clearTranscript.addActionListener(e -> clearTranscript());
        transcriptMenu.add(clearTranscript);
        if (kind == Kind.LISTEN) {
            JMenuItem populateCallsign = new JMenuItem("Populate callsign");
            populateCallsign.addActionListener(e -> populateCallsignFromHighlight());
            transcriptMenu.add(populateCallsign);
            transcriptMenu.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    restorePopupSelection();
                    String selected = transcript.getSelectedText();
                    populateCallsign.setEnabled(selected != null && !selected.isBlank());
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });
            transcript.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    popupSelStart = transcript.getSelectionStart();
                    popupSelEnd = transcript.getSelectionEnd();
                    popupClickPos = transcript.viewToModel2D(e.getPoint());
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                        SwingUtilities.invokeLater(ConnectionWindow.this::populateDoubleClickedCallsign);
                    }
                }
            });
        }
        transcript.setComponentPopupMenu(transcriptMenu);
        JScrollPane transcriptScroll = new JScrollPane(transcript);
        transcriptScroll.setBorder(BorderFactory.createTitledBorder("Transcript"));

        appTxBuffer.setEditable(false);
        appTxBuffer.setFont(chatFont());
        appTxBuffer.setRows(4);
        appTxBorder = BorderFactory.createTitledBorder(APP_TX_TITLE);
        appTxScroll = new JScrollPane(appTxBuffer);
        appTxScroll.setBorder(appTxBorder);
        JPopupMenu bufferMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(e -> editAppTxBuffer());
        JMenuItem clearBuffer = new JMenuItem("Clear");
        clearBuffer.addActionListener(e -> appTxBuffer.setText(""));
        bufferMenu.add(editItem);
        bufferMenu.add(clearBuffer);
        appTxBuffer.setComponentPopupMenu(bufferMenu);

        compose.setFont(chatFont());
        compose.setLineWrap(true);
        compose.setWrapStyleWord(true);
        JPopupMenu composeMenu = new JPopupMenu();
        JMenuItem clearCompose = new JMenuItem("Clear");
        clearCompose.addActionListener(e -> compose.setText(""));
        composeMenu.add(clearCompose);
        compose.setComponentPopupMenu(composeMenu);
        JScrollPane composeScroll = new JScrollPane(compose);
        composeScroll.setBorder(BorderFactory.createTitledBorder("Compose"));

        sendButton.addActionListener(e -> commitComposeLines());

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
        southCenter.add(appTxScroll, BorderLayout.NORTH);
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
        if (kind == Kind.ARQ) {
            statusBeforeCall = new JLabel();
            statusBeforeCall.setOpaque(false);
            mycallLabel = new JLabel();
            mycallLabel.setOpaque(false);
            mycallLabel.setFont(mycallLabel.getFont().deriveFont(Font.BOLD));
            directionArrow = new DirectionArrow();
            peerLabel = new JLabel();
            peerLabel.setOpaque(false);
            peerLabel.setFont(peerLabel.getFont().deriveFont(Font.BOLD));
            statusRow.add(statusBeforeCall);
            statusRow.add(mycallLabel);
            statusRow.add(directionArrow);
            statusRow.add(peerLabel);
        }
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

    /** Outer width of an ARQ or FEC window. Same floor for both. */
    private static final int CONNECTION_MIN_WIDTH = 580;

    @Override
    public void setBounds(int x, int y, int width, int height) {
        int[] clamped = WindowPlacement.clampWidth(this, x, width, CONNECTION_MIN_WIDTH);
        super.setBounds(clamped[0], y, clamped[1], height);
    }

    /** ARQ and FEC cannot be narrowed past the control boxes. */
    private void applyConnectionMinimumWidth() {
        WindowPlacement.installMinimumWidth(this, CONNECTION_MIN_WIDTH, 360, WindowPlacement.CONNECTION_HEIGHT);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension min = super.getMinimumSize();
        int height = min == null ? 360 : min.height;
        int width = min == null ? CONNECTION_MIN_WIDTH : Math.max(CONNECTION_MIN_WIDTH, min.width);
        return new Dimension(width, height);
    }

    private JScrollPane buildControlsScroll() {
        JPanel p;
        if (kind == Kind.ARQ) {
            JPanel changeover = sideBox("Changeover");
            JPanel disconnect = new JPanel(new GridBagLayout());
            disconnect.setOpaque(false);
            disconnect.setBorder(BorderFactory.createTitledBorder("Disconnect"));
            JPanel emergency = sideBox("Emergency");
            addSideButton(changeover, "Dump traffic & CHO NOW!",
                    "TClear (TC) then ch0 CTRL-Z $1A",
                    () -> app.arqHandoverNow(this));
            addSideButton(changeover, "CHO after traffic",
                    "ch0 CTL $20, payload CTRL-Z $1A. Waits for the Host data-ack. Does not clear the TNC buffer.",
                    () -> app.arqHoAfterTxClear(this));
            addSideButton(changeover, "Canned CHO",
                    "Canned handover text + CTRL-Z $1A in the same ch0 block",
                    () -> app.arqHoWithText(this));
            closeSideBox(changeover);
            JButton discFinished = addControl(new JPanel(), "Disc. when finished",
                    "Flush App TX, then ch0 CTRL-D $04 after TNC TX empty",
                    () -> app.arqDiscAfterTxClear(this));
            JButton discNow = addControl(new JPanel(), "Disconnect now",
                    "TClear (TC) then ch0 CTRL-D $04",
                    () -> app.arqDisconnectNow(this));
            JButton discCanned = addControl(new JPanel(), "Canned text disc.",
                    "Canned disconnect text + CTRL-D $04 in the same ch0 block",
                    () -> app.arqDiscWithText(this));
            JButton saveTranscript = saveTranscriptButton();
            equalizeButtonSize(discFinished, discNow, discCanned, saveTranscript);
            JPanel discGrid = new JPanel(new GridLayout(2, 2, 4, 2));
            discGrid.setOpaque(false);
            discGrid.add(discFinished);
            discGrid.add(discNow);
            discGrid.add(discCanned);
            discGrid.add(saveTranscript);
            disconnect.add(discGrid);
            addSideAbort(emergency,
                    "Abort link (TC, then PN if FEC/Monitor is on, else Pt). Press twice.",
                    this::onAbortPressed);
            seizeButton = addSideButton(emergency, "Seize", "Seize link / ACHG (Host AG)",
                    () -> app.arqSeize(this));
            closeSideBox(emergency);
            p = new ArqControlsRow(changeover, disconnect, emergency);
        } else {
            EdgeJustifiedPanel split = new EdgeJustifiedPanel();
            addControl(split.left(), "Send FEC", "FEC mode command → buffer → CTRL-D end",
                    this::fecEndTx);
            addControl(split.left(), "CQ",
                    "Canned CQ text × CQ repeat (Program settings) → FEC mode command + CTRL-D",
                    this::sendCq);
            split.left().add(fecModeBox());
            addAbortControl(split.right(),
                    "Abort FEC transmit (TC, then PN). Window stays open. Press twice.",
                    this::onAbortPressed);
            split.right().add(saveTranscriptButton());
            p = split;
        }
        p.setBackground(UiColors.PANEL_BG);
        p.setBorder(BorderFactory.createTitledBorder("Controls"));

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

    private JButton saveTranscriptButton() {
        JButton save = new JButton("Save transcript");
        save.setToolTipText("Save transcript to a file");
        save.addActionListener(e -> saveChat());
        return save;
    }

    /**
     * FEC controls: Send FEC, CQ, and the mode box stay left. Abort and Save transcript
     * stay right. The right pair drops to its own right-aligned line when the row is narrow.
     */
    private static final class EdgeJustifiedPanel extends JPanel implements Scrollable {
        private final JPanel left = buttonGroup(FlowLayout.LEFT);
        private final JPanel right = buttonGroup(FlowLayout.RIGHT);

        EdgeJustifiedPanel() {
            super(new EdgeJustifyLayout());
            setBackground(UiColors.PANEL_BG);
            add(left);
            add(right);
        }

        JPanel left() {
            return left;
        }

        JPanel right() {
            return right;
        }

        private static JPanel buttonGroup(int align) {
            JPanel group = new JPanel(new FlowLayout(align, 4, 2));
            group.setOpaque(false);
            return group;
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

    /** Left cluster at the start of the row, right cluster at the end. */
    private static final class EdgeJustifyLayout implements LayoutManager {
        private static final int HGAP = 8;
        private static final int VGAP = 2;

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return layoutSize(parent, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return layoutSize(parent, false);
        }

        @Override
        public void layoutContainer(Container parent) {
            if (parent.getComponentCount() < 2) {
                return;
            }
            Component left = parent.getComponent(0);
            Component right = parent.getComponent(1);
            Insets insets = parent.getInsets();
            int innerW = Math.max(0, parent.getWidth() - insets.left - insets.right);
            Dimension ld = left.getPreferredSize();
            Dimension rd = right.getPreferredSize();
            int y = insets.top;
            if (fitsOneRow(innerW, ld.width, rd.width)) {
                int rowH = Math.max(ld.height, rd.height);
                left.setBounds(insets.left, y + (rowH - ld.height) / 2, ld.width, ld.height);
                right.setBounds(insets.left + innerW - rd.width, y + (rowH - rd.height) / 2,
                        rd.width, rd.height);
            } else {
                int leftW = Math.min(ld.width, innerW);
                int rightW = Math.min(rd.width, innerW);
                left.setBounds(insets.left, y, leftW, ld.height);
                right.setBounds(insets.left + innerW - rightW, y + ld.height + VGAP, rightW, rd.height);
            }
        }

        private Dimension layoutSize(Container parent, boolean preferred) {
            Insets insets = parent.getInsets();
            Dimension ld = childSize(parent, 0, preferred);
            Dimension rd = childSize(parent, 1, preferred);
            int innerW = Math.max(0, availableWidth(parent) - insets.left - insets.right);
            int height = fitsOneRow(innerW, ld.width, rd.width)
                    ? Math.max(ld.height, rd.height)
                    : ld.height + VGAP + rd.height;
            int width = Math.max(ld.width, rd.width);
            return new Dimension(width + insets.left + insets.right, height + insets.top + insets.bottom);
        }

        private static Dimension childSize(Container parent, int index, boolean preferred) {
            if (parent.getComponentCount() <= index) {
                return new Dimension(0, 0);
            }
            Component child = parent.getComponent(index);
            return preferred ? child.getPreferredSize() : child.getMinimumSize();
        }

        /** {@code innerW == 0} is the not-yet-shown case: keep a single row. */
        private static boolean fitsOneRow(int innerW, int leftW, int rightW) {
            return innerW <= 0 || leftW + HGAP + rightW <= innerW;
        }

        private static int availableWidth(Container target) {
            Container parent = target.getParent();
            if (parent instanceof JViewport viewport && viewport.getWidth() > 0) {
                return viewport.getWidth();
            }
            if (target.getWidth() > 0) {
                return target.getWidth();
            }
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Three fixed-width boxes in a left-aligned row. Extra window width stays empty
     * on the right. Boxes share one height; their buttons stay centered inside.
     */
    private static final class ArqControlsRow extends JPanel implements Scrollable {
        private static final int HGAP = 4;

        ArqControlsRow(JPanel changeover, JPanel disconnect, JPanel emergency) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(lockBoxWidth(changeover));
            add(Box.createHorizontalStrut(HGAP));
            add(lockBoxWidth(disconnect));
            add(Box.createHorizontalStrut(HGAP));
            add(lockBoxWidth(emergency));
            add(Box.createHorizontalGlue());
        }

        /** Preferred width only. Height may grow so the three borders match. */
        private static JPanel lockBoxWidth(JPanel box) {
            box.setMaximumSize(null);
            box.invalidate();
            Dimension pref = box.getPreferredSize();
            int width = Math.max(pref.width, boxWidth(box));
            box.setAlignmentX(Component.LEFT_ALIGNMENT);
            box.setAlignmentY(Component.TOP_ALIGNMENT);
            box.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
            return box;
        }

        private static int boxWidth(JPanel box) {
            int inner = 0;
            for (Component c : box.getComponents()) {
                if (c instanceof JButton button) {
                    inner = Math.max(inner, button.getPreferredSize().width);
                } else if (c instanceof JPanel grid) {
                    inner = Math.max(inner, grid.getPreferredSize().width);
                }
            }
            Insets in = box.getInsets();
            int border = in.left + in.right;
            if (border < 16) {
                border = 16;
            }
            return inner + border;
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

    /** Titled column. A top glue is already in place; {@link #closeSideBox} adds the bottom glue. */
    private static JPanel sideBox(String title) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(false);
        box.setBorder(BorderFactory.createTitledBorder(title));
        box.add(Box.createVerticalGlue());
        return box;
    }

    private static void closeSideBox(JPanel box) {
        box.add(Box.createVerticalGlue());
    }

    private JButton addSideButton(JPanel box, String label, String tooltip, Runnable action) {
        gapBeforeSideButton(box);
        JButton b = addControl(box, label, tooltip, action);
        keepOwnWidth(b);
        return b;
    }

    private void addSideAbort(JPanel box, String tooltip, Runnable action) {
        gapBeforeSideButton(box);
        keepOwnWidth(addAbortControl(box, tooltip, action));
    }

    private static void gapBeforeSideButton(JPanel box) {
        for (Component c : box.getComponents()) {
            if (c instanceof JButton) {
                box.add(Box.createVerticalStrut(2));
                return;
            }
        }
    }

    /** One shared size: the widest and tallest of the labels. */
    private static void equalizeButtonSize(JButton... buttons) {
        int width = 0;
        int height = 0;
        for (JButton b : buttons) {
            Dimension pref = b.getPreferredSize();
            width = Math.max(width, Math.max(pref.width, labelWidth(b)));
            height = Math.max(height, pref.height);
        }
        Dimension size = new Dimension(width, height);
        for (JButton b : buttons) {
            b.setPreferredSize(size);
            b.setMinimumSize(size);
            b.setMaximumSize(size);
            b.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    /**
     * Fill the column width so a short label is not clipped, and keep the
     * button only as tall as its text. Text stays centered in the button.
     */
    private static void keepOwnWidth(JButton b) {
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setHorizontalAlignment(SwingConstants.CENTER);
        Dimension pref = b.getPreferredSize();
        int width = Math.max(pref.width, labelWidth(b));
        b.setPreferredSize(new Dimension(width, pref.height));
        b.setMinimumSize(new Dimension(width, pref.height));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
    }

    /** Text width plus the button's own padding, measured after the font exists. */
    private static int labelWidth(JButton b) {
        String text = b.getText();
        if (text == null || text.isEmpty()) {
            return 0;
        }
        Font font = b.getFont();
        FontMetrics fm = b.getFontMetrics(font);
        Insets margin = b.getMargin();
        Insets insets = b.getInsets();
        int padX = 24;
        if (margin != null) {
            padX = Math.max(padX, margin.left + margin.right + 16);
        }
        if (insets != null) {
            padX = Math.max(padX, insets.left + insets.right + 12);
        }
        return fm.stringWidth(text) + padX;
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
            b.setRequestFocusEnabled(false);
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
        add.setRequestFocusEnabled(false);
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
     * Listen, IRS, and a pending handover queue the line in App TX. ARQ ISS paints the
     * transcript and returns the text the caller must transmit. EDT.
     */
    public MacroSendStage stageMacroSendLine(String line) {
        if (!sessionActive) {
            return MacroSendStage.stopped();
        }
        if (line == null || line.isEmpty()) {
            return MacroSendStage.held();
        }
        if (holdsOutboundChat()) {
            if (!appTxBuffer.getText().isEmpty()) {
                appTxBuffer.append("\n");
            }
            appTxBuffer.append(line);
            return MacroSendStage.held();
        }
        ensureTranscriptNewline();
        appendTranscript(line.endsWith("\n") ? line : line + "\n", outgoingColor());
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
     * Enter on this window. Consumed so a focused button does not run.
     * Line mode commits Compose. Message mode and Shift+Enter insert a newline.
     */
    private boolean dispatchEnterKey(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyChar() != '\n') {
            return false;
        }
        if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow() != this) {
            return false;
        }
        if (MenuSelectionManager.defaultManager().getSelectedPath().length > 0) {
            return false;
        }
        if (e.getID() != KeyEvent.KEY_PRESSED) {
            return true;
        }
        if (e.isShiftDown() || app.config().getCommitMode() != CommitMode.LINE) {
            insertComposeNewline();
        } else {
            commitComposeLines();
        }
        return true;
    }

    private void insertComposeNewline() {
        if (!compose.isEditable()) {
            return;
        }
        compose.replaceSelection("\n");
    }

    /**
     * Commit every Compose line, including blank lines. IRS and a pending handover queue
     * the block in App TX. ISS paints it and sends it. An empty Compose does nothing.
     */
    private void commitComposeLines() {
        if (!sessionActive) {
            return;
        }
        String text = compose.getText();
        if (text == null || text.isEmpty()) {
            return;
        }
        String normalized = text.replace("\r\n", "\n").replace('\r', '\n');
        compose.setText("");
        if (holdsOutboundChat()) {
            appendAppTx(normalized);
            return;
        }
        ensureTranscriptNewline();
        String forTranscript = normalized.endsWith("\n") ? normalized : normalized + "\n";
        appendTranscript(forTranscript, outgoingColor());
        if (kind == Kind.ARQ) {
            app.sendOutboundChat(this, normalized);
        }
    }

    /** Append a compose block to App TX. Blank lines inside {@code text} are kept. */
    private void appendAppTx(String text) {
        String existing = appTxBuffer.getText();
        if (existing == null || existing.isEmpty()) {
            appTxBuffer.setText(text);
            return;
        }
        appTxBuffer.append("\n");
        appTxBuffer.append(text);
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
        appendTranscript(forTranscript, outgoingColor());
        appTxBuffer.setText("");
        refreshStatus();
        return pending;
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
        appendTranscript(forTranscript, outgoingColor());
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
        appendTranscript("\n", outgoingColor());
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
        appendTranscript(forTranscript, outgoingColor());
        refreshStatus();
        app.listenFecEndTx(this, pending, actionName, selectedFecCommand());
    }

    private void editAppTxBuffer() {
        if (!sessionActive || !holdsOutboundChat()) {
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
        StyleConstants.setFontSize(attrs, app.config().getTextSize());
        try {
            doc.insertString(doc.getLength(), text, attrs);
            transcript.setCaretPosition(doc.getLength());
        } catch (BadLocationException ignored) {
        }
    }

    /**
     * Close-dialog Abort stays one press and uses {@link AppController#arqAbort}.
     * The control-button path is {@link #onAbortPressed}.
     */
    private void abortSession() {
        if (!sessionActive || kind != Kind.ARQ) {
            return;
        }
        app.arqAbort(this);
    }

    private void onAbortPressed() {
        if (abortArm == AbortArm.SENDING) {
            return;
        }
        if (kind == Kind.ARQ && !sessionActive && !statusPreview) {
            return;
        }
        if (abortArm == AbortArm.ARMED) {
            confirmAbortPress();
            return;
        }
        if (kind == Kind.LISTEN && app.hasActiveArq()) {
            showNotice("Abort — unavailable while ARQ is up.");
            return;
        }
        startAbortArm();
    }

    private void startAbortArm() {
        stopAbortTimers();
        abortArm = AbortArm.ARMED;
        abortArmDeadlineMs = System.currentTimeMillis() + ABORT_ARM_MS;
        showAbortPhase("Press", ABORT_BRIGHT_RED);
        scheduleAbortLabel(ABORT_PRESS_MS, true);
    }

    /** Label and fill change together. Press is the lighter red, Again the darker red. */
    private void showAbortPhase(String label, Color fill) {
        abortButton.setText(label);
        abortButton.setBackground(fill);
        if (abortButton instanceof FaintAbortButton faint) {
            faint.setArmedLook(true);
        }
        abortButton.repaint();
    }

    /** After {@code delayMs}, show Again ({@code nextIsAgain}) or Press, until the 5 s deadline. */
    private void scheduleAbortLabel(int delayMs, boolean nextIsAgain) {
        if (abortLabelTimer != null) {
            abortLabelTimer.stop();
            abortLabelTimer = null;
        }
        if (abortArm != AbortArm.ARMED) {
            return;
        }
        long remaining = abortArmDeadlineMs - System.currentTimeMillis();
        if (remaining <= 0) {
            restoreAbortIdle();
            return;
        }
        int wait = (int) Math.min(delayMs, remaining);
        boolean deadline = remaining <= delayMs;
        abortLabelTimer = new Timer(wait, e -> {
            if (abortArm != AbortArm.ARMED) {
                return;
            }
            if (deadline || System.currentTimeMillis() >= abortArmDeadlineMs) {
                restoreAbortIdle();
                return;
            }
            if (nextIsAgain) {
                showAbortPhase("Again", ABORT_PULSE_RED);
            } else {
                showAbortPhase("Press", ABORT_BRIGHT_RED);
            }
            scheduleAbortLabel(nextIsAgain ? ABORT_AGAIN_MS : ABORT_PRESS_MS, !nextIsAgain);
        });
        abortLabelTimer.setRepeats(false);
        abortLabelTimer.start();
    }

    private void confirmAbortPress() {
        stopAbortTimers();
        if (kind == Kind.ARQ && (statusPreview || !sessionActive)) {
            showNotice("Abort — TNC not connected.");
            restoreAbortIdle();
            return;
        }
        if (kind == Kind.LISTEN && app.hasActiveArq()) {
            showNotice("Abort — unavailable while ARQ is up.");
            restoreAbortIdle();
            return;
        }
        abortArm = AbortArm.SENDING;
        showAbortPhase("Abort", ABORT_BRIGHT_RED);
        app.confirmLinkAbort(this, this::restoreAbortIdle);
    }

    private void restoreAbortIdle() {
        stopAbortTimers();
        abortArm = AbortArm.IDLE;
        if (abortButton == null) {
            return;
        }
        abortButton.setText("Abort");
        abortButton.setBackground(ABORT_FAINT_RED);
        if (abortButton instanceof FaintAbortButton faint) {
            faint.setArmedLook(false);
        }
        abortButton.repaint();
    }

    /** Stop the arm timers without changing the button. Safe if none are running. */
    private void stopAbortArm() {
        stopAbortTimers();
        abortArm = AbortArm.IDLE;
    }

    private void stopAbortTimers() {
        if (abortLabelTimer != null) {
            abortLabelTimer.stop();
            abortLabelTimer = null;
        }
    }

    /** Red transcript line after both abort acks. EDT. */
    public void paintAbortSequenceComplete() {
        String line = ABORT_COMPLETE_LINE + "\n";
        try {
            StyledDocument doc = transcript.getStyledDocument();
            int len = doc.getLength();
            if (len > 0 && !"\n".equals(doc.getText(len - 1, 1))) {
                line = "\n" + line;
            }
        } catch (BadLocationException ignored) {
        }
        appendTranscript(line, Color.RED);
    }

    private void populateDoubleClickedCallsign() {
        if (kind != Kind.LISTEN) {
            return;
        }
        String selected = transcript.getSelectedText();
        if (selected == null) {
            return;
        }
        String trimmed = selected.trim();
        if (!CallsignLineParser.isCallsign(trimmed)) {
            return;
        }
        pushCallsign(trimmed.toUpperCase(Locale.ROOT));
    }

    private void populateCallsignFromHighlight() {
        String selected = transcript.getSelectedText();
        if (selected == null || selected.isBlank()) {
            return;
        }
        String trimmed = selected.trim();
        String value = CallsignLineParser.isCallsign(trimmed)
                ? trimmed.toUpperCase(Locale.ROOT)
                : trimmed;
        pushCallsign(value);
    }

    private void pushCallsign(String value) {
        MainWindow main = app.mainWindow();
        if (main != null) {
            main.populateCallsign(value);
        }
    }

    /** Put back a highlight if the popup click landed inside it. */
    private void restorePopupSelection() {
        if (popupSelStart >= popupSelEnd) {
            return;
        }
        if (popupClickPos < popupSelStart || popupClickPos > popupSelEnd) {
            return;
        }
        int len = transcript.getDocument().getLength();
        int start = Math.min(popupSelStart, len);
        int end = Math.min(popupSelEnd, len);
        if (start < end) {
            transcript.select(start, end);
        }
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
        File dir = chooser.getCurrentDirectory();
        chooser.setSelectedFile(new File(dir, nextTranscriptFileName(dir)));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            Files.writeString(chooser.getSelectedFile().toPath(), transcript.getText(), StandardCharsets.UTF_8);
            showNotice("Transcript saved.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Save failed:\n" + e.getMessage(),
                    "PactorRATT_Alpha", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * First free name in {@code dir}. ARQ: {@code QSO with CALL on MM-dd-yy.txt}.
     * FEC: {@code FEC transcript MM-dd-yy.txt}. The second file is {@code (2)}.
     */
    private String nextTranscriptFileName(File dir) {
        String date = LocalDate.now().format(TRANSCRIPT_DATE);
        String base;
        if (kind == Kind.LISTEN) {
            base = "FEC transcript " + date;
        } else {
            String call = sanitizeFileToken(titleCall);
            if (call.isBlank()) {
                call = "UNKNOWN";
            }
            base = "QSO with " + call + " on " + date;
        }
        if (!new File(dir, base + ".txt").exists()) {
            return base + ".txt";
        }
        for (int n = 2; n < 10000; n++) {
            String name = base + " (" + n + ").txt";
            if (!new File(dir, name).exists()) {
                return name;
            }
        }
        return base + ".txt";
    }

    private static String sanitizeFileToken(String call) {
        if (call == null) {
            return "";
        }
        return call.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
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
        if (directionArrow != null) {
            directionArrow.setRunning(false);
        }
    }

    public boolean isLinkStatusFrozen() {
        return linkStatusFrozen;
    }

    /**
     * Demo ARQ window: cycle every chip state at 0.5 Hz. Chevron steps run at
     * 250 ms for 2 s, then 125 ms for 1 s, then the direction flips and the cycle repeats.
     * Visual only — does not enable the session or send Host commands.
     */
    public void startArqTxPreview() {
        if (kind != Kind.ARQ) {
            return;
        }
        stopArqTxPreview();
        statusPreview = true;
        linkStatusFrozen = false;
        if (abortButton != null) {
            abortButton.setEnabled(true);
        }
        txPreviewStep = 0;
        txChip.setState(TxChip.PREVIEW_STATES[0]);
        txPreviewTimer = new Timer(2000, e -> {
            txPreviewStep = (txPreviewStep + 1) % TxChip.PREVIEW_STATES.length;
            txChip.setState(TxChip.PREVIEW_STATES[txPreviewStep]);
        });
        txPreviewTimer.start();
        previewTowardLocal = true;
        applyPreviewArrow();
        schedulePreviewArrowSpeed(false);
    }

    /** Preview chase: lit chevron runs, and the whole row flips end every timer tick. */
    private void applyPreviewArrow() {
        if (directionArrow == null) {
            return;
        }
        statusPrefix.setText(previewTowardLocal ? " IRS | " : " ISS | ");
        directionArrow.setTowardLocal(previewTowardLocal);
        directionArrow.setRunning(true);
    }

    /**
     * Preview only. {@code fast} is 125 ms steps for 1000 ms; otherwise 250 ms steps for 2000 ms.
     * When the fast phase ends, the chevrons reverse and the slow phase starts again.
     */
    private void schedulePreviewArrowSpeed(boolean fast) {
        if (arrowSpeedTimer != null) {
            arrowSpeedTimer.stop();
            arrowSpeedTimer = null;
        }
        if (directionArrow == null || !statusPreview) {
            return;
        }
        directionArrow.setStepMillis(fast ? DirectionArrow.STEP_200_MS : DirectionArrow.STEP_100_MS);
        arrowSpeedTimer = new Timer(fast ? 1000 : 2000, e -> {
            if (fast) {
                previewTowardLocal = !previewTowardLocal;
                applyPreviewArrow();
            }
            schedulePreviewArrowSpeed(!fast);
        });
        arrowSpeedTimer.setRepeats(false);
        arrowSpeedTimer.start();
    }

    private void stopArqTxPreview() {
        if (txPreviewTimer != null) {
            txPreviewTimer.stop();
            txPreviewTimer = null;
        }
        if (arrowSpeedTimer != null) {
            arrowSpeedTimer.stop();
            arrowSpeedTimer = null;
        }
        statusPreview = false;
        txChip.stopMotion();
        if (directionArrow != null) {
            directionArrow.stop();
        }
    }

    /** Queued title only while a handover is pending and OPMODE still says ISS. */
    private void refreshAppTxTitle() {
        if (appTxBorder == null) {
            return;
        }
        String title = kind == Kind.ARQ && handoverLocked && !localIsIrs
                ? APP_TX_QUEUED_TITLE
                : APP_TX_TITLE;
        if (title.equals(appTxBorder.getTitle())) {
            return;
        }
        appTxBorder.setTitle(title);
        if (appTxScroll != null) {
            appTxScroll.repaint();
        }
    }

    private void refreshStatus() {
        applyControlLocks();
        refreshAppTxTitle();
        String role = localIsIrs ? "IRS" : "ISS";
        String speed = opmodeBaud != null ? String.valueOf(opmodeBaud) : "--";
        String tnc = app.isTncConnected() ? "connected" : "offline";
        if (!statusPreview) {
            statusPrefix.setText(String.format(" %s | ", role));
        }
        if (kind == Kind.ARQ && directionArrow != null) {
            statusBeforeCall.setText(String.format(" | speed %s | quality -- | retries -- | ", speed));
            mycallLabel.setText(mycallText() + " ");
            peerLabel.setText(" " + peerText());
            statusSuffix.setText(String.format(" | ticker: (stub) | TNC %s", tnc));
            if (!statusPreview) {
                directionArrow.setStepMillis(DirectionArrow.stepMillisForBaud(opmodeBaud));
                directionArrow.setTowardLocal(localIsIrs);
                directionArrow.setRunning(sessionActive && !linkStatusFrozen);
            }
        } else {
            statusSuffix.setText(String.format(
                    " | speed %s | quality -- | retries -- | call %s | ticker: (stub) | TNC %s",
                    speed, titleCall, tnc));
        }
    }

    private String mycallText() {
        String tncCall = app.tncMycall();
        if (tncCall != null && !tncCall.isBlank()) {
            return tncCall.trim();
        }
        String configured = app.config().getCallsign();
        if (configured != null && !configured.isBlank()) {
            return configured.trim();
        }
        return "----";
    }

    private String peerText() {
        if (titleCall == null || titleCall.isBlank()) {
            return "----";
        }
        return titleCall.trim();
    }

    private Font chatFont() {
        return new Font(Font.MONOSPACED, Font.PLAIN, app.config().getTextSize());
    }

    private Color outgoingColor() {
        return app.config().getOutgoingText();
    }

    private Color incomingColor() {
        return app.config().getIncomingText();
    }

    private static boolean sameColor(Color a, Color b) {
        return a != null && b != null && a.getRGB() == b.getRGB();
    }

    /** Apply the program text size to transcript, compose, and App TX. EDT. */
    public void applyTextSize() {
        Font font = chatFont();
        transcript.setFont(font);
        compose.setFont(font);
        appTxBuffer.setFont(font);
        StyledDocument doc = transcript.getStyledDocument();
        int len = doc.getLength();
        if (len <= 0) {
            return;
        }
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs, Font.MONOSPACED);
        StyleConstants.setFontSize(attrs, font.getSize());
        doc.setCharacterAttributes(0, len, attrs, false);
    }

    /**
     * Recolor transcript runs still painted with {@code previousOutgoing} or
     * {@code previousIncoming}. EDT.
     */
    public void applyTranscriptColors(Color previousOutgoing, Color previousIncoming) {
        Color outgoing = outgoingColor();
        Color incoming = incomingColor();
        StyledDocument doc = transcript.getStyledDocument();
        int len = doc.getLength();
        int pos = 0;
        while (pos < len) {
            Element el = doc.getCharacterElement(pos);
            int start = el.getStartOffset();
            int end = Math.min(el.getEndOffset(), len);
            if (end <= start) {
                break;
            }
            Color fg = StyleConstants.getForeground(el.getAttributes());
            Color next = null;
            if (sameColor(fg, previousOutgoing)) {
                next = outgoing;
            } else if (sameColor(fg, previousIncoming)) {
                next = incoming;
            }
            if (next != null && !sameColor(fg, next)) {
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setForeground(attrs, next);
                doc.setCharacterAttributes(start, end - start, attrs, false);
            }
            pos = end;
        }
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
        private static final String[] WIDTH_LABELS = {"Abort", "Press", "Again"};
        private static final Color ARMED_TEXT = Color.WHITE;
        private static final Color ARMED_OUTLINE = new Color(60, 0, 0);

        private final Font plainFont;
        private boolean armedLook;

        FaintAbortButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setOpaque(false);
            setBackground(ABORT_FAINT_RED);
            setForeground(Color.BLACK);
            plainFont = getFont().deriveFont(Font.PLAIN);
            setFont(plainFont);
            lockLabelWidth();
        }

        @Override
        public void addNotify() {
            super.addNotify();
            lockLabelWidth();
        }

        /** Bold white label while Press / Again (and during the send) is on a red fill. */
        void setArmedLook(boolean armed) {
            this.armedLook = armed;
            setForeground(armed ? ARMED_TEXT : Color.BLACK);
            setFont(armed ? plainFont.deriveFont(Font.BOLD) : plainFont);
            repaint();
        }

        /**
         * Wide enough for bold "Again", including the border insets and the text outline.
         * Measured with font metrics so a too-early preferred-size call cannot clip the word.
         */
        private void lockLabelWidth() {
            Font bold = plainFont.deriveFont(Font.BOLD);
            FontMetrics plainFm = getFontMetrics(plainFont);
            FontMetrics boldFm = getFontMetrics(bold);
            int text = plainFm.stringWidth("Abort");
            for (String label : WIDTH_LABELS) {
                FontMetrics fm = "Abort".equals(label) ? plainFm : boldFm;
                text = Math.max(text, fm.stringWidth(label));
            }
            Insets insets = getInsets();
            int chromeX = Math.max(insets.left + insets.right, 24) + 16;
            int chromeY = Math.max(insets.top + insets.bottom, 8) + 6;
            int width = text + chromeX;
            int height = Math.max(plainFm.getHeight(), boldFm.getHeight()) + chromeY;
            Dimension fixed = new Dimension(width, height);
            setPreferredSize(fixed);
            setMinimumSize(fixed);
            setMaximumSize(fixed);
            revalidate();
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            if (armedLook) {
                paintArmedOutline(g);
            }
            super.paintComponent(g);
        }

        /** Dark edge behind the white label so it stays readable on both armed reds. */
        private void paintArmedOutline(Graphics g) {
            String text = getText();
            if (text == null || text.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setFont(getFont());
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            FontMetrics fm = g2.getFontMetrics();
            Insets insets = getInsets();
            Rectangle view = new Rectangle(
                    insets.left,
                    insets.top,
                    Math.max(0, getWidth() - insets.left - insets.right),
                    Math.max(0, getHeight() - insets.top - insets.bottom));
            Rectangle iconR = new Rectangle();
            Rectangle textR = new Rectangle();
            SwingUtilities.layoutCompoundLabel(
                    this, fm, text, null,
                    getVerticalAlignment(), getHorizontalAlignment(),
                    getVerticalTextPosition(), getHorizontalTextPosition(),
                    view, iconR, textR, 0);
            g2.setColor(ARMED_OUTLINE);
            int baseline = textR.y + fm.getAscent();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    g2.drawString(text, textR.x + dx, baseline + dy);
                }
            }
            g2.dispose();
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

    /**
     * Four chevrons between the two callsigns. One is lit and steps toward the
     * receiving station: left when IRS, right when ISS.
     */
    private static final class DirectionArrow extends JComponent {
        private static final int CHEVRONS = 4;
        static final int STEP_100_MS = 250;
        static final int STEP_200_MS = 125;
        private static final Color LIT = new Color(0x20, 0x20, 0x20);
        private static final Color DIM = new Color(0xC4, 0xBE, 0xB0);

        private boolean towardLocal = true;
        private boolean running;
        private int step;
        private int stepMs = STEP_100_MS;
        private Timer timer;

        /** 200 baud steps twice as fast. Unknown speed uses the 100-baud interval. */
        static int stepMillisForBaud(Integer baud) {
            return baud != null && baud == 200 ? STEP_200_MS : STEP_100_MS;
        }

        DirectionArrow() {
            setOpaque(false);
        }

        void setTowardLocal(boolean towardLocal) {
            this.towardLocal = towardLocal;
            repaint();
        }

        /** Updates a running chase immediately when the link changes between 100 and 200. */
        void setStepMillis(int stepMs) {
            if (stepMs <= 0 || this.stepMs == stepMs) {
                return;
            }
            this.stepMs = stepMs;
            if (timer != null) {
                timer.setInitialDelay(stepMs);
                timer.setDelay(stepMs);
            }
        }

        /** {@code false} freezes every chevron dim and stops the timer. */
        void setRunning(boolean running) {
            if (this.running == running) {
                repaint();
                return;
            }
            this.running = running;
            if (running) {
                if (timer == null) {
                    timer = new Timer(stepMs, e -> {
                        step = (step + 1) % CHEVRONS;
                        repaint();
                    });
                    timer.start();
                }
            } else {
                stopTimer();
            }
            repaint();
        }

        void stop() {
            running = false;
            stopTimer();
            repaint();
        }

        private void stopTimer() {
            if (timer != null) {
                timer.stop();
                timer = null;
            }
        }

        /** Lit chevron, or {@code -1} when the link is not animating. */
        private int litIndex() {
            if (!running) {
                return -1;
            }
            return towardLocal ? (CHEVRONS - 1 - (step % CHEVRONS)) : (step % CHEVRONS);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(56, 18);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            Dimension size = getPreferredSize();
            return new Dimension(size.width, Integer.MAX_VALUE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int lit = litIndex();
            int slot = Math.max(1, getWidth() / CHEVRONS);
            int midY = getHeight() / 2;
            for (int i = 0; i < CHEVRONS; i++) {
                g2.setColor(i == lit ? LIT : DIM);
                int cx = slot * i + slot / 2;
                paintChevron(g2, cx, midY, towardLocal);
            }
            g2.dispose();
        }

        /** Triangle pointing left (IRS) or right (ISS). */
        private static void paintChevron(Graphics2D g2, int cx, int cy, boolean pointLeft) {
            int reach = 4;
            int halfH = 5;
            int dir = pointLeft ? -1 : 1;
            int tipX = cx + dir * reach;
            int tailX = cx - dir * reach;
            int[] xs = {tipX, tailX, tailX};
            int[] ys = {cy, cy - halfH, cy + halfH};
            g2.fillPolygon(xs, ys, 3);
        }
    }
}
