package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Dev-tools check for the Host {@code PD} repeat-count bug.
 * The safety checkbox is an attestation only; it does not change the radio port.
 */
public final class PdBugCheckWindow extends JFrame {

    private static final Color BUG_RED = Color.RED;
    private static final Color OK_GREEN = new Color(0x00, 0x80, 0x00);
    private static final Color UNEXPECTED_BLUE = Color.BLUE;

    private final AppController app;
    private final JCheckBox safetyBox =
            new JCheckBox("Radio OFF or switch TNC to Radio 2 port");
    private final JButton checkButton = new JButton("Check for Bug");
    private final JLabel payloadLabel = new JLabel(" ");
    private final JLabel statusLabel = new JLabel("Ready");
    private final JLabel verdictLabel = new JLabel(" ");
    private final JLabel millisLabel = new JLabel(" ");
    /** True while a check owns this window, including a watch started before it was reopened. */
    private boolean runActive;

    public PdBugCheckWindow(AppController app) {
        super("PD bug check");
        this.app = app;
        buildUi();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                app.onPdBugCheckClosed(PdBugCheckWindow.this);
            }
        });
        setSize(560, 320);
        setMinimumSize(new Dimension(480, 280));
        setLocationByPlatform(true);
    }

    private void buildUi() {
        getContentPane().setBackground(UiColors.WINDOW_BG);
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(UiColors.WINDOW_BG);
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));

        safetyBox.setBackground(UiColors.WINDOW_BG);
        safetyBox.setAlignmentX(LEFT_ALIGNMENT);
        safetyBox.addItemListener(e -> {
            if (!runActive) {
                checkButton.setEnabled(safetyBox.isSelected());
            }
        });

        checkButton.setEnabled(false);
        checkButton.setAlignmentX(LEFT_ALIGNMENT);
        checkButton.addActionListener(e -> app.startPdBugCheck());

        payloadLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        payloadLabel.setAlignmentX(LEFT_ALIGNMENT);

        statusLabel.setAlignmentX(LEFT_ALIGNMENT);

        form.add(safetyBox);
        form.add(Box.createVerticalStrut(8));
        form.add(checkButton);
        form.add(Box.createVerticalStrut(12));
        form.add(payloadLabel);
        form.add(Box.createVerticalStrut(4));
        form.add(statusLabel);

        JPanel verdictPanel = new JPanel(new GridBagLayout());
        verdictPanel.setBackground(UiColors.WINDOW_BG);
        verdictLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
        verdictLabel.setHorizontalAlignment(SwingConstants.CENTER);
        millisLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        millisLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel verdictStack = new JPanel();
        verdictStack.setLayout(new BoxLayout(verdictStack, BoxLayout.Y_AXIS));
        verdictStack.setBackground(UiColors.WINDOW_BG);
        verdictLabel.setAlignmentX(CENTER_ALIGNMENT);
        millisLabel.setAlignmentX(CENTER_ALIGNMENT);
        verdictStack.add(verdictLabel);
        verdictStack.add(Box.createVerticalStrut(4));
        verdictStack.add(millisLabel);
        verdictPanel.add(verdictStack);

        add(form, BorderLayout.NORTH);
        add(verdictPanel, BorderLayout.CENTER);
    }

    /** Connected run accepted. Clears any previous verdict. EDT. */
    public void beginRun(String payload) {
        runActive = true;
        checkButton.setEnabled(false);
        payloadLabel.setText(payload == null || payload.isEmpty() ? " " : payload);
        statusLabel.setText("Sending PD1,3…");
        clearVerdict();
    }

    /** EDT. */
    public void setStatus(String text) {
        statusLabel.setText(text == null || text.isEmpty() ? " " : text);
    }

    /**
     * No open Host session. Leaves any previous verdict in place and follows the checkbox.
     * EDT.
     */
    public void noteNotConnected() {
        statusLabel.setText("No open TNC serial session.");
        endRun();
    }

    /** Traffic-to-Idle dwell. EDT. */
    public void showVerdict(String verdict, Color color, long dwellMs) {
        verdictLabel.setText(verdict);
        verdictLabel.setForeground(color);
        millisLabel.setText(dwellMs + " ms");
        millisLabel.setForeground(color);
        statusLabel.setText("Idle");
        endRun();
    }

    /** Send failed or the Host session closed. Verdict stays blank. EDT. */
    public void showFailure(String status) {
        clearVerdict();
        statusLabel.setText(status == null || status.isEmpty() ? " " : status);
        endRun();
    }

    /**
     * The watch that was in flight when this window (or its predecessor) closed has ended.
     * No dwell verdict is shown. EDT.
     */
    public void releaseAfterDroppedWatch() {
        statusLabel.setText("Ready");
        endRun();
    }

    /** Reopened while a previous watch is still waiting on the TNC. EDT. */
    public void holdForInProgress(String status) {
        runActive = true;
        checkButton.setEnabled(false);
        statusLabel.setText(status == null || status.isEmpty() ? " " : status);
    }

    private void endRun() {
        runActive = false;
        checkButton.setEnabled(safetyBox.isSelected());
    }

    private void clearVerdict() {
        verdictLabel.setText(" ");
        verdictLabel.setForeground(Color.BLACK);
        millisLabel.setText(" ");
        millisLabel.setForeground(Color.BLACK);
    }

    public static Color bugColor() {
        return BUG_RED;
    }

    public static Color okColor() {
        return OK_GREEN;
    }

    public static Color unexpectedColor() {
        return UNEXPECTED_BLUE;
    }
}
