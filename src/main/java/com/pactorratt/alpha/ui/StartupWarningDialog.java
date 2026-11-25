package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.BuildInfo;
import com.pactorratt.alpha.config.ConfigStore;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Blocking experimental-build warning shown before the main window.
 *
 * @return {@code true} if the user chose to continue, {@code false} if they chose to exit
 */
public final class StartupWarningDialog extends JDialog {

    private boolean continueLaunch;

    private StartupWarningDialog(Path portableRoot) {
        super((Frame) null, "PactorRATT_Alpha — Warning", true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JLabel warning = new JLabel("!!WARNING!!", SwingConstants.CENTER);
        warning.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
        warning.setForeground(Color.RED);
        warning.setBorder(BorderFactory.createEmptyBorder(24, 24, 16, 24));

        Timer flash = new Timer(500, e -> {
            if (Color.RED.equals(warning.getForeground())) {
                warning.setForeground(new Color(0x66, 0x00, 0x00));
            } else {
                warning.setForeground(Color.RED);
            }
        });
        flash.setRepeats(true);
        flash.start();

        JTextArea body = new JTextArea("""
                PactorRATT_Alpha is in active development.
                It WILL: Wipe all your settings on your PK-232
                It MAY:  Leave your radio in transmit, dont use unattended
                it probly WONT: Do anything terrible.

                The program should properly exit host mode when closed normally.
                """);
        body.setEditable(false);
        body.setFocusable(false);
        body.setOpaque(false);
        body.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        body.setForeground(Color.BLACK);

        String thanks = testerThanksLine(portableRoot);
        body.setBorder(BorderFactory.createEmptyBorder(8, 28, thanks.isEmpty() ? 16 : 4, 28));

        TesterGlintLabel thanksLabel = null;
        if (!thanks.isEmpty()) {
            thanksLabel = new TesterGlintLabel(thanks);
        }
        final Timer glint = startTesterGlint(thanksLabel);

        JLabel runtimeInfo = new JLabel(
                "Java: " + javaVersion()
                        + "    |    Build: " + BuildInfo.buildNumber() + "  " + BuildInfo.buildTime(),
                SwingConstants.CENTER);
        runtimeInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        runtimeInfo.setForeground(Color.DARK_GRAY);
        runtimeInfo.setBorder(BorderFactory.createEmptyBorder(4, 28, 8, 28));

        JButton riskIt = new JButton("Risk it for the Biscuit");
        riskIt.setFont(riskIt.getFont().deriveFont(Font.BOLD, 14f));
        riskIt.addActionListener(e -> {
            continueLaunch = true;
            flash.stop();
            if (glint != null) {
                glint.stop();
            }
            dispose();
        });

        JButton bailOut = new JButton("Dont break my stuff");
        bailOut.setFont(bailOut.getFont().deriveFont(14f));
        bailOut.addActionListener(e -> {
            continueLaunch = false;
            flash.stop();
            if (glint != null) {
                glint.stop();
            }
            dispose();
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        buttons.setBorder(BorderFactory.createEmptyBorder(8, 16, 20, 16));
        buttons.add(bailOut);
        buttons.add(riskIt);

        JPanel textBlock = new JPanel(new BorderLayout());
        textBlock.setOpaque(false);
        textBlock.add(body, BorderLayout.NORTH);
        if (thanksLabel != null) {
            textBlock.add(thanksLabel, BorderLayout.SOUTH);
        }

        JPanel message = new JPanel(new BorderLayout());
        message.setOpaque(false);
        message.add(textBlock, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.add(warning, BorderLayout.NORTH);
        center.add(message, BorderLayout.CENTER);
        center.add(runtimeInfo, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(center, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        setMinimumSize(new Dimension(720, 440));
        pack();
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(bailOut);
    }

    /** Shows the modal warning. Returns true to continue launch, false to exit. */
    public static boolean confirmContinue(Path portableRoot) {
        StartupWarningDialog dialog = new StartupWarningDialog(portableRoot);
        dialog.setVisible(true);
        return dialog.continueLaunch;
    }

    /**
     * Extra line for known testers. Callsign is {@code callsign} in {@code config/settings.json}.
     */
    private static String testerThanksLine(Path portableRoot) {
        String call = new ConfigStore(portableRoot).load().getCallsign();
        if (call == null || call.isBlank()) {
            return "";
        }
        return switch (call.trim().toUpperCase(Locale.ROOT)) {
            case "AA6DW" -> "Thanks for testing Alan";
            case "KI6QI" -> "Thanks for testing Bob";
            case "KJ7RBS" -> "Christ is King!";
            default -> "";
        };
    }

    /** Gold thank-you line, two points above the 14-pt body, with a 0.5 Hz glint sweep. */
    private static Timer startTesterGlint(TesterGlintLabel label) {
        if (label == null) {
            return null;
        }
        long startNanos = System.nanoTime();
        Timer timer = new Timer(40, e -> {
            float cycles = (System.nanoTime() - startNanos) * 0.5f / 1_000_000_000f;
            label.setPhase(cycles - (float) Math.floor(cycles));
        });
        timer.setRepeats(true);
        timer.start();
        return timer;
    }

    /** Gold text with a bright band that crosses once every two seconds. */
    private static final class TesterGlintLabel extends JComponent {
        private static final Color GOLD = new Color(0xFF, 0xD7, 0x00);
        private static final int BODY_POINT_SIZE = 14;

        private final String text;
        private float phase;

        TesterGlintLabel(String text) {
            this.text = text;
            setFont(new Font(Font.SERIF, Font.BOLD, 40));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(0, 28, 12, 28));
        }

        void setPhase(float phase) {
            this.phase = phase;
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            Insets in = getInsets();
            return new Dimension(
                    fm.stringWidth(text) + in.left + in.right,
                    fm.getHeight() + in.top + in.bottom);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                Insets in = getInsets();
                int textW = fm.stringWidth(text);
                int x = in.left + Math.max(0, (getWidth() - in.left - in.right - textW) / 2);
                int y = in.top + (getHeight() - in.top - in.bottom - fm.getHeight()) / 2 + fm.getAscent();

                g2.setColor(GOLD);
                g2.drawString(text, x, y);

                if (textW <= 0) {
                    return;
                }
                GlyphVector glyphs = getFont().createGlyphVector(g2.getFontRenderContext(), text);
                Shape outline = glyphs.getOutline(x, y);
                g2.setClip(outline);
                float band = Math.max(22f, textW * 0.28f);
                float center = x - band + phase * (textW + band * 2f);
                float x0 = center - band;
                float x1 = center + band;
                if (x1 <= x0) {
                    return;
                }
                g2.setPaint(new LinearGradientPaint(
                        x0, 0f, x1, 0f,
                        new float[] {0f, 0.5f, 1f},
                        new Color[] {
                                new Color(255, 255, 255, 0),
                                new Color(255, 255, 236, 230),
                                new Color(255, 255, 255, 0)
                        }));
                g2.fillRect(0, 0, getWidth(), getHeight());
            } finally {
                g2.dispose();
            }
        }
    }

    private static String javaVersion() {
        String version = System.getProperty("java.version");
        if (version == null || version.isBlank()) {
            return "unknown";
        }
        String vendor = System.getProperty("java.vendor");
        if (vendor != null && !vendor.isBlank()) {
            return version + " (" + vendor + ")";
        }
        return version;
    }
}
