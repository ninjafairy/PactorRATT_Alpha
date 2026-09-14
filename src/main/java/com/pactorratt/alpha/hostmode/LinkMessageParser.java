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
