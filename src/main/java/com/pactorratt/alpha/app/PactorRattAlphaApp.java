package com.pactorratt.alpha.app;

import com.pactorratt.alpha.ui.MainWindow;
import com.pactorratt.alpha.ui.StartupWarningDialog;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Entry point. Portable root is the folder containing the uberjar (or {@code user.dir} in an IDE).
 * All program files are read and written under {@code config/} in that folder.
 */
public final class PactorRattAlphaApp {

    public static void main(String[] args) {
        Path portableRoot = resolvePortableRoot();
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

    /**
     * Folder beside the running jar so {@code config/} stays with the program even if cwd differs.
     */
    static Path resolvePortableRoot() {
        try {
            var src = PactorRattAlphaApp.class.getProtectionDomain().getCodeSource();
            if (src != null && src.getLocation() != null) {
                Path p = Path.of(src.getLocation().toURI());
                if (Files.isRegularFile(p) && p.getFileName().toString().toLowerCase().endsWith(".jar")) {
                    Path dir = p.getParent();
                    if (dir != null) {
                        return dir.toAbsolutePath().normalize();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
    }

    private PactorRattAlphaApp() {
    }
}
