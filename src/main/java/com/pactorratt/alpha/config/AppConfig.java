package com.pactorratt.alpha.config;

/**
 * Portable program settings. Defaults match PtRa_specification.
 */
public final class AppConfig {

    public static final String SUPPORT_EMAIL = "KJ7RBS@gmail.com";

    private String callsign = "";
    private String comPort = "";
    private int baudRate = 1200;
    private int dataBits = 7;
    private int stopBits = 1; // 1 stop bit
    private String parity = "NONE"; // NONE, EVEN, ODD
    private String flowControl = "NONE"; // NONE, RTS_CTS, XON_XOFF

    private CommitMode commitMode = CommitMode.LINE;
    private boolean listenOnStart = false;
    private boolean debugLogEnabled = false;
    private String cannedHandoverText = "KKK";
    private String cannedDisconnectText = "SK";
    private int wrapColumns = 80;
    /** PTSend {@code n}: false → 1 (100 baud), true → 2 (200 baud). */
    private boolean fec200 = false;
    /** PTSend {@code x}: unproto repeats, 1–5. */
    private int fecRetries = 1;

    private boolean buddiesExpanded = true;
    private boolean heardExpanded = true;
    private boolean mentionedExpanded = true;

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

    public boolean isDebugLogEnabled() {
        return debugLogEnabled;
    }

    public void setDebugLogEnabled(boolean debugLogEnabled) {
        this.debugLogEnabled = debugLogEnabled;
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

    public int getWrapColumns() {
        return wrapColumns;
    }

    public void setWrapColumns(int wrapColumns) {
        this.wrapColumns = wrapColumns;
    }

    public boolean isFec200() {
        return fec200;
    }

    public void setFec200(boolean fec200) {
        this.fec200 = fec200;
    }

    public int getFecRetries() {
        return fecRetries;
    }

    /** Clamps to 1–5 (PTSend repeat count). */
    public void setFecRetries(int fecRetries) {
        if (fecRetries < 1) {
            this.fecRetries = 1;
        } else if (fecRetries > 5) {
            this.fecRetries = 5;
        } else {
            this.fecRetries = fecRetries;
        }
    }

    /**
     * Host PTSend command: {@code PD} + {@code n,x} with no space (e.g. {@code PD1,1}, {@code PD2,3}).
     * {@code n} = 2 if {@link #isFec200()}, else 1; {@code x} = {@link #getFecRetries()}.
     */
    public String ptSendHostCommand() {
        int n = fec200 ? 2 : 1;
        return "PD" + n + "," + fecRetries;
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

    public String serialSummary() {
        char parityChar = switch (parity) {
            case "EVEN" -> 'E';
            case "ODD" -> 'O';
            default -> 'N';
        };
        return baudRate + " " + dataBits + parityChar + stopBits;
    }
}
