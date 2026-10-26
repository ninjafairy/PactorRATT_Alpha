package com.pactorratt.alpha.config;

/**
 * One buddies-file entry. {@link #callsign()} is the identity used by Connect.
 * {@link #listLabel()} is the Stations-tree text.
 */
public final class Buddy {

    public static final int NAME_MAX = 40;
    /** Callsign column in the Stations tree. Names start at the next column. */
    public static final int LABEL_CALL_COLUMNS = 12;

    private final String callsign;
    private final String name;

    public Buddy(String callsign, String name) {
        this.callsign = callsign == null ? "" : callsign;
        this.name = name == null ? "" : name;
    }

    public String callsign() {
        return callsign;
    }

    public String name() {
        return name;
    }

    /**
     * Callsign padded to {@link #LABEL_CALL_COLUMNS}, then the name.
     * Longer callsigns are cut on this label only.
     */
    public String listLabel() {
        String shown = callsign.length() > LABEL_CALL_COLUMNS
                ? callsign.substring(0, LABEL_CALL_COLUMNS)
                : callsign;
        if (name.isEmpty()) {
            return shown;
        }
        return String.format("%-" + LABEL_CALL_COLUMNS + "s", shown) + name;
    }

    /** Full callsign and name for the hover tip. */
    public String tooltip() {
        if (name.isEmpty()) {
            return callsign;
        }
        return callsign + " " + name;
    }

    /** Null when {@code raw} can be stored. Blank is allowed. */
    public static String nameError(String raw) {
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        if (raw.indexOf('|') >= 0) {
            return "Name cannot contain |.";
        }
        if (raw.indexOf('\n') >= 0 || raw.indexOf('\r') >= 0) {
            return "Name cannot contain a line break.";
        }
        if (raw.trim().length() > NAME_MAX) {
            return "Name is limited to 40 characters.";
        }
        return null;
    }

    @Override
    public String toString() {
        return callsign;
    }
}
