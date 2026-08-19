package com.pactorratt.alpha.ui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Shared hex+ASCII line format for Debug Monitor and Status Monitor. */
final class HostMonitorFormat {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private HostMonitorFormat() {
    }

    static String formatLine(boolean transmit, byte[] data) {
        String time = LocalTime.now().format(TIME_FMT);
        String dir = transmit ? "TX" : "RX";
        StringBuilder hex = new StringBuilder(data.length * 3);
        StringBuilder ascii = new StringBuilder(data.length);
        for (int i = 0; i < data.length; i++) {
            int v = data[i] & 0xFF;
            if (i > 0) {
                hex.append(' ');
            }
            hex.append(String.format("%02X", v));
            if (v >= 0x20 && v <= 0x7E) {
                ascii.append((char) v);
            } else {
                ascii.append('.');
            }
        }
        return time + " " + dir + "  " + hex + "  | " + ascii;
    }
}
