package com.nduginets.softwaredesign.async.rest.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.rx.client.Success;
import com.nduginets.softwaredesign.async.dao.AsyncDataBase;
import com.nduginets.softwaredesign.async.dao.User;
import io.netty.handler.codec.http.HttpMethod;
import rx.Observable;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;

public class CreateUserHandler implements Handler {

    private final AsyncDataBase db;
    ObjectMapper mapper = new ObjectMapper();

    public CreateUserHandler(AsyncDataBase db) {
        this.db = db;
    }

    @Override
    public HttpMethod type() {
        return HttpMethod.POST;
    }

    @Override
    public String urlPattern() {
        return "/user/[0-9]+";
    }

    @Override
    public Observable<String> fetch(String path, String rawValue) {
        JsonNode node;
        try {
            node = mapper.readTree(rawValue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        User user = new User(node.get("id").asInt(), node.get("name").asText(), node.get("login").asText(),
                node.get("currency").asText());
        Observable<Success> s = db.createUser(user);
        return s.map(Enum::toString);
    }
}
