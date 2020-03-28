package com.nduginets.softwaredesign.async.rest.handlers;

import com.nduginets.softwaredesign.async.dao.AsyncDataBase;
import com.nduginets.softwaredesign.async.dao.Item;
import io.netty.handler.codec.http.HttpMethod;
import rx.Observable;

public class ListItemHandler implements Handler {
    private final AsyncDataBase db;

    public ListItemHandler(AsyncDataBase db) {
        this.db = db;
    }

    @Override
    public HttpMethod type() {
        return HttpMethod.GET;
    }

    @Override
    public String urlPattern() {
        return "/items";
    }

    @Override
    public Observable<String> fetch(String path, String rawValue) {
        return db.getItems().map(item -> item.toString() + "\n");
    }
}
