package com.nduginets.softwaredesign.async.rest.handlers;

import io.netty.handler.codec.http.HttpMethod;
import rx.Observable;
import javafx.util.Pair;

public interface Handler {

    HttpMethod type();

    String urlPattern();

    Observable<String> fetch(String path, String rawValue);
}
