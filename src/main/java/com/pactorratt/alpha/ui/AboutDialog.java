package com.pactorratt.alpha.ui;

import com.pactorratt.alpha.config.AppConfig;

import javax.swing.JOptionPane;
import java.awt.Component;

public final class AboutDialog {

    private AboutDialog() {
    }

    public static void show(Component parent) {
        JOptionPane.showMessageDialog(parent,
                """
                        PactorRATT_Alpha
                        Portable PK-232 Host Mode Pactor chat (Alpha)

                        License: AGPL-3.0
                        Compat / support contact: %s

                        See PtRa_specification.md and docs/ARCHITECTURE.md
                        """.formatted(AppConfig.SUPPORT_EMAIL),
                "About PactorRATT_Alpha",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
