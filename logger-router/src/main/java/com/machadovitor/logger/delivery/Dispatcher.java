package com.machadovitor.logger.delivery;

import com.machadovitor.logger.LogEvent;
import com.machadovitor.logger.sink.LogSink;

// Both delivery modes hide behind one dispatch() call
public interface Dispatcher extends AutoCloseable {

    void dispatch(LogEvent event);

    @Override
    void close();

    static Dispatcher of(DeliveryMode mode, LogSink sink) {
        return switch (mode) {
            case SYNC -> new SyncDispatcher(sink);
            case ASYNC -> new AsyncDispatcher(sink);
        };
    }
}
