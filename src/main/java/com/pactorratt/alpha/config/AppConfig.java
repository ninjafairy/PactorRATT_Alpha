package com.pactorratt.alpha.config;

/**
 * Portable program settings. Defaults match PtRa_specification.
 */
public final class AppConfig {

    public static final String SUPPORT_EMAIL = "KJ7RBS@gmail.com";

    public static final int DEFAULT_BAUD_RATE = 9600;
    public static final int DEFAULT_DATA_BITS = 8;
    public static final int DEFAULT_STOP_BITS = 1;
    public static final String DEFAULT_PARITY = "NONE";

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
    private String cannedHandoverText = "KKK";
    private String cannedDisconnectText = "SK";
    /** Listen CQ button payload (one copy per {@link #cqRepeat} line). */
    private String cannedCqText = "";
    /**
     * How many times the Listen CQ button sends {@link #cannedCqText}. Range 0–10;
     * {@code 0} means the button sends nothing.
     */
    private int cqRepeat = 1;
    private int wrapColumns = 80;

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

    public int getCqRepeat() {
        return cqRepeat;
    }

    /** Clamps to 0–10 (Listen CQ copies; 0 = send nothing). */
    public void setCqRepeat(int cqRepeat) {
        if (cqRepeat < 0) {
            this.cqRepeat = 0;
        } else if (cqRepeat > 10) {
            this.cqRepeat = 10;
        } else {
            this.cqRepeat = cqRepeat;
        }
    }

    /**
     * Listen CQ payload: canned text copied {@link #cqRepeat} times, each copy on its own
     * line. Empty if the canned string is blank or repeat is 0.
     */
    public String cqFecPayload() {
        if (cqRepeat <= 0) {
            return "";
        }
        String canned = cannedCqText == null ? "" : cannedCqText.replace("\r\n", "\n").replace('\r', '\n');
        if (canned.isBlank()) {
            return "";
        }
        String block = canned.endsWith("\n") ? canned : canned + "\n";
        return block.repeat(cqRepeat);
    }

    public int getWrapColumns() {
        return wrapColumns;
    }

    public void setWrapColumns(int wrapColumns) {
        this.wrapColumns = wrapColumns;
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
