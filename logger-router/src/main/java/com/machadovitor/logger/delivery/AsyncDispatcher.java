package com.machadovitor.logger.delivery;

import com.machadovitor.logger.LogEvent;
import com.machadovitor.logger.sink.LogSink;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

// Logging runs on a background thread so the caller never blocks on a slow sink.
// close() waits for the queue to drain first
public final class AsyncDispatcher implements Dispatcher {

    private final LogSink sink;
    private final ExecutorService worker;

    public AsyncDispatcher(LogSink sink) {
        this.sink = sink;
        this.worker = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "log-async-" + sink.name());
            thread.setDaemon(true);
            return thread;
        });
    }

    @Override
    public void dispatch(LogEvent event) {
        try {
            worker.execute(() -> sink.write(event));
        } catch (RejectedExecutionException alreadyClosed) {
            //came in after close(), ignore it
        }
    }

    @Override
    public void close() {
        worker.shutdown();
        try {
            if (!worker.awaitTermination(5, TimeUnit.SECONDS)) {
                worker.shutdownNow();
            }
        } catch (InterruptedException e) {
            worker.shutdownNow();
            Thread.currentThread().interrupt();
        }
        sink.close();
    }
}
