package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.hostmode.CompatResult;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Non-modal *in testing* fingerprint notice. OK or a 5 s countdown closes it.
 */
public final class CompatNotifyDialog {

    public static final String PREVIEW_STARRED_LABEL = "in testing PK-900";
    private static final int COUNTDOWN_SECONDS = 5;

    private CompatNotifyDialog() {
    }

    /**
     * @param starredLabel text between {@code *} in Compat_Memory_Map (e.g. {@code in testing PK-900})
     */
    public static void show(Window owner, String starredLabel) {
        String label = starredLabel == null || starredLabel.isBlank()
                ? PREVIEW_STARRED_LABEL
                : starredLabel.trim();
        String body = label + CompatResult.NOTIFY_BUGS_SUFFIX;

        JDialog dialog = new JDialog(owner, "TNC compatibility", Dialog.ModalityType.MODELESS);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel text = new JLabel(body, SwingConstants.CENTER);
        text.setOpaque(true);
        text.setBackground(UiColors.PANEL_BG);
        text.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));

        JButton ok = new JButton(okLabel(COUNTDOWN_SECONDS));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UiColors.PANEL_BG);
        buttonPanel.add(ok);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UiColors.PANEL_BG);
        content.add(text, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        dialog.getContentPane().add(content);
        dialog.getContentPane().setBackground(UiColors.PANEL_BG);

        int[] remaining = {COUNTDOWN_SECONDS};
        Timer timer = new Timer(1000, e -> {
            remaining[0]--;
            if (remaining[0] <= 0) {
                ((Timer) e.getSource()).stop();
                dialog.dispose();
                return;
            }
            ok.setText(okLabel(remaining[0]));
        });
        timer.setRepeats(true);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                timer.start();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                timer.stop();
            }
        });
        ok.addActionListener(e -> {
            timer.stop();
            dialog.dispose();
        });

        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }

    private static String okLabel(int seconds) {
        return "OK (" + seconds + ")";
    }
}
