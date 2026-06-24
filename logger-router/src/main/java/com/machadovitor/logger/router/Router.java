package com.machadovitor.logger.router;

import com.machadovitor.logger.LogEvent;
import com.machadovitor.logger.LogLevel;
import com.machadovitor.logger.delivery.Dispatcher;

import java.util.List;

// Fans an event out to every route that accepts it.
public final class Router {

    private final List<Route> routes;

    public Router(List<Route> routes) {
        this.routes = List.copyOf(routes);
    }

    public void route(LogEvent event) {
        for (Route route : routes) {
            if (route.accepts(event)) {
                route.dispatcher().dispatch(event);
            }
        }
    }

    public void close() {
        for (Route route : routes) {
            route.dispatcher().close();
        }
    }

    public record Route(Dispatcher dispatcher, LogLevel minLevel) {

        public boolean accepts(LogEvent event) {
            return event.level().isAtLeast(minLevel);
        }
    }
}
