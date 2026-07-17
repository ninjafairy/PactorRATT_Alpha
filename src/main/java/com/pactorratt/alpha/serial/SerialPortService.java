package com.pactorratt.alpha.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import com.pactorratt.alpha.config.AppConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * jSerialComm wrapper for raw byte I/O. No Host Mode or protocol knowledge.
 */
public final class SerialPortService {

    private static final int READ_TIMEOUT_MS = 50;

    private SerialPort port;
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

    public synchronized void open(AppConfig config) throws IOException {
        close();

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
    }

    public synchronized void close() {
        if (port != null) {
            if (port.isOpen()) {
                port.closePort();
            }
            port = null;
        }
    }

    public synchronized boolean isOpen() {
        return port != null && port.isOpen();
    }

    public void write(byte[] data) throws IOException {
        write(data, 0, data.length);
    }

    public void write(byte[] data, int off, int len) throws IOException {
        byte[] txCopy;
        synchronized (this) {
            requireOpen();
            int written = port.writeBytes(data, len, off);
            if (written != len) {
                throw new IOException("Short write to " + port.getSystemPortName()
                        + ": wrote " + written + " of " + len + " bytes");
            }
            txCopy = Arrays.copyOfRange(data, off, off + len);
        }
        notifyListeners(true, txCopy);
    }

    public int read(byte[] buf) throws IOException {
        if (buf == null || buf.length == 0) {
            return 0;
        }
        byte[] rxCopy = null;
        int n;
        synchronized (this) {
            if (!isOpen()) {
                return -1;
            }
            try {
                n = port.readBytes(buf, buf.length);
                if (n < 0) {
                    throw new IOException("Read error on " + port.getSystemPortName());
                }
                if (n > 0) {
                    rxCopy = Arrays.copyOfRange(buf, 0, n);
                }
            } catch (SerialPortTimeoutException e) {
                return 0;
            }
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

    public synchronized String portName() {
        return port == null ? "" : port.getSystemPortName();
    }

    private void requireOpen() throws IOException {
        if (!isOpen()) {
            throw new IOException("Serial port is not open");
        }
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
