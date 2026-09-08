package com.pactorratt.alpha.hostmode;

/**
 * Blocking warning during TNC init (user {@code config.ini} INIT). Worker thread — EDT invokeAndWait.
 */
@FunctionalInterface
public interface InitWarningUi {

    void showInitWarning(String title, String message) throws InterruptedException;
}
