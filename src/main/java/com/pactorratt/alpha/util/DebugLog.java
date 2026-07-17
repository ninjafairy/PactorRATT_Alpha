package com.pactorratt.alpha.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Per-launch debug log under portable logs/. Toggleable; no rotation in Alpha.
 */
public final class DebugLog implements AutoCloseable {

    private static final DateTimeFormatter FILE_TS = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private static final DateTimeFormatter LINE_TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final Path logFile;
    private PrintWriter out;
    private boolean enabled;

    public DebugLog(Path portableRoot) {
        String name = "debug-" + LocalDateTime.now().format(FILE_TS) + ".log";
        this.logFile = portableRoot.resolve("logs").resolve(name);
    }

    public Path logFile() {
        return logFile;
    }

    public synchronized void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled && out == null) {
            try {
                Files.createDirectories(logFile.getParent());
                out = new PrintWriter(Files.newBufferedWriter(logFile, StandardCharsets.UTF_8), true);
                writeLine("INFO", "Debug log opened");
            } catch (IOException e) {
                this.enabled = false;
            }
        }
    }

    public synchronized boolean isEnabled() {
        return enabled;
    }

    public synchronized void info(String message) {
        writeLine("INFO", message);
    }

    public synchronized void host(String direction, String detail) {
        writeLine("HOST-" + direction, detail);
    }

    private void writeLine(String level, String message) {
        if (!enabled || out == null) {
            return;
        }
        out.println(LocalDateTime.now().format(LINE_TS) + " [" + level + "] " + message);
    }

    @Override
    public synchronized void close() {
        if (out != null) {
            writeLine("INFO", "Debug log closed");
            out.close();
            out = null;
        }
    }
}
