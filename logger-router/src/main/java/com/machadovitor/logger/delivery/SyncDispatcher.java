package com.machadovitor.logger.delivery;

import com.machadovitor.logger.LogEvent;
import com.machadovitor.logger.sink.LogSink;

public final class SyncDispatcher implements Dispatcher {

    private final LogSink sink;

    public SyncDispatcher(LogSink sink) {
        this.sink = sink;
    }

    @Override
    public void dispatch(LogEvent event) {
        sink.write(event);
    }

    @Override
    public void close() {
        sink.close();
    }
}
