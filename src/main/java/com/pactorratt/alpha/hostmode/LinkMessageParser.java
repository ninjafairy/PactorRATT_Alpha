package com.pactorratt.alpha.hostmode;

import java.nio.charset.StandardCharsets;

/**
 * Host link messages ({@code CTL $50}–{@code $5E}). Pactor uses channel 0 ({@code $50}).
 * Hardware capture: {@code SOH $50 "CONNECTED to KJ5XF via LONGPATH\\r\\n" ETB}.
 */
public final class LinkMessageParser {

    private static final String CONNECTED_TO = "CONNECTED to ";

    private LinkMessageParser() {
    }

    /**
     * Peer title from a {@code CONNECTED to …} link message, including any trailing
     * {@code via LONGPATH} text. {@code null} if this frame is not that message.
     */
    public static String connectedPeer(HostFrameCodec.Frame frame) {
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
        if (!text.startsWith(CONNECTED_TO)) {
            return null;
        }
        String peer = text.substring(CONNECTED_TO.length()).trim();
        return peer.isEmpty() ? null : peer;
    }
}
