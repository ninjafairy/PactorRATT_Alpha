package com.pactorratt.alpha.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import java.awt.Frame;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Blocking experimental-build warning shown before the main window.
 *
 * @return {@code true} if the user chose to continue, {@code false} if they chose to exit
 */
public final class StartupWarningDialog extends JDialog {

    private boolean continueLaunch;

    private StartupWarningDialog() {
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
                You have gotten your hands on an untested experimental build of PactoRATT_Alpha
                It WILL: Wipe all your settings on your PK-232
                             Leave your PK-232 in HOSTMODE
                It MAY:  Leave your radio in transmit, dont use unattended
                it probly WONT: Do a proper ARQ link yet, Work, do your taxes, mow your lawn

                Use the debug terminal to exit host mode
                Cmd: HO Payload: N
                """);
        body.setEditable(false);
        body.setFocusable(false);
        body.setOpaque(false);
        body.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        body.setForeground(Color.BLACK);
        body.setBorder(BorderFactory.createEmptyBorder(8, 28, 16, 28));

        JLabel runtimeInfo = new JLabel(
                "Java: " + javaVersion() + "    |    Build: " + jarBuildTime(),
                SwingConstants.CENTER);
        runtimeInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        runtimeInfo.setForeground(Color.DARK_GRAY);
        runtimeInfo.setBorder(BorderFactory.createEmptyBorder(4, 28, 8, 28));

        JButton riskIt = new JButton("Risk it for the Biscuit");
        riskIt.setFont(riskIt.getFont().deriveFont(Font.BOLD, 14f));
        riskIt.addActionListener(e -> {
            continueLaunch = true;
            flash.stop();
            dispose();
        });

        JButton bailOut = new JButton("Dont break my stuff");
        bailOut.setFont(bailOut.getFont().deriveFont(14f));
        bailOut.addActionListener(e -> {
            continueLaunch = false;
            flash.stop();
            dispose();
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        buttons.setBorder(BorderFactory.createEmptyBorder(8, 16, 20, 16));
        buttons.add(bailOut);
        buttons.add(riskIt);

        JPanel center = new JPanel(new BorderLayout());
        center.add(warning, BorderLayout.NORTH);
        center.add(body, BorderLayout.CENTER);
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
    public static boolean confirmContinue() {
        StartupWarningDialog dialog = new StartupWarningDialog();
        dialog.setVisible(true);
        return dialog.continueLaunch;
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

    /** Reads {@code Build-Time} from the running jar's manifest (set at Maven package). */
    private static String jarBuildTime() {
        String fromManifest = readManifestValue("Build-Time");
        if (fromManifest != null) {
            return fromManifest;
        }
        return "unknown (not a packaged jar)";
    }

    private static String readManifestValue(String key) {
        try {
            Enumeration<URL> resources = StartupWarningDialog.class.getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                try (InputStream in = url.openStream()) {
                    Manifest mf = new Manifest(in);
                    Attributes attrs = mf.getMainAttributes();
                    // Prefer our shaded app jar (has Main-Class).
                    String mainClass = attrs.getValue(Attributes.Name.MAIN_CLASS);
                    String value = attrs.getValue(key);
                    if (value != null && !value.isBlank()
                            && mainClass != null
                            && mainClass.contains("PactorRattAlphaApp")) {
                        return value.trim();
                    }
                }
            }
            // Fallback: first manifest that has Build-Time.
            resources = StartupWarningDialog.class.getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                try (InputStream in = resources.nextElement().openStream()) {
                    String value = new Manifest(in).getMainAttributes().getValue(key);
                    if (value != null && !value.isBlank() && !value.contains("${")) {
                        return value.trim();
                    }
                }
            }
        } catch (IOException ignored) {
            // Fall through.
        }
        return null;
    }
}
