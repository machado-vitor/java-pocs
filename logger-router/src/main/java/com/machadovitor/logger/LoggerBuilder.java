package com.machadovitor.logger;

import com.machadovitor.logger.delivery.DeliveryMode;
import com.machadovitor.logger.delivery.Dispatcher;
import com.machadovitor.logger.router.Router;
import com.machadovitor.logger.sink.LogSink;

import java.util.ArrayList;
import java.util.List;

// collects the routes, then builds the Logger
public final class LoggerBuilder {

    private String name = "root";
    private final List<Router.Route> routes = new ArrayList<>();

    public LoggerBuilder name(String name) {
        this.name = name;
        return this;
    }

    public LoggerBuilder route(LogSink sink, DeliveryMode mode) {
        return route(sink, mode, LogLevel.TRACE);
    }

    public LoggerBuilder route(LogSink sink, DeliveryMode mode, LogLevel minLevel) {
        routes.add(new Router.Route(Dispatcher.of(mode, sink), minLevel));
        return this;
    }

    public Logger build() {
        if (routes.isEmpty()) {
            throw new IllegalStateException("a logger needs at least one route");
        }
        return new DefaultLogger(name, new Router(routes));
    }
}
