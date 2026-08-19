package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.hostmode.HostFrameCodec;
import com.pactorratt.alpha.hostmode.OpmodeParser;
import com.pactorratt.alpha.serial.SerialByteListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Live view of Host OPMODE ({@code $4F} + {@code OP…}) TX polls and RX replies only.
 * Same stream formatting as Debug Monitor; {@code Mode:} is decoded per Ch. 4 §4.3.2.
 */
public final class StatusMonitorWindow extends JFrame implements SerialByteListener {

    private static final int MAX_CHARS = 200_000;
    private static final int RX_IDLE_FLUSH_MS = 250;

    private final AppController app;
    private final JTextArea textArea = new JTextArea();
    private final JButton pauseButton = new JButton("Pause");
    private final JLabel modeLabel = new JLabel("Mode: —");
    private volatile boolean paused;

    private final HostFrameCodec.FrameParser rxCoalesceParser = new HostFrameCodec.FrameParser();
    private final HostFrameCodec.FrameParser txCoalesceParser = new HostFrameCodec.FrameParser();
    private final ByteArrayOutputStream rxLooseBytes = new ByteArrayOutputStream();
    private final Object rxCoalesceLock = new Object();
    private final Object txCoalesceLock = new Object();
    private final Timer rxIdleFlushTimer;

    public StatusMonitorWindow(AppController app) {
        super("TNC Status Monitor");
        this.app = app;
        app.addSerialByteListener(this);
        rxIdleFlushTimer = new Timer(RX_IDLE_FLUSH_MS, e -> flushRxIdle());
        rxIdleFlushTimer.setRepeats(false);
        buildUi();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                rxIdleFlushTimer.stop();
                flushRxIdle();
                app.removeSerialByteListener(StatusMonitorWindow.this);
                app.onStatusMonitorClosed(StatusMonitorWindow.this);
            }
        });
        setSize(720, 420);
        setLocationByPlatform(true);
    }

    private void buildUi() {
        getContentPane().setBackground(UiColors.WINDOW_BG);
        setLayout(new BorderLayout(6, 6));

        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        textArea.setBackground(UiColors.TRANSCRIPT_BG);
        textArea.setLineWrap(false);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setBackground(UiColors.PANEL_BG);
        south.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));

        JPanel controlsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        controlsRow.setBackground(UiColors.PANEL_BG);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            flushRxIdle();
            textArea.setText("");
        });

        pauseButton.addActionListener(e -> {
            paused = !paused;
            pauseButton.setText(paused ? "Resume" : "Pause");
            if (paused) {
                flushRxIdle();
            }
        });

        controlsRow.add(clearButton);
        controlsRow.add(pauseButton);

        JPanel modeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        modeRow.setBackground(UiColors.PANEL_BG);
        modeLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        modeRow.add(modeLabel);

        south.add(controlsRow);
        south.add(Box.createVerticalStrut(4));
        south.add(modeRow);

        add(scroll, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    /** EDT-safe update of the decoded {@code Mode:} line. */
    public void setModeLine(String text) {
        String line = text == null || text.isBlank() ? "Mode: —" : text;
        Runnable r = () -> modeLabel.setText(line);
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    @Override
    public void onSerialBytes(boolean transmit, byte[] data, int offset, int length) {
        if (paused || length <= 0) {
            return;
        }
        if (transmit) {
            List<String> lines = new ArrayList<>();
            synchronized (txCoalesceLock) {
                int end = offset + length;
                for (int i = offset; i < end; i++) {
                    HostFrameCodec.Frame frame = txCoalesceParser.feed(data[i]);
                    if (frame != null && OpmodeParser.isOpmodeFrame(frame)) {
                        lines.add(HostMonitorFormat.formatLine(true, frame.raw));
                    }
                }
            }
            for (String line : lines) {
                SwingUtilities.invokeLater(() -> appendLine(line));
            }
            return;
        }

        List<String> lines = new ArrayList<>();
        synchronized (rxCoalesceLock) {
            int end = offset + length;
            for (int i = offset; i < end; i++) {
                byte b = data[i];
                if (rxCoalesceParser.awaitingSoh() && b != HostFrameCodec.SOH) {
                    rxLooseBytes.write(b);
                    continue;
                }
                flushLooseLocked();
                HostFrameCodec.Frame frame = rxCoalesceParser.feed(b);
                if (frame != null && OpmodeParser.isOpmodeFrame(frame)) {
                    lines.add(HostMonitorFormat.formatLine(false, frame.raw));
                }
            }
        }
        for (String line : lines) {
            SwingUtilities.invokeLater(() -> appendLine(line));
        }
        SwingUtilities.invokeLater(rxIdleFlushTimer::restart);
    }

    private void flushRxIdle() {
        synchronized (rxCoalesceLock) {
            flushLooseLocked();
            if (!rxCoalesceParser.awaitingSoh()) {
                rxCoalesceParser.reset();
            }
        }
    }

    private void flushLooseLocked() {
        rxLooseBytes.reset();
    }

    private void appendLine(String line) {
        textArea.append(line);
        textArea.append("\n");
        trimIfNeeded();
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    private void trimIfNeeded() {
        Document doc = textArea.getDocument();
        int len = doc.getLength();
        if (len <= MAX_CHARS) {
            return;
        }
        int excess = len - MAX_CHARS;
        try {
            doc.remove(0, excess);
        } catch (BadLocationException ignored) {
        }
    }
}
