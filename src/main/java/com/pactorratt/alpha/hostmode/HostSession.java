package com.pactorratt.alpha.hostmode;

import com.pactorratt.alpha.config.AppConfig;
import com.pactorratt.alpha.serial.SerialByteListener;
import com.pactorratt.alpha.serial.SerialPortService;
import com.pactorratt.alpha.util.DebugLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Owns the serial port, Host frame reader thread, and command round-trips.
 */
public final class HostSession implements AutoCloseable {

    /**
     * Host command response. For most commands, {@link #statusCode} is the binary status byte
     * at payload[2] (0x00 = OK). MEMORY read ({@code MM}, no args) uses a different format;
     * see {@link HostSession#readMemoryByte}.
     */
    public static final class CommandResponse {
        public final byte mnemonic0;
        public final byte mnemonic1;
        public final int statusCode;
        public final byte[] data;
        public final HostFrameCodec.Frame frame;

        public CommandResponse(byte mnemonic0, byte mnemonic1, int statusCode, byte[] data,
                               HostFrameCodec.Frame frame) {
            this.mnemonic0 = mnemonic0;
            this.mnemonic1 = mnemonic1;
            this.statusCode = statusCode;
            this.data = data == null ? new byte[0] : data.clone();
            this.frame = frame;
        }

        public boolean ok() {
            return statusCode == 0x00;
        }
    }

    private final SerialPortService serial = new SerialPortService();
    private final DebugLog debugLog;
    private final HostFrameCodec.FrameParser parser = new HostFrameCodec.FrameParser();
    /** Waiter queue for CTL {@code 0x4F} command responses (incl. OGG / MM). */
    private final LinkedBlockingQueue<HostFrameCodec.Frame> commandQueue = new LinkedBlockingQueue<>();
    /** Waiter queue for CTL {@code 0x5F} data-ack / status. */
    private final LinkedBlockingQueue<HostFrameCodec.Frame> statusQueue = new LinkedBlockingQueue<>();
    private final List<Consumer<HostEvent>> listeners = new CopyOnWriteArrayList<>();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Object ioLock = new Object();
    /**
     * Single Host round-trip lock (Ch. 4 §4.3 / §4.4): only one command or data wait
     * at a time. Prevents concurrent {@code sendCommand}/{@code sendData} from clearing
     * each other's waiter queues or stealing acks.
     */
    private final Object hostIoLock = new Object();

    private Thread readerThread;
    private volatile TncState state = TncState.UNKNOWN;

    public HostSession(DebugLog debugLog) {
        this.debugLog = Objects.requireNonNull(debugLog);
    }

    public TncState state() {
        return state;
    }

    public void setState(TncState state) {
        this.state = state == null ? TncState.UNKNOWN : state;
    }

    public boolean isOpen() {
        return serial.isOpen();
    }

    public void addListener(Consumer<HostEvent> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(Consumer<HostEvent> listener) {
        listeners.remove(listener);
    }

    public void addSerialByteListener(SerialByteListener listener) {
        serial.addByteListener(listener);
    }

    public void removeSerialByteListener(SerialByteListener listener) {
        serial.removeByteListener(listener);
    }

    public synchronized void open(AppConfig config) throws IOException {
        close();
        serial.open(config);
        parser.reset();
        drainWaiterQueues();
        state = TncState.UNKNOWN;
        running.set(true);
        readerThread = new Thread(this::readerLoop, "hostmode-reader");
        readerThread.setDaemon(true);
        readerThread.start();
        debugLog.info("HostSession opened on " + serial.portName() + " (" + config.serialSummary() + ")");
    }

    @Override
    public synchronized void close() {
        running.set(false);
        serial.close();
        Thread t = readerThread;
        if (t != null) {
            try {
                t.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            readerThread = null;
        }
        parser.reset();
        drainWaiterQueues();
        state = TncState.UNKNOWN;
        fire(HostEvent.disconnected("Host session closed"));
        debugLog.info("HostSession closed");
    }

    /** Drain command/status waiter queues only (does not drop UI-bound demux events). */
    public void drainInbound() {
        drainWaiterQueues();
    }

    private void drainWaiterQueues() {
        commandQueue.clear();
        statusQueue.clear();
    }

    /**
     * Collect RX bytes until {@code quietMs} elapses with no further RX, or {@code maxWaitMs} total.
     * Uses a temporary SerialByteListener (RX only). Does not send anything.
     * After return, drainInbound() and reset the frame parser so Host framing starts clean.
     *
     * @return captured bytes decoded as ISO-8859-1 / US-ASCII-friendly string (keep CR/LF)
     */
    public String awaitQuietCapture(long quietMs, long maxWaitMs) throws InterruptedException {
        ByteArrayOutputStream capture = new ByteArrayOutputStream();
        final boolean[] receivedAny = {false};
        final long[] lastRxNanos = {System.nanoTime()};
        long startNanos = lastRxNanos[0];

        SerialByteListener listener = (tx, data, off, len) -> {
            if (tx || len <= 0) {
                return;
            }
            synchronized (capture) {
                capture.write(data, off, len);
                receivedAny[0] = true;
                lastRxNanos[0] = System.nanoTime();
            }
        };

        addSerialByteListener(listener);
        try {
            long quietNanos = TimeUnit.MILLISECONDS.toNanos(quietMs);
            long maxNanos = TimeUnit.MILLISECONDS.toNanos(maxWaitMs);
            while (true) {
                if (Thread.interrupted()) {
                    throw new InterruptedException("Quiet capture interrupted");
                }
                Thread.sleep(50);
                long now = System.nanoTime();
                if (now - startNanos >= maxNanos) {
                    break;
                }
                if (receivedAny[0] && (now - lastRxNanos[0]) >= quietNanos) {
                    break;
                }
                if (!receivedAny[0] && (now - startNanos) >= quietNanos) {
                    break;
                }
            }
        } finally {
            removeSerialByteListener(listener);
            parser.reset();
            drainInbound();
        }

        String result = capture.toString(StandardCharsets.ISO_8859_1);
        int end = result.length();
        while (end > 0 && Character.isWhitespace(result.charAt(end - 1))) {
            end--;
        }
        result = result.substring(0, end);
        debugLog.info("Quiet capture: " + result.length() + " chars");
        return result;
    }

    /** Send autobaud kick '*' (0x2A) with no CR. */
    public void sendAutobaudAsterisk() throws IOException {
        synchronized (ioLock) {
            debugLog.host("TX", "AUTOBAUD * | 2A");
            serial.write(new byte[] {0x2A});
        }
    }

    public void sendRaw(byte[] data) throws IOException {
        Objects.requireNonNull(data, "data");
        synchronized (ioLock) {
            serial.write(data);
        }
    }

    /**
     * Send raw ASCII text (command-mode entry). Appends CR if missing.
     */
    public void sendAsciiLine(String line) throws IOException {
        String text = line == null ? "" : line;
        if (!text.endsWith("\r")) {
            text = text + "\r";
        }
        byte[] bytes = text.getBytes(StandardCharsets.US_ASCII);
        synchronized (ioLock) {
            debugLog.host("TX", "ASCII " + text.replace("\r", "\\r") + " | " + HostFrameCodec.toHex(bytes));
            serial.write(bytes);
        }
    }

    public boolean probeOgg(long timeoutMs) throws IOException, InterruptedException {
        return probeOgg(false, timeoutMs);
    }

    public boolean probeOggResync(long timeoutMs) throws IOException, InterruptedException {
        return probeOgg(true, timeoutMs);
    }

    private boolean probeOgg(boolean resync, long timeoutMs) throws IOException, InterruptedException {
        synchronized (hostIoLock) {
            drainWaiterQueues();
            byte[] tx = resync ? HostFrameCodec.encodeOggResync() : HostFrameCodec.encodeOggProbe();
            synchronized (ioLock) {
                debugLog.host("TX", (resync ? "OGG-resync " : "OGG ") + HostFrameCodec.toHex(tx));
                serial.write(tx);
            }
            long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs);
            while (System.nanoTime() < deadline) {
                if (Thread.interrupted()) {
                    throw new InterruptedException("OGG probe interrupted");
                }
                long remaining = TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime());
                if (remaining <= 0) {
                    break;
                }
                HostFrameCodec.Frame frame = commandQueue.poll(Math.min(remaining, 100), TimeUnit.MILLISECONDS);
                if (frame == null) {
                    continue;
                }
                logRx(frame);
                if (HostFrameCodec.isOggSuccess(frame)) {
                    state = TncState.HOST_MODE;
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Send a global Host command and wait for a matching 0x4F response.
     *
     * @param mnemonicAndArgs e.g. {@code "HP OFF"}, {@code "GG"}, {@code "AE6"}
     * @see #readMemoryByte for MEMORY read ({@code MM} with no args)
     */
    public CommandResponse sendCommand(String mnemonicAndArgs, long timeoutMs)
            throws IOException, InterruptedException {
        Objects.requireNonNull(mnemonicAndArgs, "mnemonicAndArgs");
        if (mnemonicAndArgs.length() < 2) {
            throw new IllegalArgumentException("Host mnemonic must be at least 2 characters");
        }
        byte expect0 = (byte) mnemonicAndArgs.charAt(0);
        byte expect1 = (byte) mnemonicAndArgs.charAt(1);

        synchronized (hostIoLock) {
            drainWaiterQueues();
            byte[] tx = HostFrameCodec.encodeGlobalCommand(mnemonicAndArgs);
            synchronized (ioLock) {
                debugLog.host("TX", "CMD " + mnemonicAndArgs + " | " + HostFrameCodec.toHex(tx));
                serial.write(tx);
            }

            long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs);
            while (System.nanoTime() < deadline) {
                if (Thread.interrupted()) {
                    throw new InterruptedException("Host command interrupted: " + mnemonicAndArgs);
                }
                long remaining = TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime());
                if (remaining <= 0) {
                    break;
                }
                HostFrameCodec.Frame frame = commandQueue.poll(Math.min(remaining, 100), TimeUnit.MILLISECONDS);
                if (frame == null) {
                    continue;
                }
                logRx(frame);
                if (frame.ctl != HostFrameCodec.CTL_GLOBAL || frame.payload.length < 3) {
                    continue;
                }
                if (frame.payload[0] != expect0 || frame.payload[1] != expect1) {
                    continue;
                }
                int status = frame.payload[2] & 0xFF;
                byte[] data = new byte[frame.payload.length - 3];
                if (data.length > 0) {
                    System.arraycopy(frame.payload, 3, data, 0, data.length);
                }
                return new CommandResponse(expect0, expect1, status, data, frame);
            }
            // Drop a late response so the next round-trip cannot treat it as its own.
            commandQueue.clear();
            throw new IOException("Timeout waiting for Host response to: " + mnemonicAndArgs);
        }
    }

    /**
     * Send a global Host command without waiting for a response.
     * Intended for debug-monitor manual sends (probe-only exception to Ch. 4 §4.3 wait).
     * Still takes {@link #hostIoLock} for the write so it cannot run mid round-trip.
     */
    public void sendHostCommandFireAndForget(String mnemonicAndArgs) throws IOException {
        Objects.requireNonNull(mnemonicAndArgs, "mnemonicAndArgs");
        if (mnemonicAndArgs.length() < 2) {
            throw new IllegalArgumentException("Host mnemonic must be at least 2 characters");
        }
        byte[] tx = HostFrameCodec.encodeGlobalCommand(mnemonicAndArgs);
        synchronized (hostIoLock) {
            synchronized (ioLock) {
                debugLog.host("TX", "CMD " + mnemonicAndArgs + " | " + HostFrameCodec.toHex(tx));
                serial.write(tx);
            }
        }
    }

    /**
     * Send Host data to channel {@code channel} and wait for a data-ack ({@code CTL $5F … $00})
     * after each framed block. With HPOLL OFF the ack is pushed; do not send further data until
     * this returns.
     * <p>
     * Serialized with {@link #sendCommand} on {@link #hostIoLock} (Ch. 4 §4.3 / §4.4).
     * Ch. 4 §4.8: payloads larger than {@link HostFrameCodec#MAX_HOST_TO_TNC_PAYLOAD} (330)
     * payload characters are split into multiple blocks. The limit counts pre-escape payload
     * bytes only (SOH/CTL/DLE/ETB excluded). Empty payload is a no-op.
     */
    public void sendData(int channel, byte[] payload, long timeoutMs)
            throws IOException, InterruptedException {
        Objects.requireNonNull(payload, "payload");
        if (channel < 0 || channel > 9) {
            throw new IllegalArgumentException("Host data channel must be 0-9, got " + channel);
        }
        if (payload.length == 0) {
            return;
        }

        synchronized (hostIoLock) {
            int offset = 0;
            int chunkIndex = 0;
            boolean multi = payload.length > HostFrameCodec.MAX_HOST_TO_TNC_PAYLOAD;
            while (offset < payload.length) {
                int len = Math.min(HostFrameCodec.MAX_HOST_TO_TNC_PAYLOAD, payload.length - offset);
                byte[] chunk = Arrays.copyOfRange(payload, offset, offset + len);
                sendDataBlockLocked(channel, chunk, timeoutMs, chunkIndex, multi);
                offset += len;
                chunkIndex++;
            }
        }
    }

    /** Caller must hold {@link #hostIoLock}. */
    private void sendDataBlockLocked(int channel, byte[] chunk, long timeoutMs, int chunkIndex, boolean multi)
            throws IOException, InterruptedException {
        drainWaiterQueues();
        byte[] tx = HostFrameCodec.encodeData(channel, chunk);
        synchronized (ioLock) {
            String label = multi
                    ? "DATA ch" + channel + " chunk=" + chunkIndex + " len=" + chunk.length
                    : "DATA ch" + channel + " len=" + chunk.length;
            debugLog.host("TX", label + " | " + HostFrameCodec.toHex(tx));
            serial.write(tx);
        }

        long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs);
        while (System.nanoTime() < deadline) {
            if (Thread.interrupted()) {
                throw new InterruptedException("Host data send interrupted (ch" + channel + ")");
            }
            long remaining = TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime());
            if (remaining <= 0) {
                break;
            }
            HostFrameCodec.Frame frame = statusQueue.poll(Math.min(remaining, 100), TimeUnit.MILLISECONDS);
            if (frame == null) {
                continue;
            }
            logRx(frame);
            if (HostFrameCodec.isDataAck(frame)) {
                return;
            }
            if (HostFrameCodec.isDataStatusError(frame)) {
                int last = frame.payload[frame.payload.length - 1] & 0xFF;
                throw new IOException("Host data status error (ch" + channel
                        + (multi ? ", chunk=" + chunkIndex : "")
                        + "): $5F … $" + String.format("%02X", last));
            }
        }
        // Drop a late data-ack so the next round-trip cannot treat it as its own.
        statusQueue.clear();
        throw new IOException("Timeout waiting for Host data-ack (ch" + channel
                + (multi ? ", chunk=" + chunkIndex : "") + ")");
    }

    /**
     * Read {@code count} bytes starting at ROM address via AE + MM (auto-increment).
     * HPOLL may still be ON during early reads (e.g. compat gate); AE/MM are solicited host
     * commands with responses, not async GG polls. HP OFF is applied later in coded init.
     */
    public byte[] readMemory(int address, int count, long timeoutMs)
            throws IOException, InterruptedException {
        if (count <= 0) {
            return new byte[0];
        }
        // Host AE takes a decimal integer address no space between (e.g. $0006 → "AE6"), not hex digits.
        String addrCmd = "AE" + (address & 0xFFFF);
        CommandResponse ae = sendCommand(addrCmd, timeoutMs);
        if (!ae.ok()) {
            throw new IOException("ADDRESS failed, status=0x" + Integer.toHexString(ae.statusCode));
        }
        byte[] out = new byte[count];
        for (int i = 0; i < count; i++) {
            out[i] = (byte) readMemoryByte(timeoutMs);
        }
        return out;
    }

    /**
     * MEMORY read (Host MM, no args). Response payload is: 'M''M' '$' &lt;hex digits...&gt;
     * Example: MM$93 → value byte 0x93. Ingest the hex digits after '$'.
     */
    public int readMemoryByte(long timeoutMs) throws IOException, InterruptedException {
        synchronized (hostIoLock) {
            drainWaiterQueues();
            byte[] tx = HostFrameCodec.encodeGlobalCommand("MM");
            synchronized (ioLock) {
                debugLog.host("TX", "CMD MM | " + HostFrameCodec.toHex(tx));
                serial.write(tx);
            }

            long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs);
            while (System.nanoTime() < deadline) {
                if (Thread.interrupted()) {
                    throw new InterruptedException("Host MM read interrupted");
                }
                long remaining = TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime());
                if (remaining <= 0) {
                    break;
                }
                HostFrameCodec.Frame frame = commandQueue.poll(Math.min(remaining, 100), TimeUnit.MILLISECONDS);
                if (frame == null) {
                    continue;
                }
                logRx(frame);
                if (frame.ctl != HostFrameCodec.CTL_GLOBAL || frame.payload.length < 3) {
                    continue;
                }
                if (frame.payload[0] != 'M' || frame.payload[1] != 'M') {
                    continue;
                }
                if (frame.payload[2] == '$') {
                    StringBuilder hex = new StringBuilder();
                    for (int i = 3; i < frame.payload.length; i++) {
                        hex.append((char) (frame.payload[i] & 0xFF));
                    }
                    String hexStr = hex.toString().trim();
                    if (hexStr.isEmpty()) {
                        throw new IOException("MEMORY read returned no hex digits after '$': "
                                + HostFrameCodec.toHex(frame.payload));
                    }
                    int len = Math.min(hexStr.length(), 2);
                    String byteHex = hexStr.substring(0, len);
                    if (byteHex.length() == 1) {
                        byteHex = "0" + byteHex;
                    }
                    try {
                        return Integer.parseInt(byteHex, 16) & 0xFF;
                    } catch (NumberFormatException e) {
                        throw new IOException("MEMORY read invalid hex after '$': " + hexStr
                                + " (payload=" + HostFrameCodec.toHex(frame.payload) + ")");
                    }
                }
                if (frame.payload[2] == 0x00) {
                    if (frame.payload.length < 4) {
                        throw new IOException("MEMORY read legacy form missing data byte (payload="
                                + HostFrameCodec.toHex(frame.payload) + ")");
                    }
                    return frame.payload[3] & 0xFF;
                }
                throw new IOException("MEMORY read unexpected response (payload="
                        + HostFrameCodec.toHex(frame.payload) + ")");
            }
            commandQueue.clear();
            throw new IOException("Timeout waiting for Host MM response");
        }
    }

    private void readerLoop() {
        byte[] buf = new byte[256];
        try {
            while (running.get()) {
                if (!serial.isOpen()) {
                    break;
                }
                int n = serial.read(buf);
                if (n < 0) {
                    break;
                }
                if (n == 0) {
                    continue;
                }
                List<HostFrameCodec.Frame> frames = parser.feed(buf, 0, n);
                for (HostFrameCodec.Frame frame : frames) {
                    dispatchFrame(frame);
                }
            }
        } catch (IOException e) {
            if (running.get()) {
                debugLog.host("ERR", "Reader I/O: " + e.getMessage());
                fire(HostEvent.error(e.getMessage()));
            }
        } finally {
            running.set(false);
        }
    }

    private void dispatchFrame(HostFrameCodec.Frame frame) {
        HostEvent.Type type = HostFrameCodec.classifyCtl(frame.ctl);
        switch (type) {
            case COMMAND_RESPONSE -> commandQueue.offer(frame);
            case DATA_ACK_OR_STATUS -> statusQueue.offer(frame);
            default -> {
            }
        }
        fire(HostEvent.of(type, frame));
    }

    private void logRx(HostFrameCodec.Frame frame) {
        debugLog.host("RX", "CTL=0x" + String.format("%02X", frame.ctl)
                + " payload=" + HostFrameCodec.toHex(frame.payload)
                + " raw=" + HostFrameCodec.toHex(frame.raw));
    }

    private void fire(HostEvent event) {
        for (Consumer<HostEvent> listener : listeners) {
            try {
                listener.accept(event);
            } catch (RuntimeException ignored) {
                // Listener failures must not kill the session.
            }
        }
    }

    /** Test helper: enqueue a frame as if received (unused in production). */
    void injectFrameForTest(HostFrameCodec.Frame frame) {
        dispatchFrame(frame);
    }

    List<HostFrameCodec.Frame> snapshotCommandQueueForTest() {
        return new ArrayList<>(commandQueue);
    }
}
