package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.config.AppConfig;
import com.pactorratt.alpha.hostmode.CompatResult;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;

/**
 * Compatibility warn dialog with a selectable fingerprint line and copy-to-clipboard.
 */
public final class CompatWarningDialog {

    private CompatWarningDialog() {
    }

    /**
     * @return {@link JOptionPane#YES_OPTION} to continue, otherwise decline/close
     */
    public static int show(Window owner, CompatResult compat) {
        String hex = compat == null ? "" : compat.hexFingerprint();
        String copyText = "Unknown TNC fingerprint " + hex;

        JTextField field = new JTextField(copyText, 36);
        field.setEditable(false);
        field.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        field.setCaretPosition(0);
        field.setSelectionStart(0);
        field.setSelectionEnd(copyText.length());

        JButton copy = new JButton("Copy to clipboard");
        copy.addActionListener(e -> {
            field.selectAll();
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(copyText), null);
            copy.setText("Copied");
        });

        JPanel fieldRow = new JPanel(new BorderLayout(8, 0));
        fieldRow.setOpaque(false);
        fieldRow.add(field, BorderLayout.CENTER);
        fieldRow.add(copy, BorderLayout.EAST);

        JPanel body = new JPanel(new GridLayout(0, 1, 0, 8));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        String label = compat == null ? "" : compat.label();
        if (label != null && !label.isBlank() && !"unknown".equalsIgnoreCase(label)) {
            body.add(new JLabel(label + " (fingerprint " + hex + ")."));
        }
        body.add(fieldRow);
        body.add(new JLabel("You may continue at your own risk."));
        body.add(new JLabel("Please email the fingerprint to " + AppConfig.SUPPORT_EMAIL + "."));
        body.add(new JLabel("Continue connecting?"));

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.add(body);

        return JOptionPane.showConfirmDialog(
                owner,
                wrap,
                "TNC compatibility warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
    }
}
