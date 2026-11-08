package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.config.AppConfig;
import com.pactorratt.alpha.config.CommitMode;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class ProgramSettingsDialog extends JDialog {

    private final JTextArea cannedCqArea;
    private final JTextArea cqFooterArea;
    private final JSpinner cqRepeatSpinner;

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
        listenOnStart.setToolTipText("After the TNC connects, open FEC/Monitor once this launch.");
        JCheckBox autoConnectTnc = new JCheckBox("Autoconnect", config.isAutoConnectTnc());
        autoConnectTnc.setToolTipText("On startup, run TNC → Connect after the main window opens.");
        JCheckBox onlyOneArqWindow = new JCheckBox("Only 1 ARQ window", config.isOnlyOneArqWindow());
        onlyOneArqWindow.setToolTipText("When a new ARQ link opens, close any previous ARQ window.");
        JCheckBox closeFecOnArqLink = new JCheckBox("Close FEC on ARQ", config.isCloseFecOnArqLink());
        closeFecOnArqLink.setToolTipText(
                "When an ARQ link opens, close the FEC window and turn Listen off. Does not send Pt.");
        JCheckBox suppressConnect = new JCheckBox("Suppress connect frames", config.isSuppressConnectFrames());
        suppressConnect.setToolTipText(
                "When a new FEC line ends with <C>, remove that line. Lines already on screen stay. The Connect list still updates.");
        int stampMode = config.getTranscriptTimestamp();
        JRadioButton stampOff = new JRadioButton("Off", stampMode == AppConfig.TIMESTAMP_OFF);
        JRadioButton stampHm = new JRadioButton("Hour and minute", stampMode == AppConfig.TIMESTAMP_HM);
        JRadioButton stampDate = new JRadioButton("Date and time", stampMode == AppConfig.TIMESTAMP_DATE);
        stampOff.setToolTipText("No timestamp on transcript lines.");
        stampHm.setToolTipText("Start each new transcript line with [HH:mm]. Example: [15:04] ");
        stampDate.setToolTipText(
                "Start each new transcript line with [DD-MM-YY HH:mm]. Example: [06-11-26 15:04] ");
        ButtonGroup stampGroup = new ButtonGroup();
        stampGroup.add(stampOff);
        stampGroup.add(stampHm);
        stampGroup.add(stampDate);
        JCheckBox sendAllOnCho = new JCheckBox("Send all on CHO", config.isSendAllOnCho());
        sendAllOnCho.setToolTipText(
                "Before CHO after traffic, Canned CHO, Disc. when finished, or Canned text disc., "
                        + "send Compose and wait for the Host ack.");
        JTextField handover = new JTextField(config.getCannedHandoverText(), 20);
        JTextField disconnect = new JTextField(config.getCannedDisconnectText(), 20);
        cannedCqArea = multiline(config.getCannedCqText(),
                "Repeated as a block. A blank block sends CQ Footer once.");
        cqFooterArea = multiline(config.getCannedCqFooter(),
                "Sent once after the repeated CQ block.");
        JTextArea awayMsg = multiline(config.getAwayMsg(),
                "Loaded into a new ARQ window's TX buffer when the FEC Away box is checked.");
        JTextField wrap = new JTextField(Integer.toString(config.getWrapColumns()), 4);

        cqRepeatSpinner = new JSpinner(new SpinnerNumberModel(config.getCqRepeat(), 1, 10, 1));
        cqRepeatSpinner.setToolTipText("How many copies of the canned CQ block to send. 1–10.");
        JPanel cqRepeatRow = labeled("Repeat", cqRepeatSpinner);

        Color originalOutgoing = config.getOutgoingText();
        Color originalIncoming = config.getIncomingText();
        int originalTextSize = config.getTextSize();
        ColorSwatch outgoingSwatch = new ColorSwatch(originalOutgoing);
        ColorSwatch incomingSwatch = new ColorSwatch(originalIncoming);
        outgoingSwatch.addActionListener(e -> {
            Color picked = chooseColor(outgoingSwatch.color());
            if (picked != null) {
                outgoingSwatch.setColor(picked);
            }
        });
        incomingSwatch.addActionListener(e -> {
            Color picked = chooseColor(incomingSwatch.color());
            if (picked != null) {
                incomingSwatch.setColor(picked);
            }
        });
        JSpinner textSize = new JSpinner(new SpinnerNumberModel(
                originalTextSize, AppConfig.MIN_TEXT_SIZE, AppConfig.MAX_TEXT_SIZE, 1));
        textSize.setToolTipText("Monospaced size for the transcript, compose, and App TX buffer.");
        textSize.addChangeListener(e -> {
            config.setTextSize((Integer) textSize.getValue());
            app.applyChatFont();
        });

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        addRow(form, labeled("Local callsign", callsign));
        addRow(form, sideBySide(line, message));
        addRow(form, sideBySide(
                new JLabel("Text outgoing color"), outgoingSwatch,
                new JLabel("Text incoming color"), incomingSwatch));
        addRow(form, autoConnectTnc);
        addRow(form, listenOnStart);
        addRow(form, sendAllOnCho);
        addRow(form, suppressConnect);
        addRow(form, onlyOneArqWindow);
        addRow(form, closeFecOnArqLink);
        addRow(form, sideBySide(new JLabel("Transcript timestamps"), stampOff, stampHm, stampDate));
        addRow(form, labeled("Text size", textSize));
        addRow(form, labeled("Wrap", wrap));

        JPanel canned = section("Canned text");
        addRow(canned, labeled("Handover", handover));
        addRow(canned, labeled("Disconnect", disconnect));
        addRow(canned, labeled("Away", new JScrollPane(awayMsg)));
        addRow(form, canned);

        JPanel cq = section("CQ text");
        addRow(cq, labeled("CQ", new JScrollPane(cannedCqArea)));
        addRow(cq, cqRepeatRow);
        addRow(cq, labeled("Footer", new JScrollPane(cqFooterArea)));
        addRow(form, cq);

        boolean[] committed = {false};
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            config.setCallsign(callsign.getText());
            config.setCommitMode(line.isSelected() ? CommitMode.LINE : CommitMode.MESSAGE);
            config.setListenOnStart(listenOnStart.isSelected());
            config.setAutoConnectTnc(autoConnectTnc.isSelected());
            config.setOnlyOneArqWindow(onlyOneArqWindow.isSelected());
            config.setCloseFecOnArqLink(closeFecOnArqLink.isSelected());
            config.setSuppressConnectFrames(suppressConnect.isSelected());
            config.setTranscriptTimestamp(stampDate.isSelected()
                    ? AppConfig.TIMESTAMP_DATE
                    : stampHm.isSelected() ? AppConfig.TIMESTAMP_HM : AppConfig.TIMESTAMP_OFF);
            config.setSendAllOnCho(sendAllOnCho.isSelected());
            config.setCannedHandoverText(handover.getText());
            config.setCannedDisconnectText(disconnect.getText());
            config.setCannedCqText(cannedCqArea.getText());
            config.setCannedCqFooter(cqFooterArea.getText());
            config.setAwayMsg(awayMsg.getText());
            config.setCqRepeat((Integer) cqRepeatSpinner.getValue());
            try {
                config.setWrapColumns(Integer.parseInt(wrap.getText().trim()));
            } catch (NumberFormatException ignored) {
                config.setWrapColumns(80);
            }
            config.setOutgoingText(outgoingSwatch.color());
            config.setIncomingText(incomingSwatch.color());
            config.setTextSize((Integer) textSize.getValue());
            app.saveConfig();
            app.applyChatColors(originalOutgoing, originalIncoming);
            app.applyChatFont();
            committed[0] = true;
            dispose();
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                app.detachProgramSettings(ProgramSettingsDialog.this);
                if (!committed[0]) {
                    config.setTextSize(originalTextSize);
                    app.applyChatFont();
                }
            }
        });
        app.attachProgramSettings(this);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancel);
        buttons.add(save);

        JScrollPane formScroll = new JScrollPane(form,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        formScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        formScroll.setPreferredSize(new Dimension(760, 680));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(formScroll, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    /** Push a CQ-editor save into the open settings fields so a later Save keeps it. */
    public void showSavedCq(String canned, String footer, int repeat) {
        cannedCqArea.setText(canned == null ? "" : canned);
        cqFooterArea.setText(footer == null ? "" : footer);
        cqRepeatSpinner.setValue(repeat);
    }

    private static JTextArea multiline(String text, String tip) {
        JTextArea area = new JTextArea(text == null ? "" : text, 3, 24);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setToolTipText(tip);
        return area;
    }

    private static void addRow(JPanel form, Component row) {
        if (row instanceof JComponent component) {
            component.setAlignmentX(Component.LEFT_ALIGNMENT);
            Dimension pref = component.getPreferredSize();
            component.setMaximumSize(new Dimension(Integer.MAX_VALUE, Math.max(pref.height, 1)));
        }
        form.add(row);
    }

    private static JPanel section(String title) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                new EmptyBorder(4, 6, 6, 6)));
        return box;
    }

    private static JPanel sideBySide(Component... parts) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        for (Component part : parts) {
            row.add(part);
        }
        return row;
    }

    private static JPanel labeled(String label, java.awt.Component field) {
        JPanel p = new JPanel(new BorderLayout(6, 0));
        p.add(new JLabel(label), BorderLayout.WEST);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    /** Palette (swatches) plus HSB saturation. Null when the chooser is cancelled. */
    private Color chooseColor(Color current) {
        JColorChooser chooser = new JColorChooser(current);
        JLabel demo = new JLabel("RR 73 OM SK");
        demo.setOpaque(true);
        demo.setBackground(UiColors.TRANSCRIPT_BG);
        demo.setForeground(current);
        demo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        demo.setBorder(new EmptyBorder(8, 12, 8, 12));
        ChangeListener preview = e -> demo.setForeground(chooser.getColor());
        chooser.getSelectionModel().addChangeListener(preview);
        chooser.setPreviewPanel(demo);

        Color[] picked = {null};
        JDialog dialog = JColorChooser.createDialog(this, "Text color", true, chooser,
                e -> picked[0] = chooser.getColor(),
                null);
        dialog.setVisible(true);
        chooser.getSelectionModel().removeChangeListener(preview);
        return picked[0];
    }

    /** Paints its own fill so the current color stays visible on the Windows look-and-feel. */
    private static final class ColorSwatch extends JButton {
        private Color color;

        ColorSwatch(Color color) {
            this.color = color == null ? Color.BLACK : color;
            setPreferredSize(new Dimension(36, 22));
            setMinimumSize(new Dimension(36, 22));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setToolTipText("Choose color");
        }

        Color color() {
            return color;
        }

        void setColor(Color color) {
            this.color = color == null ? Color.BLACK : color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            int x = 2;
            int y = 2;
            int w = Math.max(1, getWidth() - 4);
            int h = Math.max(1, getHeight() - 4);
            g2.setColor(color);
            g2.fillRect(x, y, w, h);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(x, y, w - 1, h - 1);
            g2.dispose();
        }
    }
}
