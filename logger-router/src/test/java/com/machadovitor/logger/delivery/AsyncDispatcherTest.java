package com.machadovitor.logger.delivery;

import com.machadovitor.logger.LogEvent;
import com.machadovitor.logger.LogLevel;
import com.machadovitor.logger.sink.LogSink;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsyncDispatcherTest {

    @Test
    void deliversEveryEventInOrderAndFlushesOnClose() {
        RecordingSink sink = new RecordingSink();
        Dispatcher dispatcher = Dispatcher.of(DeliveryMode.ASYNC, sink);

        int count = 1000;
        for (int i = 0; i < count; i++) {
            dispatcher.dispatch(new LogEvent(Instant.now(), "t", LogLevel.INFO, "m" + i, "main"));
        }
        dispatcher.close(); // must block until the backlog is drained

        assertEquals(count, sink.messages.size());
        assertEquals("m0", sink.messages.getFirst());
        assertEquals("m999", sink.messages.getLast());
    }

    private static final class RecordingSink implements LogSink {
        private final List<String> messages = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void write(LogEvent event) {
            messages.add(event.message());
        }
    }
}
