package com.pactorratt.alpha.config;

import java.awt.Color;

/**
 * Portable program settings. Defaults match PtRa_specification.
 */
public final class AppConfig {

    public static final String SUPPORT_EMAIL = "KJ7RBS@gmail.com";

    public static final int DEFAULT_BAUD_RATE = 9600;
    public static final int DEFAULT_DATA_BITS = 8;
    public static final int DEFAULT_STOP_BITS = 1;
    public static final String DEFAULT_PARITY = "NONE";

    /** Transcript local outbound. Matches the previous fixed grey. */
    public static final Color DEFAULT_OUTGOING_TEXT = Color.GRAY;
    /** Transcript remote text. Matches the previous fixed black. */
    public static final Color DEFAULT_INCOMING_TEXT = Color.BLACK;
    public static final int DEFAULT_TEXT_SIZE = 13;
    public static final int MIN_TEXT_SIZE = 8;
    public static final int MAX_TEXT_SIZE = 28;
    /** No timestamp on transcript lines. */
    public static final int TIMESTAMP_OFF = 0;
    /** {@code [HH:mm] } at the start of each new transcript line. */
    public static final int TIMESTAMP_HM = 1;
    /** {@code [dd-MM-yy HH:mm] } at the start of each new transcript line. */
    public static final int TIMESTAMP_DATE = 2;

    private String callsign = "";
    private String comPort = "";
    private int baudRate = DEFAULT_BAUD_RATE;
    private int dataBits = DEFAULT_DATA_BITS;
    private int stopBits = DEFAULT_STOP_BITS;
    private String parity = DEFAULT_PARITY; // NONE, EVEN, ODD
    private String flowControl = "NONE"; // NONE, RTS_CTS, XON_XOFF

    private CommitMode commitMode = CommitMode.LINE;
    private boolean listenOnStart = false;
    /** When true, startup's last step is TNC → Connect. */
    private boolean autoConnectTnc = false;
    private boolean debugLogEnabled = false;
    /**
     * When false, skip PK-232 startup-message dialogs and the TNC firmware/hardware
     * ({@code $0009}) info window during connect.
     */
    private boolean displayStartup = false;
    /** When true, a new ARQ link disposes leftover ARQ windows. A live QSO is left up. */
    private boolean onlyOneArqWindow = false;
    /** When true, a new ARQ link disposes the FEC window and turns Listen off. Does not send Pt. */
    private boolean closeFecOnArqLink = false;
    /**
     * When true, a finished FEC connect frame ({@code ?>… <C>}) is removed from the
     * transcript after it is painted. The Connect list still updates.
     */
    private boolean suppressConnectFrames = false;
    /** {@link #TIMESTAMP_OFF}, {@link #TIMESTAMP_HM}, or {@link #TIMESTAMP_DATE}. */
    private int transcriptTimestamp = TIMESTAMP_OFF;
    /**
     * When true, CHO after traffic, Canned CHO, Disc. when finished, and Canned text disc.
     * each send Compose and wait for its Host ack before their own sequence.
     */
    private boolean sendAllOnCho = false;
    private String cannedHandoverText = "KKK";
    private String cannedDisconnectText = "SK";
    /** Listen CQ body. Repeated {@link #cqRepeat} times, then {@link #cannedCqFooter}. */
    private String cannedCqText = "";
    /** Sent once after the repeated CQ block. Sent alone when the canned block is blank. */
    private String cannedCqFooter = "";
    /**
     * How many times the Listen CQ button sends {@link #cannedCqText}. Range 1–10.
     * A stored 0 is clamped to 1. A blank canned block sends the footer once.
     */
    private int cqRepeat = 1;
    /** Preloaded into a new ARQ window's TX buffer when the FEC Away box is checked. */
    private String awayMsg = "";
    private int wrapColumns = 80;
    private Color outgoingText = DEFAULT_OUTGOING_TEXT;
    private Color incomingText = DEFAULT_INCOMING_TEXT;
    /** Monospaced size for transcript, compose, and App TX buffer. */
    private int textSize = DEFAULT_TEXT_SIZE;

    private boolean buddiesExpanded = true;
    private boolean heardExpanded = true;
    private boolean mentionedExpanded = true;

    /** Saved {@code x,y,width,height}, or blank for the platform default. */
    private String windowMain = "";
    private String windowArq = "";
    private String windowFec = "";

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign == null ? "" : callsign.trim().toUpperCase();
    }

    public String getComPort() {
        return comPort;
    }

    public void setComPort(String comPort) {
        this.comPort = comPort == null ? "" : comPort;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity == null ? "NONE" : parity;
    }

    public String getFlowControl() {
        return flowControl;
    }

    public void setFlowControl(String flowControl) {
        this.flowControl = flowControl == null ? "NONE" : flowControl;
    }

    public CommitMode getCommitMode() {
        return commitMode;
    }

    public void setCommitMode(CommitMode commitMode) {
        this.commitMode = commitMode == null ? CommitMode.LINE : commitMode;
    }

    public boolean isListenOnStart() {
        return listenOnStart;
    }

    public void setListenOnStart(boolean listenOnStart) {
        this.listenOnStart = listenOnStart;
    }

    public boolean isAutoConnectTnc() {
        return autoConnectTnc;
    }

    public void setAutoConnectTnc(boolean autoConnectTnc) {
        this.autoConnectTnc = autoConnectTnc;
    }

    public boolean isDebugLogEnabled() {
        return debugLogEnabled;
    }

    public void setDebugLogEnabled(boolean debugLogEnabled) {
        this.debugLogEnabled = debugLogEnabled;
    }

    public boolean isDisplayStartup() {
        return displayStartup;
    }

    public void setDisplayStartup(boolean displayStartup) {
        this.displayStartup = displayStartup;
    }

    public boolean isOnlyOneArqWindow() {
        return onlyOneArqWindow;
    }

    public void setOnlyOneArqWindow(boolean onlyOneArqWindow) {
        this.onlyOneArqWindow = onlyOneArqWindow;
    }

    public boolean isCloseFecOnArqLink() {
        return closeFecOnArqLink;
    }

    public void setCloseFecOnArqLink(boolean closeFecOnArqLink) {
        this.closeFecOnArqLink = closeFecOnArqLink;
    }

    public boolean isSuppressConnectFrames() {
        return suppressConnectFrames;
    }

    public void setSuppressConnectFrames(boolean suppressConnectFrames) {
        this.suppressConnectFrames = suppressConnectFrames;
    }

    public int getTranscriptTimestamp() {
        return transcriptTimestamp;
    }

    /** Values other than {@link #TIMESTAMP_HM} and {@link #TIMESTAMP_DATE} store {@link #TIMESTAMP_OFF}. */
    public void setTranscriptTimestamp(int transcriptTimestamp) {
        if (transcriptTimestamp == TIMESTAMP_HM || transcriptTimestamp == TIMESTAMP_DATE) {
            this.transcriptTimestamp = transcriptTimestamp;
        } else {
            this.transcriptTimestamp = TIMESTAMP_OFF;
        }
    }

    public boolean isSendAllOnCho() {
        return sendAllOnCho;
    }

    public void setSendAllOnCho(boolean sendAllOnCho) {
        this.sendAllOnCho = sendAllOnCho;
    }

    public String getCannedHandoverText() {
        return cannedHandoverText;
    }

    public void setCannedHandoverText(String cannedHandoverText) {
        this.cannedHandoverText = cannedHandoverText == null ? "" : cannedHandoverText;
    }

    public String getCannedDisconnectText() {
        return cannedDisconnectText;
    }

    public void setCannedDisconnectText(String cannedDisconnectText) {
        this.cannedDisconnectText = cannedDisconnectText == null ? "" : cannedDisconnectText;
    }

    public String getCannedCqText() {
        return cannedCqText;
    }

    public void setCannedCqText(String cannedCqText) {
        this.cannedCqText = cannedCqText == null ? "" : cannedCqText;
    }

    public String getCannedCqFooter() {
        return cannedCqFooter;
    }

    public void setCannedCqFooter(String cannedCqFooter) {
        this.cannedCqFooter = cannedCqFooter == null ? "" : cannedCqFooter;
    }

    public String getAwayMsg() {
        return awayMsg;
    }

    public void setAwayMsg(String awayMsg) {
        this.awayMsg = awayMsg == null ? "" : awayMsg;
    }

    public int getCqRepeat() {
        return cqRepeat;
    }

    /** Clamps to 1–10. Values below 1, including a stored 0, become 1. */
    public void setCqRepeat(int cqRepeat) {
        if (cqRepeat < 1) {
            this.cqRepeat = 1;
        } else if (cqRepeat > 10) {
            this.cqRepeat = 10;
        } else {
            this.cqRepeat = cqRepeat;
        }
    }

    /**
     * Listen CQ payload from the saved canned text, footer, and repeat.
     * Empty when both strings are blank.
     */
    public String cqFecPayload() {
        return cqPayload(cannedCqText, cannedCqFooter, cqRepeat);
    }

    /**
     * Canned block repeated {@code repeat} times, then the footer once.
     * A newline is added between copies only when the block does not already end with one.
     * A blank canned block sends the footer once. Both blank sends nothing.
     */
    public static String cqPayload(String cannedText, String footerText, int repeat) {
        int copies = repeat < 1 ? 1 : Math.min(repeat, 10);
        String canned = normalizeNewlines(cannedText);
        String footer = normalizeNewlines(footerText);
        if (canned.isBlank() && footer.isBlank()) {
            return "";
        }
        if (canned.isBlank()) {
            return footer.endsWith("\n") ? footer : footer + "\n";
        }
        String block = canned.endsWith("\n") ? canned : canned + "\n";
        String repeated = block.repeat(copies);
        if (footer.isBlank()) {
            return repeated;
        }
        return footer.endsWith("\n") ? repeated + footer : repeated + footer + "\n";
    }

    private static String normalizeNewlines(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.replace("\r\n", "\n").replace('\r', '\n');
    }

    public int getWrapColumns() {
        return wrapColumns;
    }

    public void setWrapColumns(int wrapColumns) {
        this.wrapColumns = wrapColumns;
    }

    public Color getOutgoingText() {
        return outgoingText;
    }

    public void setOutgoingText(Color outgoingText) {
        this.outgoingText = outgoingText == null ? DEFAULT_OUTGOING_TEXT : outgoingText;
    }

    public Color getIncomingText() {
        return incomingText;
    }

    public void setIncomingText(Color incomingText) {
        this.incomingText = incomingText == null ? DEFAULT_INCOMING_TEXT : incomingText;
    }

    public int getTextSize() {
        return textSize;
    }

    /** Clamps to {@link #MIN_TEXT_SIZE}–{@link #MAX_TEXT_SIZE}. */
    public void setTextSize(int textSize) {
        if (textSize < MIN_TEXT_SIZE) {
            this.textSize = MIN_TEXT_SIZE;
        } else if (textSize > MAX_TEXT_SIZE) {
            this.textSize = MAX_TEXT_SIZE;
        } else {
            this.textSize = textSize;
        }
    }

    /** {@code #RRGGBB}. Falls back when the string is missing or not six hex digits. */
    public static Color parseColor(String hex, Color fallback) {
        if (hex == null) {
            return fallback;
        }
        String s = hex.trim();
        if (s.startsWith("#")) {
            s = s.substring(1);
        }
        if (s.length() != 6) {
            return fallback;
        }
        try {
            int rgb = Integer.parseInt(s, 16);
            return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static String formatColor(Color color) {
        Color c = color == null ? Color.BLACK : color;
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }

    public boolean isBuddiesExpanded() {
        return buddiesExpanded;
    }

    public void setBuddiesExpanded(boolean buddiesExpanded) {
        this.buddiesExpanded = buddiesExpanded;
    }

    public boolean isHeardExpanded() {
        return heardExpanded;
    }

    public void setHeardExpanded(boolean heardExpanded) {
        this.heardExpanded = heardExpanded;
    }

    public boolean isMentionedExpanded() {
        return mentionedExpanded;
    }

    public void setMentionedExpanded(boolean mentionedExpanded) {
        this.mentionedExpanded = mentionedExpanded;
    }

    public String getWindowMain() {
        return windowMain;
    }

    public void setWindowMain(String windowMain) {
        this.windowMain = windowMain == null ? "" : windowMain.trim();
    }

    public String getWindowArq() {
        return windowArq;
    }

    public void setWindowArq(String windowArq) {
        this.windowArq = windowArq == null ? "" : windowArq.trim();
    }

    public String getWindowFec() {
        return windowFec;
    }

    public void setWindowFec(String windowFec) {
        this.windowFec = windowFec == null ? "" : windowFec.trim();
    }

    public String serialSummary() {
        char parityChar = switch (parity) {
            case "EVEN" -> 'E';
            case "ODD" -> 'O';
            default -> 'N';
        };
        return baudRate + " " + dataBits + parityChar + stopBits;
    }
}
