package com.pactorratt.alpha.ui;

import com.fazecast.jSerialComm.SerialPort;
import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.config.AppConfig;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;

public final class ComPortDialog extends JDialog {

    public ComPortDialog(Window owner, AppController app) {
        super(owner, "Settings — COM Port", ModalityType.APPLICATION_MODAL);
        AppConfig config = app.config();

        JComboBox<String> ports = new JComboBox<>();
        ports.setEditable(true);
        String portEnumFailure = null;
        try {
            SerialPort[] found = SerialPort.getCommPorts();
            if (found != null) {
                for (SerialPort port : found) {
                    ports.addItem(port.getSystemPortName());
                }
            }
        } catch (Throwable t) {
            portEnumFailure = t.getClass().getSimpleName()
                    + (t.getMessage() == null ? "" : ": " + t.getMessage());
        }
        if (!config.getComPort().isBlank()) {
            ports.setSelectedItem(config.getComPort());
        }

        JComboBox<Integer> baud = new JComboBox<>(new Integer[]{
                300, 600, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200
        });
        baud.setSelectedItem(config.getBaudRate());

        JComboBox<Integer> dataBits = new JComboBox<>(new Integer[]{7, 8});
        dataBits.setSelectedItem(config.getDataBits());

        JComboBox<String> parity = new JComboBox<>(new String[]{"NONE", "EVEN", "ODD"});
        parity.setSelectedItem(config.getParity());

        JComboBox<Integer> stopBits = new JComboBox<>(new Integer[]{1, 2});
        stopBits.setSelectedItem(config.getStopBits());

        JComboBox<String> flow = new JComboBox<>(new String[]{"NONE", "RTS_CTS", "XON_XOFF"});
        flow.setSelectedItem(config.getFlowControl());

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        form.add(new JLabel("Port"));
        form.add(ports);
        form.add(new JLabel("Baud"));
        form.add(baud);
        form.add(new JLabel("Data bits"));
        form.add(dataBits);
        form.add(new JLabel("Parity"));
        form.add(parity);
        form.add(new JLabel("Stop bits"));
        form.add(stopBits);
        form.add(new JLabel("Flow control"));
        form.add(flow);

        JLabel summary = new JLabel(portEnumFailure == null
                ? "Default first-run: 1200 7N1"
                : "Could not list serial ports. You can still type a port name (e.g. COM3).");
        if (portEnumFailure != null) {
            summary.setForeground(new Color(0xB00020));
            summary.setToolTipText(portEnumFailure);
        }
        summary.setBorder(new EmptyBorder(0, 10, 8, 10));

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            Object portSel = ports.getSelectedItem();
            config.setComPort(portSel == null ? "" : portSel.toString());
            config.setBaudRate((Integer) baud.getSelectedItem());
            config.setDataBits((Integer) dataBits.getSelectedItem());
            config.setParity((String) parity.getSelectedItem());
            config.setStopBits((Integer) stopBits.getSelectedItem());
            config.setFlowControl((String) flow.getSelectedItem());
            app.saveConfig();
            if (app.isTncConnected() || app.isTncBusy()) {
                app.disconnectTnc();
            } else {
                app.setTncConnected(false);
            }
            dispose();
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancel);
        buttons.add(save);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(summary, BorderLayout.NORTH);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }
}
