package com.machadovitor.logger.sink;

import com.machadovitor.logger.LogEvent;
import com.machadovitor.logger.LogLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileSystemSinkTest {

    @Test
    void appendsOneFormattedLinePerEvent(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("nested/app.log"); // parent dir doesn't exist yet

        LogSink sink = LogSink.fileSystem(file);
        sink.write(new LogEvent(Instant.now(), "t", LogLevel.INFO, "hello", "main"));
        sink.write(new LogEvent(Instant.now(), "t", LogLevel.ERROR, "boom", "main"));
        sink.close();

        List<String> lines = Files.readAllLines(file);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("[INFO]"));
        assertTrue(lines.get(0).contains("hello"));
        assertTrue(lines.get(1).contains("[ERROR]"));
    }
}
