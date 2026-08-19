package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.config.CommitMode;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Listen or ARQ connection window: transcript, App TX buffer, compose, controls, status.
 */
public final class ConnectionWindow extends JFrame {

    public enum Kind {
        LISTEN,
        ARQ
    }

    private final AppController app;
    private final Kind kind;
    private final String titleCall;

    private final JTextPane transcript = new JTextPane();
    private final JTextArea appTxBuffer = new JTextArea();
    private final JTextArea compose = new JTextArea(3, 40);
    private final JLabel statusBar = new JLabel();
    private final JLabel noticeLabel = new JLabel(" ");
    private final JButton sendButton = new JButton("Send");
    private final List<JButton> controlButtons = new ArrayList<>();

    private volatile boolean sessionActive = true;
    /** Phase 1 offline default: hold commits in App TX buffer (IRS). */
    private boolean localIsIrs = true;
    /** Last OPMODE {@code w} word (Idle/Traffic/Standby/…); null until a decoded reply. */
    private String opmodeWLabel;
    /** True after a non-Standby OPMODE so later Standby can mark the link dead. */
    private boolean opmodeWasLive;

    public ConnectionWindow(AppController app, Kind kind, String titleCall) {
        super(kind == Kind.LISTEN ? "PactorRATT_Alpha — Listen" : "PactorRATT_Alpha — " + titleCall);
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
        });
        setSize(640, 520);
        setLocationByPlatform(true);
    }

    /**
     * Apply OPMODE {@code w} (link phase) and optional {@code x} (ISS/IRS).
     * {@code x} uses the same S=Tx/ISS R=Rx/IRS table for every mode that includes *x*.
     * IRS→ISS flushes App TX buffer the same as {@link #flushIss}.
     */
    public void applyOpmodeLink(String wLabel, Boolean transmit) {
        if (wLabel != null && !wLabel.isBlank()) {
            this.opmodeWLabel = wLabel;
        }
        if (transmit != null && sessionActive && kind == Kind.ARQ) {
            boolean wantIrs = !transmit;
            if (wantIrs) {
                localIsIrs = true;
            } else if (localIsIrs) {
                flushIss();
                return;
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

    public boolean isSessionActive() {
        return sessionActive;
    }

    public void setSessionActive(boolean active) {
        this.sessionActive = active;
        compose.setEditable(active);
        sendButton.setEnabled(active);
        for (JButton b : controlButtons) {
            b.setEnabled(active);
        }
        refreshStatus();
    }

    public void showNotice(String text) {
        noticeLabel.setText(text == null || text.isBlank() ? " " : text);
    }

    /** Append remote (inbound Host) text to the transcript. EDT-safe. */
    public void appendRemoteText(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        String normalized = text.replace("\r\n", "\n").replace('\r', '\n');
        Runnable r = () -> appendTranscript(normalized, UiColors.REMOTE_TEXT);
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
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

        sendButton.addActionListener(e -> {
            if (app.config().getCommitMode() == CommitMode.LINE) {
                commitComposeLines(true);
            } else {
                commitComposeLines(false);
            }
        });

        JPanel composeRow = new JPanel(new BorderLayout(4, 4));
        composeRow.setBackground(UiColors.PANEL_BG);
        composeRow.add(composeScroll, BorderLayout.CENTER);
        JPanel sendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sendPanel.setBackground(UiColors.PANEL_BG);
        sendPanel.add(sendButton);
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
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 6, 4, 6));
        statusBar.setOpaque(true);
        statusBar.setBackground(UiColors.STATUS_BG);
        bottom.add(noticeLabel, BorderLayout.NORTH);
        bottom.add(buildControlsScroll(), BorderLayout.CENTER);
        bottom.add(statusBar, BorderLayout.SOUTH);
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
            addControl(p, "Disc. after TX clear", "Disconnect after TX clear (Host RE)",
                    () -> app.arqDiscAfterTxClear(this));
            addControl(p, "Disconnect now", "Disconnect immediately (deferred until Rcve confirmed)",
                    () -> stubAction("Disconnect now (deferred)"));
            addControl(p, "Abort", "Abort link (PN if Listen on, else Pt)", this::abortSession);
            addControl(p, "Handover", "Handover now (stubbed)",
                    () -> stubAction("Handover now (deferred)"));
            addControl(p, "HO after TX clear", "Handover after TX clear (Host PV)",
                    () -> app.arqHoAfterTxClear(this));
            addControl(p, "Seize", "Seize link / ACHG (Host AG)",
                    () -> app.arqSeize(this));
            addControl(p, "HO with text", "Canned handover text then Host PV",
                    () -> app.arqHoWithText(this));
            addControl(p, "Disc. with text", "Canned disconnect text then Host RE",
                    () -> app.arqDiscWithText(this));
            addControl(p, "Flush ISS", "Flush App TX buffer to TNC (Host data ch0); mark local ISS",
                    this::flushIss);
        } else {
            addControl(p, "FEC / End TX", "PTSend from Program settings (FEC 200 / Retries) → buffer → CTRL-D end",
                    this::fecEndTx);
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

    private void addControl(JPanel p, String label, String tooltip, Runnable action) {
        JButton b = new JButton(label);
        b.setToolTipText(tooltip);
        b.addActionListener(e -> action.run());
        controlButtons.add(b);
        p.add(b);
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
        // ISS: grey transcript immediately; Host data when TNC connected (ARQ only).
        String forTranscript = line.endsWith("\n") ? line : line + "\n";
        appendTranscript(forTranscript, UiColors.LOCAL_PENDING);
        if (kind == Kind.ARQ) {
            app.sendOutboundChat(this, line);
        }
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
        String forTranscript = pending.endsWith("\n") ? pending : pending + "\n";
        appendTranscript(forTranscript, UiColors.LOCAL_PENDING);
        appTxBuffer.setText("");
        refreshStatus();
        app.listenFecEndTx(this, pending);
    }

    /**
     * ARQ: become ISS — drain App TX buffer to transcript (grey) and Host ch0 data.
     * Empty buffer still flips IRS→ISS so later commits go outbound.
     */
    private void flushIss() {
        if (!sessionActive || kind != Kind.ARQ) {
            return;
        }
        String pending = appTxBuffer.getText();
        localIsIrs = false;
        if (pending.isBlank()) {
            showNotice("Now ISS. New commits go to TNC (grey in transcript).");
            refreshStatus();
            return;
        }
        String forTranscript = pending.endsWith("\n") ? pending : pending + "\n";
        appendTranscript(forTranscript, UiColors.LOCAL_PENDING);
        appTxBuffer.setText("");
        refreshStatus();
        app.sendOutboundChat(this, pending);
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

    private void refreshStatus() {
        String role = localIsIrs ? "IRS" : "ISS";
        String link;
        if (opmodeWLabel != null && !opmodeWLabel.isBlank()) {
            link = opmodeWLabel;
        } else if (sessionActive) {
            link = kind == Kind.LISTEN ? "LISTEN" : "ARQ";
        } else {
            link = "DEAD";
        }
        statusBar.setText(String.format(
                " %s | %s | TX OFF | speed -- | quality -- | retries -- | call %s | ticker: (stub) | TNC %s",
                role, link, titleCall, app.isTncConnected() ? "connected" : "offline"));
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
}
