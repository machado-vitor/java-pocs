package com.machadovitor.logger;

import com.machadovitor.logger.sink.LogSink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// records every event it sees, for assertions
final class CapturingSink implements LogSink {

    private final List<LogEvent> events = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void write(LogEvent event) {
        events.add(event);
    }

    List<LogEvent> events() {
        synchronized (events) {
            return List.copyOf(events);
        }
    }

    List<String> messages() {
        synchronized (events) {
            return events.stream().map(LogEvent::message).toList();
        }
    }
}
