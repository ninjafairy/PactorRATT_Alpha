package com.pactorratt.alpha.app;

import com.pactorratt.alpha.config.AppConfig;
import com.pactorratt.alpha.config.ConfigStore;
import com.pactorratt.alpha.config.HostCommandIni;
import com.pactorratt.alpha.config.MacroFile;
import com.pactorratt.alpha.hostmode.CallsignLineParser;
import com.pactorratt.alpha.hostmode.CompatResult;
import com.pactorratt.alpha.hostmode.DigitalLedState;
import com.pactorratt.alpha.hostmode.HostEvent;
import com.pactorratt.alpha.hostmode.HostFrameCodec;
import com.pactorratt.alpha.hostmode.HostSession;
import com.pactorratt.alpha.hostmode.LinkMessageParser;
import com.pactorratt.alpha.hostmode.OpmodeParser;
import com.pactorratt.alpha.hostmode.TncInitializer;
import com.pactorratt.alpha.serial.SerialByteListener;
import com.pactorratt.alpha.ui.CompatNotifyDialog;
import com.pactorratt.alpha.ui.CompatWarningDialog;
import com.pactorratt.alpha.ui.ConnectionWindow;
import com.pactorratt.alpha.ui.DebugMonitorWindow;
import com.pactorratt.alpha.ui.DisplayMonitorWindow;
import com.pactorratt.alpha.ui.MainWindow;
import com.pactorratt.alpha.ui.PdBugCheckWindow;
import com.pactorratt.alpha.ui.StatusMonitorWindow;
import com.pactorratt.alpha.ui.Ubit10MonitorWindow;
import com.pactorratt.alpha.ui.UiColors;
import com.pactorratt.alpha.ui.WindowPlacement;
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
import java.awt.Color;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Coordinates modes, windows, and TNC Host session lifecycle.
 */
public final class AppController {

    private static final long ARQ_HOST_TIMEOUT_MS = 2000;
    /**
     * Fallback outbound-call UI timer. TNC ARQTMO default is 60 s and reports
     * {@code $50 Timeout} (no {@code DISCONNECTED:}) when the call dies; this timer
     * covers a missed frame. Neither path Aborts the TNC (Cancel still does that).
     */
    private static final int CALLING_TIMEOUT_MS = 60_000;
    /** Default RECeive character (CTRL-D) — disconnect / FEC end after TNC TX clears. */
    private static final byte RECEIVE_CHAR_CTRL_D = 0x04;
    /** Default PTOver character (CTRL-Z) — ARQ ISS→IRS changeover. */
    private static final byte PTOVER_CHAR_CTRL_Z = 0x1A;
    /** OP poll while waiting for PTSend ({@code PD}) to leave the air. */
    private static final long FEC_WATCH_POLL_MS = 500;
    /** Wait this long for the first {@code PD} OPMODE after PTSend. */
    private static final long FEC_WAIT_ENTER_MS = 10_000;
    /** After seeing {@code PD}, wait this long for end (Idle then {@code Pt} or {@code PN}). */
    private static final long FEC_WAIT_LEAVE_MS = 300_000;
    /** Host PTSend used only by the PD bug check (100 baud, 3 repeats). */
    private static final String PD_BUG_COMMAND = "PD1,3";
    private static final int PD_BUG_PAYLOAD_LEN = 32;
    /** Inclusive Traffic-to-Idle dwell that means the repeat count was ignored. */
    private static final long PD_BUG_BUG_MIN_MS = 3_500;
    private static final long PD_BUG_BUG_MAX_MS = 4_500;
    /** Inclusive dwell for three 100-baud passes of 32 bytes. */
    private static final long PD_BUG_OK_MIN_MS = 11_000;
    private static final long PD_BUG_OK_MAX_MS = 13_000;

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
    private Ubit10MonitorWindow ubit10MonitorWindow;
    private PdBugCheckWindow pdBugCheckWindow;
    private DisplayMonitorWindow displayMonitorWindow;
    private final List<ConnectionWindow> deadArqWindows = new ArrayList<>();

    private volatile HostSession hostSession;
    private final AtomicBoolean tncBusy = new AtomicBoolean(false);
    /** Guards overlapping Listen FEC / End TX. */
    private final AtomicBoolean fecBusy = new AtomicBoolean(false);
    /** Guards overlapping Listen ON/OFF Host {@code OP}/{@code PN}/{@code Pt} round-trips. */
    private final AtomicBoolean listenHostBusy = new AtomicBoolean(false);
    /** One user macro at a time; Host commands and ISS sends share the single Host session. */
    private final AtomicBoolean macroBusy = new AtomicBoolean(false);
    private final MacroFile macroFile;
    private volatile Thread connectThread;
    private final AtomicBoolean connectCancelled = new AtomicBoolean(false);
    private volatile HostSession pendingSession;

    private volatile boolean tncConnected;
    /** Listen-on-start for this launch. Consumed by the first successful TNC connect. */
    private volatile boolean listenOnStartPending;
    /** Last Host {@code ML} query value, shown after TNC status. Cleared when not connected. */
    private volatile String tncMycall = "";
    private AppMode mode = AppMode.IDLE;

    /**
     * Outbound {@code PG} in progress (no ARQ window yet). Bumped on each new call / cancel /
     * timeout so a stale {@code PG} worker cannot clear a newer attempt.
     */
    private final AtomicInteger callingEpoch = new AtomicInteger(0);
    private volatile boolean calling;
    private volatile String callingCallsign;
    private Timer callingTimer;
    /**
     * Linked-ARQ {@code $50 Timeout}; consumed by the following {@code DISCONNECTED:}.
     * Not set for call no-answer (Timeout alone while Calling).
     */
    private volatile boolean pendingArqLinkTimeout;

    private final List<String> heardCalls = new ArrayList<>();
    private final List<String> mentionedCalls = new ArrayList<>();
    /** Session-only &lt;C&gt;onnect list; not persisted. */
    private final List<String> connectCalls = new ArrayList<>();
    /** Last connect-frame tokens (oldest first), cap {@link CallsignLineParser#CONNECT_WINDOW}. */
    private final List<String> connectFrameRecent = new ArrayList<>();

    /** Last UBIT 10 / seed-OP *w* byte ({@code $30}–{@code $37}), or null until one arrives. */
    private Integer lastLinkStatusN;
    /**
     * PD bug check is in flight (sending, or armed and waiting for Traffic then Idle).
     * Closing the window does not clear this; a second run waits until Idle or session close.
     */
    private final AtomicBoolean pdBugCheckBusy = new AtomicBoolean(false);
    private final Object pdBugLock = new Object();
    /** Set after the channel-0 data ACK. UBIT samples before this are ignored. */
    private boolean pdBugArmed;
    /** First {@code $34} after arm. Later Traffic pushes do not restart the clock. */
    private boolean pdBugSawTraffic;
    private long pdBugTrafficNanos;
    /** Window was closed; do not paint this run's dwell on a later window. */
    private boolean pdBugDropped;
    /** Serializes UBIT 10–triggered {@code OP}; extra {@code $50 n} queues one follow-up. */
    private final AtomicBoolean ubit10OpInFlight = new AtomicBoolean(false);
    private final AtomicBoolean ubit10OpFollowup = new AtomicBoolean(false);
    /**
     * UBIT 10 is *w* only. Idle IRS→ISS keeps {@code $33}, so the pickup side never
     * gets {@code $50 n}. While the ARQ window is IRS, solicit {@code OP} for *x*.
     */
    private static final int IRS_ROLE_WATCH_MS = 1000;
    private Timer irsRoleWatchTimer;

    public AppController(Path portableRoot) {
        this.portableRoot = Objects.requireNonNull(portableRoot);
        this.configStore = new ConfigStore(portableRoot);
        this.macroFile = new MacroFile(configStore.configDir());
        this.config = configStore.load();
        this.debugLog = new DebugLog(portableRoot);
        this.debugLog.setEnabled(config.isDebugLogEnabled());
        this.tncInitializer = new TncInitializer(
                debugLog, serialTapFanout, this::showStartupMessageOnEdt, this::showCompatInfoOnEdt,
                this::showCompatNotifyOnEdt, this::showInitWarningOnEdt, configStore.configDir());
        this.tncConnected = false;
        loadMonitorLists();
        try {
            new HostCommandIni(configStore.configDir()).ensureFile();
        } catch (IOException e) {
            debugLog.info("Could not create config.ini: " + e.getMessage());
        }
        try {
            macroFile.ensureFile();
        } catch (IOException e) {
            debugLog.info("Could not create macros.ini: " + e.getMessage());
        }
    }

    private void loadMonitorLists() {
        heardCalls.clear();
        mentionedCalls.clear();
        String own = ownCallsign();
        for (String call : configStore.loadMonitorList(configStore.heardFile())) {
            if (!call.equals(own)) {
                heardCalls.add(call);
            }
        }
        for (String call : configStore.loadMonitorList(configStore.mentionedFile())) {
            if (!call.equals(own)) {
                mentionedCalls.add(call);
            }
        }
    }

    public List<String> heardCalls() {
        return List.copyOf(heardCalls);
    }

    public List<String> mentionedCalls() {
        return List.copyOf(mentionedCalls);
    }

    public List<String> connectCalls() {
        return List.copyOf(connectCalls);
    }

    public void clearHeardList() {
        if (heardCalls.isEmpty()) {
            return;
        }
        heardCalls.clear();
        persistMonitorLists();
        if (mainWindow != null) {
            mainWindow.refreshMonitorLists();
        }
    }

    public void clearMentionedList() {
        if (mentionedCalls.isEmpty()) {
            return;
        }
        mentionedCalls.clear();
        persistMonitorLists();
        if (mainWindow != null) {
            mainWindow.refreshMonitorLists();
        }
    }

    public void clearHeardCall(String call) {
        if (removeMonitorCall(heardCalls, call)) {
            persistMonitorLists();
            if (mainWindow != null) {
                mainWindow.refreshMonitorLists();
            }
        }
    }

    public void clearMentionedCall(String call) {
        if (removeMonitorCall(mentionedCalls, call)) {
            persistMonitorLists();
            if (mainWindow != null) {
                mainWindow.refreshMonitorLists();
            }
        }
    }

    public void clearConnectList() {
        if (connectCalls.isEmpty() && connectFrameRecent.isEmpty()) {
            return;
        }
        connectCalls.clear();
        connectFrameRecent.clear();
        if (mainWindow != null) {
            mainWindow.refreshMonitorLists();
        }
    }

    public void clearConnectCall(String call) {
        if (removeMonitorCall(connectCalls, call)) {
            if (mainWindow != null) {
                mainWindow.refreshMonitorLists();
            }
        }
    }

    public void addBuddy(String call) {
        String u = normalizeListCall(call);
        if (u.isEmpty()) {
            return;
        }
        try {
            configStore.ensureBuddiesFile();
            List<String> buddies = configStore.loadBuddyList();
            if (buddies.contains(u)) {
                return;
            }
            buddies.add(0, u);
            configStore.saveBuddyList(buddies);
            if (mainWindow != null) {
                mainWindow.refreshBuddies();
            }
        } catch (IOException e) {
            debugLog.info("Could not add buddy: " + e.getMessage());
        }
    }

    public void moveBuddyToTop(String call) {
        String u = normalizeListCall(call);
        if (u.isEmpty()) {
            return;
        }
        try {
            List<String> buddies = configStore.loadBuddyList();
            if (!buddies.remove(u)) {
                return;
            }
            buddies.add(0, u);
            configStore.saveBuddyList(buddies);
            if (mainWindow != null) {
                mainWindow.refreshBuddies();
            }
        } catch (IOException e) {
            debugLog.info("Could not move buddy: " + e.getMessage());
        }
    }

    public void removeBuddy(String call) {
        String u = normalizeListCall(call);
        if (u.isEmpty()) {
            return;
        }
        try {
            List<String> buddies = configStore.loadBuddyList();
            if (!buddies.remove(u)) {
                return;
            }
            configStore.saveBuddyList(buddies);
            if (mainWindow != null) {
                mainWindow.refreshBuddies();
            }
        } catch (IOException e) {
            debugLog.info("Could not remove buddy: " + e.getMessage());
        }
    }

    private boolean removeMonitorCall(List<String> list, String call) {
        String u = normalizeListCall(call);
        return !u.isEmpty() && list.remove(u);
    }

    private static String normalizeListCall(String call) {
        return call == null ? "" : call.trim().toUpperCase(Locale.ROOT);
    }

    private String ownCallsign() {
        String own = config.getCallsign();
        return own == null ? "" : own.trim().toUpperCase(Locale.ROOT);
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

    public void openUbit10Monitor() {
        runOnEdt(() -> {
            if (ubit10MonitorWindow == null || !ubit10MonitorWindow.isDisplayable()) {
                ubit10MonitorWindow = new Ubit10MonitorWindow(this);
                ubit10MonitorWindow.setVisible(true);
            } else {
                ubit10MonitorWindow.toFront();
            }
        });
    }

    public void onUbit10MonitorClosed(Ubit10MonitorWindow window) {
        if (ubit10MonitorWindow == window) {
            ubit10MonitorWindow = null;
        }
        if (!tncConnected && !isAnyMonitorOpen()) {
            closeRetainedDebugSession();
        }
    }

    public void openPdBugCheck() {
        runOnEdt(() -> {
            if (pdBugCheckWindow == null || !pdBugCheckWindow.isDisplayable()) {
                pdBugCheckWindow = new PdBugCheckWindow(this);
                String progress = pdBugProgressStatus();
                if (progress != null) {
                    pdBugCheckWindow.holdForInProgress(progress);
                }
                pdBugCheckWindow.setVisible(true);
            } else {
                pdBugCheckWindow.toFront();
            }
        });
    }

    /**
     * Closing the window does not abort the transmission. The in-flight watch stays busy
     * until Idle or the Host session closes, and its dwell is not painted on a new window.
     */
    public void onPdBugCheckClosed(PdBugCheckWindow window) {
        if (pdBugCheckWindow == window) {
            pdBugCheckWindow = null;
        }
        synchronized (pdBugLock) {
            if (pdBugCheckBusy.get()) {
                pdBugDropped = true;
            }
        }
    }

    /**
     * Checkbox-gated PD bug check. Requires an open Host session. Does not refuse an
     * active ARQ or Listen FEC, and does not send {@code PN} or {@code Pt} afterward.
     */
    public void startPdBugCheck() {
        if (!SwingUtilities.isEventDispatchThread()) {
            runOnEdt(this::startPdBugCheck);
            return;
        }
        if (!pdBugCheckBusy.compareAndSet(false, true)) {
            return;
        }
        PdBugCheckWindow window = pdBugCheckWindow;
        HostSession session = hostSession;
        if (window == null || !tncConnected || session == null || !session.isOpen()) {
            pdBugCheckBusy.set(false);
            if (window != null) {
                window.noteNotConnected();
            }
            return;
        }
        synchronized (pdBugLock) {
            pdBugArmed = false;
            pdBugSawTraffic = false;
            pdBugDropped = false;
        }
        byte[] body = new byte[PD_BUG_PAYLOAD_LEN];
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < body.length; i++) {
            body[i] = (byte) random.nextInt(0x20, 0x7F);
        }
        byte[] withEnd = new byte[body.length + 1];
        System.arraycopy(body, 0, withEnd, 0, body.length);
        withEnd[body.length] = RECEIVE_CHAR_CTRL_D;
        window.beginRun(new String(body, StandardCharsets.US_ASCII));

        Thread worker = new Thread(() -> {
            try {
                sendHostOk(session, PD_BUG_COMMAND);
                session.sendData(0, withEnd, ARQ_HOST_TIMEOUT_MS);
                debugLog.info("PD bug check sent " + PD_BUG_COMMAND + " + "
                        + PD_BUG_PAYLOAD_LEN + " chars + CTRL-D");
                armPdBugWatch();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failPdBug("PD bug check interrupted.");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                failPdBug(msg);
            }
        }, "pd-bug-check");
        worker.setDaemon(true);
        worker.start();
    }

    public void openDisplayMonitor() {
        runOnEdt(() -> {
            if (displayMonitorWindow == null || !displayMonitorWindow.isDisplayable()) {
                displayMonitorWindow = new DisplayMonitorWindow(this);
                displayMonitorWindow.setVisible(true);
            } else {
                displayMonitorWindow.toFront();
            }
        });
    }

    public void onDisplayMonitorClosed(DisplayMonitorWindow window) {
        if (displayMonitorWindow == window) {
            displayMonitorWindow = null;
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

    private boolean isUbit10MonitorOpen() {
        Ubit10MonitorWindow w = ubit10MonitorWindow;
        return w != null && w.isDisplayable();
    }

    private boolean isDisplayMonitorOpen() {
        DisplayMonitorWindow w = displayMonitorWindow;
        return w != null && w.isDisplayable();
    }

    /** LED faceplate peek only while TNC → Dev Tools → Display is open. */
    private void refreshDisplayMonitorFromUbit10() {
        DisplayMonitorWindow w = displayMonitorWindow;
        if (w != null && w.isDisplayable()) {
            w.refreshFromHost();
        }
    }

    private boolean isAnyMonitorOpen() {
        return isDebugMonitorOpen() || isStatusMonitorOpen() || isUbit10MonitorOpen()
                || isDisplayMonitorOpen();
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

    /** Host {@code ML} query text, or empty when the TNC is not connected. */
    public String tncMycall() {
        return tncMycall == null ? "" : tncMycall;
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

    /**
     * Query Host {@code UB10} (Ch. 4 §4.2: no space after mnemonic; query is bit number only).
     * {@code callback} runs on the EDT with {@code true}/{@code false}, or {@code null}
     * if the TNC is offline or the reply could not be parsed.
     */
    public void queryUbit10(Consumer<Boolean> callback) {
        Objects.requireNonNull(callback, "callback");
        HostSession session = openHostSessionOrNull();
        if (session == null) {
            runOnEdt(() -> callback.accept(null));
            return;
        }
        Thread worker = new Thread(() -> {
            Boolean enabled = null;
            try {
                HostSession.CommandResponse response = session.sendCommand("UB10", ARQ_HOST_TIMEOUT_MS);
                enabled = parseUbitEnabled(hostQueryValue(response));
                debugLog.info("UBIT 10 query: " + (enabled == null ? "(unknown)" : enabled));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("UBIT 10 query interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "failed" : e.getMessage();
                debugLog.info("UBIT 10 query failed: " + msg);
            }
            Boolean result = enabled;
            runOnEdt(() -> callback.accept(result));
        }, "ubit10-query");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * Read-only peek of 8536 ports B/C (digital LED drive). {@code callback} runs on the EDT.
     * {@link DigitalLedPeek#error} is set when the TNC is offline or the Host round-trip fails.
     */
    public void peekDigitalLeds(Consumer<DigitalLedPeek> callback) {
        Objects.requireNonNull(callback, "callback");
        HostSession session = openHostSessionOrNull();
        if (session == null) {
            runOnEdt(() -> callback.accept(DigitalLedPeek.fail("No open TNC serial session.")));
            return;
        }
        Thread worker = new Thread(() -> {
            DigitalLedPeek result;
            try {
                int[] ports = session.readLedDrivePorts(ARQ_HOST_TIMEOUT_MS);
                DigitalLedState state = DigitalLedState.fromPorts(ports[0], ports[1]);
                debugLog.info(String.format("LED peek B=$%02X C=$%02X", ports[0], ports[1]));
                result = DigitalLedPeek.ok(state);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("LED peek interrupted");
                result = DigitalLedPeek.fail("LED peek interrupted.");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed." : e.getMessage();
                debugLog.info("LED peek failed: " + msg);
                result = DigitalLedPeek.fail(msg);
            }
            DigitalLedPeek done = result;
            runOnEdt(() -> callback.accept(done));
        }, "led-peek");
        worker.setDaemon(true);
        worker.start();
    }

    /** Result of a digital LED port peek. */
    public static final class DigitalLedPeek {
        public final DigitalLedState state;
        public final String error;

        private DigitalLedPeek(DigitalLedState state, String error) {
            this.state = state;
            this.error = error;
        }

        static DigitalLedPeek ok(DigitalLedState state) {
            return new DigitalLedPeek(state, null);
        }

        static DigitalLedPeek fail(String error) {
            return new DigitalLedPeek(null, error);
        }
    }

    /**
     * Set UBIT 10 via Host {@code UB10 ON} / {@code UB10 OFF} (verbose args after a space;
     * Ch. 4 §4.2) and wait for the command reply. {@code onDone} runs on the EDT with
     * {@code null} on success, or an error string.
     */
    public void setUbit10(boolean enabled, Consumer<String> onDone) {
        Objects.requireNonNull(onDone, "onDone");
        HostSession session = openHostSessionOrNull();
        if (session == null) {
            runOnEdt(() -> onDone.accept("No open TNC serial session."));
            return;
        }
        String cmd = enabled ? "UB10 ON" : "UB10 OFF";
        Thread worker = new Thread(() -> {
            String error = null;
            try {
                HostSession.CommandResponse response = session.sendCommand(cmd, ARQ_HOST_TIMEOUT_MS);
                Boolean parsed = parseUbitEnabled(hostQueryValue(response));
                if (!response.ok() && parsed == null) {
                    error = cmd + " failed, status=0x" + Integer.toHexString(response.statusCode);
                } else if (parsed != null && parsed != enabled) {
                    error = "TNC UBIT 10 is still " + (parsed ? "ON" : "OFF") + " after " + cmd + ".";
                } else {
                    debugLog.info("UBIT 10 set " + (enabled ? "ON" : "OFF"));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                error = "UBIT 10 command interrupted.";
            } catch (IOException e) {
                error = e.getMessage() == null ? "Host I/O failed." : e.getMessage();
            }
            String result = error;
            runOnEdt(() -> onDone.accept(result));
        }, "ubit10-set");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * Query Host {@code TL} (TMail). {@code callback} runs on the EDT with {@code true}/
     * {@code false}, or {@code null} if the TNC is offline or the reply could not be parsed.
     */
    public void queryTmail(Consumer<Boolean> callback) {
        Objects.requireNonNull(callback, "callback");
        HostSession session = openHostSessionOrNull();
        if (session == null) {
            runOnEdt(() -> callback.accept(null));
            return;
        }
        Thread worker = new Thread(() -> {
            Boolean enabled = null;
            try {
                HostSession.CommandResponse response = session.sendCommand("TL", ARQ_HOST_TIMEOUT_MS);
                enabled = parseTmailEnabled(hostQueryValue(response));
                debugLog.info("TMail query: " + (enabled == null ? "(unknown)" : enabled));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("TMail query interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "failed" : e.getMessage();
                debugLog.info("TMail query failed: " + msg);
            }
            Boolean result = enabled;
            runOnEdt(() -> callback.accept(result));
        }, "tmail-query");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * Disable TMail via Host {@code TLN}. {@code onDone} runs on the EDT with {@code null}
     * on success, or an error string.
     */
    public void disableTmail(Consumer<String> onDone) {
        Objects.requireNonNull(onDone, "onDone");
        HostSession session = openHostSessionOrNull();
        if (session == null) {
            runOnEdt(() -> onDone.accept("No open TNC serial session."));
            return;
        }
        Thread worker = new Thread(() -> {
            String error = null;
            try {
                HostSession.CommandResponse response = session.sendCommand("TLN", ARQ_HOST_TIMEOUT_MS);
                Boolean parsed = parseTmailEnabled(hostQueryValue(response));
                if (!response.ok() && parsed == null) {
                    error = "TLN failed, status=0x" + Integer.toHexString(response.statusCode);
                } else if (parsed != null && parsed) {
                    error = "TNC TMail is still ON after TLN.";
                } else {
                    debugLog.info("TMail disabled (TLN)");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                error = "TMail disable interrupted.";
            } catch (IOException e) {
                error = e.getMessage() == null ? "Host I/O failed." : e.getMessage();
            }
            String result = error;
            runOnEdt(() -> onDone.accept(result));
        }, "tmail-disable");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * Host query value after {@code UB}: {@code Y}/{@code ON} or {@code N}/{@code OFF},
     * optionally prefixed by the bit number ({@code 10 ON}).
     */
    static Boolean parseUbitEnabled(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String v = raw.trim().toUpperCase(Locale.ROOT);
        if (v.startsWith("UBIT")) {
            v = v.substring(4).trim();
        }
        if (v.startsWith("10")) {
            v = v.substring(2).trim();
        }
        if (v.equals("Y") || v.equals("ON") || v.equals("YES")) {
            return Boolean.TRUE;
        }
        if (v.equals("N") || v.equals("OFF") || v.equals("NO")) {
            return Boolean.FALSE;
        }
        return null;
    }

    /**
     * Host {@code TL} / {@code TLN} value: {@code Y}/{@code ON} or {@code N}/{@code OFF},
     * optionally prefixed by verbose {@code TMail}.
     */
    static Boolean parseTmailEnabled(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String v = raw.trim().toUpperCase(Locale.ROOT);
        if (v.startsWith("TMAIL")) {
            v = v.substring(5).trim();
        }
        if (v.equals("Y") || v.equals("ON") || v.equals("YES")) {
            return Boolean.TRUE;
        }
        if (v.equals("N") || v.equals("OFF") || v.equals("NO")) {
            return Boolean.FALSE;
        }
        return null;
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

    /**
     * Clear TX and Handover — Host {@code TC} (TClear), wait ACK, then ch0 {@code $1A}.
     * Locks HO buttons until ISS again.
     */
    public void arqHandoverNow(ConnectionWindow window) {
        if (window == null || window.kind() != ConnectionWindow.Kind.ARQ || !window.isSessionActive()) {
            return;
        }
        if (window.isLocalIrs()) {
            noticeWindow(window, "Clear TX and Handover — not ISS (use Seize to take the link).");
            return;
        }
        if (!window.lockHandoverControls()) {
            noticeWindow(window, "Clear TX and Handover — handover already pending.");
            return;
        }
        runArqHostAction(window, "Clear TX and Handover", session -> {
            sendHostOk(session, "TC");
            sendCh0Control(session, PTOVER_CHAR_CTRL_Z);
        }, window::unlockHandoverControls,
                "Clear TX and Handover — sent TC then CTRL-Z ($1A); HO buttons locked until ISS again.");
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
        byte[] payload = hostDataWithControl(pending, PTOVER_CHAR_CTRL_Z);
        String notice = pending.isBlank()
                ? "HO after TX clear — sent CTRL-Z; HO buttons locked until ISS again."
                : "HO after TX clear — flushed App TX + CTRL-Z; HO buttons locked until ISS again.";
        runArqHostAction(window, "HO after TX clear",
                session -> session.sendData(0, payload, ARQ_HOST_TIMEOUT_MS),
                window::unlockHandoverControls,
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
        arqAbort(window, null);
    }

    /**
     * Same as {@link #arqAbort(ConnectionWindow)}; {@code onDone} runs on the EDT after
     * the window is marked dead (or immediately if there is nothing to abort).
     */
    public void arqAbort(ConnectionWindow window, Runnable onDone) {
        Runnable finished = () -> {
            if (onDone != null) {
                onDone.run();
            }
        };
        if (window == null || window.kind() != ConnectionWindow.Kind.ARQ) {
            runOnEdt(finished);
            return;
        }
        if (!window.isSessionActive()) {
            runOnEdt(finished);
            return;
        }
        boolean listenOn = mainWindow != null && mainWindow.isListenSelected();
        String mnemonic = listenOn ? "PN" : "Pt";

        if (!tncConnected) {
            runOnEdt(() -> {
                noticeArq(window, "Abort — TNC not connected; closing ARQ window.");
                markArqDead(window);
                finished.run();
            });
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            runOnEdt(() -> {
                noticeArq(window, "Abort — no open Host session; closing ARQ window.");
                markArqDead(window);
                finished.run();
            });
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
                finished.run();
            });
        }, "arq-abort");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * One-press FEC abort kept for callers that are not the control button.
     * The FEC Abort button uses {@link #confirmLinkAbort} ({@code TC} then {@code PN}).
     * This path sends {@code PN} if FEC/Monitor is on, else {@code Pt}, leaves the window
     * open, and refuses while an ARQ link is up.
     */
    public void fecAbort(ConnectionWindow window) {
        if (window == null || window.kind() != ConnectionWindow.Kind.LISTEN) {
            return;
        }
        if (hasActiveArq()) {
            runOnEdt(() -> noticeWindow(window, "Abort — unavailable while ARQ is up."));
            return;
        }
        boolean listenOn = mainWindow != null && mainWindow.isListenSelected();
        String mnemonic = listenOn ? "PN" : "Pt";
        if (!tncConnected) {
            runOnEdt(() -> noticeWindow(window, "Abort — TNC not connected."));
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            runOnEdt(() -> noticeWindow(window, "Abort — no open Host session."));
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
                resultNotice = "Abort — interrupted.";
                debugLog.info("FEC Abort interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                resultNotice = "Abort — " + msg;
                debugLog.info("FEC Abort failed: " + msg);
            }
            final String notice = resultNotice;
            runOnEdt(() -> noticeWindow(window, notice));
        }, "fec-abort");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * Two-press abort from the ARQ or FEC control button.
     * {@code TC}, wait for ack, then {@code PN} (FEC, or ARQ while FEC/Monitor is on) or {@code Pt},
     * wait for ack. Both acks paint the red transcript line and then mark an ARQ link dead.
     * Failure leaves the link up and reports on the notice line. {@code onDone} runs on the EDT
     * either way so the button can return to faint Abort. Exit and the Calling dialog keep
     * {@link #arqAbort(ConnectionWindow)}.
     */
    public void confirmLinkAbort(ConnectionWindow window, Runnable onDone) {
        Runnable finished = () -> {
            if (onDone != null) {
                onDone.run();
            }
        };
        if (window == null) {
            runOnEdt(finished);
            return;
        }
        boolean fec = window.kind() == ConnectionWindow.Kind.LISTEN;
        boolean arq = window.kind() == ConnectionWindow.Kind.ARQ;
        if (!fec && !arq) {
            runOnEdt(finished);
            return;
        }
        if (fec && hasActiveArq()) {
            runOnEdt(() -> {
                noticeWindow(window, "Abort — unavailable while ARQ is up.");
                finished.run();
            });
            return;
        }
        if (arq && !window.isSessionActive()) {
            runOnEdt(() -> {
                noticeWindow(window, "Abort — TNC not connected.");
                finished.run();
            });
            return;
        }
        boolean pn = fec || (mainWindow != null && mainWindow.isListenSelected());
        String mnemonic = pn ? "PN" : "Pt";
        if (!tncConnected) {
            runOnEdt(() -> {
                noticeWindow(window, "Abort — TNC not connected.");
                finished.run();
            });
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            runOnEdt(() -> {
                noticeWindow(window, "Abort — no open Host session.");
                finished.run();
            });
            return;
        }

        Thread worker = new Thread(() -> {
            String error = null;
            try {
                sendHostOk(session, "TC");
                sendHostOk(session, mnemonic);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                error = "Abort — interrupted.";
                debugLog.info("Abort sequence interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                error = "Abort — " + msg;
                debugLog.info("Abort sequence failed: " + msg);
            }
            final String failure = error;
            runOnEdt(() -> {
                if (failure != null) {
                    noticeWindow(window, failure);
                } else {
                    window.paintAbortSequenceComplete();
                    if (arq) {
                        markArqDead(window);
                    }
                }
                finished.run();
            });
        }, fec ? "fec-abort" : "arq-abort");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * Exit-path graceful disconnect: {@code TC}, {@code AG} if IRS, then ch0 {@code $04}.
     * Does not mark the window dead — wait for {@code DISCONNECTED} or abort.
     */
    public void beginExitGracefulDisconnect(ConnectionWindow window) {
        if (window == null || window.kind() != ConnectionWindow.Kind.ARQ || !window.isSessionActive()) {
            return;
        }
        boolean seize = window.isLocalIrs();
        HostSession session = hostSession;
        if (!tncConnected || session == null || !session.isOpen()) {
            debugLog.info("Exit disconnect — no Host session");
            return;
        }
        Thread worker = new Thread(() -> {
            try {
                sendHostOk(session, "TC");
                if (seize) {
                    sendHostOk(session, "AG");
                }
                sendCh0Control(session, RECEIVE_CHAR_CTRL_D);
                debugLog.info("Exit disconnect — TC"
                        + (seize ? " AG" : "") + " CTRL-D ($04)");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("Exit disconnect interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("Exit disconnect failed: " + msg);
            }
        }, "arq-exit-disc");
        worker.setDaemon(true);
        worker.start();
    }

    /** HO with text — canned handover + {@code $1A} in the same ch0 block. Locks HO until ISS again. */
    public void arqHoWithText(ConnectionWindow window) {
        runHandoverAction(window, "HO with text", config.getCannedHandoverText(), PTOVER_CHAR_CTRL_Z);
    }

    /** Disc. with text — canned disconnect + {@code $04} in the same ch0 block. */
    public void arqDiscWithText(ConnectionWindow window) {
        String canned = config.getCannedDisconnectText();
        runArqHostAction(window, "Disc. with text",
                session -> session.sendData(0,
                        hostDataWithControl(canned, RECEIVE_CHAR_CTRL_D),
                        ARQ_HOST_TIMEOUT_MS),
                null,
                "Disc. with text — sent canned text + CTRL-D ($04).",
                () -> paintCannedIfPresent(window, canned));
    }

    /**
     * ISS flush / commit: send text as Host channel-0 data, chunked per Ch. 4 §4.8.
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
                if (bytes.length == 0) {
                    return;
                }
                session.sendData(0, bytes, ARQ_HOST_TIMEOUT_MS);
                int chars = bytes.length;
                runOnEdt(() -> noticeWindow(window,
                        "ISS outbound — sent " + chars + " char(s) to TNC."));
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

    public List<MacroFile.Macro> loadMacros() {
        try {
            return macroFile.load();
        } catch (IOException e) {
            debugLog.info("Could not read macros.ini: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * @param originalName null when creating; the existing group name when editing
     * @return an error message, or null after the file is written and open bars are reloaded
     */
    public String saveMacro(String originalName, String newName, String body) {
        try {
            List<MacroFile.Macro> existing = macroFile.load();
            String nameErr = MacroFile.nameError(newName, existing, originalName);
            if (nameErr != null) {
                return nameErr;
            }
            String bodyErr = MacroFile.bodyError(body);
            if (bodyErr != null) {
                return bodyErr;
            }
            String name = newName.trim();
            List<String> lines = MacroFile.linesFromEditor(body);
            if (originalName == null) {
                macroFile.add(name, lines);
            } else {
                macroFile.replace(originalName, name, lines);
            }
            refreshMacroBars();
            return null;
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "Could not save macros." : e.getMessage();
            debugLog.info("Could not save macros.ini: " + msg);
            return msg;
        }
    }

    /** @return an error message, or null after the group is removed and open bars are reloaded */
    public String deleteMacro(String name) {
        try {
            macroFile.delete(name);
            refreshMacroBars();
            return null;
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "Could not delete macro." : e.getMessage();
            debugLog.info("Could not delete macro: " + msg);
            return msg;
        }
    }

    private void refreshMacroBars() {
        runOnEdt(() -> {
            if (listenWindow != null && listenWindow.isDisplayable()) {
                listenWindow.reloadMacros();
            }
            if (activeArqWindow != null && activeArqWindow.isDisplayable()) {
                activeArqWindow.reloadMacros();
            }
            for (ConnectionWindow w : new ArrayList<>(deadArqWindows)) {
                if (w != null && w.isDisplayable()) {
                    w.reloadMacros();
                }
            }
        });
    }

    /** Monospaced chat size on every open Listen and ARQ window. EDT-safe. */
    public void applyChatFont() {
        runOnEdt(() -> {
            if (listenWindow != null && listenWindow.isDisplayable()) {
                listenWindow.applyTextSize();
            }
            if (activeArqWindow != null && activeArqWindow.isDisplayable()) {
                activeArqWindow.applyTextSize();
            }
            for (ConnectionWindow w : new ArrayList<>(deadArqWindows)) {
                if (w != null && w.isDisplayable()) {
                    w.applyTextSize();
                }
            }
        });
    }

    /**
     * Recolor transcript runs that still use the previous outgoing or incoming color.
     * EDT-safe.
     */
    public void applyChatColors(Color previousOutgoing, Color previousIncoming) {
        runOnEdt(() -> {
            if (listenWindow != null && listenWindow.isDisplayable()) {
                listenWindow.applyTranscriptColors(previousOutgoing, previousIncoming);
            }
            if (activeArqWindow != null && activeArqWindow.isDisplayable()) {
                activeArqWindow.applyTranscriptColors(previousOutgoing, previousIncoming);
            }
            for (ConnectionWindow w : new ArrayList<>(deadArqWindows)) {
                if (w != null && w.isDisplayable()) {
                    w.applyTranscriptColors(previousOutgoing, previousIncoming);
                }
            }
        });
    }

    /**
     * Run one macro top to bottom on a background thread. Compose edits hop to the UI thread.
     * A send that actually transmits, and every Host command, finishes before the next line.
     * A bad mnemonic, a banned mnemonic, a missing TNC, or a failed ACK stops the rest.
     */
    public void runMacro(ConnectionWindow window, MacroFile.Macro macro) {
        if (window == null || macro == null) {
            return;
        }
        String name = macro.name();
        if (!window.isSessionActive()) {
            window.showNotice(name + " — session is not active.");
            return;
        }
        if (tncBusy.get()) {
            window.showNotice(name + " — TNC connect in progress.");
            return;
        }
        if (fecBusy.get()) {
            window.showNotice(name + " — FEC send in progress.");
            return;
        }
        if (!macroBusy.compareAndSet(false, true)) {
            window.showNotice(name + " — already running.");
            return;
        }
        List<String> lines = macro.lines();
        String threadName = "macro-" + name.replaceAll("[^A-Za-z0-9._-]+", "-");
        if (threadName.length() > 48) {
            threadName = threadName.substring(0, 48);
        }
        Thread worker = new Thread(() -> {
            try {
                runMacroLines(window, name, lines);
            } finally {
                macroBusy.set(false);
            }
        }, threadName);
        worker.setDaemon(true);
        worker.start();
    }

    private void runMacroLines(ConnectionWindow window, String name, List<String> lines) {
        try {
            for (String line : lines) {
                if (!window.isSessionActive()) {
                    noticeWindowLater(window, name + " — session is not active.");
                    return;
                }
                if (line.startsWith(">>") || line.startsWith("\\\\")) {
                    if (!appendMacroLine(window, line.substring(1))) {
                        noticeWindowLater(window, name + " — session is not active.");
                        return;
                    }
                } else if (line.startsWith(">")) {
                    ConnectionWindow.MacroSendStage staged = stageMacroSend(window, line.substring(1));
                    if (staged.inactive()) {
                        noticeWindowLater(window, name + " — session is not active.");
                        return;
                    }
                    if (staged.transmit() != null) {
                        sendMacroOutbound(staged.transmit());
                    }
                } else if (line.startsWith("\\")) {
                    sendMacroHost(line.substring(1).trim());
                } else if (!appendMacroLine(window, line)) {
                    noticeWindowLater(window, name + " — session is not active.");
                    return;
                }
            }
            noticeWindowLater(window, name + " — done.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            debugLog.info("Macro " + name + " interrupted");
            noticeWindowLater(window, name + " — interrupted.");
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "failed" : e.getMessage();
            debugLog.info("Macro " + name + " failed: " + msg);
            noticeWindowLater(window, name + " — " + msg);
        }
    }

    private boolean appendMacroLine(ConnectionWindow window, String text)
            throws InterruptedException, IOException {
        AtomicBoolean ok = new AtomicBoolean();
        onEdt(() -> ok.set(window.appendMacroComposeLine(text)));
        return ok.get();
    }

    private ConnectionWindow.MacroSendStage stageMacroSend(ConnectionWindow window, String text)
            throws InterruptedException, IOException {
        AtomicReference<ConnectionWindow.MacroSendStage> staged = new AtomicReference<>();
        onEdt(() -> staged.set(window.stageMacroSendLine(text)));
        return staged.get();
    }

    private void onEdt(Runnable action) throws InterruptedException, IOException {
        try {
            SwingUtilities.invokeAndWait(action);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            String msg = cause.getMessage() == null ? "Macro UI update failed." : cause.getMessage();
            throw new IOException(msg, cause);
        }
    }

    private void sendMacroOutbound(String text) throws IOException, InterruptedException {
        if (!tncConnected) {
            throw new IOException("TNC not connected.");
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            throw new IOException("no open Host session.");
        }
        byte[] bytes = toHostDataBytes(text);
        if (bytes.length == 0) {
            return;
        }
        session.sendData(0, bytes, ARQ_HOST_TIMEOUT_MS);
        debugLog.info("Macro outbound sent " + bytes.length + " char(s).");
    }

    private void sendMacroHost(String command) throws IOException, InterruptedException {
        HostCommandIni.InitLine parsed = HostCommandIni.parseCommandLine(command);
        if (parsed == null || parsed.invalid()) {
            throw new IOException("Host command needs a 2-character mnemonic.");
        }
        if (parsed.banned()) {
            throw new IOException(parsed.mnemonic() + " is not allowed.");
        }
        if (parsed.extraSpaces()) {
            debugLog.info("Macro host command had extra spaces: " + command);
        }
        if (!tncConnected) {
            throw new IOException("TNC not connected.");
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            throw new IOException("no open Host session.");
        }
        sendHostOk(session, parsed.wire());
    }

    private void noticeWindowLater(ConnectionWindow window, String text) {
        runOnEdt(() -> noticeWindow(window, text));
    }

    /**
     * Listen FEC / End TX: Host {@code PD} (PTSend), then App TX text as ch0 data (chunked §4.8),
     * then CTRL-D ({@code $04}) so the TNC returns to receive after TX clear.
     * After OPMODE leaves {@code PD}, restore Listen only if the TNC landed on {@code Pt}
     * (older firmware). Newer firmware returns to {@code PN} on its own — do not send {@code PN}.
     * Caller paints grey transcript first. FEC / End TX also clears App TX; CQ does not.
     */
    public void listenFecEndTx(ConnectionWindow window, String text) {
        listenFecEndTx(window, text, "FEC / End TX", "PD");
    }

    /**
     * @param pdCommand Host PTSend spelling captured when FEC / End TX or CQ was clicked
     *                  ({@code PD}, {@code PD1,1}, or {@code PD2,2})
     */
    public void listenFecEndTx(ConnectionWindow window, String text, String actionName, String pdCommand) {
        String action = actionName == null || actionName.isBlank() ? "FEC / End TX" : actionName;
        final String pdCmd = pdCommand == null || pdCommand.isBlank() ? "PD" : pdCommand;
        if (window == null || window.kind() != ConnectionWindow.Kind.LISTEN) {
            return;
        }
        String payloadText = text == null ? "" : text;
        if (payloadText.isBlank()) {
            noticeWindow(window, action + " — nothing to send.");
            return;
        }
        if (!tncConnected) {
            noticeWindow(window, action + " — TNC not connected (transcript only).");
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            noticeWindow(window, action + " — no open Host session (transcript only).");
            return;
        }
        if (!fecBusy.compareAndSet(false, true)) {
            noticeWindow(window, action + " — already in progress.");
            return;
        }

        Thread worker = new Thread(() -> {
            String resultNotice = null;
            boolean sent = false;
            try {
                runOnEdt(() -> {
                    mode = AppMode.UNPROTO;
                    if (mainWindow != null) {
                        mainWindow.refreshModeLabel();
                    }
                });
                sendHostOk(session, pdCmd);
                byte[] body = toHostDataBytes(payloadText);
                byte[] withEnd = new byte[body.length + 1];
                System.arraycopy(body, 0, withEnd, 0, body.length);
                withEnd[body.length] = RECEIVE_CHAR_CTRL_D;
                session.sendData(0, withEnd, ARQ_HOST_TIMEOUT_MS);
                sent = true;
                String sentNotice = action + " — sent " + pdCmd + " + " + body.length
                        + " char(s) + CTRL-D (grey until TX-empty).";
                runOnEdt(() -> noticeWindow(window, sentNotice));
                watchPostFecListenCompat(session);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                resultNotice = sent ? null : action + " — interrupted.";
                debugLog.info(action + " interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                if (!sent) {
                    resultNotice = action + " — " + msg;
                }
                debugLog.info(action + " failed: " + msg);
            } finally {
                fecBusy.set(false);
                if (!sent) {
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
            }
            if (resultNotice != null) {
                final String notice = resultNotice;
                runOnEdt(() -> noticeWindow(window, notice));
            }
        }, "listen-fec-end-tx");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * Poll {@code OP} until PTSend ({@code PD}) has finished. Idle ({@code $33}) is still FEC
     * (TX about to end). When the tag leaves {@code PD}:
     * <ul>
     *   <li>{@code Pt} and Listen still on → {@code PN} (older firmware stays in standby)</li>
     *   <li>{@code PN} → nothing (later firmware auto-Listen)</li>
     *   <li>Listen off and {@code PN} → {@code Pt} (honor Listen OFF)</li>
     * </ul>
     * No {@code PN}/{@code Pt} while ARQ is active.
     */
    private void watchPostFecListenCompat(HostSession session)
            throws IOException, InterruptedException {
        if (session == null || !session.isOpen()) {
            return;
        }
        long started = System.currentTimeMillis();
        boolean sawPd = false;
        while (tncConnected && session.isOpen() && !hasActiveArq()) {
            OpmodeParser.Decoded decoded = queryOpmode(session);
            long elapsed = System.currentTimeMillis() - started;
            if (decoded != null && decoded.isPactorFec()) {
                sawPd = true;
            } else if (decoded != null && decoded.isPactorStandby()) {
                applyPostFecListenCompat(session, decoded);
                return;
            } else if (decoded != null && decoded.isPactorListen()) {
                if (sawPd || elapsed >= FEC_WAIT_ENTER_MS) {
                    applyPostFecListenCompat(session, decoded);
                    return;
                }
            } else if (sawPd) {
                debugLog.info("FEC ended — OPMODE "
                        + (decoded == null || decoded.modeName == null ? "unknown" : decoded.modeName)
                        + " (no PN/Pt)");
                return;
            } else if (elapsed >= FEC_WAIT_ENTER_MS) {
                debugLog.info("FEC watch — never saw PD OPMODE; leaving TNC as-is");
                return;
            }
            if (sawPd && elapsed >= FEC_WAIT_LEAVE_MS) {
                debugLog.info("FEC watch — still PD after " + FEC_WAIT_LEAVE_MS + " ms; not sending PN");
                return;
            }
            Thread.sleep(FEC_WATCH_POLL_MS);
        }
    }

    private void applyPostFecListenCompat(HostSession session, OpmodeParser.Decoded decoded)
            throws IOException, InterruptedException {
        applyListenCompatFromOpmode(session, decoded, "FEC ended");
    }

    /**
     * After FEC or ARQ, align TNC {@code Pt}/{@code PN} with the Listen checkbox.
     * {@code Pt} + Listen on → {@code PN}. Already {@code PN} + Listen on → nothing.
     * {@code PN} + Listen off → {@code Pt}. Ignores other OPMODE tags.
     */
    private void applyListenCompatFromOpmode(HostSession session, OpmodeParser.Decoded decoded,
                                            String reason)
            throws IOException, InterruptedException {
        if (session == null || decoded == null || hasActiveArq()) {
            return;
        }
        boolean listenOn = mainWindow != null && mainWindow.isListenSelected();
        if (decoded.isPactorStandby()) {
            if (listenOn) {
                sendHostOk(session, "PN");
                debugLog.info(reason + " — Pt; sent PN (Listen still on)");
                queryOpmode(session);
            } else {
                debugLog.info(reason + " — Pt; Listen off, left standby");
            }
            return;
        }
        if (decoded.isPactorListen()) {
            if (listenOn) {
                debugLog.info(reason + " — already PN; no Host change");
            } else {
                sendHostOk(session, "Pt");
                debugLog.info(reason + " — PN but Listen off; sent Pt");
                queryOpmode(session);
            }
        }
    }

    /**
     * ARQ is dead: {@code OP}, then the same Listen {@code Pt}/{@code PN} rule as FEC.
     * Starts a worker; safe from the EDT.
     */
    private void restoreListenAfterArq() {
        HostSession session = hostSession;
        if (session == null || !session.isOpen() || hasActiveArq()) {
            return;
        }
        Thread worker = new Thread(() -> {
            try {
                OpmodeParser.Decoded decoded = queryOpmode(session);
                applyListenCompatFromOpmode(session, decoded, "ARQ ended");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("ARQ Listen restore interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("ARQ Listen restore failed: " + msg);
            }
        }, "arq-listen-compat");
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
     * Appends a trailing CR if missing.
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

    /**
     * Host {@code ML} query (Ch. 4 §4.3.1): reply is {@code ML} + verbose value, not ACK {@code $00}.
     * Empty on timeout or a blank payload — does not fail TNC Connect.
     */
    private String queryMycall(HostSession session) {
        if (session == null || !session.isOpen()) {
            return "";
        }
        try {
            HostSession.CommandResponse response = session.sendCommand("ML", ARQ_HOST_TIMEOUT_MS);
            String value = hostQueryValue(response);
            debugLog.info("ML query: " + (value.isEmpty() ? "(empty)" : value));
            return value;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            debugLog.info("ML query interrupted");
            return "";
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "failed" : e.getMessage();
            debugLog.info("ML query failed: " + msg);
            return "";
        }
    }

    /**
     * ASCII after the two-letter mnemonic. Query replies have no status byte; skip a leading
     * {@code $00} if firmware still sends one. Strip a verbose {@code MYcall} prefix.
     */
    static String hostQueryValue(HostSession.CommandResponse response) {
        if (response == null || response.frame == null || response.frame.payload == null) {
            return "";
        }
        byte[] payload = response.frame.payload;
        if (payload.length < 3) {
            return "";
        }
        int start = 2;
        if (payload[start] == 0x00) {
            start++;
        }
        if (start >= payload.length) {
            return "";
        }
        String raw = new String(payload, start, payload.length - start, StandardCharsets.US_ASCII)
                .replace("\r", "")
                .replace("\n", "")
                .trim();
        if (raw.length() >= 6 && raw.regionMatches(true, 0, "mycall", 0, 6)) {
            raw = raw.substring(6).trim();
        }
        return raw;
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
        runArqHostAction(window, actionName, work, null, actionName + " — sent.", null);
    }

    private void runArqHostAction(ConnectionWindow window, String actionName, ArqHostWork work,
            Runnable onFailure, String successNotice) {
        runArqHostAction(window, actionName, work, onFailure, successNotice, null);
    }

    private void runArqHostAction(ConnectionWindow window, String actionName, ArqHostWork work,
            Runnable onFailure, String successNotice, Runnable onDispatch) {
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
        if (onDispatch != null) {
            onDispatch.run();
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
        byte[] payload = hostDataWithControl(canned, control);
        runArqHostAction(window, actionName,
                session -> session.sendData(0, payload, ARQ_HOST_TIMEOUT_MS),
                window::unlockHandoverControls,
                actionName + " — sent CTRL-Z; HO buttons locked until ISS again.",
                () -> paintCannedIfPresent(window, canned));
    }

    /** Grey transcript paint for canned HO / Disc text. No-op when there is no text. */
    private static void paintCannedIfPresent(ConnectionWindow window, String canned) {
        if (window != null) {
            window.paintCannedLocalOutbound(canned);
        }
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
        if (event.type() == HostEvent.Type.LINK_MESSAGE) {
            HostFrameCodec.Frame linkFrame = event.frame();
            if (LinkMessageParser.isUbit10StatusChange(linkFrame)) {
                int n = LinkMessageParser.ubit10StatusByte(linkFrame);
                long stampNanos = System.nanoTime();
                lastLinkStatusN = n;
                notePdBugUbit(n, stampNanos);
                runOnEdt(() -> paintActiveLinkStatus(n));
                requestOpFromUbit10();
                runOnEdt(this::refreshDisplayMonitorFromUbit10);
                return;
            }
            String connected = LinkMessageParser.connectedPeer(linkFrame);
            if (connected != null) {
                runOnEdt(() -> onArqLinkConnected(connected));
                return;
            }
            if (LinkMessageParser.isTimeout(linkFrame)) {
                runOnEdt(this::onArqLinkTimeout);
                return;
            }
            String disconnected = LinkMessageParser.disconnectedPeer(linkFrame);
            if (disconnected != null) {
                runOnEdt(() -> onArqLinkDisconnected(disconnected));
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
     * {@code x} (S/R) drives ISS/IRS. ARQ end is {@code $50 DISCONNECTED:} (and {@code Timeout}
     * immediately before it). Call no-answer is {@code $50 Timeout} alone while Calling.
     * OPMODE {@code Pt} / *w*=Standby after a live OPMODE is only a fallback if that
     * link message is missed.
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
            applyMainModeFromOpmode(decoded);
            stopIrsRoleWatch();
            return;
        }
        if (decoded.standby) {
            String w = decoded.wLabel != null ? decoded.wLabel : "Standby";
            arq.applyOpmodeLink(decoded.hasDirection() ? decoded.transmit : null, decoded.pactorBaud);
            if (arq.hasSeenLiveOpmode()) {
                markArqDead(arq);
                noticeArq(arq, "ARQ ended — OPMODE " + w + " (no $50 DISCONNECTED).");
            }
            syncIrsRoleWatch();
            return;
        }
        arq.markOpmodeLive();
        arq.applyOpmodeLink(decoded.hasDirection() ? decoded.transmit : null, decoded.pactorBaud);
        syncIrsRoleWatch();
    }

    /**
     * While linked IRS, poll {@code OP} so an Idle *w* pickup still sees *x*=S.
     * Stop as soon as OPMODE reports ISS or the ARQ window dies.
     */
    private void syncIrsRoleWatch() {
        ConnectionWindow arq = activeArqWindow;
        boolean need = arq != null && arq.isSessionActive() && arq.isLocalIrs();
        if (need) {
            startIrsRoleWatch();
        } else {
            stopIrsRoleWatch();
        }
    }

    private void startIrsRoleWatch() {
        runOnEdt(() -> {
            if (irsRoleWatchTimer != null && irsRoleWatchTimer.isRunning()) {
                return;
            }
            if (irsRoleWatchTimer == null) {
                irsRoleWatchTimer = new Timer(IRS_ROLE_WATCH_MS, e -> {
                    ConnectionWindow arq = activeArqWindow;
                    if (arq == null || !arq.isSessionActive() || !arq.isLocalIrs()) {
                        stopIrsRoleWatch();
                        return;
                    }
                    requestOpFromUbit10();
                });
                irsRoleWatchTimer.setRepeats(true);
            }
            irsRoleWatchTimer.start();
        });
    }

    private void stopIrsRoleWatch() {
        runOnEdt(() -> {
            if (irsRoleWatchTimer != null && irsRoleWatchTimer.isRunning()) {
                irsRoleWatchTimer.stop();
            }
        });
    }

    /** Map a Pactor OPMODE reply onto the main-window Mode label when no ARQ window is live. */
    private void applyMainModeFromOpmode(OpmodeParser.Decoded decoded) {
        if (decoded == null || hasActiveArq()) {
            return;
        }
        if (decoded.isPactorArq() && !decoded.standby) {
            return;
        }
        if (decoded.isPactorFec()) {
            mode = AppMode.UNPROTO;
        } else if (decoded.isPactorListen()) {
            mode = AppMode.LISTEN;
        } else if (decoded.isPactorStandby() || decoded.standby) {
            mode = AppMode.IDLE;
        }
        if (mainWindow != null) {
            mainWindow.refreshModeLabel();
        }
    }

    /**
     * UBIT 10 {@code SOH $50 n ETB} is only *w*. Send {@code OP} for ISS/IRS / baud / mode tag.
     * Overlapping frames queue one follow-up {@code OP} after the in-flight round-trip.
     */
    private void requestOpFromUbit10() {
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            return;
        }
        ubit10OpFollowup.set(true);
        boolean started = ubit10OpInFlight.compareAndSet(false, true);
        if (!started) {
            return;
        }
        Thread worker = new Thread(() -> {
            try {
                while (ubit10OpFollowup.compareAndSet(true, false)) {
                    HostSession s = hostSession;
                    if (s == null || !s.isOpen()) {
                        break;
                    }
                    try {
                        // OPMODE replies are not ACK $00; do not use sendHostOk.
                        s.sendCommand("OP", ARQ_HOST_TIMEOUT_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        debugLog.info("UBIT 10 OP interrupted");
                        break;
                    } catch (IOException e) {
                        String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                        debugLog.info("UBIT 10 OP failed: " + msg);
                        break;
                    }
                }
            } finally {
                ubit10OpInFlight.set(false);
                if (ubit10OpFollowup.get()) {
                    requestOpFromUbit10();
                }
            }
        }, "ubit10-op");
        worker.setDaemon(true);
        worker.start();
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
        if (!connected) {
            tncMycall = "";
            failPdBug("Host session closed.");
        }
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
        int choice = showCompatWarningOnEdt(result.compat);
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
            tncMycall = queryMycall(result.session);
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
                    if (ubit10MonitorWindow != null && ubit10MonitorWindow.isDisplayable()) {
                        ubit10MonitorWindow.refreshUbit10FromTnc();
                    }
                    boolean fromStart = listenOnStartPending;
                    listenOnStartPending = false;
                    boolean checkbox = mainWindow != null && mainWindow.isListenSelected();
                    if ((fromStart || checkbox) && !hasActiveArq()) {
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

    /** TNC menu Disconnect: leave Host Mode, close the session, and clear connected flag. */
    public void disconnectTnc() {
        disconnectTnc(false);
    }

    /**
     * @param waitForClose if true, block until {@code HON} and port close finish (program exit).
     */
    private void disconnectTnc(boolean waitForClose) {
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
        stopCallingUi();
        stopIrsRoleWatch();
        if (mainWindow != null) {
            mainWindow.refreshConnectionState();
        }
        debugLog.info("TNC disconnect requested");

        Thread closer = new Thread(() -> {
            tncInitializer.abort(session, waitForClose);
            tncInitializer.abort(pending, waitForClose);
        }, "tnc-disconnect");
        closer.setDaemon(!waitForClose);
        closer.start();
        if (waitForClose) {
            try {
                closer.join(6000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void showStartupMessageOnEdt(String message) throws InterruptedException {
        if (!config.isDisplayStartup()) {
            return;
        }
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

    private void showInitWarningOnEdt(String title, String message) throws InterruptedException {
        if (SwingUtilities.isEventDispatchThread()) {
            showInitWarningDialog(title, message);
            return;
        }
        try {
            SwingUtilities.invokeAndWait(() -> showInitWarningDialog(title, message));
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new InterruptedException("INIT warning dialog failed: " + e.getMessage());
        }
    }

    private void showInitWarningDialog(String title, String message) {
        String body = message == null ? "" : message.replace("\r\n", "\n").replace('\r', '\n');
        JOptionPane.showMessageDialog(
                mainWindow,
                body,
                title == null || title.isBlank() ? "INIT" : title,
                JOptionPane.WARNING_MESSAGE);
    }

    private void showCompatInfoOnEdt(String message) {
        if (!config.isDisplayStartup()) {
            return;
        }
        if (SwingUtilities.isEventDispatchThread()) {
            showCompatInfoDialog(message);
            return;
        }
        SwingUtilities.invokeLater(() -> showCompatInfoDialog(message));
    }

    private void showCompatNotifyOnEdt(String starredLabel) {
        if (SwingUtilities.isEventDispatchThread()) {
            CompatNotifyDialog.show(mainWindow, starredLabel);
            return;
        }
        SwingUtilities.invokeLater(() -> CompatNotifyDialog.show(mainWindow, starredLabel));
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

    private int showCompatWarningOnEdt(CompatResult compat) throws InterruptedException {
        if (SwingUtilities.isEventDispatchThread()) {
            return CompatWarningDialog.show(mainWindow, compat);
        }
        final int[] choice = {JOptionPane.CLOSED_OPTION};
        try {
            SwingUtilities.invokeAndWait(() ->
                    choice[0] = CompatWarningDialog.show(mainWindow, compat));
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new InterruptedException("Compat warning dialog failed: " + e.getMessage());
        }
        return choice[0];
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
        if (mainWindow != null) {
            mainWindow.refreshMonitorLists();
        }
    }

    /** Listen inbound line completed (newline after {@code $08}). EDT. */
    public void onListenInboundLine(String line) {
        if (line == null || line.isEmpty()) {
            return;
        }
        CallsignLineParser.Hits hits = CallsignLineParser.parse(line);
        if (hits.connectToken != null) {
            noteConnectFrame(hits.connectToken);
            return;
        }
        boolean changed = false;
        for (String call : hits.heard) {
            changed |= promoteMonitorCall(heardCalls, call);
        }
        for (String call : hits.mentioned) {
            changed |= promoteMonitorCall(mentionedCalls, call);
        }
        if (!changed) {
            return;
        }
        persistMonitorLists();
        if (mainWindow != null) {
            mainWindow.refreshMonitorLists();
        }
    }

    /**
     * Connect frame: never Heard/Mentioned. Session list updates only if a gate passes.
     */
    private void noteConnectFrame(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }
        connectFrameRecent.add(token);
        while (connectFrameRecent.size() > CallsignLineParser.CONNECT_WINDOW) {
            connectFrameRecent.remove(0);
        }
        String promo = CallsignLineParser.promoteConnect(connectFrameRecent);
        if (promo == null || !promoteMonitorCall(connectCalls, promo)) {
            return;
        }
        if (mainWindow != null) {
            mainWindow.refreshMonitorLists();
        }
    }

    private boolean promoteMonitorCall(List<String> list, String call) {
        if (call == null || call.isEmpty()) {
            return false;
        }
        String u = call.toUpperCase(Locale.ROOT);
        if (u.equals(ownCallsign())) {
            return false;
        }
        int existing = list.indexOf(u);
        if (existing == 0) {
            return false;
        }
        if (existing > 0) {
            list.remove(existing);
        }
        list.add(0, u);
        while (list.size() > ConfigStore.MONITOR_LIST_CAP) {
            list.remove(list.size() - 1);
        }
        return true;
    }

    private void persistMonitorLists() {
        try {
            configStore.saveMonitorList(configStore.heardFile(), heardCalls);
            configStore.saveMonitorList(configStore.mentionedFile(), mentionedCalls);
        } catch (IOException e) {
            debugLog.info("Could not save heard/mentioned: " + e.getMessage());
        }
    }

    public MainWindow mainWindow() {
        return mainWindow;
    }

    /** One-shot: open Listen after the next successful TNC connect, not before. */
    public void armListenOnStart() {
        listenOnStartPending = true;
    }

    /**
     * Forget saved main, ARQ, and FEC bounds, then stack whatever is open on the main window.
     */
    public void resetWindowLocations() {
        config.setWindowMain("");
        config.setWindowArq("");
        config.setWindowFec("");
        saveConfig();
        if (mainWindow == null) {
            return;
        }
        ArrayList<Window> stack = new ArrayList<>();
        if (listenWindow != null && listenWindow.isDisplayable()) {
            stack.add(listenWindow);
        }
        if (activeArqWindow != null && activeArqWindow.isDisplayable()) {
            stack.add(activeArqWindow);
        }
        for (ConnectionWindow w : new ArrayList<>(deadArqWindows)) {
            if (w != null && w.isDisplayable() && w != activeArqWindow && w != listenWindow) {
                stack.add(w);
            }
        }
        WindowPlacement.restack(mainWindow, stack);
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

    /**
     * One {@code OP} when a connection window is created. Paints *w* only if that window
     * is still the active link window when the reply arrives. Later {@code OP} replies
     * do not move the chip.
     */
    private void seedLinkStatus(ConnectionWindow window) {
        if (window == null || !tncConnected) {
            return;
        }
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            return;
        }
        Integer atStart = lastLinkStatusN;
        Thread worker = new Thread(() -> {
            try {
                OpmodeParser.Decoded decoded = queryOpmode(session);
                Integer n = OpmodeParser.wByte(decoded == null ? null : decoded.wLabel);
                if (n == null) {
                    return;
                }
                runOnEdt(() -> {
                    if (lastLinkStatusN != atStart) {
                        return;
                    }
                    if (!isActiveLinkWindow(window) || window.isLinkStatusFrozen()) {
                        return;
                    }
                    lastLinkStatusN = n;
                    window.showLinkByte(n);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("Link-status seed interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("Link-status seed failed: " + msg);
            }
        }, "link-status-seed");
        worker.setDaemon(true);
        worker.start();
    }

    /** UBIT 10 *n* paints only the active window. EDT. */
    private void paintActiveLinkStatus(int n) {
        ConnectionWindow target = activeLinkWindow();
        if (target != null) {
            target.showLinkByte(n);
        }
    }

    private void applyRememberedLinkStatus(ConnectionWindow window) {
        if (window == null) {
            return;
        }
        if (lastLinkStatusN != null) {
            window.showLinkByte(lastLinkStatusN);
        } else {
            window.showStandby();
        }
    }

    /**
     * Live ARQ window, otherwise the session-active Listen window.
     */
    private ConnectionWindow activeLinkWindow() {
        if (activeArqWindow != null && activeArqWindow.isSessionActive()
                && !activeArqWindow.isLinkStatusFrozen()) {
            return activeArqWindow;
        }
        if (listenWindow != null && listenWindow.isSessionActive()
                && !listenWindow.isLinkStatusFrozen()) {
            return listenWindow;
        }
        return null;
    }

    private boolean isActiveLinkWindow(ConnectionWindow window) {
        return window != null && window == activeLinkWindow();
    }

    /** OPMODE query — replies are not ACK {@code $00}; do not use {@link #sendHostOk}. */
    private OpmodeParser.Decoded queryOpmode(HostSession session)
            throws IOException, InterruptedException {
        HostSession.CommandResponse response = session.sendCommand("OP", ARQ_HOST_TIMEOUT_MS);
        return OpmodeParser.decode(response.frame);
    }

    private void ensureListenWindow(boolean active) {
        boolean created = listenWindow == null;
        if (created) {
            listenWindow = new ConnectionWindow(this, ConnectionWindow.Kind.LISTEN, "Listen");
            listenWindow.setInboundLineListener(this::onListenInboundLine);
            listenWindow.setVisible(true);
        }
        boolean live = active && mode != AppMode.ARQ;
        listenWindow.setSessionActive(live);
        if (!live) {
            listenWindow.showInactive();
        } else {
            applyRememberedLinkStatus(listenWindow);
            if (created) {
                seedLinkStatus(listenWindow);
            }
        }
        if (!tncConnected) {
            listenWindow.showNotice("Offline preview — TNC not connected. Layout only.");
        }
    }

    /**
     * Main-window Connect / buddy double-click: send Host {@code PG}+callsign (PTConn) without
     * opening an ARQ window. The window opens on {@code $50} CONNECTED (same path as inbound).
     * If main-window {@code LP:} is checked, prefix {@code !} unless the call already has one.
     * Does not rewrite the callsign field.
     */
    public void requestConnect(String remoteCallsign) {
        if (!tncConnected) {
            JOptionPane.showMessageDialog(mainWindow,
                    "TNC is not connected. Use TNC → Connect after configuring the COM port.",
                    "PactorRATT_Alpha",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (hasActiveArq()) {
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
        if (mainWindow != null && mainWindow.isLongpathSelected() && !call.startsWith("!")) {
            call = "!" + call;
        }
        final String pgCall = call;
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            JOptionPane.showMessageDialog(mainWindow,
                    "No open Host session.",
                    "PactorRATT_Alpha",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int epoch = beginCalling(pgCall);
        final String hostCmd = "PG" + pgCall;
        Thread worker = new Thread(() -> {
            try {
                HostSession.CommandResponse response =
                        session.sendCommand(hostCmd, ARQ_HOST_TIMEOUT_MS);
                if (!response.ok()) {
                    throw new IOException("PG failed, status=0x"
                            + Integer.toHexString(response.statusCode));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("PTConn interrupted for " + pgCall);
                runOnEdt(() -> failCallingIfCurrent(epoch, "Connect interrupted."));
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("PTConn failed for " + pgCall + ": " + msg);
                runOnEdt(() -> failCallingIfCurrent(epoch, "Connect failed: " + msg));
            }
        }, "arq-ptconn");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * Cancel an outbound call (main-window Cancel). Same Host action as ARQ Abort:
     * Listen on → {@code PN}, else {@code Pt}. Then {@code OP} to refresh Mode.
     */
    public void cancelOutboundCall() {
        if (!calling) {
            return;
        }
        stopCallingUi();
        boolean listenOn = mainWindow != null && mainWindow.isListenSelected();
        String mnemonic = listenOn ? "PN" : "Pt";
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            pollOpmodeAfterCalling();
            return;
        }
        Thread worker = new Thread(() -> {
            try {
                sendHostOk(session, mnemonic);
                debugLog.info("Call cancel — sent " + mnemonic);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("Call cancel interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("Call cancel failed: " + msg);
            } finally {
                pollOpmodeAfterCallingOn(session);
            }
        }, "arq-call-cancel");
        worker.setDaemon(true);
        worker.start();
    }

    /** {@code $50} CONNECTED — inbound or outbound; one ARQ window-open path. */
    private void onArqLinkConnected(String peerTitle) {
        pendingArqLinkTimeout = false;
        stopCallingUi();
        if (hasActiveArq()) {
            debugLog.info("CONNECTED ignored — ARQ already active: " + peerTitle);
            return;
        }
        openArqWindowForLink(peerTitle);
        pollOpmodeAfterCalling();
    }

    /**
     * {@code $50 Timeout}. Linked ARQ: remember it; {@code DISCONNECTED:} follows.
     * Calling (no ARQ window): call no-answer — same strip as the 60 s fallback;
     * TNC already stopped, so do not Abort and do not expect {@code DISCONNECTED:}.
     */
    private void onArqLinkTimeout() {
        if (hasActiveArq()) {
            pendingArqLinkTimeout = true;
            debugLog.info("Link Timeout");
            return;
        }
        if (calling) {
            showCallNoAnswer("Call Timeout ($50)");
            return;
        }
        debugLog.info("Timeout ignored — no active ARQ, not calling");
    }

    /**
     * {@code $50 DISCONNECTED: <call>}. Marks the ARQ window dead. If a {@code Timeout}
     * frame preceded it, the notice says Timeout; otherwise a normal disconnect.
     */
    private void onArqLinkDisconnected(String peer) {
        boolean timeout = pendingArqLinkTimeout;
        pendingArqLinkTimeout = false;
        if (calling) {
            stopCallingUi();
        }
        if (!hasActiveArq()) {
            debugLog.info("DISCONNECTED ignored — no active ARQ: " + peer);
            pollOpmodeAfterCalling();
            return;
        }
        ConnectionWindow arq = activeArqWindow;
        String notice = timeout
                ? "ARQ ended — Timeout (" + peer + ")."
                : "ARQ ended — DISCONNECTED: " + peer + ".";
        markArqDead(arq);
        noticeArq(arq, notice);
        debugLog.info(notice);
    }

    private int beginCalling(String call) {
        int epoch = callingEpoch.incrementAndGet();
        calling = true;
        callingCallsign = call;
        if (mainWindow != null) {
            mainWindow.setCallingDisplay(call);
        }
        if (callingTimer == null) {
            callingTimer = new Timer(CALLING_TIMEOUT_MS, e -> onCallingTimeout());
            callingTimer.setRepeats(false);
        }
        callingTimer.restart();
        debugLog.info("Calling " + call);
        return epoch;
    }

    private void stopCallingUi() {
        stopCallingState();
        if (mainWindow != null) {
            mainWindow.setCallingDisplay(null);
        }
    }

    private void stopCallingState() {
        callingEpoch.incrementAndGet();
        calling = false;
        callingCallsign = null;
        if (callingTimer != null) {
            callingTimer.stop();
        }
    }

    /**
     * {@code <call> no answer} on the Calling strip; hide Cancel. Does not Abort.
     * Used for {@code $50 Timeout} while calling and for the 60 s local fallback.
     */
    private void showCallNoAnswer(String reason) {
        if (!calling) {
            return;
        }
        String call = callingCallsign;
        stopCallingState();
        if (mainWindow != null) {
            mainWindow.setCallNoAnswerDisplay(call);
        }
        debugLog.info(reason + " — " + call + " no answer");
        pollOpmodeAfterCalling();
    }

    private void onCallingTimeout() {
        showCallNoAnswer("Calling timeout (60 s)");
    }

    private void failCallingIfCurrent(int epoch, String message) {
        if (callingEpoch.get() != epoch) {
            return;
        }
        stopCallingUi();
        showConnectError(message);
        pollOpmodeAfterCalling();
    }

    private void pollOpmodeAfterCalling() {
        HostSession session = hostSession;
        if (session == null || !session.isOpen()) {
            return;
        }
        pollOpmodeAfterCallingOn(session);
    }

    private void pollOpmodeAfterCallingOn(HostSession session) {
        Thread worker = new Thread(() -> {
            try {
                session.sendCommand("OP", ARQ_HOST_TIMEOUT_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                debugLog.info("Post-call OP interrupted");
            } catch (IOException e) {
                String msg = e.getMessage() == null ? "Host I/O failed" : e.getMessage();
                debugLog.info("Post-call OP failed: " + msg);
            }
        }, "arq-call-op");
        worker.setDaemon(true);
        worker.start();
    }

    /** Opens the ARQ window after {@code $50} CONNECTED (inbound or outbound). */
    private void openArqWindowForLink(String call) {
        if (activeArqWindow != null) {
            return;
        }
        mode = AppMode.ARQ;
        if (listenWindow != null) {
            listenWindow.setSessionActive(false);
            listenWindow.showInactive();
        }
        activeArqWindow = new ConnectionWindow(this, ConnectionWindow.Kind.ARQ, call);
        activeArqWindow.setSessionActive(true);
        activeArqWindow.setVisible(true);
        seedLinkStatus(activeArqWindow);
        if (mainWindow != null) {
            mainWindow.hideCallingDialog();
            mainWindow.refreshModeLabel();
            SwingUtilities.invokeLater(mainWindow::hideCallingDialog);
        }
        debugLog.info("ARQ window opened for " + call);
        syncIrsRoleWatch();
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
        preview.startArqTxPreview();
        preview.showNotice("Offline preview window — not linked.");
        preview.setVisible(true);
        deadArqWindows.add(preview);
    }

    /** Offline UI helper: open a Listen/FEC window for layout testing (no Host I/O). */
    public void openPreviewFecWindow() {
        ConnectionWindow preview = new ConnectionWindow(this, ConnectionWindow.Kind.LISTEN, "PREVIEW");
        preview.setSessionActive(false);
        preview.showNotice("Offline preview — TNC not connected. Layout only.");
        preview.setVisible(true);
        deadArqWindows.add(preview);
    }

    /** Dev Tools: same non-blocking *in testing* notify used on Connect for {@code 93 03 05 E3}. */
    public void previewInTestingNotify() {
        runOnEdt(() -> CompatNotifyDialog.show(mainWindow, CompatNotifyDialog.PREVIEW_STARRED_LABEL));
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
            return;
        }
        deadArqWindows.remove(window);
    }

    public boolean hasActiveArq() {
        return activeArqWindow != null && activeArqWindow.isSessionActive();
    }

    public ConnectionWindow activeArqWindow() {
        return activeArqWindow;
    }

    public void markArqDead(ConnectionWindow window) {
        if (window == activeArqWindow) {
            window.showDead();
            pendingArqLinkTimeout = false;
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
            stopIrsRoleWatch();
            restoreListenAfterArq();
        }
    }

    public void shutdown() {
        rememberOpenConnectionBounds();
        disconnectTnc(true);
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

    /** Live FEC and ARQ win over preview windows that share the same saved slot. */
    private void rememberOpenConnectionBounds() {
        for (ConnectionWindow w : new ArrayList<>(deadArqWindows)) {
            if (w != null && w.isDisplayable()) {
                w.rememberBounds();
            }
        }
        if (activeArqWindow != null && activeArqWindow.isDisplayable()) {
            activeArqWindow.rememberBounds();
        }
        if (listenWindow != null && listenWindow.isDisplayable()) {
            listenWindow.rememberBounds();
        }
    }

    /** Status while a check is busy, or null when idle. */
    private String pdBugProgressStatus() {
        synchronized (pdBugLock) {
            if (!pdBugCheckBusy.get()) {
                return null;
            }
            if (pdBugSawTraffic) {
                return "Traffic";
            }
            if (pdBugArmed) {
                return "Waiting for Traffic";
            }
            return "Sending PD1,3…";
        }
    }

    /** Accept UBIT 10 samples only after the channel-0 data ACK. */
    private void armPdBugWatch() {
        boolean waiting;
        synchronized (pdBugLock) {
            if (!pdBugCheckBusy.get()) {
                return;
            }
            pdBugArmed = true;
            waiting = !pdBugSawTraffic && !pdBugDropped;
        }
        if (!waiting) {
            return;
        }
        runOnEdt(() -> {
            boolean stillWaiting;
            synchronized (pdBugLock) {
                stillWaiting = pdBugCheckBusy.get() && pdBugArmed && !pdBugSawTraffic && !pdBugDropped;
            }
            if (!stillWaiting) {
                return;
            }
            PdBugCheckWindow window = pdBugCheckWindow;
            if (window != null) {
                window.setStatus("Waiting for Traffic");
            }
        });
    }

    /**
     * Traffic ({@code $34}) starts the clock. The next Idle ({@code $33}) stops it.
     * Samples before arm, and Idle before Traffic, are ignored.
     */
    private void notePdBugUbit(int n, long stampNanos) {
        if (n != 0x33 && n != 0x34) {
            return;
        }
        boolean showTraffic = false;
        boolean finished = false;
        boolean dropped = false;
        long dwellMs = 0;
        synchronized (pdBugLock) {
            if (pdBugCheckBusy.get() && pdBugArmed) {
                if (n == 0x34 && !pdBugSawTraffic) {
                    pdBugSawTraffic = true;
                    pdBugTrafficNanos = stampNanos;
                    showTraffic = !pdBugDropped;
                } else if (n == 0x33 && pdBugSawTraffic) {
                    dwellMs = Math.round((stampNanos - pdBugTrafficNanos) / 1_000_000.0);
                    dropped = pdBugDropped;
                    pdBugDropped = false;
                    pdBugArmed = false;
                    pdBugSawTraffic = false;
                    pdBugCheckBusy.set(false);
                    finished = true;
                }
            }
        }
        if (showTraffic) {
            runOnEdt(() -> {
                if (!pdBugCheckBusy.get()) {
                    return;
                }
                PdBugCheckWindow window = pdBugCheckWindow;
                if (window != null) {
                    window.setStatus("Traffic");
                }
            });
        }
        if (finished) {
            long ms = dwellMs;
            boolean wasDropped = dropped;
            runOnEdt(() -> deliverPdBugDwell(ms, wasDropped));
        }
    }

    private void deliverPdBugDwell(long dwellMs, boolean dropped) {
        String verdict = pdBugVerdictText(dwellMs);
        debugLog.info("PD bug check " + verdict + " " + dwellMs + " ms"
                + (dropped ? " (window closed)" : ""));
        PdBugCheckWindow window = pdBugCheckWindow;
        if (window == null) {
            return;
        }
        if (dropped) {
            window.releaseAfterDroppedWatch();
            return;
        }
        window.showVerdict(verdict, pdBugVerdictColor(dwellMs), dwellMs);
    }

    private void failPdBug(String status) {
        boolean ended;
        synchronized (pdBugLock) {
            ended = pdBugCheckBusy.get();
            if (ended) {
                pdBugCheckBusy.set(false);
                pdBugArmed = false;
                pdBugSawTraffic = false;
                pdBugDropped = false;
            }
        }
        if (!ended) {
            return;
        }
        String text = status == null || status.isBlank() ? "Host I/O failed" : status;
        debugLog.info("PD bug check failed: " + text);
        runOnEdt(() -> {
            PdBugCheckWindow window = pdBugCheckWindow;
            if (window != null) {
                window.showFailure(text);
            }
        });
    }

    private static String pdBugVerdictText(long dwellMs) {
        if (dwellMs >= PD_BUG_BUG_MIN_MS && dwellMs <= PD_BUG_BUG_MAX_MS) {
            return "BUG PRESENT";
        }
        if (dwellMs >= PD_BUG_OK_MIN_MS && dwellMs <= PD_BUG_OK_MAX_MS) {
            return "PD OK";
        }
        return "Unexpected Runtime";
    }

    private static Color pdBugVerdictColor(long dwellMs) {
        if (dwellMs >= PD_BUG_BUG_MIN_MS && dwellMs <= PD_BUG_BUG_MAX_MS) {
            return PdBugCheckWindow.bugColor();
        }
        if (dwellMs >= PD_BUG_OK_MIN_MS && dwellMs <= PD_BUG_OK_MAX_MS) {
            return PdBugCheckWindow.okColor();
        }
        return PdBugCheckWindow.unexpectedColor();
    }

    public void runOnEdt(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }
}
