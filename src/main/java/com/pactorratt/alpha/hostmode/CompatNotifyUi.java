package com.pactorratt.alpha.hostmode;

/**
 * Non-blocking *in testing* fingerprint notify. Implementations must marshal onto the EDT
 * with {@code invokeLater} and must not block the caller.
 */
@FunctionalInterface
public interface CompatNotifyUi {

    /**
     * @param starredLabel text between {@code *} in {@code docs/Compat_Memory_Map.md}
     */
    void showInTestingNotify(String starredLabel);
}
