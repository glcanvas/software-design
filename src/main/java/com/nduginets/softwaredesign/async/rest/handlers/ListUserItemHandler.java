package com.nduginets.softwaredesign.async.rest.handlers;

import com.nduginets.softwaredesign.async.dao.AsyncDataBase;
import com.nduginets.softwaredesign.async.dao.Cartesian;
import com.nduginets.softwaredesign.async.dao.Item;
import com.nduginets.softwaredesign.async.dao.User;
import io.netty.handler.codec.http.HttpMethod;
import rx.Observable;

public class ListUserItemHandler implements Handler {

    private final AsyncDataBase db;

    public ListUserItemHandler(AsyncDataBase db) {
        this.db = db;
    }

    @Override
    public HttpMethod type() {
        return HttpMethod.GET;
    }

    @Override
    public String urlPattern() {
        return "/currency/[0-9]+/[a-zA-Z]+";
    }

    @Override
    public Observable<String> fetch(String path, String rawValue) {
        String paths[] = path.split("/");
        int userIdx = Integer.parseInt(paths[2]);
        String itemName = paths[3];

        Observable<User> users = db.getUserById(userIdx);
        return db.getItemsByName(itemName)
                .flatMap(i -> users.map(u -> new Cartesian(u, i)))
                .map(x -> x.toString() + "\n");
    }
}
