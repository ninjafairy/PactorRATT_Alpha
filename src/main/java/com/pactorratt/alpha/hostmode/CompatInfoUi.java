package com.pactorratt.alpha.hostmode;

/**
 * Presents decoded TNC firmware date and hardware bits after reading {@code $0006..$0009}.
 * Implementations must marshal UI onto the EDT and block until dismissed or auto-closed.
 */
@FunctionalInterface
public interface CompatInfoUi {

    /**
     * Show compatibility fingerprint decode; block until OK or timeout. Called from worker
     * thread — must use EDT invokeAndWait.
     */
    void showCompatInfo(String message) throws InterruptedException;
}
