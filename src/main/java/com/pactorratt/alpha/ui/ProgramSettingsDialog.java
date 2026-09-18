package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.config.AppConfig;
import com.pactorratt.alpha.config.CommitMode;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;

public final class ProgramSettingsDialog extends JDialog {

    public ProgramSettingsDialog(Window owner, AppController app) {
        super(owner, "Settings — Program", ModalityType.APPLICATION_MODAL);
        AppConfig config = app.config();

        JTextField callsign = new JTextField(config.getCallsign(), 12);
        JRadioButton line = new JRadioButton("Line (Enter commits line)", config.getCommitMode() == CommitMode.LINE);
        JRadioButton message = new JRadioButton("Message (Send commits compose)", config.getCommitMode() == CommitMode.MESSAGE);
        ButtonGroup commitGroup = new ButtonGroup();
        commitGroup.add(line);
        commitGroup.add(message);

        JCheckBox listenOnStart = new JCheckBox("Listen on start", config.isListenOnStart());
        JCheckBox debugLog = new JCheckBox("Debug log", config.isDebugLogEnabled());
        JCheckBox displayStartup = new JCheckBox("Display Startup", config.isDisplayStartup());
        displayStartup.setToolTipText("Show PK-232 startup text and TNC firmware/hardware ($0009) windows on connect.");
        JTextField handover = new JTextField(config.getCannedHandoverText(), 20);
        JTextField disconnect = new JTextField(config.getCannedDisconnectText(), 20);
        JTextField cannedCq = new JTextField(config.getCannedCqText(), 20);
        cannedCq.setToolTipText("Listen CQ button sends this line CQ-repeat times (e.g. CQ de KJ7RBS)");
        JTextField wrap = new JTextField(Integer.toString(config.getWrapColumns()), 4);

        JCheckBox fec200 = new JCheckBox("FEC 200", config.isFec200());
        fec200.setToolTipText("PTSend baud: checked = 200 (n=2), unchecked = 100 (n=1)");
        JSpinner fecRetries = new JSpinner(new SpinnerNumberModel(config.getFecRetries(), 1, 5, 1));
        fecRetries.setToolTipText("PTSend unproto repeats (x), 1–5");
        JPanel fecRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        fecRow.setOpaque(false);
        fecRow.add(fec200);
        fecRow.add(new JLabel("Retries"));
        fecRow.add(fecRetries);

        JSpinner cqRepeat = new JSpinner(new SpinnerNumberModel(config.getCqRepeat(), 0, 10, 1));
        cqRepeat.setToolTipText("Listen CQ button: how many copies of canned CQ text to send. 0 = nothing.");
        JPanel cqRepeatRow = labeled("CQ repeat", cqRepeat);

        JSpinner opPoll = new JSpinner(new SpinnerNumberModel(config.getOpPoll(), 0, 10, 1));
        opPoll.setToolTipText("OPMODE polls per second while an ARQ window is linked (not dead). 0 = off.");
        JPanel opPollRow = labeled("OPPOLL", opPoll);

        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        form.add(labeled("Local callsign", callsign));
        form.add(line);
        form.add(message);
        form.add(listenOnStart);
        form.add(debugLog);
        form.add(displayStartup);
        form.add(labeled("Canned handover text", handover));
        form.add(labeled("Canned disconnect text", disconnect));
        form.add(labeled("Canned CQ text", cannedCq));
        form.add(cqRepeatRow);
        form.add(labeled("Wrap columns", wrap));
        form.add(fecRow);
        form.add(opPollRow);

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            config.setCallsign(callsign.getText());
            config.setCommitMode(line.isSelected() ? CommitMode.LINE : CommitMode.MESSAGE);
            config.setListenOnStart(listenOnStart.isSelected());
            config.setDebugLogEnabled(debugLog.isSelected());
            config.setDisplayStartup(displayStartup.isSelected());
            config.setCannedHandoverText(handover.getText());
            config.setCannedDisconnectText(disconnect.getText());
            config.setCannedCqText(cannedCq.getText());
            config.setCqRepeat((Integer) cqRepeat.getValue());
            try {
                config.setWrapColumns(Integer.parseInt(wrap.getText().trim()));
            } catch (NumberFormatException ignored) {
                config.setWrapColumns(80);
            }
            config.setFec200(fec200.isSelected());
            config.setFecRetries((Integer) fecRetries.getValue());
            config.setOpPoll((Integer) opPoll.getValue());
            app.saveConfig();
            dispose();
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancel);
        buttons.add(save);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private static JPanel labeled(String label, java.awt.Component field) {
        JPanel p = new JPanel(new BorderLayout(6, 0));
        p.add(new JLabel(label), BorderLayout.WEST);
        p.add(field, BorderLayout.CENTER);
        return p;
    }
}
