package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.config.CommitMode;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private final List<JButton> handoverButtons = new ArrayList<>();

    private volatile boolean sessionActive = true;
    /** Phase 1 offline default: hold commits in App TX buffer (IRS). */
    private boolean localIsIrs = true;
    /** Last OPMODE {@code w} word (Idle/Traffic/Standby/…); null until a decoded reply. */
    private String opmodeWLabel;
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
    /** ARQ preview-only: 0 dead TX OFF, 1 live TX OFF, 2 live TX ON. */
    private Timer txPreviewTimer;
    private int txPreviewStep;

    public ConnectionWindow(AppController app, Kind kind, String titleCall) {
        super(kind == Kind.LISTEN ? "PtR FEC" : "PtR ARQ — " + titleCall);
        this.app = app;
        this.kind = kind;
        this.titleCall = titleCall;
        buildUi();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
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
        setSize(640, 520);
        setLocationByPlatform(true);
        if (kind == Kind.ARQ) {
            beginTmailMailboxCheck();
        }
    }

    /**
     * Apply OPMODE {@code w} (link phase), optional {@code x} (ISS/IRS), and optional
     * Pactor {@code u} baud (100/200) for the ARQ status-bar speed slot.
     * {@code x} uses the same S=Tx/ISS R=Rx/IRS table for every mode that includes *x*.
     * IRS→ISS drains App TX to Host ({@link #flushIss}).
     */
    public void applyOpmodeLink(String wLabel, Boolean transmit, Integer pactorBaud) {
        if (wLabel != null && !wLabel.isBlank()) {
            this.opmodeWLabel = wLabel;
        }
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
        bufferMenu.add(editItem);
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
        bottom.add(noticeLabel, BorderLayout.NORTH);
        bottom.add(buildControlsScroll(), BorderLayout.CENTER);
        bottom.add(statusRow, BorderLayout.SOUTH);
        bottom.setMinimumSize(new Dimension(120, 90));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chatPane, bottom);
        split.setResizeWeight(0.75);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(true);
        add(split, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> split.setDividerLocation(0.75));
        refreshStatus();
    }

    private JScrollPane buildControlsScroll() {
        JPanel p = new JPanel(new WrapLayout(FlowLayout.LEFT, 4, 2));
        p.setBackground(UiColors.PANEL_BG);
        p.setBorder(BorderFactory.createTitledBorder("Controls"));

        if (kind == Kind.ARQ) {
            addControl(p, "Disc. after TX clear",
                    "Flush App TX, then ch0 CTRL-D $04 after TNC TX empty",
                    () -> app.arqDiscAfterTxClear(this));
            addControl(p, "Disconnect now", "TClear (TC) then ch0 CTRL-D $04",
                    () -> app.arqDisconnectNow(this));
            addControl(p, "Abort", "Abort link (PN if Listen on, else Pt)", this::abortSession);
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
            addControl(p, "FEC / End TX", "PTSend from Program settings (FEC 200 / Retries) → buffer → CTRL-D end",
                    this::fecEndTx);
            addControl(p, "CQ", "Canned CQ text × CQ repeat (Program settings) → PTSend + CTRL-D",
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
        });
        return scroll;
    }

    private JButton addControl(JPanel p, String label, String tooltip, Runnable action) {
        JButton b = new JButton(label);
        b.setToolTipText(tooltip);
        b.addActionListener(e -> action.run());
        controlButtons.add(b);
        p.add(b);
        return b;
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
        app.listenFecEndTx(this, pending, actionName);
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
     * Preview ARQ window: cycle dead TX OFF / live TX OFF / live TX ON at 1 Hz.
     * Visual only — does not enable the session.
     */
    public void startArqTxPreview() {
        if (kind != Kind.ARQ) {
            return;
        }
        stopArqTxPreview();
        txPreviewStep = 0;
        txPreviewTimer = new Timer(1000, e -> {
            txPreviewStep = (txPreviewStep + 1) % 3;
            refreshStatus();
        });
        txPreviewTimer.start();
        refreshStatus();
    }

    private void stopArqTxPreview() {
        if (txPreviewTimer != null) {
            txPreviewTimer.stop();
            txPreviewTimer = null;
        }
    }

    private boolean isTxPreviewRunning() {
        return txPreviewTimer != null && txPreviewTimer.isRunning();
    }

    private void refreshStatus() {
        boolean preview = kind == Kind.ARQ && isTxPreviewRunning();
        boolean live = preview ? txPreviewStep != 0 : sessionActive;
        boolean txOn = preview ? txPreviewStep == 2 : (sessionActive && !localIsIrs);

        String role = localIsIrs ? "IRS" : "ISS";
        if (preview && txPreviewStep == 2) {
            role = "ISS";
        } else if (preview && txPreviewStep == 1) {
            role = "IRS";
        }
        String link;
        if (preview) {
            link = live ? "ARQ" : "DEAD";
        } else if (opmodeWLabel != null && !opmodeWLabel.isBlank()) {
            link = opmodeWLabel;
        } else if (sessionActive) {
            link = kind == Kind.LISTEN ? "LISTEN" : "ARQ";
        } else {
            link = "DEAD";
        }
        String speed = opmodeBaud != null ? String.valueOf(opmodeBaud) : "--";
        String tnc = app.isTncConnected() ? "connected" : "offline";
        statusPrefix.setText(String.format(" %s | %s | ", role, link));
        statusSuffix.setText(String.format(
                " | speed %s | quality -- | retries -- | call %s | ticker: (stub) | TNC %s",
                speed, titleCall, tnc));
        if (kind == Kind.ARQ) {
            if (!live) {
                txChip.setMode(TxChip.Mode.DEAD_OFF);
            } else if (txOn) {
                txChip.setMode(TxChip.Mode.LIVE_ON);
            } else {
                txChip.setMode(TxChip.Mode.LIVE_OFF);
            }
        } else {
            txChip.setMode(TxChip.Mode.DEAD_OFF);
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
        app.onConnectionWindowClosed(this);
        dispose();
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
     * TX indicator chip. Dead TX OFF is plain black; live TX OFF is bold on green;
     * live TX ON is bold on red.
     */
    private static final class TxChip extends JComponent {
        enum Mode { DEAD_OFF, LIVE_OFF, LIVE_ON }

        private static final Font DEAD_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        private static final Font LIVE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);

        private Mode mode = Mode.DEAD_OFF;

        TxChip() {
            setOpaque(false);
            setFont(DEAD_FONT);
        }

        void setMode(Mode mode) {
            this.mode = mode == null ? Mode.DEAD_OFF : mode;
            setFont(this.mode == Mode.DEAD_OFF ? DEAD_FONT : LIVE_FONT);
            revalidate();
            repaint();
        }

        private String label() {
            return mode == Mode.LIVE_ON ? "TX ON" : "TX OFF";
        }

        private Color fill() {
            return switch (mode) {
                case LIVE_OFF -> UiColors.TX_OFF_LIVE_BG;
                case LIVE_ON -> UiColors.TX_ON_LIVE_BG;
                default -> null;
            };
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(LIVE_FONT);
            int w = fm.stringWidth("TX OFF") + 12;
            int h = Math.max(fm.getHeight() + 4, 16);
            return new Dimension(w, h);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String text = label();
            Color fill = fill();
            if (fill != null) {
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
            }
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(Color.BLACK);
            g2.drawString(text, x, y);
            g2.dispose();
        }
    }
}
