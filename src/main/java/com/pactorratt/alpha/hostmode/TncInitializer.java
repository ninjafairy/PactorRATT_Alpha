package com.pactorratt.alpha.hostmode;

import com.pactorratt.alpha.config.AppConfig;
import com.pactorratt.alpha.serial.SerialByteListener;
import com.pactorratt.alpha.util.DebugLog;

import java.io.IOException;
import java.util.Objects;

/**
 * Opens the serial port, enters Host Mode, runs compatibility checks, and applies coded init.
 * Does not show disposition dialogs; {@link com.pactorratt.alpha.app.AppController} presents
 * compat fingerprint info, startup text, and warn/refuse dialogs on the EDT.
 */
public final class TncInitializer {

    private static final long PROBE_TIMEOUT_MS = 1500;
    private static final long COMMAND_TIMEOUT_MS = 2000;
    private static final long ASCII_SETTLE_MS = 300;
    private static final long RESTART_SETTLE_MS = 500;
    private static final long STARTUP_QUIET_MS = 2000;
    private static final long STARTUP_MAX_WAIT_MS = 15000;

    public enum Outcome {
        SUCCESS,
        HARD_REFUSE,
        WARN_NEEDS_CONFIRM,
        FAILED,
        CANCELLED
    }

    public static final class InitResult {
        public final Outcome outcome;
        public final CompatResult compat;
        public final String message;
        public final HostSession session;
        public final String firmwareLabel;

        public InitResult(Outcome outcome, CompatResult compat, String message,
                          HostSession session, String firmwareLabel) {
            this.outcome = outcome;
            this.compat = compat;
            this.message = message == null ? "" : message;
            this.session = session;
            this.firmwareLabel = firmwareLabel;
        }
    }

    private final DebugLog debugLog;
    private final SerialByteListener serialTap;
    private final StartupMessageUi startupUi;
    private final CompatInfoUi compatInfoUi;

    public TncInitializer(DebugLog debugLog, SerialByteListener serialTap,
                          StartupMessageUi startupUi, CompatInfoUi compatInfoUi) {
        this.debugLog = Objects.requireNonNull(debugLog);
        this.serialTap = serialTap;
        this.startupUi = startupUi;
        this.compatInfoUi = compatInfoUi;
    }

    /**
     * Open port, enter Host Mode, and run compatibility check. On warn, returns with session
     * still open; on success runs coded init.
     */
    public InitResult connect(AppConfig config) throws InterruptedException {
        Objects.requireNonNull(config, "config");
        HostSession session = null;
        try {
            session = new HostSession(debugLog);
            if (serialTap != null) {
                session.addSerialByteListener(serialTap);
            }
            session.open(config);
            checkInterrupted();
            runAutobaudKick(session);

            if (!enterHostMode(session)) {
                // Leave session open — AppController may retain it for Debug Monitor.
                return failed(session, "Could not enter Host Mode.");
            }

            CompatResult compat = readCompat(session);
            switch (compat.disposition()) {
                case HARD_REFUSE -> {
                    return new InitResult(
                            Outcome.HARD_REFUSE, compat, compat.message(), session, compat.label());
                }
                case WARN_CONTINUE -> {
                    return new InitResult(
                            Outcome.WARN_NEEDS_CONFIRM,
                            compat,
                            compat.message(),
                            session,
                            compat.label());
                }
                case SUPPORTED -> {
                    runCodedInit(session, config);
                    return new InitResult(
                            Outcome.SUCCESS, compat, compat.message(), session, compat.label());
                }
                default -> throw new IllegalStateException("Unexpected disposition: " + compat.disposition());
            }
        } catch (IOException e) {
            debugLog.info("TNC connect failed: " + e.getMessage());
            // Leave session open if port came up — AppController decides retain vs close.
            return failed(session, e.getMessage());
        } catch (InterruptedException e) {
            // Do not close here; AppController retains for Debug Monitor or aborts on disconnect.
            throw e;
        }
    }

    /**
     * After the user confirms a compatibility warning, run coded init on the existing session.
     */
    public InitResult continueAfterWarn(HostSession session, AppConfig config, CompatResult compat)
            throws InterruptedException {
        Objects.requireNonNull(session, "session");
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(compat, "compat");
        if (!session.isOpen()) {
            return failed(compat, null, "Host session is not open.");
        }
        try {
            runCodedInit(session, config);
            return new InitResult(
                    Outcome.SUCCESS, compat, compat.message(), session, compat.label());
        } catch (IOException e) {
            debugLog.info("TNC init after warn failed: " + e.getMessage());
            return failed(compat, session, e.getMessage());
        } catch (InterruptedException e) {
            throw e;
        }
    }

    /** Close the session if still open (user cancel or error cleanup). */
    public void abort(HostSession session) {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    private boolean enterHostMode(HostSession session) throws IOException, InterruptedException {
        if (session.probeOgg(PROBE_TIMEOUT_MS)) {
            return true;
        }
        if (session.probeOggResync(PROBE_TIMEOUT_MS)) {
            return true;
        }

        session.setState(TncState.COMMAND_MODE);
        sendAsciiWithSettle(session, "AWLEN 8");
        sendAsciiWithSettle(session, "PARITY 0");
        sendAsciiWithSettle(session, "8BITCONV ON");
        sendAsciiWithSettle(session, "RESTART");
        sleepMs(RESTART_SETTLE_MS);
        runAutobaudKick(session);
        sendAsciiWithSettle(session, "HOST ON");
        sleepMs(ASCII_SETTLE_MS);

        if (session.probeOgg(COMMAND_TIMEOUT_MS)) {
            return true;
        }
        return session.probeOggResync(COMMAND_TIMEOUT_MS);
    }

    private CompatResult readCompat(HostSession session) throws IOException, InterruptedException {
        // HPOLL stays at TNC default during compat; AE/MM are solicited host commands, not GG polls.
        // HPN (HPOLL OFF) is applied in runCodedInit after compat succeeds.
        byte[] fingerprint = session.readMemory(0x0006, 4, COMMAND_TIMEOUT_MS);
        CompatResult compat = CompatChecker.evaluate(fingerprint);
        debugLog.info("Compat check: " + compat);
        if (compatInfoUi != null) {
            compatInfoUi.showCompatInfo(compat.compatInfoMessage());
        }
        return compat;
    }

    private void runCodedInit(HostSession session, AppConfig config)
            throws IOException, InterruptedException {
        // Host Mode ON/OFF switches take ASCII Y/N with no space (TRM 4.2.4), possibly "ON"/"OFF" also without space.
        sendRequiredCommand(session, "HPN");
        sendRequiredCommand(session, "EAN");
        sendRequiredCommand(session, "PBY");
        sendRequiredCommand(session, "PH0");
        sendRequiredCommand(session, "WON");

        String callsign = config.getCallsign();
        if (callsign != null && !callsign.isBlank()) {
            sendRequiredCommand(session, "ML" + callsign.trim());
            sendRequiredCommand(session, "Mf" + callsign.trim());
        }

        sendRequiredCommand(session, "AA" + config.getWrapColumns());
        sendRequiredCommand(session, "Pt");
        debugLog.info("Coded TNC init completed");
    }

    private void sendRequiredCommand(HostSession session, String mnemonicAndArgs)
            throws IOException, InterruptedException {
        checkInterrupted();
        HostSession.CommandResponse response = session.sendCommand(mnemonicAndArgs, COMMAND_TIMEOUT_MS);
        if (!response.ok()) {
            throw new IOException("Host command failed: " + mnemonicAndArgs
                    + " (status=0x" + Integer.toHexString(response.statusCode) + ")");
        }
    }

    private void sendAsciiWithSettle(HostSession session, String line)
            throws IOException, InterruptedException {
        checkInterrupted();
        session.sendAsciiLine(line);
        sleepMs(ASCII_SETTLE_MS);
    }

    private void runAutobaudKick(HostSession session) throws IOException, InterruptedException {
        checkInterrupted();
        session.drainInbound();
        session.sendAutobaudAsterisk();
        String startup = session.awaitQuietCapture(STARTUP_QUIET_MS, STARTUP_MAX_WAIT_MS);
        debugLog.info("Autobaud/startup capture (" + startup.length() + " chars)");
        if (startupUi != null && !startup.isBlank()) {
            startupUi.showStartupMessage(startup);
        }
    }

    private static void sleepMs(long ms) throws InterruptedException {
        if (ms > 0) {
            Thread.sleep(ms);
        }
        checkInterrupted();
    }

    private static void checkInterrupted() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException("TNC initialization interrupted");
        }
    }

    private static InitResult failed(HostSession session, String message) {
        return failed(null, session, message);
    }

    private static InitResult failed(CompatResult compat, HostSession session, String message) {
        HostSession open = (session != null && session.isOpen()) ? session : null;
        return new InitResult(Outcome.FAILED, compat, message, open, null);
    }
}
