package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.app.AppController;
import com.pactorratt.alpha.config.AppConfig;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Window;

/** Modeless Dev Tools window. Checkbox changes apply immediately. */
public final class DebugSettingsDialog extends JDialog {

    public DebugSettingsDialog(Window owner, AppController app) {
        super(owner, "Debug settings");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        AppConfig config = app.config();

        JCheckBox windowDimensions = new JCheckBox("Window dimensions", DebugOverlay.isWindowDimensions());
        windowDimensions.addActionListener(e -> DebugOverlay.setWindowDimensions(windowDimensions.isSelected()));

        JCheckBox debugLog = new JCheckBox("Debug log", config.isDebugLogEnabled());
        debugLog.addActionListener(e -> {
            config.setDebugLogEnabled(debugLog.isSelected());
            app.debugLog().setEnabled(debugLog.isSelected());
            app.saveConfig();
        });

        JCheckBox displayStartup = new JCheckBox("Display Startup", config.isDisplayStartup());
        displayStartup.setToolTipText(
                "Show PK-232 startup text and TNC firmware/hardware ($0009) windows on connect.");
        displayStartup.addActionListener(e -> {
            config.setDisplayStartup(displayStartup.isSelected());
            app.saveConfig();
        });

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(12, 12, 12, 12));
        body.add(windowDimensions);
        body.add(debugLog);
        body.add(displayStartup);
        add(body);
        pack();
        setLocationRelativeTo(owner);
    }
}
