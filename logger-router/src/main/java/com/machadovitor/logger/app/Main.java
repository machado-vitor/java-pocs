package com.machadovitor.logger.app;

import com.machadovitor.logger.LogLevel;
import com.machadovitor.logger.Logger;
import com.machadovitor.logger.delivery.DeliveryMode;
import com.machadovitor.logger.sink.LogSink;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    static void main() throws IOException {
        Path logFile = Path.of("target/logs/app.log");

        // same builder, three destinations at different levels and modes
        Logger logger = Logger.builder()
                .name("checkout")
                .route(LogSink.console(), DeliveryMode.SYNC, LogLevel.DEBUG)
                .route(LogSink.fileSystem(logFile), DeliveryMode.ASYNC, LogLevel.INFO)
                .route(LogSink.elk("https://elk.local:9200/logs"), DeliveryMode.ASYNC, LogLevel.WARN)
                .build();

        logger.debug("loading configuration");
        logger.info("server started on :8080");
        logger.warn("disk usage at 85%");
        logger.error("checkout 42 failed");

        logger.close(); // flush async routes before we read the file

        System.out.println("--- " + logFile + " ---");
        Files.readAllLines(logFile).forEach(System.out::println);
    }
}
