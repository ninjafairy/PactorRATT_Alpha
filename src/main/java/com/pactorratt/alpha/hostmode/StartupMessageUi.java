package com.pactorratt.alpha.hostmode;

/**
 * Presents the PK-232 startup/sign-on text captured after autobaud.
 * Implementations must marshal UI onto the EDT and block until dismissed.
 */
@FunctionalInterface
public interface StartupMessageUi {

    /**
     * Show message; block until OK. Called from worker thread — must use EDT invokeAndWait.
     */
    void showStartupMessage(String message) throws InterruptedException;
}
