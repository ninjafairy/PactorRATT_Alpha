package com.pactorratt.alpha.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Portable {@code config/macros.ini}: ordered {@code [Name]} groups. The group name is the
 * macro button label. File order is button order. Re-read when the macro bar is rebuilt.
 */
public final class MacroFile {

    public static final String FILE_NAME = "macros.ini";

    private static final String DEFAULT_INI = """
            # PactorRATT_Alpha macro buttons
            # Location: config/macros.ini  (next to settings.json)
            # Button order is the order of [groups] in this file. The group name is the button label.
            #
            # No prefix: append to Compose (not sent).
            # >line: send that line the same way as the Send button.
            # \\XX payload: Host command (first space after the mnemonic is removed, same as config.ini).
            # >> or \\\\ at the start of a line is literal Compose text.
            # Blank lines inside a group become blank Compose lines.
            # OP, MM, and AE are refused. A failed command stops the rest of the macro.

            """;

    private final Path file;

    public MacroFile(Path configDir) {
        this.file = configDir.resolve(FILE_NAME);
    }

    public Path file() {
        return file;
    }

    /** Create {@code macros.ini} with instruction comments if missing (CRLF). */
    public void ensureFile() throws IOException {
        if (Files.isRegularFile(file)) {
            return;
        }
        Files.createDirectories(file.getParent());
        String crlf = DEFAULT_INI.replace("\r\n", "\n").replace("\n", "\r\n");
        Files.writeString(file, crlf, StandardCharsets.UTF_8);
    }

    public synchronized List<Macro> load() throws IOException {
        return read().macros;
    }

    public synchronized void add(String name, List<String> lines) throws IOException {
        Parsed parsed = read();
        List<Macro> updated = new ArrayList<>(parsed.macros);
        updated.add(new Macro(name, lines));
        write(parsed.header, updated);
    }

    public synchronized void replace(String originalName, String newName, List<String> lines) throws IOException {
        Parsed parsed = read();
        int idx = indexOf(parsed.macros, originalName);
        if (idx < 0) {
            throw new IOException("Macro \"" + originalName + "\" was not found.");
        }
        List<Macro> updated = new ArrayList<>(parsed.macros);
        updated.set(idx, new Macro(newName, lines));
        write(parsed.header, updated);
    }

    public synchronized void delete(String name) throws IOException {
        Parsed parsed = read();
        int idx = indexOf(parsed.macros, name);
        if (idx < 0) {
            throw new IOException("Macro \"" + name + "\" was not found.");
        }
        List<Macro> updated = new ArrayList<>(parsed.macros);
        updated.remove(idx);
        write(parsed.header, updated);
    }

    /**
     * @param ignoreName existing name to skip when renaming; null when creating
     * @return an error message, or null when the name can be saved
     */
    public static String nameError(String name, List<Macro> existing, String ignoreName) {
        if (name == null || name.isBlank()) {
            return "Enter a macro name.";
        }
        String trimmed = name.trim();
        if (trimmed.indexOf('[') >= 0 || trimmed.indexOf(']') >= 0
                || trimmed.indexOf('\n') >= 0 || trimmed.indexOf('\r') >= 0) {
            return "Macro name cannot contain [ ] or line breaks.";
        }
        if (existing != null) {
            for (Macro macro : existing) {
                if (macro.name().equals(trimmed) && (ignoreName == null || !ignoreName.equals(macro.name()))) {
                    return "A macro named \"" + trimmed + "\" already exists.";
                }
            }
        }
        return null;
    }

    /**
     * @return an error message when a body line would be read back as a new group, or null
     */
    public static String bodyError(String body) {
        for (String line : linesFromEditor(body)) {
            if (isSectionLine(line)) {
                return "A line cannot look like a [group] header. Those start a new macro.";
            }
        }
        return null;
    }

    /**
     * Editor text to stored lines. One trailing newline is the end of the text, not an extra
     * blank Compose line. A blank line between other lines is kept.
     */
    public static List<String> linesFromEditor(String body) {
        if (body == null || body.isEmpty()) {
            return List.of();
        }
        String normalized = body.replace("\r\n", "\n").replace('\r', '\n');
        String[] parts = normalized.split("\n", -1);
        List<String> lines = new ArrayList<>(parts.length);
        for (String part : parts) {
            lines.add(part);
        }
        if (!lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        return List.copyOf(lines);
    }

    private Parsed read() throws IOException {
        ensureFile();
        String text = Files.readString(file, StandardCharsets.UTF_8);
        return parse(text);
    }

    private void write(List<String> header, List<Macro> macros) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String line : header) {
            sb.append(line).append("\r\n");
        }
        for (int i = 0; i < macros.size(); i++) {
            Macro macro = macros.get(i);
            sb.append('[').append(macro.name()).append("]\r\n");
            for (String line : macro.lines()) {
                sb.append(line).append("\r\n");
            }
            if (i < macros.size() - 1) {
                sb.append("\r\n");
            }
        }
        Files.createDirectories(file.getParent());
        Files.writeString(file, sb.toString(), StandardCharsets.UTF_8);
    }

    static Parsed parse(String text) {
        List<String> header = new ArrayList<>();
        List<Macro> macros = new ArrayList<>();
        String currentName = null;
        List<String> current = new ArrayList<>();
        boolean seenSection = false;
        String normalized = text == null ? "" : text.replace("\r\n", "\n").replace('\r', '\n');
        for (String line : normalized.split("\n", -1)) {
            if (!seenSection) {
                if (isSectionLine(line)) {
                    seenSection = true;
                    currentName = sectionName(line);
                    current = new ArrayList<>();
                } else {
                    header.add(line);
                }
            } else if (isSectionLine(line)) {
                macros.add(finish(currentName, current));
                currentName = sectionName(line);
                current = new ArrayList<>();
            } else {
                current.add(line);
            }
        }
        if (seenSection) {
            macros.add(finish(currentName, current));
        } else if (!header.isEmpty() && header.get(header.size() - 1).isEmpty()) {
            header.remove(header.size() - 1);
        }
        return new Parsed(header, macros);
    }

    private static Macro finish(String name, List<String> lines) {
        if (!lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        return new Macro(name, lines);
    }

    private static int indexOf(List<Macro> macros, String name) {
        for (int i = 0; i < macros.size(); i++) {
            if (macros.get(i).name().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    static boolean isSectionLine(String line) {
        if (line == null) {
            return false;
        }
        String trimmed = line.trim();
        if (trimmed.length() < 3 || !trimmed.startsWith("[") || !trimmed.endsWith("]")) {
            return false;
        }
        return !trimmed.substring(1, trimmed.length() - 1).trim().isEmpty();
    }

    private static String sectionName(String line) {
        String trimmed = line.trim();
        return trimmed.substring(1, trimmed.length() - 1).trim();
    }

    public record Macro(String name, List<String> lines) {
        public Macro {
            lines = List.copyOf(lines);
        }

        /** Text for the editor. A group that is only a blank line comes back as one newline. */
        public String bodyText() {
            if (lines.isEmpty()) {
                return "";
            }
            if (lines.size() == 1 && lines.get(0).isEmpty()) {
                return "\n";
            }
            return String.join("\n", lines);
        }
    }

    record Parsed(List<String> header, List<Macro> macros) {
    }
}
