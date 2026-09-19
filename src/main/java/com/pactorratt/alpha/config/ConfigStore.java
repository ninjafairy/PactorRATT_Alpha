package com.pactorratt.alpha.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Minimal JSON-ish key/value persistence in {@code config/} beside the jar.
 * Avoids adding a JSON library for Phase 1.
 */
public final class ConfigStore {

    private static final String DEFAULT_BUDDIES_JSON = """
            [
              "N0CALL",
              "KJ7RBS"
            ]
            """;

    public static final int MONITOR_LIST_CAP = 12;

    private final Path configDir;
    private final Path settingsFile;
    private final Path buddiesFile;
    private final Path heardFile;
    private final Path mentionedFile;

    public static Path configDir(Path portableRoot) {
        return portableRoot.resolve("config");
    }

    public ConfigStore(Path portableRoot) {
        this.configDir = configDir(portableRoot);
        this.settingsFile = configDir.resolve("settings.json");
        this.buddiesFile = configDir.resolve("buddies.json");
        this.heardFile = configDir.resolve("heard.json");
        this.mentionedFile = configDir.resolve("mentioned.json");
    }

    public Path configDir() {
        return configDir;
    }

    public Path settingsFile() {
        return settingsFile;
    }

    public Path buddiesFile() {
        return buddiesFile;
    }

    public Path heardFile() {
        return heardFile;
    }

    public Path mentionedFile() {
        return mentionedFile;
    }

    /** Missing file → empty list. Most-recent-first. Caps at {@link #MONITOR_LIST_CAP}. */
    public List<String> loadMonitorList(Path file) {
        List<String> calls = new ArrayList<>();
        if (file == null || !Files.isRegularFile(file)) {
            return calls;
        }
        try {
            String text = Files.readString(file, StandardCharsets.UTF_8).trim();
            if (text.startsWith("[")) {
                String body = text.substring(1, text.endsWith("]") ? text.length() - 1 : text.length());
                for (String part : body.split(",")) {
                    String s = part.trim();
                    if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
                        appendMonitorCall(calls, s.substring(1, s.length() - 1));
                    }
                }
            }
        } catch (IOException ignored) {
            return calls;
        }
        return calls;
    }

    public void saveMonitorList(Path file, List<String> calls) throws IOException {
        Files.createDirectories(file.getParent());
        StringBuilder sb = new StringBuilder();
        sb.append("[\r\n");
        List<String> capped = new ArrayList<>();
        if (calls != null) {
            for (String call : calls) {
                appendMonitorCall(capped, call);
            }
        }
        for (int i = 0; i < capped.size(); i++) {
            sb.append("  \"").append(escape(capped.get(i))).append('"');
            if (i + 1 < capped.size()) {
                sb.append(',');
            }
            sb.append("\r\n");
        }
        sb.append("]\r\n");
        Files.writeString(file, sb.toString(), StandardCharsets.UTF_8);
    }

    /** Keeps existing order; skips blanks and duplicates; stops at {@link #MONITOR_LIST_CAP}. */
    private static void appendMonitorCall(List<String> calls, String raw) {
        if (raw == null) {
            return;
        }
        String call = raw.trim().toUpperCase(Locale.ROOT);
        if (call.isEmpty() || calls.contains(call) || calls.size() >= MONITOR_LIST_CAP) {
            return;
        }
        calls.add(call);
    }

    public AppConfig load() {
        AppConfig config = new AppConfig();
        if (!Files.isRegularFile(settingsFile)) {
            return config;
        }
        try {
            String text = Files.readString(settingsFile, StandardCharsets.UTF_8);
            Map<String, String> map = parseSimpleJsonObject(text);
            apply(config, map);
        } catch (IOException ignored) {
            // Keep defaults on read failure.
        }
        return config;
    }

    public void save(AppConfig config) throws IOException {
        Files.createDirectories(settingsFile.getParent());
        Files.writeString(settingsFile, toJson(config), StandardCharsets.UTF_8);
    }

    /** Existing {@code buddies.json} order; empty if missing after {@link #ensureBuddiesFile}. */
    public List<String> loadBuddyList() {
        List<String> calls = new ArrayList<>();
        if (!Files.isRegularFile(buddiesFile)) {
            return calls;
        }
        try {
            String text = Files.readString(buddiesFile, StandardCharsets.UTF_8).trim();
            if (text.startsWith("[")) {
                String body = text.substring(1, text.endsWith("]") ? text.length() - 1 : text.length());
                for (String part : body.split(",")) {
                    String s = part.trim();
                    if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
                        appendBuddyCall(calls, s.substring(1, s.length() - 1));
                    }
                }
            } else {
                for (String line : text.split("\\R")) {
                    String s = line.trim();
                    if (!s.isEmpty() && !s.startsWith("#")) {
                        appendBuddyCall(calls, s);
                    }
                }
            }
        } catch (IOException ignored) {
            return calls;
        }
        return calls;
    }

    public void saveBuddyList(List<String> calls) throws IOException {
        Files.createDirectories(buddiesFile.getParent());
        List<String> unique = new ArrayList<>();
        if (calls != null) {
            for (String call : calls) {
                appendBuddyCall(unique, call);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[\r\n");
        for (int i = 0; i < unique.size(); i++) {
            sb.append("  \"").append(escape(unique.get(i))).append('"');
            if (i + 1 < unique.size()) {
                sb.append(',');
            }
            sb.append("\r\n");
        }
        sb.append("]\r\n");
        Files.writeString(buddiesFile, sb.toString(), StandardCharsets.UTF_8);
    }

    private static void appendBuddyCall(List<String> calls, String raw) {
        if (raw == null) {
            return;
        }
        String call = raw.trim().toUpperCase(Locale.ROOT);
        if (call.isEmpty() || calls.contains(call)) {
            return;
        }
        calls.add(call);
    }

    /** Creates {@code config/buddies.json} with defaults if the file is missing (CRLF endings). */
    public void ensureBuddiesFile() throws IOException {
        if (Files.isRegularFile(buddiesFile)) {
            return;
        }
        Files.createDirectories(buddiesFile.getParent());
        String crlf = DEFAULT_BUDDIES_JSON.replace("\r\n", "\n").replace("\n", "\r\n");
        Files.writeString(buddiesFile, crlf, StandardCharsets.UTF_8);
    }

    private static String toJson(AppConfig c) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        append(sb, "callsign", c.getCallsign(), true);
        append(sb, "comPort", c.getComPort(), true);
        append(sb, "baudRate", c.getBaudRate());
        append(sb, "dataBits", c.getDataBits());
        append(sb, "stopBits", c.getStopBits());
        append(sb, "parity", c.getParity(), true);
        append(sb, "flowControl", c.getFlowControl(), true);
        append(sb, "commitMode", c.getCommitMode().name(), true);
        append(sb, "listenOnStart", c.isListenOnStart());
        append(sb, "debugLogEnabled", c.isDebugLogEnabled());
        append(sb, "Display Startup", c.isDisplayStartup());
        append(sb, "cannedHandoverText", c.getCannedHandoverText(), true);
        append(sb, "cannedDisconnectText", c.getCannedDisconnectText(), true);
        append(sb, "cannedCqText", c.getCannedCqText(), true);
        append(sb, "wrapColumns", c.getWrapColumns());
        append(sb, "fec200", c.isFec200());
        append(sb, "fecRetries", c.getFecRetries());
        append(sb, "cqRepeat", c.getCqRepeat());
        append(sb, "buddiesExpanded", c.isBuddiesExpanded());
        append(sb, "heardExpanded", c.isHeardExpanded());
        append(sb, "mentionedExpanded", c.isMentionedExpanded(), false);
        sb.append("}\n");
        return sb.toString();
    }

    private static void append(StringBuilder sb, String key, String value, boolean comma) {
        sb.append("  \"").append(key).append("\": \"").append(escape(value)).append("\"");
        if (comma) {
            sb.append(',');
        }
        sb.append('\n');
    }

    private static void append(StringBuilder sb, String key, int value) {
        sb.append("  \"").append(key).append("\": ").append(value).append(",\n");
    }

    private static void append(StringBuilder sb, String key, boolean value) {
        append(sb, key, value, true);
    }

    private static void append(StringBuilder sb, String key, boolean value, boolean comma) {
        sb.append("  \"").append(key).append("\": ").append(value);
        if (comma) {
            sb.append(',');
        }
        sb.append('\n');
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static void apply(AppConfig config, Map<String, String> map) {
        if (map.containsKey("callsign")) {
            config.setCallsign(map.get("callsign"));
        }
        if (map.containsKey("comPort")) {
            config.setComPort(map.get("comPort"));
        }
        if (map.containsKey("baudRate")) {
            config.setBaudRate(parseInt(map.get("baudRate"), AppConfig.DEFAULT_BAUD_RATE));
        }
        if (map.containsKey("dataBits")) {
            config.setDataBits(parseInt(map.get("dataBits"), AppConfig.DEFAULT_DATA_BITS));
        }
        if (map.containsKey("stopBits")) {
            config.setStopBits(parseInt(map.get("stopBits"), AppConfig.DEFAULT_STOP_BITS));
        }
        if (map.containsKey("parity")) {
            config.setParity(map.get("parity"));
        }
        if (map.containsKey("flowControl")) {
            config.setFlowControl(map.get("flowControl"));
        }
        if (map.containsKey("commitMode")) {
            try {
                config.setCommitMode(CommitMode.valueOf(map.get("commitMode")));
            } catch (IllegalArgumentException e) {
                config.setCommitMode(CommitMode.LINE);
            }
        }
        if (map.containsKey("listenOnStart")) {
            config.setListenOnStart(Boolean.parseBoolean(map.get("listenOnStart")));
        }
        if (map.containsKey("debugLogEnabled")) {
            config.setDebugLogEnabled(Boolean.parseBoolean(map.get("debugLogEnabled")));
        }
        if (map.containsKey("Display Startup")) {
            config.setDisplayStartup(Boolean.parseBoolean(map.get("Display Startup")));
        } else if (map.containsKey("displayStartup")) {
            config.setDisplayStartup(Boolean.parseBoolean(map.get("displayStartup")));
        }
        if (map.containsKey("cannedHandoverText")) {
            config.setCannedHandoverText(map.get("cannedHandoverText"));
        }
        if (map.containsKey("cannedDisconnectText")) {
            config.setCannedDisconnectText(map.get("cannedDisconnectText"));
        }
        if (map.containsKey("cannedCqText")) {
            config.setCannedCqText(map.get("cannedCqText"));
        }
        if (map.containsKey("wrapColumns")) {
            config.setWrapColumns(parseInt(map.get("wrapColumns"), 80));
        }
        if (map.containsKey("fec200")) {
            config.setFec200(Boolean.parseBoolean(map.get("fec200")));
        }
        if (map.containsKey("fecRetries")) {
            config.setFecRetries(parseInt(map.get("fecRetries"), 1));
        }
        if (map.containsKey("cqRepeat")) {
            config.setCqRepeat(parseInt(map.get("cqRepeat"), 1));
        }
        if (map.containsKey("buddiesExpanded")) {
            config.setBuddiesExpanded(Boolean.parseBoolean(map.get("buddiesExpanded")));
        }
        if (map.containsKey("heardExpanded")) {
            config.setHeardExpanded(Boolean.parseBoolean(map.get("heardExpanded")));
        }
        if (map.containsKey("mentionedExpanded")) {
            config.setMentionedExpanded(Boolean.parseBoolean(map.get("mentionedExpanded")));
        }
    }

    private static int parseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    /** Very small subset parser for flat string/number/boolean JSON objects. */
    static Map<String, String> parseSimpleJsonObject(String text) {
        Map<String, String> map = new LinkedHashMap<>();
        String body = text.trim();
        if (body.startsWith("{") && body.endsWith("}")) {
            body = body.substring(1, body.length() - 1);
        }
        // Split on commas not inside quotes
        StringBuilder token = new StringBuilder();
        boolean inQuotes = false;
        boolean escape = false;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (escape) {
                token.append(c);
                escape = false;
                continue;
            }
            if (c == '\\') {
                escape = true;
                continue;
            }
            if (c == '"') {
                inQuotes = !inQuotes;
                token.append(c);
                continue;
            }
            if (c == ',' && !inQuotes) {
                putPair(map, token.toString());
                token.setLength(0);
                continue;
            }
            token.append(c);
        }
        if (!token.isEmpty()) {
            putPair(map, token.toString());
        }
        return map;
    }

    private static void putPair(Map<String, String> map, String pair) {
        String p = pair.trim();
        if (p.isEmpty()) {
            return;
        }
        int colon = p.indexOf(':');
        if (colon < 0) {
            return;
        }
        String key = stripQuotes(p.substring(0, colon).trim());
        String value = stripQuotes(p.substring(colon + 1).trim());
        map.put(key, value);
    }

    private static String stripQuotes(String s) {
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            return s.substring(1, s.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return s;
    }
}
