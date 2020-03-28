package com.nduginets.softwaredesign.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nduginets.softwaredesign.async.dao.Item;
import com.nduginets.softwaredesign.async.dao.User;
import com.nduginets.softwaredesign.async.rest.AsyncHttpServer;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class AsyncTest {
    private static final int PORT = 8080;

    private WebTarget target;
    private AsyncHttpServer server;

    @BeforeEach
    public void init() {
        server = new AsyncHttpServer(PORT);

        target = ResteasyClientBuilder.newClient()
                .target(String.format("http://localhost:%d", PORT));
    }

    @AfterEach
    public void close() {
        server.stop();
    }


    @Test
    public void fillTest() {
        Response r = target.request("/user/1").post(Entity.json(new User(1, "A", "B", "dollar")));
        Assertions.assertEquals(200, r.getStatus());

        r = target.request("/item/1").post(Entity.json(new Item(1, "A", "B", 20.0)));
        Assertions.assertEquals(200, r.getStatus());
    }

    @Test
    public void listTest() {
        Response r = target.request("/user/1").post(Entity.json(new User(1, "A", "B", "dollar")));
        Assertions.assertEquals(200, r.getStatus());
        r = target.request("/item/1").post(Entity.json(new Item(1, "A", "B", 20.0)));
        Assertions.assertEquals(200, r.getStatus());
        r = target.request("/items").get();
        Assertions.assertEquals(200, r.getStatus());
        String res = r.readEntity(String.class);
        Assertions.assertEquals(1, res.split("\n").length);
    }


    @Test
    public void mapperTest() throws IOException {
        Response r = target.request("/item/1").post(Entity.json(new Item(2, "A", "C", 20.0)));
        Assertions.assertEquals(200, r.getStatus());
        r = target.request("/user/1").post(Entity.json(new User(2, "A", "B", "EUR")));
        Assertions.assertEquals(200, r.getStatus());

        r = target.request("/currency/2/C").get();
        Assertions.assertEquals(200, r.getStatus());

        String res = r.readEntity(String.class);
        Assertions.assertEquals(1, res.split("\n").length);
        ObjectMapper objectMapper = new ObjectMapper();
        Assertions.assertEquals(30.0, objectMapper.readTree(res).get("price").asDouble());

    }

}
