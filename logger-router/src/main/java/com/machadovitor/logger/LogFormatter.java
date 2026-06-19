package com.machadovitor.logger;

// renders an event to a single line (text for console, JSON for ELK)
@FunctionalInterface
public interface LogFormatter {

    String format(LogEvent event);

    static LogFormatter text() {
        return event -> event.timestamp() + " [" + event.level() + "] "
                + event.logger() + " (" + event.thread() + "): " + event.message();
    }

    static LogFormatter json() {
        return event -> "{"
                + "\"timestamp\":\"" + event.timestamp() + "\","
                + "\"level\":\"" + event.level() + "\","
                + "\"logger\":\"" + escape(event.logger()) + "\","
                + "\"thread\":\"" + escape(event.thread()) + "\","
                + "\"message\":\"" + escape(event.message()) + "\""
                + "}";
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
