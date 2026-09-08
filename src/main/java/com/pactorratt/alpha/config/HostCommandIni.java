package com.pactorratt.alpha.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Portable {@code config/config.ini}: named Host-command groups. Only {@code [INIT]} is run
 * (after coded TNC init). Re-read on every TNC Connect.
 */
public final class HostCommandIni {

    public static final String FILE_NAME = "config.ini";
    public static final String SECTION_INIT = "INIT";

    /** Queries / non-ACK commands — skip with a warning. Case-sensitive Host mnemonics. */
    public static final Set<String> BANNED_MNEMONICS = Set.of("OP", "MM", "AE");

    private static final String DEFAULT_INI = """
            # PactorRATT_Alpha host-command groups
            # Location: config/config.ini  (next to settings.json)
            # Re-read on every TNC → Connect. [INIT] runs AFTER the program's coded init
            # (HPOLL off, EAS, PT200, callsign ML/Mf, wrap AA, Pt, …).
            #
            # Unknown [section] names are ignored until the program wires them.
            #
            # Line format (Host mnemonics are two characters, case-sensitive):
            #   HP N     → sent as HPN  (the first space is removed)
            #   Pt       → sent as Pt   (no payload, no space)
            #   ML N7ML  → sent as MLN7ML
            #
            # Extra spaces after the mnemonic are collapsed; you get a warning, then it is sent.
            # OP, MM, and AE are skipped (they are not simple ACK commands).
            # A bad Host ACK aborts TNC Connect.
            #
            # Examples (uncomment a line under [INIT] to send it):
            # HP N
            # EA N
            # Pt

            [INIT]
            """;

    private final Path file;

    public HostCommandIni(Path configDir) {
        this.file = configDir.resolve(FILE_NAME);
    }

    public Path file() {
        return file;
    }

    /** Create {@code config.ini} with instruction comments if missing (CRLF). */
    public void ensureFile() throws IOException {
        if (Files.isRegularFile(file)) {
            return;
        }
        Files.createDirectories(file.getParent());
        String crlf = DEFAULT_INI.replace("\r\n", "\n").replace("\n", "\r\n");
        Files.writeString(file, crlf, StandardCharsets.UTF_8);
    }

    public List<InitLine> loadInitLines() throws IOException {
        ensureFile();
        String text = Files.readString(file, StandardCharsets.UTF_8);
        return parseInitLines(text);
    }

    static List<InitLine> parseInitLines(String text) {
        List<InitLine> out = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return out;
        }
        String section = null;
        for (String raw : text.split("\n", -1)) {
            String line = raw.replace("\r", "");
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            if (trimmed.startsWith("[") && trimmed.endsWith("]") && trimmed.length() >= 2) {
                section = trimmed.substring(1, trimmed.length() - 1);
                continue;
            }
            if (!SECTION_INIT.equals(section)) {
                continue;
            }
            InitLine parsed = parseCommandLine(trimmed);
            if (parsed != null) {
                out.add(parsed);
            }
        }
        return out;
    }

    /**
     * Two-letter mnemonic; optional payload after spaces. {@code HPN} (already joined) is allowed.
     */
    static InitLine parseCommandLine(String trimmed) {
        if (trimmed == null || trimmed.isEmpty()) {
            return null;
        }
        if (trimmed.length() < 2) {
            return InitLine.invalid(trimmed, "INIT line too short (need a 2-character Host mnemonic).");
        }
        String mnemonic = trimmed.substring(0, 2);
        String rest = trimmed.substring(2);
        boolean extraSpaces = false;
        String wire;
        if (rest.isEmpty()) {
            wire = mnemonic;
        } else if (rest.charAt(0) == ' ') {
            int i = 0;
            while (i < rest.length() && rest.charAt(i) == ' ') {
                i++;
            }
            extraSpaces = i > 1;
            wire = mnemonic + rest.substring(i);
        } else {
            wire = trimmed;
        }
        boolean banned = BANNED_MNEMONICS.contains(mnemonic);
        return new InitLine(trimmed, wire, extraSpaces, banned, null);
    }

    public record InitLine(
            String original,
            String wire,
            boolean extraSpaces,
            boolean banned,
            String invalidReason
    ) {
        static InitLine invalid(String original, String reason) {
            return new InitLine(original, "", false, false, reason);
        }

        public boolean invalid() {
            return invalidReason != null;
        }

        public String mnemonic() {
            if (wire != null && wire.length() >= 2) {
                return wire.substring(0, 2);
            }
            if (original != null && original.length() >= 2) {
                return original.substring(0, 2);
            }
            return "";
        }
    }
}
