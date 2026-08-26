package com.pactorratt.alpha.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import com.pactorratt.alpha.config.AppConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * jSerialComm wrapper for raw byte I/O. No Host Mode or protocol knowledge.
 * Native read/write do not take {@code ioLock}, so a hung COM read cannot block Host TX
 * or window-open logic. {@link #isOpen()} is a volatile flag only.
 */
public final class SerialPortService {

    private static final int READ_TIMEOUT_MS = 50;

    private final Object ioLock = new Object();
    private volatile SerialPort port;
    /** Set without waiting on {@link SerialPort#isOpen()} so the EDT never calls into jSerialComm. */
    private volatile boolean opened;
    private final CopyOnWriteArrayList<SerialByteListener> listeners = new CopyOnWriteArrayList<>();

    public SerialPortService() {
    }

    public void addByteListener(SerialByteListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeByteListener(SerialByteListener listener) {
        listeners.remove(listener);
    }

    public void open(AppConfig config) throws IOException {
        synchronized (ioLock) {
            closeLocked();

            String name = config.getComPort() == null ? "" : config.getComPort().trim();
            if (name.isEmpty()) {
                throw new IOException("COM port not configured");
            }
            if (!portExists(name)) {
                throw new IOException("COM port not found: " + name);
            }

            SerialPort candidate = SerialPort.getCommPort(name);
            candidate.setComPortParameters(
                    config.getBaudRate(),
                    config.getDataBits(),
                    mapStopBits(config.getStopBits()),
                    mapParity(config.getParity()));
            candidate.setFlowControl(mapFlowControl(config.getFlowControl()));
            candidate.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, READ_TIMEOUT_MS, 0);

            if (!candidate.openPort()) {
                throw new IOException("Failed to open COM port: " + name);
            }
            port = candidate;
            opened = true;
        }
    }

    public void close() {
        synchronized (ioLock) {
            closeLocked();
        }
    }

    /** Caller must hold {@link #ioLock}. */
    private void closeLocked() {
        opened = false;
        SerialPort p = port;
        port = null;
        if (p != null && p.isOpen()) {
            p.closePort();
        }
    }

    /** Lock-free flag; never calls into jSerialComm. */
    public boolean isOpen() {
        return opened;
    }

    public void write(byte[] data) throws IOException {
        write(data, 0, data.length);
    }

    public void write(byte[] data, int off, int len) throws IOException {
        SerialPort p;
        synchronized (ioLock) {
            p = port;
            if (!opened || p == null) {
                throw new IOException("Serial port is not open");
            }
        }
        int written = p.writeBytes(data, len, off);
        if (written != len) {
            throw new IOException("Short write to " + p.getSystemPortName()
                    + ": wrote " + written + " of " + len + " bytes");
        }
        notifyListeners(true, Arrays.copyOfRange(data, off, off + len));
    }

    public int read(byte[] buf) throws IOException {
        if (buf == null || buf.length == 0) {
            return 0;
        }
        SerialPort p;
        synchronized (ioLock) {
            p = port;
            if (!opened || p == null) {
                return -1;
            }
        }
        byte[] rxCopy = null;
        int n;
        try {
            n = p.readBytes(buf, buf.length);
            if (n < 0) {
                throw new IOException("Read error on " + p.getSystemPortName());
            }
            if (n > 0) {
                rxCopy = Arrays.copyOfRange(buf, 0, n);
            }
        } catch (SerialPortTimeoutException e) {
            return 0;
        }
        if (rxCopy != null) {
            notifyListeners(false, rxCopy);
        }
        return n;
    }

    private void notifyListeners(boolean transmit, byte[] data) {
        if (data.length == 0 || listeners.isEmpty()) {
            return;
        }
        for (SerialByteListener listener : listeners) {
            try {
                listener.onSerialBytes(transmit, data, 0, data.length);
            } catch (RuntimeException ignored) {
                // Listener failures must not kill I/O.
            }
        }
    }

    public String portName() {
        SerialPort p = port;
        return p == null ? "" : p.getSystemPortName();
    }

    private static boolean portExists(String name) {
        return Arrays.stream(SerialPort.getCommPorts())
                .anyMatch(p -> name.equals(p.getSystemPortName()));
    }

    private static int mapParity(String parity) throws IOException {
        return switch (parity == null ? "NONE" : parity) {
            case "NONE" -> SerialPort.NO_PARITY;
            case "EVEN" -> SerialPort.EVEN_PARITY;
            case "ODD" -> SerialPort.ODD_PARITY;
            default -> throw new IOException("Unsupported parity: " + parity);
        };
    }

    private static int mapStopBits(int stopBits) throws IOException {
        return switch (stopBits) {
            case 1 -> SerialPort.ONE_STOP_BIT;
            case 2 -> SerialPort.TWO_STOP_BITS;
            default -> throw new IOException("Unsupported stop bits: " + stopBits);
        };
    }

    private static int mapFlowControl(String flowControl) throws IOException {
        return switch (flowControl == null ? "NONE" : flowControl) {
            case "NONE" -> SerialPort.FLOW_CONTROL_DISABLED;
            case "RTS_CTS" -> SerialPort.FLOW_CONTROL_RTS_ENABLED | SerialPort.FLOW_CONTROL_CTS_ENABLED;
            case "XON_XOFF" -> SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED
                    | SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED;
            default -> throw new IOException("Unsupported flow control: " + flowControl);
        };
    }
}
