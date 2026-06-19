package com.machadovitor.logger;

// Wrapping filters in a named type; the router still takes a raw Predicate for now.
@FunctionalInterface
public interface LogFilter {

    boolean test(LogEvent event);

    static LogFilter all() {
        return event -> true;
    }

    static LogFilter minLevel(LogLevel threshold) {
        return event -> event.level().isAtLeast(threshold);
    }

    // TODO: messageContains(text), fromLogger(name), and()/or() composition,
    //       then accept a LogFilter in Router.Route instead of Predicate.
}
