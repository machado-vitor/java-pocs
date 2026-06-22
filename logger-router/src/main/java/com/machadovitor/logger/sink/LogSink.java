package com.machadovitor.logger.sink;

import com.machadovitor.logger.LogEvent;
import com.machadovitor.logger.LogFormatter;

import java.nio.file.Path;

//anything that can take a log event. implement it to add your own
public interface LogSink extends AutoCloseable {

    void write(LogEvent event);

    default String name() {
        return getClass().getSimpleName();
    }

    @Override
    default void close() {
    }

    static LogSink console() {
        return new ConsoleSink(LogFormatter.text());
    }

    static LogSink fileSystem(Path path) {
        return new FileSystemSink(path, LogFormatter.text());
    }

    // a real HTTP client would go in the transport; this one just prints
    static LogSink elk(String endpoint) {
        return new ElkSink(endpoint, LogFormatter.json(), json -> System.out.println("POST " + endpoint + " " + json));
    }
}
