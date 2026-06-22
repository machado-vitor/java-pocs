package com.machadovitor.logger.sink;

import com.machadovitor.logger.LogEvent;
import com.machadovitor.logger.LogFormatter;

import java.util.function.Consumer;

public final class ElkSink implements LogSink {

    private final String endpoint;
    private final LogFormatter formatter;
    private final Consumer<String> transport;

    public ElkSink(String endpoint, LogFormatter formatter, Consumer<String> transport) {
        this.endpoint = endpoint;
        this.formatter = formatter;
        this.transport = transport;
    }

    @Override
    public void write(LogEvent event) {
        transport.accept(formatter.format(event));
    }

    @Override
    public String name() {
        return "elk(" + endpoint + ")";
    }
}
