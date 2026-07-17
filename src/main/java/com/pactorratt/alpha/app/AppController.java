package com.pactorratt.alpha.app;

import com.pactorratt.alpha.config.AppConfig;
import com.pactorratt.alpha.config.ConfigStore;
import com.pactorratt.alpha.ui.ConnectionWindow;
import com.pactorratt.alpha.ui.MainWindow;
import com.pactorratt.alpha.util.DebugLog;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Coordinates modes, windows, and TNC connection flag for Phase 1 offline shell.
 */
public final class AppController {

    private final Path portableRoot;
    private final ConfigStore configStore;
    private final AppConfig config;
    private final DebugLog debugLog;

    private MainWindow mainWindow;
    private ConnectionWindow listenWindow;
    private ConnectionWindow activeArqWindow;
    private final List<ConnectionWindow> deadArqWindows = new ArrayList<>();

    private boolean tncConnected;
    private AppMode mode = AppMode.IDLE;

    public AppController(Path portableRoot) {
        this.portableRoot = Objects.requireNonNull(portableRoot);
        this.configStore = new ConfigStore(portableRoot);
        this.config = configStore.load();
        this.debugLog = new DebugLog(portableRoot);
        this.debugLog.setEnabled(config.isDebugLogEnabled());
        this.tncConnected = false;
    }

    public Path portableRoot() {
        return portableRoot;
    }

    public AppConfig config() {
        return config;
    }

    public DebugLog debugLog() {
        return debugLog;
    }

    public boolean isTncConnected() {
        return tncConnected;
    }

    /** Phase 1: no real Host session yet. Kept for UI gating. */
    public void setTncConnected(boolean connected) {
        this.tncConnected = connected;
        if (mainWindow != null) {
            mainWindow.refreshConnectionState();
        }
    }

    public AppMode mode() {
        return mode;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public MainWindow mainWindow() {
        return mainWindow;
    }

    public void saveConfig() {
        try {
            configStore.save(config);
            debugLog.setEnabled(config.isDebugLogEnabled());
            debugLog.info("Config saved");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainWindow,
                    "Could not save settings:\n" + e.getMessage(),
                    "PactorRATT_Alpha",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setListenEnabled(boolean enabled) {
        if (enabled) {
            if (mode == AppMode.ARQ) {
                // Spec: Listen window may exist but inactive while ARQ active.
                ensureListenWindow(false);
                if (mainWindow != null) {
                    mainWindow.setListenToggleSilently(true);
                }
                return;
            }
            mode = AppMode.LISTEN;
            ensureListenWindow(true);
        } else {
            if (listenWindow != null) {
                listenWindow.dispose();
                listenWindow = null;
            }
            if (mode == AppMode.LISTEN || mode == AppMode.UNPROTO) {
                mode = AppMode.IDLE;
            }
        }
        if (mainWindow != null) {
            mainWindow.refreshModeLabel();
        }
    }

    private void ensureListenWindow(boolean active) {
        if (listenWindow == null) {
            listenWindow = new ConnectionWindow(this, ConnectionWindow.Kind.LISTEN, "Listen");
            listenWindow.setVisible(true);
        }
        listenWindow.setSessionActive(active && mode != AppMode.ARQ);
        if (!tncConnected) {
            listenWindow.showNotice("Offline preview — TNC not connected. Layout only.");
        }
    }

    public void requestConnect(String remoteCallsign) {
        if (!tncConnected) {
            JOptionPane.showMessageDialog(mainWindow,
                    "TNC is not connected. Configure COM port and open a Host session (Phase 3+).",
                    "PactorRATT_Alpha",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (activeArqWindow != null) {
            JOptionPane.showMessageDialog(mainWindow,
                    "An ARQ link is already active.",
                    "PactorRATT_Alpha",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String call = remoteCallsign == null ? "" : remoteCallsign.trim().toUpperCase();
        if (call.isEmpty()) {
            JOptionPane.showMessageDialog(mainWindow,
                    "Enter a remote callsign.",
                    "PactorRATT_Alpha",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        mode = AppMode.ARQ;
        if (listenWindow != null) {
            listenWindow.setSessionActive(false);
        }
        activeArqWindow = new ConnectionWindow(this, ConnectionWindow.Kind.ARQ, call);
        activeArqWindow.setSessionActive(true);
        activeArqWindow.setVisible(true);
        if (mainWindow != null) {
            mainWindow.refreshModeLabel();
        }
        // Host PTConn will be issued in a later phase.
        debugLog.info("ARQ window opened for " + call + " (Host connect not yet implemented)");
    }

    /** Offline UI helper: open a dead/preview ARQ window for layout testing. */
    public void openPreviewArqWindow() {
        ConnectionWindow preview = new ConnectionWindow(this, ConnectionWindow.Kind.ARQ, "PREVIEW");
        preview.setSessionActive(false);
        preview.showNotice("Offline preview window — not linked.");
        preview.setVisible(true);
        deadArqWindows.add(preview);
    }

    public void onConnectionWindowClosed(ConnectionWindow window) {
        if (window == listenWindow) {
            listenWindow = null;
            if (mode == AppMode.LISTEN || mode == AppMode.UNPROTO) {
                mode = AppMode.IDLE;
            }
            if (mainWindow != null) {
                mainWindow.setListenToggleSilently(false);
                mainWindow.refreshModeLabel();
            }
            return;
        }
        if (window == activeArqWindow) {
            activeArqWindow = null;
            deadArqWindows.remove(window);
            if (config.isListenOnStart() || (mainWindow != null && mainWindow.isListenSelected())) {
                mode = AppMode.LISTEN;
                ensureListenWindow(true);
            } else {
                mode = AppMode.IDLE;
            }
            if (mainWindow != null) {
                mainWindow.refreshModeLabel();
            }
            return;
        }
        deadArqWindows.remove(window);
    }

    public boolean hasActiveArq() {
        return activeArqWindow != null && activeArqWindow.isSessionActive();
    }

    public void markArqDead(ConnectionWindow window) {
        if (window == activeArqWindow) {
            activeArqWindow = null;
            deadArqWindows.add(window);
            window.setSessionActive(false);
            if (mainWindow != null && mainWindow.isListenSelected()) {
                mode = AppMode.LISTEN;
                ensureListenWindow(true);
            } else {
                mode = AppMode.IDLE;
            }
            if (mainWindow != null) {
                mainWindow.refreshModeLabel();
            }
        }
    }

    public void shutdown() {
        saveConfig();
        debugLog.close();
        if (listenWindow != null) {
            listenWindow.dispose();
        }
        if (activeArqWindow != null) {
            activeArqWindow.dispose();
        }
        for (ConnectionWindow w : new ArrayList<>(deadArqWindows)) {
            w.dispose();
        }
        deadArqWindows.clear();
    }

    public void runOnEdt(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }
}
