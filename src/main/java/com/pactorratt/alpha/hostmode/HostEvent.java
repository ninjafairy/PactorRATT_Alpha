package com.pactorratt.alpha.hostmode;

/**
 * Minimal Host Mode event for session lifecycle. Pactor data/status demux comes later.
 */
public final class HostEvent {

    public enum Type {
        FRAME,
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

    public static HostEvent frame(HostFrameCodec.Frame frame) {
        return new HostEvent(Type.FRAME, frame, null);
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
