package com.pactorratt.alpha.hostmode;

import com.pactorratt.alpha.config.AppConfig;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Applies firmware fingerprint policy from {@code docs/Compat_Memory_Map.md} and PtRa §11.
 */
public final class CompatChecker {

    private static final byte[][] HARD_REFUSE = {
            { (byte) 0x86, (byte) 0x09, (byte) 0x15, (byte) 0xC3 },
            { (byte) 0x87, (byte) 0x03, (byte) 0x04, (byte) 0x62 },
            { (byte) 0x87, (byte) 0x06, (byte) 0x25, (byte) 0x62 },
            { (byte) 0x88, (byte) 0x02, (byte) 0x23, (byte) 0x62 },
            { (byte) 0x89, (byte) 0x10, (byte) 0x31, (byte) 0xC2 },
            { (byte) 0x90, (byte) 0x07, (byte) 0x19, (byte) 0xC2 },
            { (byte) 0x91, (byte) 0x08, (byte) 0x01, (byte) 0xC2 },
    };

    private static final Map<String, String> SUPPORTED = linkedMap(
            "93 03 05 C2", "supported v7.0",
            "93 12 01 C2", "supported v7.0a",
            "95 09 13 C2", "supported v7.1",
            "98 08 10 C2", "supported v7.2"
    );

    private static final Map<String, String> WARN_HK = linkedMap(
            "87 06 25 69", "unsupported HK-232",
            "88 02 23 69", "unsupported HK-232",
            "89 10 31 69", "unsupported HK-232"
    );

    private CompatChecker() {
    }

    /**
     * @param fingerprint4 four bytes from {@code $0006..$0009} (hardware byte at index 3)
     */
    public static CompatResult evaluate(byte[] fingerprint4) {
        byte[] fp = normalize(fingerprint4);
        String hex = formatHex(fp);

        for (byte[] refuse : HARD_REFUSE) {
            if (Arrays.equals(fp, refuse)) {
                return new CompatResult(
                        CompatResult.Disposition.HARD_REFUSE,
                        "Unsupported",
                        fp,
                        "This TNC firmware is not supported (fingerprint " + hex + "). "
                                + "PactorRATT Alpha cannot connect to this device.");
            }
        }

        String supportedLabel = SUPPORTED.get(hex);
        if (supportedLabel != null) {
            return new CompatResult(
                    CompatResult.Disposition.SUPPORTED,
                    supportedLabel,
                    fp,
                    "Compatible firmware detected: " + supportedLabel + " (" + hex + ").");
        }

        String hkLabel = WARN_HK.get(hex);
        if (hkLabel != null) {
            return warnContinue(fp, hkLabel, hex);
        }

        byte hardware = fp[3];
        int bits765 = (hardware & 0xE0) >> 5;
        int bits40 = hardware & 0x1F;

        if (bits765 == 0b100) {
            return warnContinue(fp, "unknown UDC-232", hex);
        }
        if (bits40 == 0b01001) {
            return warnContinue(fp, "HK-232", hex);
        }

        // bits765 == 0b011 or bits40 == 0b00010 are PK-232 hints only; still unknown fingerprint.
        return warnContinue(fp, "unknown", hex);
    }

    private static CompatResult warnContinue(byte[] fp, String label, String hex) {
        String detail = label.equals("unknown")
                ? "Unknown TNC fingerprint " + hex + "."
                : label + " (fingerprint " + hex + ").";
        String message = detail
                + " You may continue at your own risk. Please email the fingerprint to "
                + AppConfig.SUPPORT_EMAIL + ".";
        return new CompatResult(CompatResult.Disposition.WARN_CONTINUE, label, fp, message);
    }

    private static String formatHex(byte[] fp) {
        StringBuilder sb = new StringBuilder(fp.length * 3 - 1);
        for (int i = 0; i < fp.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.format("%02X", fp[i] & 0xFF));
        }
        return sb.toString();
    }

    private static byte[] normalize(byte[] fingerprint4) {
        if (fingerprint4 == null || fingerprint4.length != 4) {
            throw new IllegalArgumentException("fingerprint must be exactly 4 bytes");
        }
        return fingerprint4.clone();
    }

    private static Map<String, String> linkedMap(String... keysAndValues) {
        if (keysAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("keysAndValues must be key/value pairs");
        }
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            map.put(keysAndValues[i], keysAndValues[i + 1]);
        }
        return map;
    }
}
