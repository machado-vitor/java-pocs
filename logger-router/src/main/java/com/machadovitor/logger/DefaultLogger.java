package com.machadovitor.logger;

import com.machadovitor.logger.router.Router;

import java.time.Instant;

public final class DefaultLogger implements Logger {

    private final String name;
    private final Router router;

    public DefaultLogger(String name, Router router) {
        this.name = name;
        this.router = router;
    }

    @Override
    public void log(LogLevel level, String message) {
        router.route(new LogEvent(Instant.now(), name, level, message, Thread.currentThread().getName()));
    }

    @Override
    public void close() {
        router.close();
    }
}
