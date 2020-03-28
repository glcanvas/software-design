package com.nduginets.softwaredesign.async.rest.handlers;

import io.netty.handler.codec.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Resolver {

    private final List<Handler> handlers;

    public Resolver() {
        this.handlers = new ArrayList<>();
    }

    public Resolver register(Handler handler) {
        handlers.add(handler);
        return this;
    }

    public Handler resolveHandler(String path, HttpMethod method) {
        for (Handler h : handlers) {
            if (h.type() == method && Pattern.matches(h.urlPattern(), path)) {
                return h;
            }
        }
        return null;
    }
}
