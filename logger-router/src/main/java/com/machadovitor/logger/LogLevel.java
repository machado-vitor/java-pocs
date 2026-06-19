package com.machadovitor.logger;

// severity rises with the ordinal, which is what isAtLeast leans on
public enum LogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR;

    public boolean isAtLeast(LogLevel threshold) {
        return ordinal() >= threshold.ordinal();
    }
}
