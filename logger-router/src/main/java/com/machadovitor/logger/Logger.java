package com.machadovitor.logger;

public interface Logger extends AutoCloseable {

    void log(LogLevel level, String message);

    default void trace(String message) {
        log(LogLevel.TRACE, message);
    }

    default void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    default void info(String message) {
        log(LogLevel.INFO, message);
    }

    default void warn(String message) {
        log(LogLevel.WARN, message);
    }

    default void error(String message) {
        log(LogLevel.ERROR, message);
    }

    @Override
    void close();

    static LoggerBuilder builder() {
        return new LoggerBuilder();
    }
}
