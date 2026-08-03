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

        setLayout(new BorderLayout());
        add(center, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        setMinimumSize(new Dimension(720, 420));
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
}
