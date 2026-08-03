package com.pactorratt.alpha.hostmode;

/**
 * Typed Host Mode events after CTL demux.
 */
public final class HostEvent {

    public enum Type {
        /** CTL {@code 0x4F} command response. */
        COMMAND_RESPONSE,
        /** CTL {@code 0x5F} data-ack / status. */
        DATA_ACK_OR_STATUS,
        /** CTL {@code 0x30}–{@code 0x3F} inbound text (Pactor ch0 / monitor). */
        INBOUND_DATA,
        /** CTL {@code 0x2F} echoed data. */
        ECHO,
        /** CTL {@code 0x40}–{@code 0x49} link status. */
        LINK_STATUS,
        /** CTL {@code 0x50}–{@code 0x5E} link messages. */
        LINK_MESSAGE,
        /** Unclassified CTL. */
        UNKNOWN_FRAME,
        DISCONNECTED,
        ERROR
    }

    private final Type type;
    private final HostFrameCodec.Frame frame;
    private final String message;

    private HostEvent(Type type, HostFrameCodec.Frame frame, String message) {
        this.type = type;
        this.frame = frame;
        this.message = message;
    }

    public static HostEvent of(Type type, HostFrameCodec.Frame frame) {
        return new HostEvent(type, frame, null);
    }

    public static HostEvent disconnected(String message) {
        return new HostEvent(Type.DISCONNECTED, null, message);
    }

    public static HostEvent error(String message) {
        return new HostEvent(Type.ERROR, null, message);
    }

    public Type type() {
        return type;
    }

    public HostFrameCodec.Frame frame() {
        return frame;
    }

    public String message() {
        return message;
    }
}
