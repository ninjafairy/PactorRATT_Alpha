package com.pactorratt.alpha.hostmode;

import java.util.Arrays;

/**
 * Outcome of a TNC firmware / hardware compatibility check ({@code $0006..$0009}).
 */
public final class CompatResult {

    public enum Disposition {
        SUPPORTED,
        HARD_REFUSE,
        WARN_CONTINUE
    }

    private final Disposition disposition;
    private final String label;
    private final byte[] fingerprint;
    private final String message;

    public CompatResult(Disposition disposition, String label, byte[] fingerprint, String message) {
        this.disposition = disposition;
        this.label = label == null ? "" : label;
        this.fingerprint = fingerprint == null ? new byte[0] : fingerprint.clone();
        this.message = message == null ? "" : message;
    }

    public Disposition disposition() {
        return disposition;
    }

    public String label() {
        return label;
    }

    public byte[] fingerprint() {
        return fingerprint.clone();
    }

    public String message() {
        return message;
    }

    /**
     * Firmware date as {@code YY-MM-DD} with each byte's hex digits read as decimal
     * (e.g. {@code 0x93 0x03 0x05} → {@code "93-03-05"}).
     */
    public String firmwareDateShort() {
        if (fingerprint.length < 3) {
            return "??-??-??";
        }
        return String.format("%02d-%02d-%02d",
                decimalFromDateByte(fingerprint[0]),
                decimalFromDateByte(fingerprint[1]),
                decimalFromDateByte(fingerprint[2]));
    }

    /**
     * Firmware date with century: {@code 19YY} when {@code YY >= 70}, else {@code 20YY}.
     */
    public String firmwareDateLong() {
        if (fingerprint.length < 3) {
            return firmwareDateShort();
        }
        int yy = decimalFromDateByte(fingerprint[0]);
        int century = yy >= 70 ? 1900 : 2000;
        return String.format("%d-%02d-%02d",
                century + yy,
                decimalFromDateByte(fingerprint[1]),
                decimalFromDateByte(fingerprint[2]));
    }

    /** Uppercase hex of hardware byte {@code $0009}. */
    public String hardwareByteHex() {
        if (fingerprint.length < 4) {
            return "??";
        }
        return String.format("%02X", fingerprint[3] & 0xFF);
    }

    /**
     * Hardware byte {@code $0009} as labeled bit lines (bit 7 down to bit 0) plus hex header.
     */
    public String hardwareBitsDisplay() {
        if (fingerprint.length < 4) {
            return "";
        }
        int hw = fingerprint[3] & 0xFF;
        StringBuilder sb = new StringBuilder(96);
        sb.append("Hardware ($0009): ").append(String.format("%02X", hw)).append('\n');
        for (int bit = 7; bit >= 0; bit--) {
            sb.append("Bit").append(bit).append(": ").append((hw >> bit) & 1);
            if (bit > 0) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    /** Full text for the post-read compatibility info popup. */
    public String compatInfoMessage() {
        return "Firmware date: " + firmwareDateShort() + " (" + firmwareDateLong() + ")\n\n"
                + hardwareBitsDisplay();
    }

    /** Space-separated uppercase hex, e.g. {@code "93 03 05 C2"}. */
    public String hexFingerprint() {
        if (fingerprint.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(fingerprint.length * 3 - 1);
        for (int i = 0; i < fingerprint.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.format("%02X", fingerprint[i] & 0xFF));
        }
        return sb.toString();
    }

    /** Hex digits of a date byte interpreted as decimal (e.g. {@code 0x93} → {@code 93}). */
    private static int decimalFromDateByte(byte b) {
        return Integer.parseInt(String.format("%02X", b & 0xFF));
    }

    @Override
    public String toString() {
        return "CompatResult{disposition=" + disposition
                + ", label='" + label + '\''
                + ", fingerprint=" + hexFingerprint()
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompatResult that)) {
            return false;
        }
        return disposition == that.disposition
                && label.equals(that.label)
                && message.equals(that.message)
                && Arrays.equals(fingerprint, that.fingerprint);
    }

    @Override
    public int hashCode() {
        int result = disposition.hashCode();
        result = 31 * result + label.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + Arrays.hashCode(fingerprint);
        return result;
    }
}
