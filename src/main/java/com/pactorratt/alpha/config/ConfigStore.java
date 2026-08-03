package com.pactorratt.alpha.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal JSON-ish key/value persistence beside the jar (portable root).
 * Avoids adding a JSON library for Phase 1.
 */
public final class ConfigStore {

    private static final String DEFAULT_BUDDIES_JSON = """
            [
              "N0CALL",
              "KJ7RBS"
            ]
            """;

    private final Path settingsFile;
    private final Path buddiesFile;

    public ConfigStore(Path portableRoot) {
        Path configDir = portableRoot.resolve("config");
        this.settingsFile = configDir.resolve("settings.json");
        this.buddiesFile = configDir.resolve("buddies.json");
    }

    public Path settingsFile() {
        return settingsFile;
    }

    public Path buddiesFile() {
        return buddiesFile;
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

    /** Creates {@code config/buddies.json} with defaults if the file is missing. */
    public void ensureBuddiesFile() throws IOException {
        if (Files.isRegularFile(buddiesFile)) {
            return;
        }
        Files.createDirectories(buddiesFile.getParent());
        Files.writeString(buddiesFile, DEFAULT_BUDDIES_JSON, StandardCharsets.UTF_8);
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
        append(sb, "cannedHandoverText", c.getCannedHandoverText(), true);
        append(sb, "cannedDisconnectText", c.getCannedDisconnectText(), true);
        append(sb, "wrapColumns", c.getWrapColumns());
        append(sb, "fec200", c.isFec200());
        append(sb, "fecRetries", c.getFecRetries());
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
            config.setBaudRate(parseInt(map.get("baudRate"), 1200));
        }
        if (map.containsKey("dataBits")) {
            config.setDataBits(parseInt(map.get("dataBits"), 7));
        }
        if (map.containsKey("stopBits")) {
            config.setStopBits(parseInt(map.get("stopBits"), 1));
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
            } catch (IllegalArgumentException ignored) {
                config.setCommitMode(CommitMode.LINE);
            }
        }
        if (map.containsKey("listenOnStart")) {
            config.setListenOnStart(Boolean.parseBoolean(map.get("listenOnStart")));
        }
        if (map.containsKey("debugLogEnabled")) {
            config.setDebugLogEnabled(Boolean.parseBoolean(map.get("debugLogEnabled")));
        }
        if (map.containsKey("cannedHandoverText")) {
            config.setCannedHandoverText(map.get("cannedHandoverText"));
        }
        if (map.containsKey("cannedDisconnectText")) {
            config.setCannedDisconnectText(map.get("cannedDisconnectText"));
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
