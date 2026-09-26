package com.pactorratt.alpha.hostmode;

import java.nio.charset.StandardCharsets;

/**
 * Host link messages ({@code CTL $50}–{@code $5E}). Pactor uses channel 0 ({@code $50}).
 * Hardware:
 * <ul>
 *   <li>{@code SOH $50 "CONNECTED to KJ5XF via LONGPATH\\r\\n" ETB}</li>
 *   <li>{@code SOH $50 "DISCONNECTED: KJ5XF\\r\\n" ETB} (clean disconnect)</li>
 *   <li>{@code SOH $50 "Timeout\\r\\n" ETB} then {@code "DISCONNECTED: KA4UPI\\r\\n"} (link timeout)</li>
 *   <li>{@code SOH $50 "Timeout\\r\\n" ETB} alone while calling (no {@code DISCONNECTED:}) — call no-answer</li>
     *   <li>{@code SOH $50 n ETB} — UBIT 10 status-change; {@code n} is OPMODE *w* {@code $30}–{@code $37}
     *       (hardware-proven on Pactor; printed UBIT 10 list omitted it)</li>
 * </ul>
 */
public final class LinkMessageParser {

    private static final String CONNECTED_TO = "CONNECTED to ";
    private static final String DISCONNECTED = "DISCONNECTED: ";
    private static final String TIMEOUT = "Timeout";

    private LinkMessageParser() {
    }

    /**
     * Peer title from a {@code CONNECTED to …} link message, including any trailing
     * {@code via LONGPATH} text. {@code null} if this frame is not that message.
     */
    public static String connectedPeer(HostFrameCodec.Frame frame) {
        return prefixRest(frame, CONNECTED_TO);
    }

    /**
     * Peer callsign from a {@code DISCONNECTED: …} link message.
     * Hardware uses a colon (not {@code DISCONNECTED to}). {@code null} if not that message.
     */
    public static String disconnectedPeer(HostFrameCodec.Frame frame) {
        return prefixRest(frame, DISCONNECTED);
    }

    /**
     * {@code $50} {@code Timeout} — same wire text in two contexts:
     * linked-ARQ timeout (this frame, then {@code DISCONNECTED:}), or call no-answer
     * (this frame alone while Calling, no {@code DISCONNECTED:}).
     */
    public static boolean isTimeout(HostFrameCodec.Frame frame) {
        String text = payloadText(frame);
        return text != null && text.equals(TIMEOUT);
    }

    /**
     * UBIT 10 status-change: payload is a single *w* byte {@code $30}–{@code $37}.
     * Not {@code CONNECTED}/{@code DISCONNECTED}/{@code Timeout} text.
     */
    public static boolean isUbit10StatusChange(HostFrameCodec.Frame frame) {
        return ubit10StatusByte(frame) >= 0;
    }

    /**
     * UBIT 10 *w* byte, or {@code -1} if this frame is not {@code SOH $50 n ETB}
     * with {@code n} in {@code $30}–{@code $37}.
     */
    public static int ubit10StatusByte(HostFrameCodec.Frame frame) {
        if (frame == null || (frame.ctl & 0xFF) != 0x50) {
            return -1;
        }
        byte[] payload = frame.payload;
        if (payload == null || payload.length != 1) {
            return -1;
        }
        int n = payload[0] & 0xFF;
        return n >= 0x30 && n <= 0x37 ? n : -1;
    }

    /**
     * Human-readable CTL {@code $50} payload: UBIT 10 one-byte *w* status, or link text.
     * {@code null} if the frame is not channel-0 {@code $50}.
     */
    public static String describe(HostFrameCodec.Frame frame) {
        if (frame == null || (frame.ctl & 0xFF) != 0x50) {
            return null;
        }
        byte[] payload = frame.payload;
        if (payload == null || payload.length == 0) {
            return "empty $50 payload";
        }
        if (payload.length == 1) {
            int n = payload[0] & 0xFF;
            if (n >= 0x30 && n <= 0x37) {
                return "Status change: " + OpmodeParser.wLabel(n)
                        + String.format(" ($%02X)", n);
            }
        }
        String connected = connectedPeer(frame);
        if (connected != null) {
            return "CONNECTED to " + connected;
        }
        String disconnected = disconnectedPeer(frame);
        if (disconnected != null) {
            return "DISCONNECTED: " + disconnected;
        }
        if (isTimeout(frame)) {
            return "Timeout";
        }
        String text = payloadText(frame);
        if (text != null) {
            return text;
        }
        return "unrecognized $50 payload (" + payload.length + " byte(s))";
    }

    private static String prefixRest(HostFrameCodec.Frame frame, String prefix) {
        String text = payloadText(frame);
        if (text == null || !text.startsWith(prefix)) {
            return null;
        }
        String rest = text.substring(prefix.length()).trim();
        return rest.isEmpty() ? null : rest;
    }

    private static String payloadText(HostFrameCodec.Frame frame) {
        if (frame == null) {
            return null;
        }
        int ctl = frame.ctl & 0xFF;
        if (ctl < 0x50 || ctl > 0x5E) {
            return null;
        }
        byte[] payload = frame.payload;
        if (payload == null || payload.length == 0) {
            return null;
        }
        String text = new String(payload, StandardCharsets.ISO_8859_1)
                .replace("\r\n", "\n")
                .replace('\r', '\n')
                .trim();
        return text.isEmpty() ? null : text;
    }
}
