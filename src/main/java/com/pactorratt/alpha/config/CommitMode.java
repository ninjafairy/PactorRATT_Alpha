package com.pactorratt.alpha.config;

public enum CommitMode {
    /** Enter commits one line into the App TX buffer (or ISS Host send). */
    LINE,
    /** Enter inserts newline; Send commits compose into the App TX buffer (or ISS Host send). */
    MESSAGE
}
