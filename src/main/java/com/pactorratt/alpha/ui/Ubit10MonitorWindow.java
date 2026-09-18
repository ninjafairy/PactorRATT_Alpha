package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.hostmode.HostFrameCodec;
import com.pactorratt.alpha.hostmode.LinkMessageParser;
import com.pactorratt.alpha.serial.SerialByteListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Debug view of Host CTL {@code $50} frames (link messages and UBIT 10 status pushes).
 * Radio buttons send Host {@code UB10 ON} / {@code UB10 OFF}.
 */
public final class Ubit10MonitorWindow extends JFrame implements SerialByteListener {

    private static final int MAX_CHARS = 200_000;

    private final AppController app;
    private final JTextArea textArea = new JTextArea();
    private final JButton pauseButton = new JButton("Pause");
    private final JRadioButton enableButton = new JRadioButton("Enable");
    private final JRadioButton disableButton = new JRadioButton("Disable");
    private volatile boolean paused;
    private boolean syncingRadios;
    private boolean radiosBusy;

    private final HostFrameCodec.FrameParser rxParser = new HostFrameCodec.FrameParser();
    private final Object rxLock = new Object();

    public Ubit10MonitorWindow(AppController app) {
        super("TNC UBIT 10");
        this.app = app;
        app.addSerialByteListener(this);
        buildUi();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                app.removeSerialByteListener(Ubit10MonitorWindow.this);
                app.onUbit10MonitorClosed(Ubit10MonitorWindow.this);
            }
        });
        setSize(720, 420);
        setLocationByPlatform(true);
        refreshUbit10FromTnc();
    }

    private void buildUi() {
        getContentPane().setBackground(UiColors.WINDOW_BG);
        setLayout(new BorderLayout(6, 6));

        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        north.setBackground(UiColors.PANEL_BG);
        north.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        north.add(new JLabel("UBIT 10:"));

        ButtonGroup group = new ButtonGroup();
        group.add(enableButton);
        group.add(disableButton);
        disableButton.setSelected(true);
        enableButton.setBackground(UiColors.PANEL_BG);
        disableButton.setBackground(UiColors.PANEL_BG);
        enableButton.addActionListener(e -> onRadioChosen(true));
        disableButton.addActionListener(e -> onRadioChosen(false));
        north.add(enableButton);
        north.add(disableButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> textArea.setText(""));
        pauseButton.addActionListener(e -> {
            paused = !paused;
            pauseButton.setText(paused ? "Resume" : "Pause");
        });
        north.add(clearButton);
        north.add(pauseButton);

        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        textArea.setBackground(UiColors.TRANSCRIPT_BG);
        textArea.setLineWrap(false);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));

        add(north, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    /** Query TNC UBIT 10 and align the radios. Safe to call after Connect. */
    public void refreshUbit10FromTnc() {
        setRadiosBusy(true);
        app.queryUbit10(enabled -> {
            applyRadioState(enabled);
            setRadiosBusy(false);
        });
    }

    private void onRadioChosen(boolean enable) {
        if (syncingRadios || radiosBusy) {
            return;
        }
        setRadiosBusy(true);
        app.setUbit10(enable, error -> {
            setRadiosBusy(false);
            if (error != null) {
                applyRadioState(!enable);
                JOptionPane.showMessageDialog(this, error, "TNC UBIT 10", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void applyRadioState(Boolean enabled) {
        syncingRadios = true;
        try {
            if (Boolean.TRUE.equals(enabled)) {
                enableButton.setSelected(true);
            } else {
                disableButton.setSelected(true);
            }
        } finally {
            syncingRadios = false;
        }
    }

    private void setRadiosBusy(boolean busy) {
        radiosBusy = busy;
        enableButton.setEnabled(!busy);
        disableButton.setEnabled(!busy);
    }

    @Override
    public void onSerialBytes(boolean transmit, byte[] data, int offset, int length) {
        if (paused || transmit || length <= 0) {
            return;
        }
        List<HostFrameCodec.Frame> frames = new ArrayList<>();
        synchronized (rxLock) {
            int end = offset + length;
            for (int i = offset; i < end; i++) {
                HostFrameCodec.Frame frame = rxParser.feed(data[i]);
                if (frame != null && (frame.ctl & 0xFF) == 0x50) {
                    frames.add(frame);
                }
            }
        }
        for (HostFrameCodec.Frame frame : frames) {
            String raw = HostMonitorFormat.formatLine(false, frame.raw);
            String decoded = LinkMessageParser.describe(frame);
            if (decoded == null) {
                decoded = "unrecognized $50 frame";
            }
            String line = raw + System.lineSeparator() + "  " + decoded;
            SwingUtilities.invokeLater(() -> appendBlock(line));
        }
    }

    private void appendBlock(String block) {
        textArea.append(block);
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
