package com.nduginets.softwaredesign.refactoring;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class ServerTest {

    private Server server;
    private WebTarget target;

    @BeforeEach
    public void initServer() throws Exception {
        DatabaseRequest databaseRequest = new DatabaseRequest();
        databaseRequest.dropProduct();
        server = new Server(databaseRequest, 8081);
        server.start();
        target = ResteasyClientBuilder.newClient()
                .target("http://127.0.1:8081");
    }

    @AfterEach
    public void stopServer() throws Exception {
        server.stop();
    }

    @Test
    public void createProductTest() {
        Response response = createProduct("a", 1);
        Assertions.assertEquals(201, response.getStatus());
        response.close();

        createProduct("b", 2);
        Assertions.assertEquals(201, response.getStatus());
        response.close();
    }

    @Test
    public void getProductsTest() {
        Response response = createProduct("a", 1);
        Assertions.assertEquals(201, response.getStatus());
        response.close();

        response = getProducts();
        Assertions.assertEquals(200, response.getStatus());
        System.err.println(response.readEntity(String.class));
        response.close();

        response = getStatistics("count");
        Assertions.assertEquals(200, response.getStatus());
        System.err.println(response.readEntity(String.class));
        response.close();
    }

    private Response createProduct(String name, long price) {
        return target.path("/add-product")
                .queryParam("name", name)
                .queryParam("price", price)
                .request().get();
    }

    private Response getProducts() {
        return target.path("/get-products")
                .request().get();
    }

    private Response getStatistics(String stat) {
        return target.path("/query")
                .queryParam("command", stat)
                .request().get();
    }

}
