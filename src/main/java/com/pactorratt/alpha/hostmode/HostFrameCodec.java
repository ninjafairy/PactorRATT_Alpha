package com.pactorratt.alpha.hostmode;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * PK-232 Host Mode block framing: SOH, CTL, DLE-escaped payload, ETB.
 */
public final class HostFrameCodec {

    public static final byte SOH = 0x01;
    public static final byte DLE = 0x10;
    public static final byte ETB = 0x17;
    public static final int CTL_GLOBAL = 0x4F;
    /** Host → PK-232 data to channel 0 ({@code $20}). */
    public static final int CTL_DATA_CH0 = 0x20;
    /** PK-232 → Host status / data-ack class ({@code $5F}). */
    public static final int CTL_DATA_ACK = 0x5F;
    /**
     * Ch. 4 §4.8: max payload characters Host→PK-232, not counting SOH, CTL, DLE, or ETB.
     * Count payload bytes <em>before</em> DLE escaping.
     */
    public static final int MAX_HOST_TO_TNC_PAYLOAD = 330;

    private HostFrameCodec() {
    }

    /** Pactor inbound text: any CTL {@code 0x30}–{@code 0x3F} (channel data or monitor). */
    public static boolean isInboundDataCtl(int ctl) {
        int c = ctl & 0xFF;
        return c >= 0x30 && c <= 0x3F;
    }

    public static HostEvent.Type classifyCtl(int ctl) {
        int c = ctl & 0xFF;
        if (c == CTL_GLOBAL) {
            return HostEvent.Type.COMMAND_RESPONSE;
        }
        if (c == CTL_DATA_ACK) {
            return HostEvent.Type.DATA_ACK_OR_STATUS;
        }
        if (isInboundDataCtl(c)) {
            return HostEvent.Type.INBOUND_DATA;
        }
        if (c == 0x2F) {
            return HostEvent.Type.ECHO;
        }
        if (c >= 0x40 && c <= 0x49) {
            return HostEvent.Type.LINK_STATUS;
        }
        if (c >= 0x50 && c <= 0x5E) {
            return HostEvent.Type.LINK_MESSAGE;
        }
        return HostEvent.Type.UNKNOWN_FRAME;
    }

    public static byte[] encodeBlock(int ctl, byte[] payload) {
        byte[] data = payload == null ? new byte[0] : payload;
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length + 4);
        out.write(SOH);
        out.write(ctl & 0xFF);
        for (byte b : data) {
            if (b == SOH || b == DLE || b == ETB) {
                out.write(DLE);
            }
            out.write(b);
        }
        out.write(ETB);
        return out.toByteArray();
    }

    public static byte[] encodeGlobalCommand(String mnemonicAndArgs) {
        String text = mnemonicAndArgs == null ? "" : mnemonicAndArgs;
        return encodeBlock(CTL_GLOBAL, text.getBytes(StandardCharsets.US_ASCII));
    }

    /**
     * Encode Host data to channel {@code channel} (0–9). Non-Packet modes use channel 0.
     */
    public static byte[] encodeData(int channel, byte[] payload) {
        if (channel < 0 || channel > 9) {
            throw new IllegalArgumentException("Host data channel must be 0-9, got " + channel);
        }
        return encodeBlock(0x20 | (channel & 0x0F), payload);
    }

    /** Ch.4 data-ack: {@code SOH $5F X X $00 ETB}. */
    public static boolean isDataAck(Frame frame) {
        if (frame == null || frame.ctl != CTL_DATA_ACK || frame.payload.length != 3) {
            return false;
        }
        return (frame.payload[2] & 0xFF) == 0x00;
    }

    /** Ch.4 bad block ({@code … W}) or bad CTL ({@code … Y}) on {@code $5F}. */
    public static boolean isDataStatusError(Frame frame) {
        if (frame == null || frame.ctl != CTL_DATA_ACK || frame.payload.length < 3) {
            return false;
        }
        int last = frame.payload[frame.payload.length - 1] & 0xFF;
        return last == 'W' || last == 'Y';
    }

    public static byte[] encodeOggProbe() {
        return encodeGlobalCommand("GG");
    }

    public static byte[] encodeOggResync() {
        byte[] block = encodeGlobalCommand("GG");
        byte[] out = new byte[block.length + 1];
        out[0] = SOH;
        System.arraycopy(block, 0, out, 1, block.length);
        return out;
    }

    public static String toHex(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(data.length * 3 - 1);
        for (int i = 0; i < data.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.format("%02X", data[i] & 0xFF));
        }
        return sb.toString();
    }

    public static boolean isOggSuccess(Frame frame) {
        if (frame == null || frame.ctl != CTL_GLOBAL || frame.payload.length < 3) {
            return false;
        }
        return frame.payload[0] == 'G'
                && frame.payload[1] == 'G'
                && frame.payload[2] == 0x00;
    }

    public static final class Frame {
        public final int ctl;
        public final byte[] payload;
        public final byte[] raw;

        public Frame(int ctl, byte[] payload, byte[] raw) {
            this.ctl = ctl & 0xFF;
            this.payload = payload == null ? new byte[0] : payload.clone();
            this.raw = raw == null ? new byte[0] : raw.clone();
        }
    }

    public static final class FrameParser {

        private enum State {
            WAIT_SOH,
            AFTER_SOH,
            READ_PAYLOAD,
            READ_ESCAPE
        }

        private State state = State.WAIT_SOH;
        private int ctl;
        private final ByteArrayOutputStream payload = new ByteArrayOutputStream();
        private final ByteArrayOutputStream raw = new ByteArrayOutputStream();

        public void reset() {
            state = State.WAIT_SOH;
            ctl = 0;
            payload.reset();
            raw.reset();
        }

        /** True when the next byte must be SOH to begin a block (idle / between frames). */
        public boolean awaitingSoh() {
            return state == State.WAIT_SOH;
        }

        public List<Frame> feed(byte[] data, int off, int len) {
            List<Frame> frames = new ArrayList<>();
            if (data == null || len <= 0) {
                return frames;
            }
            int end = off + len;
            for (int i = off; i < end; i++) {
                Frame frame = feed(data[i]);
                if (frame != null) {
                    frames.add(frame);
                }
            }
            return frames;
        }

        public Frame feed(byte b) {
            switch (state) {
                case WAIT_SOH -> {
                    if (b == SOH) {
                        raw.reset();
                        payload.reset();
                        raw.write(b);
                        state = State.AFTER_SOH;
                    }
                }
                case AFTER_SOH -> {
                    if (b == SOH) {
                        raw.write(b);
                    } else {
                        ctl = b & 0xFF;
                        raw.write(b);
                        state = State.READ_PAYLOAD;
                    }
                }
                case READ_PAYLOAD -> {
                    if (b == DLE) {
                        raw.write(b);
                        state = State.READ_ESCAPE;
                    } else if (b == ETB) {
                        raw.write(b);
                        state = State.WAIT_SOH;
                        return new Frame(ctl, payload.toByteArray(), raw.toByteArray());
                    } else {
                        raw.write(b);
                        payload.write(b);
                    }
                }
                case READ_ESCAPE -> {
                    raw.write(b);
                    payload.write(b);
                    state = State.READ_PAYLOAD;
                }
                default -> reset();
            }
            return null;
        }
    }
}
