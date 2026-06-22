package com.machadovitor.logger.sink;

import com.machadovitor.logger.LogEvent;
import com.machadovitor.logger.LogFormatter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

// one line per event, synchronized so writes don't interleave
public final class FileSystemSink implements LogSink {

    private final Path path;
    private final LogFormatter formatter;
    private final BufferedWriter writer;

    public FileSystemSink(Path path, LogFormatter formatter) {
        this.path = path;
        this.formatter = formatter;
        try {
            Path parent = path.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            this.writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException("could not open log file: " + path, e);
        }
    }

    @Override
    public synchronized void write(LogEvent event) {
        try {
            writer.write(formatter.format(event));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("could not write to log file: " + path, e);
        }
    }

    @Override
    public synchronized void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new UncheckedIOException("could not close log file: " + path, e);
        }
    }

    @Override
    public String name() {
        return "fs(" + path + ")";
    }
}
