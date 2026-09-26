package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.config.MacroFile;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;

/**
 * Create or edit one macro group. Delete stays disabled until the macro already exists.
 */
public final class MacroEditDialog extends JDialog {

    private MacroEditDialog(Window owner, AppController app, String originalName, String body, boolean creating) {
        super(owner, creating ? "New macro" : "Edit macro", ModalityType.APPLICATION_MODAL);

        JTextField nameField = new JTextField(originalName == null ? "" : originalName, 24);
        JPanel nameRow = new JPanel(new BorderLayout(6, 0));
        nameRow.setBackground(UiColors.PANEL_BG);
        nameRow.add(new JLabel("Macro name:"), BorderLayout.WEST);
        nameRow.add(nameField, BorderLayout.CENTER);

        JTextArea lines = new JTextArea(body == null ? "" : body, 12, 48);
        lines.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        lines.setLineWrap(false);
        JScrollPane scroll = new JScrollPane(lines,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(480, 220));

        JButton delete = new JButton("Delete");
        delete.setEnabled(!creating);
        delete.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Delete macro \"" + originalName + "\"?",
                    "Delete macro",
                    JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
            String err = app.deleteMacro(originalName);
            if (err != null) {
                JOptionPane.showMessageDialog(this, err, "PactorRATT_Alpha", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dispose();
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            String err = app.saveMacro(originalName, nameField.getText(), lines.getText());
            if (err != null) {
                JOptionPane.showMessageDialog(this, err, "PactorRATT_Alpha", JOptionPane.WARNING_MESSAGE);
                return;
            }
            dispose();
        });

        JPanel buttons = new JPanel(new BorderLayout());
        buttons.setBackground(UiColors.PANEL_BG);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);
        left.add(delete);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(cancel);
        right.add(save);
        buttons.add(left, BorderLayout.WEST);
        buttons.add(right, BorderLayout.EAST);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBackground(UiColors.PANEL_BG);
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.add(nameRow, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);

        setContentPane(root);
        getRootPane().setDefaultButton(save);
        pack();
        setLocationRelativeTo(owner);
    }

    public static void openNew(Window owner, AppController app) {
        new MacroEditDialog(owner, app, null, "", true).setVisible(true);
    }

    public static void open(Window owner, AppController app, MacroFile.Macro macro) {
        new MacroEditDialog(owner, app, macro.name(), macro.bodyText(), false).setVisible(true);
    }
}
