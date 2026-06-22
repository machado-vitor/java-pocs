package com.machadovitor.logger.sink;

import com.machadovitor.logger.LogEvent;
import com.machadovitor.logger.LogFormatter;

import java.io.PrintStream;

public final class ConsoleSink implements LogSink {

    private final PrintStream out;
    private final LogFormatter formatter;

    public ConsoleSink(LogFormatter formatter) {
        this(System.out, formatter);
    }

    public ConsoleSink(PrintStream out, LogFormatter formatter) {
        this.out = out;
        this.formatter = formatter;
    }

    @Override
    public synchronized void write(LogEvent event) {
        out.println(formatter.format(event));
    }
}
