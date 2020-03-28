package com.nduginets.softwaredesign.async.rest.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nduginets.softwaredesign.async.dao.AsyncDataBase;
import com.nduginets.softwaredesign.async.dao.Item;
import com.nduginets.softwaredesign.async.dao.User;
import io.netty.handler.codec.http.HttpMethod;
import rx.Observable;

import java.io.IOException;

public class CreateItemHandler implements Handler {
    private final AsyncDataBase db;
    ObjectMapper mapper = new ObjectMapper();

    public CreateItemHandler(AsyncDataBase db) {
        this.db = db;
    }

    @Override
    public HttpMethod type() {
        return HttpMethod.POST;
    }

    @Override
    public String urlPattern() {
        return "/item/[0-9]+";
    }

    @Override
    public Observable<String> fetch(String path, String rawValue) {
        JsonNode node;
        try {
            node = mapper.readTree(rawValue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Item user = new Item(node.get("id").asInt(), node.get("type").asText(), node.get("name").asText(),
                node.get("dollarPrice").asDouble());
        return db.createItem(user).map(Enum::toString);
    }

}
