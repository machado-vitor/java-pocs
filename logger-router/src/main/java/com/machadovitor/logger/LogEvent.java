package com.machadovitor.logger;

import java.time.Instant;

public record LogEvent(Instant timestamp, String logger, LogLevel level, String message, String thread) {
}
