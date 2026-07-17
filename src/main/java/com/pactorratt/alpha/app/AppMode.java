package com.pactorratt.alpha.app;

/**
 * Active air / app mode. UI may label {@link #UNPROTO} as "FEC".
 */
public enum AppMode {
    IDLE("Idle"),
    LISTEN("Listen"),
    UNPROTO("FEC"),
    ARQ("ARQ");

    private final String displayName;

    AppMode(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
