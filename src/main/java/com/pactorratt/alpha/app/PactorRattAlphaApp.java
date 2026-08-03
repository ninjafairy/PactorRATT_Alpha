package com.pactorratt.alpha.app;

import com.pactorratt.alpha.ui.MainWindow;
import com.pactorratt.alpha.ui.StartupWarningDialog;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Entry point. Portable root is the working directory (folder containing the uberjar when launched there).
 */
public final class PactorRattAlphaApp {

    public static void main(String[] args) {
        Path portableRoot = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Keep default L&F.
            }
            if (!StartupWarningDialog.confirmContinue()) {
                System.exit(0);
                return;
            }
            AppController controller = new AppController(portableRoot);
            MainWindow main = new MainWindow(controller);
            controller.setMainWindow(main);
            if (controller.config().isListenOnStart()) {
                main.setListenToggleSilently(true);
                controller.setListenEnabled(true);
            }
            main.setVisible(true);
            controller.debugLog().info("PactorRATT_Alpha started (portableRoot=" + portableRoot + ")");
        });
    }

    private PactorRattAlphaApp() {
    }
}
