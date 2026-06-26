package com.machadovitor.logger;

import com.machadovitor.logger.delivery.DeliveryMode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoggerRoutingTest {

    @Test
    void fansOutToEveryRouteRespectingItsLevel() {
        CapturingSink everything = new CapturingSink();
        CapturingSink warnAndUp = new CapturingSink();

        Logger logger = Logger.builder()
                .name("t")
                .route(everything, DeliveryMode.SYNC, LogLevel.TRACE)
                .route(warnAndUp, DeliveryMode.SYNC, LogLevel.WARN)
                .build();

        logger.debug("d");
        logger.info("i");
        logger.warn("w");
        logger.error("e");
        logger.close();

        assertEquals(List.of("d", "i", "w", "e"), everything.messages());
        assertEquals(List.of("w", "e"), warnAndUp.messages());
    }

    @Test
    void appliesThePerRoutePredicateFilter() {
        CapturingSink paymentsOnly = new CapturingSink();

        Logger logger = Logger.builder()
                .route(paymentsOnly, DeliveryMode.SYNC, LogLevel.TRACE, event -> event.message().contains("pay"))
                .build();

        logger.info("payment captured");
        logger.info("user logged in");
        logger.close();

        assertEquals(List.of("payment captured"), paymentsOnly.messages());
    }

    @Test
    void stampsLoggerNameLevelAndThread() {
        CapturingSink sink = new CapturingSink();

        Logger logger = Logger.builder().name("checkout")
                .route(sink, DeliveryMode.SYNC, LogLevel.TRACE)
                .build();
        logger.error("boom");
        logger.close();

        LogEvent event = sink.events().getFirst();
        assertEquals("checkout", event.logger());
        assertEquals(LogLevel.ERROR, event.level());
        assertEquals(Thread.currentThread().getName(), event.thread());
    }

    @Test
    void rejectsALoggerWithNoRoutes() {
        assertThrows(IllegalStateException.class, () -> Logger.builder().build());
    }
}
