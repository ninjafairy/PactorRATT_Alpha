package com.pactorratt.alpha.hostmode;

/**
 * Presents decoded TNC firmware date and hardware bits after reading {@code $0006..$0009}.
 * Implementations must marshal UI onto the EDT; must not block the caller.
 */
@FunctionalInterface
public interface CompatInfoUi {

    /**
     * Show compatibility fingerprint decode (non-blocking). Called from worker
     * thread — must use EDT {@code invokeLater}.
     */
    void showCompatInfo(String message);
}
