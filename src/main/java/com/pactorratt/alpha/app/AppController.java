package com.pactorratt.alpha.app;

import com.pactorratt.alpha.config.AppConfig;
import com.pactorratt.alpha.config.ConfigStore;
import com.pactorratt.alpha.hostmode.HostEvent;
import com.pactorratt.alpha.hostmode.HostFrameCodec;
import com.pactorratt.alpha.hostmode.HostSession;
import com.pactorratt.alpha.hostmode.OpmodeParser;
import com.pactorratt.alpha.hostmode.TncInitializer;
import com.pactorratt.alpha.serial.SerialByteListener;
import com.pactorratt.alpha.ui.ConnectionWindow;
import com.pactorratt.alpha.ui.DebugMonitorWindow;
import com.pactorratt.alpha.ui.MainWindow;
import com.pactorratt.alpha.ui.StatusMonitorWindow;
import com.pactorratt.alpha.ui.UiColors;
import com.pactorratt.alpha.util.DebugLog;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Coordinates modes, windows, and TNC Host session lifecycle.
 */
public final class AppController {

    private static final long ARQ_HOST_TIMEOUT_MS = 2000;
    /** Default RECeive character (CTRL-D) — disconnect / FEC end after TNC TX clears. */
    private static final byte RECEIVE_CHAR_CTRL_D = 0x04;
    /** Default PTOver character (CTRL-Z) — ARQ ISS→IRS changeover. */
    private static final byte PTOVER_CHAR_CTRL_Z = 0x1A;
    /** OP poll period while HO buttons are locked and program OPPOLL is 0. */
    private static final long HANDOVER_WATCH_POLL_MS = 500;

    private final Path portableRoot;
    private final ConfigStore configStore;
    private final AppConfig config;
    private final DebugLog debugLog;
    private final TncInitializer tncInitializer;
    private final CopyOnWriteArrayList<SerialByteListener> serialTaps = new CopyOnWriteArrayList<>();
    private final SerialByteListener serialTapFanout = (tx, data, off, len) -> {
        for (SerialByteListener listener : serialTaps) {
            try {
                listener.onSerialBytes(tx, data, off, len);
            } catch (RuntimeException ignored) {
            }
        }
    };
    private final Consumer<HostEvent> hostEventListener = this::onHostEvent;

    private MainWindow mainWindow;
    private ConnectionWindow listenWindow;
    private volatile ConnectionWindow activeArqWindow;
    private DebugMonitorWindow debugMonitorWindow;
    private StatusMonitorWindow statusMonitorWindow;
    private final List<ConnectionWindow> deadArqWindows = new ArrayList<>();

    private volatile HostSession hostSession;
    private final AtomicBoolean tncBusy = new AtomicBoolean(false);
    /** Guards overlapping main-window Connect → PTConn ({@code PG}) attempts. */
    private final AtomicBoolean arqConnectBusy = new AtomicBoolean(false);
    /** Guards overlapping Listen FEC / End TX ({@code PD} + data + CTRL-D) attempts. */
    private final AtomicBoolean fecBusy = new AtomicBoolean(false);
    /** Guards overlapping Listen ON/OFF Host {@code OP}/{@code PN}/{@code Pt} round-trips. */
    private final AtomicBoolean listenHostBusy = new AtomicBoolean(false);
    private volatile Thread connectThread;
    private final AtomicBoolean connectCancelled = new AtomicBoolean(false);
    private volatile HostSession pendingSession;

    private volatile boolean tncConnected;
    private AppMode mode = AppMode.IDLE;

    private final Object opPollLock = new Object();
    private final AtomicBoolean opPollInFlight = new AtomicBoolean(false);
    private ScheduledExecutorService opPollExecutor;
    private ScheduledFuture<?> opPollFuture;

    public AppController(Path portableRoot) {
        this.portableRoot = Objects.requireNonNull(portableRoot);
        this.configStore = new ConfigStore(portableRoot);
        this.config = configStore.load();
        this.debugLog = new DebugLog(portableRoot);
        this.debugLog.setEnabled(config.isDebugLogEnabled());
        this.tncInitializer = new TncInitializer(
                debugLog, serialTapFanout, this::showStartupMessageOnEdt, this::showCompatInfoOnEdt);
        this.tncConnected = false;
    }

    public void addSerialByteListener(SerialByteListener listener) {
        if (listener != null) {
            serialTaps.add(listener);
        }
    }

    public void removeSerialByteListener(SerialByteListener listener) {
        serialTaps.remove(listener);
    }

    public void openDebugMonitor() {
        runOnEdt(() -> {
            if (debugMonitorWindow == null || !debugMonitorWindow.isDisplayable()) {
                debugMonitorWindow = new DebugMonitorWindow(this);
                debugMonitorWindow.setVisible(true);
            } else {
                debugMonitorWindow.toFront();
            }
        });
    }

    public void onDebugMonitorClosed(DebugMonitorWindow window) {
        if (debugMonitorWindow == window) {
            debugMonitorWindow = null;
        }
        // After a failed/partial connect we may have kept the serial port open for the monitor.
        if (!tncConnected && !isAnyMonitorOpen()) {
            closeRetainedDebugSession();
        }
    }

    public void openStatusMonitor() {
        runOnEdt(() -> {
            if (statusMonitorWindow == null || !statusMonitorWindow.isDisplayable()) {
                statusMonitorWindow = new StatusMonitorWindow(this);
                statusMonitorWindow.setVisible(true);
            } else {
                statusMonitorWindow.toFront();
            }
        });
    }

    public void onStatusMonitorClosed(StatusMonitorWindow window) {
        if (statusMonitorWindow == window) {
            statusMonitorWindow = null;
        }
        if (!tncConnected && !isAnyMonitorOpen()) {
            closeRetainedDebugSession();
        }
    }

    private boolean isDebugMonitorOpen() {
        DebugMonitorWindow w = debugMonitorWindow;
        return w != null && w.isDisplayable();
    }

    private boolean isStatusMonitorOpen() {
        StatusMonitorWindow w = statusMonitorWindow;
        return w != null && w.isDisplayable();
    }

    private boolean isAnyMonitorOpen() {
        return isDebugMonitorOpen() || isStatusMonitorOpen();
    }

    /**
     * On connect failure: keep the serial session if Debug or Status Monitor is open; otherwise close it.
     */
    private void retainOrCloseOnFailure(HostSession session) {
        if (session == null || !session.isOpen()) {
            return;
        }
        if (isAnyMonitorOpen()) {
            pendingSession = session;
            debugLog.info("Keeping serial session open for monitor after connect failure");
        } else {
            tncInitializer.abort(session);
            if (pendingSession == session) {
                pendingSession = null;
            }
        }
    }

    private void closeRetainedDebugSession() {
        HostSession pending = pendingSession;
        pendingSession = null;
        if (!tncConnected) {
            HostSession connected = hostSession;
            hostSession = null;
            tncInitializer.abort(pending);
            tncInitializer.abort(connected);
            debugLog.info("Closed serial session after Debug Monitor closed");
        } else {
            tncInitializer.abort(pending);
        }
    }

    public Path portableRoot() {
        return portableRoot;
    }

    public ConfigStore configStore() {
        return configStore;
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

    public boolean isTncBusy() {
        return tncBusy.get();
    }

    public HostSession hostSession() {
        return hostSession;
    }

    /** Prefer connected session; else open pending session (mid-init). */
    public HostSession openHostSessionOrNull() {
        HostSession s = hostSession;
        if (s != null && s.isOpen()) {
            return s;
        }
        s = pendingSession;
        if (s != null && s.isOpen()) {
            return s;
        }
        return null;
    }

    /**
     * Fire-and-forget framed Host global command for debug monitor.
     * Concatenate mnemonic + payload with no space. Does not wait for a response.
     *
     * @return null on accepted, or error message string
     */
    public String sendDebugHostCommand(String mnemonic, String payload) {
        String cmd = mnemonic == null ? "" : mnemonic.trim();
        if (cmd.length() != 2) {
            return "Host command must be exactly 2 characters.";
        }
        String args = payload == null ? "" : payload.trim();
        String mnemonicAndArgs = cmd + args;

        HostSession session = openHostSessionOrNull();
        if (session == null) {
            return "No open TNC serial session.";
        }
        try {
            session.sendHostCommandFireAndForget(mnemonicAndArgs);
            return null;
        } catch (IOException e) {
            return e.getMessage() == null ? "Host command write failed." : e.getMessage();
        }
    }

    /** Disc. after TX clear — flush App TX, then ch0 {@code $04} in the same block. */
    public void arqDiscAfterTxClear(ConnectionWindow window) {
        if (window == null || window.kind() != ConnectionWindow.Kind.ARQ || !window.isSessionActive()) {
            return;
        }
        String pending = window.drainAppTxBufferToTranscript();
        String notice = pending.isBlank()
                ? "Disc. after TX clear — sent CTRL-D ($04)."
                : "Disc. after TX clear — flushed App TX + CTRL-D ($04).";
        runArqHostAction(window, "Disc. after TX clear",
                session -> session.sendData(0,
                        hostDataWithControl(pending, RECEIVE_CHAR_CTRL_D),
                        ARQ_HOST_TIMEOUT_MS),
                null,
                notice);
    }

    /**
     * Disconnect now — Host {@code TC} (TClear), wait ACK, then ch0 data {@code $04}.
     */
    public void arqDisconnectNow(ConnectionWindow window) {
        runArqHostAction(window, "Disconnect now", session -> {
            sendHostOk(session, "TC");
            sendCh0Control(session, RECEIVE_CHAR_CTRL_D);
        }, null, "Disconnect now — sent TC then CTRL-D ($04).");
    }

    /** Handover now — ch0 data {@code $1A} (CTRL-Z). Locks HO buttons until ISS again. */
    public void arqHandoverNow(ConnectionWindow window) {
        runHandoverAction(window, "Handover", null, PTOVER_CHAR_CTRL_Z);
    }

    /**
     * HO after TX clear — flush App TX, then ch0 {@code $1A} in the same block.
     * Locks HO buttons until ISS again. Allowed while IRS if App TX has text to flush.
     */
    public void arqHoAfterTxClear(ConnectionWindow window) {
        if (window == null || window.kind() != ConnectionWindow.Kind.ARQ || !window.isSessionActive()) {
            return;
        }
        if (window.isLocalIrs() && window.isAppTxEmpty()) {
            noticeWindow(window, "HO after TX clear — not ISS (use Seize to take the link).");
            return;
        }
        if (!window.lockHandoverControls()) {
            noticeWindow(window, "HO after TX clear — handover already pending.");
            return;
        }
        String pending = window.drainAppTxBufferToTranscript();
        syncOpPollScheduler();
        byte[] payload = hostDataWithControl(pending, PTOVER_CHAR_CTRL_Z);
        String notice = pending.isBlank()
                ? "HO after TX clear — sent CTRL-Z; HO buttons locked until ISS again."
                : "HO after TX clear — flushed App TX + CTRL-Z; HO buttons locked until ISS again.";
        runArqHostAction(window, "HO after TX clear",
                session -> session.sendData(0, payload, ARQ_HOST_TIMEOUT_MS),
                () -> {
                    window.unlockHandoverControls();
                    syncOpPollScheduler();
                },
                notice);
    }

    /** Seize — Host {@code AG} (AChg). */
    public void arqSeize(ConnectionWindow window) {
        runArqHostAction(window, "Seize", session -> sendHostOk(session, "AG"));
    }

    /**
     * Abort — Listen checkbox on → {@code PN}, else {@code Pt}; then {@link #markArqDead}.
     */
    public void arqAbort(ConnectionWindow window) {
        if (window == null || window.kind() != ConnectionWindow.Kind.ARQ) {
            return;
        }
        if (!window.isSessionActive()) {
            return;
        }
        boolean listenOn = mainWindow != null && mainWindow.isListenSelected();
        String mnemonic = listenOn ? "PN" : "Pt";

        if (!tncConnected) {
            noticeArq(window, "Abort — TNC not connected; closing ARQ window.");
            markArqDead(window);
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            noticeArq(window, "Abort — no open Host session; closing ARQ window.");
            markArqDead(window);
            return;
        }

        Thread worker = new Thread(() -> {
            String resultNotice;
            try {
                sendHostOk(session, mnemonic);
                resultNotice = "Abort — sent " + mnemonic
                        + (listenOn ? " (Listen on)" : " (Idle)");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                resultNotice = "Abort — interrupted; closing ARQ window.";
                debugLog.info("ARQ Abort interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                resultNotice = "Abort — " + msg + "; closing ARQ window.";
                debugLog.info("ARQ Abort failed: " + msg);
            }
            final String notice = resultNotice;
            runOnEdt(() -> {
                markArqDead(window);
                noticeArq(window, notice);
            });
        }, "arq-abort");
        worker.setDaemon(true);
        worker.start();
    }

    /** HO with text — canned handover + {@code $1A} in the same ch0 block. Locks HO until ISS again. */
    public void arqHoWithText(ConnectionWindow window) {
        runHandoverAction(window, "HO with text", config.getCannedHandoverText(), PTOVER_CHAR_CTRL_Z);
    }

    /** Disc. with text — canned disconnect + {@code $04} in the same ch0 block. */
    public void arqDiscWithText(ConnectionWindow window) {
        runArqHostAction(window, "Disc. with text",
                session -> session.sendData(0,
                        hostDataWithControl(config.getCannedDisconnectText(), RECEIVE_CHAR_CTRL_D),
                        ARQ_HOST_TIMEOUT_MS),
                null,
                "Disc. with text — sent canned text + CTRL-D ($04).");
    }

    /**
     * ISS chat / App TX flush: send text as Host channel-0 data (CR line endings), chunked per
     * Ch. 4 §4.8 inside {@link HostSession#sendData}. Runs off the EDT.
     * Offline / no session: notice only (caller already painted grey transcript).
     */
    public void sendOutboundChat(ConnectionWindow window, String text) {
        if (window == null) {
            return;
        }
        String payloadText = text == null ? "" : text;
        if (payloadText.isEmpty()) {
            return;
        }
        if (!tncConnected) {
            noticeWindow(window, "ISS outbound — TNC not connected (transcript only).");
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            noticeWindow(window, "ISS outbound — no open Host session (transcript only).");
            return;
        }
        Thread worker = new Thread(() -> {
            try {
                byte[] bytes = toHostDataBytes(payloadText);
                session.sendData(0, bytes, ARQ_HOST_TIMEOUT_MS);
                int chars = bytes.length;
                runOnEdt(() -> noticeWindow(window,
                        "ISS outbound — sent " + chars + " char(s) to TNC (grey until TX-empty)."));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("ISS outbound interrupted");
                runOnEdt(() -> noticeWindow(window, "ISS outbound — interrupted."));
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("ISS outbound failed: " + msg);
                runOnEdt(() -> noticeWindow(window, "ISS outbound — " + msg));
            }
        }, "iss-outbound");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * Listen FEC / End TX: Host {@code PD} (PTSend), then App TX text as ch0 data (chunked §4.8),
     * then CTRL-D ({@code $04}) so the TNC returns to receive after TX clear.
     * Caller paints grey transcript and clears the App TX buffer before calling.
     */
    public void listenFecEndTx(ConnectionWindow window, String text) {
        if (window == null || window.kind() != ConnectionWindow.Kind.LISTEN) {
            return;
        }
        String payloadText = text == null ? "" : text;
        if (payloadText.isBlank()) {
            noticeWindow(window, "FEC / End TX — nothing to send.");
            return;
        }
        if (!tncConnected) {
            noticeWindow(window, "FEC / End TX — TNC not connected (transcript only).");
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            noticeWindow(window, "FEC / End TX — no open Host session (transcript only).");
            return;
        }
        if (!fecBusy.compareAndSet(false, true)) {
            noticeWindow(window, "FEC / End TX — already in progress.");
            return;
        }

        Thread worker = new Thread(() -> {
            String resultNotice;
            try {
                runOnEdt(() -> {
                    mode = AppMode.UNPROTO;
                    if (mainWindow != null) {
                        mainWindow.refreshModeLabel();
                    }
                });
                String pdCmd = config.ptSendHostCommand();
                sendHostOk(session, pdCmd);
                byte[] body = toHostDataBytes(payloadText);
                byte[] withEnd = new byte[body.length + 1];
                System.arraycopy(body, 0, withEnd, 0, body.length);
                withEnd[body.length] = RECEIVE_CHAR_CTRL_D;
                session.sendData(0, withEnd, ARQ_HOST_TIMEOUT_MS);
                resultNotice = "FEC / End TX — sent " + pdCmd + " + " + body.length
                        + " char(s) + CTRL-D (grey until TX-empty).";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                resultNotice = "FEC / End TX — interrupted.";
                debugLog.info("FEC / End TX interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                resultNotice = "FEC / End TX — " + msg;
                debugLog.info("FEC / End TX failed: " + msg);
            } finally {
                fecBusy.set(false);
                runOnEdt(() -> {
                    if (mode == AppMode.UNPROTO) {
                        boolean listenOn = mainWindow != null && mainWindow.isListenSelected();
                        mode = listenOn ? AppMode.LISTEN : AppMode.IDLE;
                    }
                    if (mainWindow != null) {
                        mainWindow.refreshModeLabel();
                    }
                });
            }
            final String notice = resultNotice;
            runOnEdt(() -> noticeWindow(window, notice));
        }, "listen-fec-end-tx");
        worker.setDaemon(true);
        worker.start();
    }

    private void sendCh0Control(HostSession session, byte control)
            throws IOException, InterruptedException {
        session.sendData(0, new byte[]{control}, ARQ_HOST_TIMEOUT_MS);
    }

    /**
     * Canned text (if any) plus a trailing control character in one ch0 payload.
     * Empty text → the control byte alone (no extra CR).
     */
    static byte[] hostDataWithControl(String text, byte control) {
        if (text == null || text.isEmpty()) {
            return new byte[]{control};
        }
        byte[] body = toHostDataBytes(text);
        byte[] out = new byte[body.length + 1];
        System.arraycopy(body, 0, out, 0, body.length);
        out[body.length] = control;
        return out;
    }

    /**
     * Normalize to Host data bytes: {@code \r\n}/{@code \n} → {@code \r}, US-ASCII.
     * Appends a trailing CR if the text is non-empty and does not already end with one.
     */
    static byte[] toHostDataBytes(String text) {
        if (text == null || text.isEmpty()) {
            return new byte[0];
        }
        String normalized = text.replace("\r\n", "\n").replace('\r', '\n').replace('\n', '\r');
        if (normalized.charAt(normalized.length() - 1) != '\r') {
            normalized = normalized + '\r';
        }
        return normalized.getBytes(StandardCharsets.US_ASCII);
    }

    private void sendHostOk(HostSession session, String mnemonic)
            throws IOException, InterruptedException {
        HostSession.CommandResponse response = session.sendCommand(mnemonic, ARQ_HOST_TIMEOUT_MS);
        if (!response.ok()) {
            throw new IOException(mnemonic + " failed, status=0x"
                    + Integer.toHexString(response.statusCode));
        }
    }

    @FunctionalInterface
    private interface ArqHostWork {
        void run(HostSession session) throws IOException, InterruptedException;
    }

    private void runArqHostAction(ConnectionWindow window, String actionName, ArqHostWork work) {
        runArqHostAction(window, actionName, work, null, actionName + " — sent.");
    }

    private void runArqHostAction(ConnectionWindow window, String actionName, ArqHostWork work,
            Runnable onFailure, String successNotice) {
        if (!tncConnected) {
            noticeWindow(window, actionName + " — TNC not connected.");
            if (onFailure != null) {
                onFailure.run();
            }
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            noticeWindow(window, actionName + " — no open Host session.");
            if (onFailure != null) {
                onFailure.run();
            }
            return;
        }
        Thread worker = new Thread(() -> {
            try {
                work.run(session);
                runOnEdt(() -> noticeWindow(window, successNotice));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("ARQ " + actionName + " interrupted");
                runOnEdt(() -> {
                    if (onFailure != null) {
                        onFailure.run();
                    }
                    noticeWindow(window, actionName + " — interrupted.");
                });
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("ARQ " + actionName + " failed: " + msg);
                runOnEdt(() -> {
                    if (onFailure != null) {
                        onFailure.run();
                    }
                    noticeWindow(window, actionName + " — " + msg);
                });
            }
        }, "arq-" + actionName.replaceAll("\\s+", "-").toLowerCase());
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * Send PTOver ({@code $1A}) as ch0 data (optionally after canned text in the same block).
     * Locks all HO buttons until OPMODE shows IRS then ISS again.
     */
    private void runHandoverAction(ConnectionWindow window, String actionName, String canned, byte control) {
        if (window == null || window.kind() != ConnectionWindow.Kind.ARQ || !window.isSessionActive()) {
            return;
        }
        if (window.isLocalIrs()) {
            noticeWindow(window, actionName + " — not ISS (use Seize to take the link).");
            return;
        }
        if (!window.lockHandoverControls()) {
            noticeWindow(window, actionName + " — handover already pending.");
            return;
        }
        syncOpPollScheduler();
        byte[] payload = hostDataWithControl(canned, control);
        runArqHostAction(window, actionName,
                session -> session.sendData(0, payload, ARQ_HOST_TIMEOUT_MS),
                () -> {
                    window.unlockHandoverControls();
                    syncOpPollScheduler();
                },
                actionName + " — sent CTRL-Z; HO buttons locked until ISS again.");
    }

    private void noticeArq(ConnectionWindow window, String text) {
        noticeWindow(window, text);
    }

    private void noticeWindow(ConnectionWindow window, String text) {
        if (window != null) {
            window.showNotice(text);
        }
        debugLog.info((window != null ? window.kind() : "HOST") + ": " + text);
    }

    private void attachHostEventListener(HostSession session) {
        if (session != null) {
            session.addListener(hostEventListener);
        }
    }

    private void detachHostEventListener(HostSession session) {
        if (session != null) {
            session.removeListener(hostEventListener);
        }
    }

    private void onHostEvent(HostEvent event) {
        if (event == null) {
            return;
        }
        if (event.type() == HostEvent.Type.COMMAND_RESPONSE) {
            OpmodeParser.Decoded decoded = OpmodeParser.decode(event.frame());
            if (decoded != null) {
                runOnEdt(() -> applyOpmodeDecoded(decoded));
            }
            return;
        }
        if (event.type() != HostEvent.Type.INBOUND_DATA) {
            return;
        }
        HostFrameCodec.Frame frame = event.frame();
        if (frame == null || frame.payload.length == 0) {
            return;
        }
        String text = new String(frame.payload, StandardCharsets.ISO_8859_1);
        runOnEdt(() -> {
            ConnectionWindow target = inboundTranscriptTarget();
            if (target != null) {
                target.appendRemoteText(text);
            } else {
                debugLog.info("INBOUND_DATA (no active window) CTL=0x"
                        + String.format("%02X", frame.ctl) + " len=" + frame.payload.length);
            }
        });
    }

    /**
     * Drive Status Monitor {@code Mode:} and the active ARQ window from a decoded OPMODE reply.
     * Pactor Standby ({@code Pt}) or *w*={@code $30} ends the ARQ link: stop polling, mark dead,
     * freeze ISS/IRS. {@code x} (S/R) drives ISS/IRS for every mode that includes *x*.
     */
    private void applyOpmodeDecoded(OpmodeParser.Decoded decoded) {
        if (decoded == null) {
            return;
        }
        if (statusMonitorWindow != null && statusMonitorWindow.isDisplayable()) {
            statusMonitorWindow.setModeLine(decoded.statusLine());
        }
        ConnectionWindow arq = activeArqWindow;
        if (arq == null || !arq.isSessionActive()) {
            return;
        }
        if (decoded.standby) {
            String w = decoded.wLabel != null ? decoded.wLabel : "Standby";
            arq.applyOpmodeLink(w, decoded.hasDirection() ? decoded.transmit : null);
            if (arq.hasSeenLiveOpmode()) {
                markArqDead(arq);
                noticeArq(arq, "ARQ ended — OPMODE " + w + ".");
            }
            return;
        }
        arq.markOpmodeLive();
        boolean hoLocked = arq.isHandoverLocked();
        arq.applyOpmodeLink(decoded.wLabel, decoded.hasDirection() ? decoded.transmit : null);
        if (hoLocked && !arq.isHandoverLocked()) {
            syncOpPollScheduler();
        }
    }

    /**
     * Poll Host {@code OP} at {@code OPPOLL} Hz while TNC is connected and an ARQ window is linked.
     * {@code OPPOLL=0} disables, except while HO buttons are locked (then 2 Hz) so ISS/IRS can
     * release the lock. Safe to call from EDT or the poller thread.
     */
    private void syncOpPollScheduler() {
        synchronized (opPollLock) {
            int hz = config.getOpPoll();
            HostSession session = hostSession;
            boolean handoverWatch = isHandoverWatchActive();
            boolean shouldRun = tncConnected
                    && hasActiveArq()
                    && (hz > 0 || handoverWatch)
                    && session != null
                    && session.isOpen();
            if (opPollFuture != null) {
                opPollFuture.cancel(false);
                opPollFuture = null;
            }
            if (!shouldRun) {
                return;
            }
            if (opPollExecutor == null || opPollExecutor.isShutdown()) {
                opPollExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
                    Thread t = new Thread(r, "opmode-poll");
                    t.setDaemon(true);
                    return t;
                });
            }
            long periodMs = hz > 0 ? Math.max(1L, 1000L / hz) : HANDOVER_WATCH_POLL_MS;
            opPollFuture = opPollExecutor.scheduleAtFixedRate(
                    this::pollOpmodeOnce, 0, periodMs, TimeUnit.MILLISECONDS);
        }
    }

    private boolean isHandoverWatchActive() {
        ConnectionWindow arq = activeArqWindow;
        return arq != null && arq.isSessionActive() && arq.isHandoverLocked();
    }

    private void stopOpPollScheduler() {
        synchronized (opPollLock) {
            if (opPollFuture != null) {
                opPollFuture.cancel(false);
                opPollFuture = null;
            }
            if (opPollExecutor != null) {
                opPollExecutor.shutdownNow();
                opPollExecutor = null;
            }
        }
    }

    private void pollOpmodeOnce() {
        if (!tncConnected || !hasActiveArq() || (config.getOpPoll() <= 0 && !isHandoverWatchActive())) {
            syncOpPollScheduler();
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            syncOpPollScheduler();
            return;
        }
        if (!opPollInFlight.compareAndSet(false, true)) {
            return;
        }
        try {
            // OPMODE replies are not ACK $00; do not use sendHostOk.
            session.sendCommand("OP", ARQ_HOST_TIMEOUT_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            debugLog.info("OPMODE poll interrupted");
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
            debugLog.info("OPMODE poll failed: " + msg);
        } finally {
            opPollInFlight.set(false);
        }
    }

    /** ARQ active wins; else active Listen window. */
    private ConnectionWindow inboundTranscriptTarget() {
        if (activeArqWindow != null && activeArqWindow.isSessionActive()) {
            return activeArqWindow;
        }
        if (listenWindow != null && listenWindow.isSessionActive()) {
            return listenWindow;
        }
        return null;
    }

    /** Updates the connected flag and refreshes MainWindow (EDT-safe). */
    public void setTncConnected(boolean connected) {
        this.tncConnected = connected;
        runOnEdt(() -> {
            if (mainWindow != null) {
                mainWindow.refreshConnectionState();
            }
        });
    }

    /**
     * TNC menu Connect: open COM, enter Host Mode, compat gate, coded init.
     * Runs off the EDT. Main-window Connect issues Host PTConn ({@link #requestConnect}).
     */
    public void connectTnc() {
        if (tncConnected) {
            JOptionPane.showMessageDialog(mainWindow,
                    "TNC is already connected.",
                    "PactorRATT_Alpha",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (config.getComPort() == null || config.getComPort().isBlank()) {
            JOptionPane.showMessageDialog(mainWindow,
                    "Select a COM port under Settings → COM Port before connecting.",
                    "PactorRATT_Alpha",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!tncBusy.compareAndSet(false, true)) {
            return;
        }
        connectCancelled.set(false);
        pendingSession = null;
        if (mainWindow != null) {
            mainWindow.refreshConnectionState();
        }

        Thread worker = new Thread(() -> {
            HostSession localPending = null;
            try {
                TncInitializer.InitResult result = tncInitializer.connect(config);
                if (result.session != null) {
                    localPending = result.session;
                    pendingSession = result.session;
                }
                if (connectCancelled.get()) {
                    // Explicit disconnect/cancel — always close.
                    tncInitializer.abort(result.session != null ? result.session : localPending);
                    pendingSession = null;
                    return;
                }
                result = handleInitResult(result);
                if (connectCancelled.get()) {
                    tncInitializer.abort(result.session != null ? result.session : localPending);
                    pendingSession = null;
                    return;
                }
                finishConnect(result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                HostSession toHandle = pendingSession != null ? pendingSession : hostSession;
                if (connectCancelled.get()) {
                    tncInitializer.abort(toHandle);
                    pendingSession = null;
                    hostSession = null;
                } else {
                    retainOrCloseOnFailure(toHandle);
                    hostSession = null;
                    runOnEdt(() -> {
                        setTncConnected(false);
                        JOptionPane.showMessageDialog(mainWindow,
                                "TNC connection cancelled.",
                                "PactorRATT_Alpha",
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                }
            } finally {
                // Do not clear pendingSession here — may be retained for Debug Monitor.
                tncBusy.set(false);
                connectThread = null;
                runOnEdt(() -> {
                    if (mainWindow != null) {
                        mainWindow.refreshConnectionState();
                    }
                });
            }
        }, "tnc-connect");
        connectThread = worker;
        worker.setDaemon(true);
        worker.start();
    }

    private TncInitializer.InitResult handleInitResult(TncInitializer.InitResult result)
            throws InterruptedException {
        if (result.outcome != TncInitializer.Outcome.WARN_NEEDS_CONFIRM) {
            return result;
        }
        pendingSession = result.session;
        int choice = showConfirmOnEdt(
                result.message + "\n\nContinue connecting?",
                "TNC compatibility warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (connectCancelled.get() || Thread.interrupted()) {
            if (connectCancelled.get()) {
                tncInitializer.abort(result.session);
                pendingSession = null;
            } else {
                retainOrCloseOnFailure(result.session);
            }
            throw new InterruptedException("TNC connection cancelled");
        }
        if (choice == JOptionPane.YES_OPTION) {
            return tncInitializer.continueAfterWarn(result.session, config, result.compat);
        }
        // User declined warn — still retain session for Debug Monitor if open.
        return new TncInitializer.InitResult(
                TncInitializer.Outcome.CANCELLED,
                result.compat,
                "Connection cancelled after compatibility warning.",
                result.session,
                result.firmwareLabel);
    }

    private void finishConnect(TncInitializer.InitResult result) {
        if (connectCancelled.get()) {
            tncInitializer.abort(result.session);
            pendingSession = null;
            return;
        }
        if (result.outcome == TncInitializer.Outcome.SUCCESS && result.session != null) {
            // Publish session on worker thread before EDT update so disconnect can see it.
            hostSession = result.session;
            pendingSession = null;
            attachHostEventListener(result.session);
        } else if (result.session != null && result.session.isOpen()) {
            retainOrCloseOnFailure(result.session);
        }
        runOnEdt(() -> {
            if (connectCancelled.get()) {
                HostSession s = hostSession;
                detachHostEventListener(s);
                hostSession = null;
                pendingSession = null;
                tncInitializer.abort(s);
                setTncConnected(false);
                return;
            }
            switch (result.outcome) {
                case SUCCESS -> {
                    setTncConnected(true);
                    String label = result.firmwareLabel == null ? "" : result.firmwareLabel;
                    debugLog.info("TNC connected" + (label.isEmpty() ? "" : " (" + label + ")"));
                    syncOpPollScheduler();
                    if (mainWindow != null && mainWindow.isListenSelected() && !hasActiveArq()) {
                        enterListenHostThenUi();
                    }
                }
                case HARD_REFUSE -> {
                    hostSession = null;
                    setTncConnected(false);
                    JOptionPane.showMessageDialog(mainWindow,
                            result.message,
                            "Unsupported TNC",
                            JOptionPane.ERROR_MESSAGE);
                }
                case FAILED -> {
                    hostSession = null;
                    setTncConnected(false);
                    JOptionPane.showMessageDialog(mainWindow,
                            result.message == null || result.message.isBlank()
                                    ? "TNC connection failed."
                                    : result.message,
                            "TNC connection failed",
                            JOptionPane.ERROR_MESSAGE);
                }
                case CANCELLED -> {
                    hostSession = null;
                    setTncConnected(false);
                    debugLog.info("TNC connection cancelled by user");
                }
                case WARN_NEEDS_CONFIRM -> {
                    hostSession = null;
                    setTncConnected(false);
                }
            }
        });
    }

    /** TNC menu Disconnect: close Host session and clear connected flag. */
    public void disconnectTnc() {
        connectCancelled.set(true);
        Thread worker = connectThread;
        if (worker != null && worker.isAlive()) {
            worker.interrupt();
        }

        final HostSession session = hostSession;
        final HostSession pending = pendingSession;
        detachHostEventListener(session);
        detachHostEventListener(pending);
        hostSession = null;
        pendingSession = null;
        setTncConnected(false);
        tncBusy.set(false);
        syncOpPollScheduler();
        if (mainWindow != null) {
            mainWindow.refreshConnectionState();
        }
        debugLog.info("TNC disconnect requested");

        Thread closer = new Thread(() -> {
            tncInitializer.abort(session);
            tncInitializer.abort(pending);
        }, "tnc-disconnect");
        closer.setDaemon(true);
        closer.start();
    }

    private void showStartupMessageOnEdt(String message) throws InterruptedException {
        if (SwingUtilities.isEventDispatchThread()) {
            showStartupMessageDialog(message);
            return;
        }
        try {
            SwingUtilities.invokeAndWait(() -> showStartupMessageDialog(message));
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new InterruptedException("Startup message dialog failed: " + e.getMessage());
        }
    }

    private void showStartupMessageDialog(String message) {
        String body = message == null ? "" : message.replace("\r\n", "\n").replace('\r', '\n');
        String html = "<html><div style='text-align:center'>"
                + escapeHtml(body).replace("\n", "<br>")
                + "</div></html>";
        JLabel label = new JLabel(html, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(UiColors.PANEL_BG);
        label.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JOptionPane.showMessageDialog(
                mainWindow,
                label,
                "PK-232 Startup Message",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showCompatInfoOnEdt(String message) {
        if (SwingUtilities.isEventDispatchThread()) {
            showCompatInfoDialog(message);
            return;
        }
        SwingUtilities.invokeLater(() -> showCompatInfoDialog(message));
    }

    private void showCompatInfoDialog(String message) {
        String body = message == null ? "" : message.replace("\r\n", "\n").replace('\r', '\n');
        String html = "<html><div style='text-align:center'>"
                + escapeHtml(body).replace("\n", "<br>")
                + "</div></html>";

        JDialog dialog = new JDialog(mainWindow, "TNC Firmware / Hardware", false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel(html, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(UiColors.PANEL_BG);
        label.setBorder(BorderFactory.createEmptyBorder(12, 20, 8, 20));

        JButton ok = new JButton("OK");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UiColors.PANEL_BG);
        buttonPanel.add(ok);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UiColors.PANEL_BG);
        content.add(label, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        dialog.getContentPane().add(content);
        dialog.getContentPane().setBackground(UiColors.PANEL_BG);

        Timer timer = new Timer(4000, e -> dialog.dispose());
        timer.setRepeats(false);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                timer.start();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                timer.stop();
            }
        });
        ok.addActionListener(e -> {
            timer.stop();
            dialog.dispose();
        });

        dialog.pack();
        dialog.setLocationRelativeTo(mainWindow);
        dialog.setVisible(true);
    }

    private static String escapeHtml(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private int showConfirmOnEdt(String message, String title, int optionType, int messageType)
            throws InterruptedException {
        if (SwingUtilities.isEventDispatchThread()) {
            return JOptionPane.showConfirmDialog(mainWindow, message, title, optionType, messageType);
        }
        final int[] choice = {JOptionPane.CLOSED_OPTION};
        try {
            SwingUtilities.invokeAndWait(() ->
                    choice[0] = JOptionPane.showConfirmDialog(
                            mainWindow, message, title, optionType, messageType));
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new InterruptedException("Confirm dialog failed: " + e.getMessage());
        }
        return choice[0];
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
            syncOpPollScheduler();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainWindow,
                    "Could not save settings:\n" + e.getMessage(),
                    "PactorRATT_Alpha",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setListenEnabled(boolean enabled) {
        if (enabled) {
            if (mode == AppMode.ARQ || hasActiveArq()) {
                // Spec: Listen window may exist but inactive while ARQ active. Do not send PN.
                ensureListenWindow(false);
                if (mainWindow != null) {
                    mainWindow.setListenToggleSilently(true);
                }
                return;
            }
            enterListenHostThenUi();
        } else {
            ConnectionWindow closing = listenWindow;
            listenWindow = null;
            if (closing != null) {
                closing.dispose();
            }
            if (mode == AppMode.LISTEN || mode == AppMode.UNPROTO) {
                mode = AppMode.IDLE;
            }
            if (mainWindow != null) {
                mainWindow.refreshModeLabel();
            }
            leaveListenHostIfPn();
        }
    }

    /**
     * Listen ON: if TNC is {@code Pt}, send {@code PN}; if already {@code PN}, leave it.
     * Any other OPMODE refuses Listen ON. No Host I/O while ARQ is active or TNC is offline.
     */
    private void enterListenHostThenUi() {
        if (!tncConnected || hasActiveArq()) {
            applyListenUiOn();
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            applyListenUiOn();
            return;
        }
        applyListenUiOn();
        if (!listenHostBusy.compareAndSet(false, true)) {
            return;
        }
        Thread worker = new Thread(() -> {
            try {
                OpmodeParser.Decoded decoded = queryOpmode(session);
                if (decoded != null && decoded.isPactorListen()) {
                    listenHostBusy.set(false);
                    return;
                }
                if (decoded != null && decoded.isPactorStandby()) {
                    sendHostOk(session, "PN");
                    listenHostBusy.set(false);
                    return;
                }
                String current = decoded == null || decoded.modeName == null
                        ? "unknown"
                        : decoded.modeName;
                runOnEdt(() -> {
                    try {
                        refuseListenOn("TNC is not in Pactor standby (Pt). Listen was not enabled.\n"
                                + "Current OPMODE: " + current);
                    } finally {
                        listenHostBusy.set(false);
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("Listen ON interrupted");
                runOnEdt(() -> {
                    try {
                        refuseListenOn("Listen ON interrupted.");
                    } finally {
                        listenHostBusy.set(false);
                    }
                });
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("Listen ON failed: " + msg);
                runOnEdt(() -> {
                    try {
                        refuseListenOn("Listen ON failed: " + msg);
                    } finally {
                        listenHostBusy.set(false);
                    }
                });
            }
        }, "listen-enter");
        worker.setDaemon(true);
        worker.start();
    }

    private void applyListenUiOn() {
        if (mode == AppMode.ARQ || hasActiveArq()) {
            ensureListenWindow(false);
            if (mainWindow != null) {
                mainWindow.setListenToggleSilently(true);
                mainWindow.refreshModeLabel();
            }
            return;
        }
        mode = AppMode.LISTEN;
        ensureListenWindow(true);
        if (mainWindow != null) {
            mainWindow.setListenToggleSilently(true);
            mainWindow.refreshModeLabel();
        }
    }

    private void refuseListenOn(String message) {
        ConnectionWindow closing = listenWindow;
        listenWindow = null;
        if (closing != null) {
            closing.dispose();
        }
        if (mode == AppMode.LISTEN || mode == AppMode.UNPROTO) {
            mode = AppMode.IDLE;
        }
        if (mainWindow != null) {
            mainWindow.setListenToggleSilently(false);
            mainWindow.refreshModeLabel();
        }
        JOptionPane.showMessageDialog(mainWindow, message, "PactorRATT_Alpha",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Listen window closed / Listen OFF: if TNC is {@code PN}, send {@code Pt}.
     * Skip while ARQ is active or TNC is offline.
     */
    private void leaveListenHostIfPn() {
        if (!tncConnected || hasActiveArq()) {
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            return;
        }
        Thread worker = new Thread(() -> {
            try {
                OpmodeParser.Decoded decoded = queryOpmode(session);
                if (decoded != null && decoded.isPactorListen()) {
                    sendHostOk(session, "Pt");
                    debugLog.info("Listen OFF — sent Pt (was PN)");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("Listen OFF Host interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("Listen OFF Host failed: " + msg);
            }
        }, "listen-leave");
        worker.setDaemon(true);
        worker.start();
    }

    /** OPMODE query — replies are not ACK {@code $00}; do not use {@link #sendHostOk}. */
    private OpmodeParser.Decoded queryOpmode(HostSession session)
            throws IOException, InterruptedException {
        HostSession.CommandResponse response = session.sendCommand("OP", ARQ_HOST_TIMEOUT_MS);
        return OpmodeParser.decode(response.frame);
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

    /**
     * Main-window Connect / buddy double-click: open the ARQ window, then send Host
     * {@code PG}+callsign (PTConn) in the background. Failure closes the window.
     */
    public void requestConnect(String remoteCallsign) {
        if (!tncConnected) {
            JOptionPane.showMessageDialog(mainWindow,
                    "TNC is not connected. Use TNC → Connect after configuring the COM port.",
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
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            JOptionPane.showMessageDialog(mainWindow,
                    "No open Host session.",
                    "PactorRATT_Alpha",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!arqConnectBusy.compareAndSet(false, true)) {
            return;
        }

        // Host wire: no space — PG + callsign (leading ! preserved for long path).
        final String hostCmd = "PG" + call;
        openArqWindowForConnect(call);
        Thread worker = new Thread(() -> {
            try {
                HostSession.CommandResponse response =
                        session.sendCommand(hostCmd, ARQ_HOST_TIMEOUT_MS);
                if (!response.ok()) {
                    throw new IOException("PG failed, status=0x"
                            + Integer.toHexString(response.statusCode));
                }
                runOnEdt(() -> arqConnectBusy.set(false));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("PTConn interrupted for " + call);
                runOnEdt(() -> {
                    try {
                        abortFailedConnect("Connect interrupted.");
                    } finally {
                        arqConnectBusy.set(false);
                    }
                });
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("PTConn failed for " + call + ": " + msg);
                runOnEdt(() -> {
                    try {
                        abortFailedConnect("Connect failed: " + msg);
                    } finally {
                        arqConnectBusy.set(false);
                    }
                });
            }
        }, "arq-ptconn");
        worker.setDaemon(true);
        worker.start();
    }

    /** Opens the ARQ window; Host {@code PG} is sent in the background. */
    private void openArqWindowForConnect(String call) {
        if (activeArqWindow != null) {
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
        debugLog.info("ARQ window opened for " + call);
        syncOpPollScheduler();
    }

    private void abortFailedConnect(String message) {
        ConnectionWindow w = activeArqWindow;
        if (w != null) {
            onConnectionWindowClosed(w);
            w.dispose();
        }
        showConnectError(message);
    }

    private void showConnectError(String message) {
        JOptionPane.showMessageDialog(mainWindow,
                message,
                "PactorRATT_Alpha",
                JOptionPane.ERROR_MESSAGE);
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
            leaveListenHostIfPn();
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
            syncOpPollScheduler();
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
            syncOpPollScheduler();
        }
    }

    public void shutdown() {
        stopOpPollScheduler();
        disconnectTnc();
        if (debugMonitorWindow != null) {
            debugMonitorWindow.dispose();
            debugMonitorWindow = null;
        }
        if (statusMonitorWindow != null) {
            statusMonitorWindow.dispose();
            statusMonitorWindow = null;
        }
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
