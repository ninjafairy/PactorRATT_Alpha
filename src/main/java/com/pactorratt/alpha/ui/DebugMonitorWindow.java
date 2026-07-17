package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.serial.SerialByteListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Live view of raw serial port traffic (hex + ASCII).
 */
public final class DebugMonitorWindow extends JFrame implements SerialByteListener {

    private static final int MAX_CHARS = 200_000;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final AppController app;
    private final JTextArea textArea = new JTextArea();
    private final JButton pauseButton = new JButton("Pause");
    private final JTextField cmdField = new JTextField(4);
    private final JTextField payloadField = new JTextField(20);
    private final JButton sendButton = new JButton("Send");
    private volatile boolean paused;

    public DebugMonitorWindow(AppController app) {
        super("TNC Debug Monitor");
        this.app = app;
        app.addSerialByteListener(this);
        buildUi();
        sendButton.setEnabled(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                app.removeSerialByteListener(DebugMonitorWindow.this);
                app.onDebugMonitorClosed(DebugMonitorWindow.this);
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
        clearButton.addActionListener(e -> textArea.setText(""));

        pauseButton.addActionListener(e -> {
            paused = !paused;
            pauseButton.setText(paused ? "Resume" : "Pause");
        });

        controlsRow.add(clearButton);
        controlsRow.add(pauseButton);

        JPanel sendRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        sendRow.setBackground(UiColors.PANEL_BG);
        sendRow.add(new JLabel("Cmd"));
        sendRow.add(cmdField);
        sendRow.add(new JLabel("Payload"));
        sendRow.add(payloadField);
        sendRow.add(sendButton);

        sendButton.addActionListener(e -> onSendClicked());

        south.add(controlsRow);
        south.add(Box.createVerticalStrut(4));
        south.add(sendRow);

        add(scroll, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    private void onSendClicked() {
        String cmd = cmdField.getText().trim();
        if (cmd.length() != 2) {
            JOptionPane.showMessageDialog(this,
                    "Host command must be exactly 2 characters (e.g. HP, MM, AE).",
                    "TNC Debug Monitor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String payload = payloadField.getText().trim();
        String error = app.sendDebugHostCommand(cmd, payload);
        if (error != null) {
            JOptionPane.showMessageDialog(this,
                    error,
                    "TNC Debug Monitor",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onSerialBytes(boolean transmit, byte[] data, int offset, int length) {
        if (paused || length <= 0) {
            return;
        }
        byte[] chunk = Arrays.copyOfRange(data, offset, offset + length);
        String line = formatLine(transmit, chunk);
        SwingUtilities.invokeLater(() -> appendLine(line));
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
            // Best-effort trim; ignore if document changed concurrently.
        }
    }

    private static String formatLine(boolean transmit, byte[] data) {
        String time = LocalTime.now().format(TIME_FMT);
        String dir = transmit ? "TX" : "RX";
        StringBuilder hex = new StringBuilder(data.length * 3);
        StringBuilder ascii = new StringBuilder(data.length);
        for (int i = 0; i < data.length; i++) {
            int v = data[i] & 0xFF;
            if (i > 0) {
                hex.append(' ');
            }
            hex.append(String.format("%02X", v));
            if (v >= 0x20 && v <= 0x7E) {
                ascii.append((char) v);
            } else {
                ascii.append('.');
            }
        }
        return time + " " + dir + "  " + hex + "  | " + ascii;
    }
}
