package com.nduginets.softwaredesign.cqrs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nduginets.softwaredesign.cqrs.gate.GateApi;
import com.nduginets.softwaredesign.cqrs.manager.ManagerApi;
import com.nduginets.softwaredesign.cqrs.stats.StatsApi;
import com.nduginets.softwaredesign.cqrs.warehouse.DslHolder;
import com.nduginets.softwaredesign.cqrs.warehouse.Warehouse;
import com.sun.net.httpserver.HttpServer;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SystemTest {

    private Warehouse warehouse;
    private HttpServer managementServer;
    private HttpServer gateServer;

    private WebTarget managerTarget;
    private WebTarget gateTarget;
    private WebTarget statsTarget;


    private StatsApi statsApi;

    @BeforeEach
    public void init() {
        DSLContext context = DslHolder.createDB("jdbc:sqlite:test1.db");
        warehouse = new Warehouse(context);


        ManagerApi managerApi = new ManagerApi(8000, warehouse);
        managementServer = managerApi.initServer();

        GateApi gateApi = new GateApi(8001, warehouse);
        gateServer = gateApi.initServer();
        statsApi = new StatsApi(8002, warehouse);
        statsApi.initServer();

        managerTarget = ResteasyClientBuilder.newClient().target(String.format("http://%s:%s", "localhost", "8000"));
        gateTarget = ResteasyClientBuilder.newClient().target(String.format("http://%s:%s", "localhost", "8001"));
        statsTarget = ResteasyClientBuilder.newClient().target(String.format("http://%s:%s", "localhost", "8002"));


    }

    @AfterEach
    public void close() {
        managementServer.stop(1);
        gateServer.stop(1);
        warehouse.dropDB();
        statsApi.close();
    }

    @Test
    public void createUserTest() {
        Response response = managerTarget.path("/manager/create").request()
                .header("user_id", "1")
                .header("time_begin", "2019-12-31")
                .header("time_end", "2021-12-31").get();
        response.close();
        assertEquals(201, response.getStatus());
        assertEquals(1, warehouse.getUserEvents().size());
    }


    @Test
    public void modifyUserTest() throws Exception {
        Response response = managerTarget.path("/manager/create").request()
                .header("user_id", "1")
                .header("time_begin", "2019-12-31")
                .header("time_end", "2021-12-31").get();
        response.close();
        assertEquals(201, response.getStatus());

        response = managerTarget.path("/manager/create").request()
                .header("user_id", "1")
                .header("time_begin", "2018-12-31")
                .header("time_end", "2022-12-31").get();
        response.close();
        assertEquals(201, response.getStatus());


        response = managerTarget.path("/manager/get")
                .request()
                .header("user_id", "1")
                .get();
        assertEquals(200, response.getStatus());
        String s = response.readEntity(String.class);
        JsonNode user = new ObjectMapper().readTree(s);
        response.close();

        assertEquals(1, user.get("userId").asInt());
        assertEquals("2022-12-31", user.get("timeEnd").asText());
        assertEquals("2018-12-31", user.get("timeBegin").asText());
    }

    @Test
    public void gateTest() {
        Response response = managerTarget.path("/manager/create").request()
                .header("user_id", "1")
                .header("time_begin", "2019-12-31")
                .header("time_end", "2021-12-31").get();
        response.close();
        assertEquals(201, response.getStatus());

        Response gateResponse = gateTarget.path("/gate/enter")
                .request()
                .header("user_id", "1")
                .get();
        gateResponse.close();
        assertEquals(200, gateResponse.getStatus());

        gateResponse = gateTarget.path("/gate/exit")
                .request()
                .header("user_id", "1")
                .get();
        gateResponse.close();
        assertEquals(200, gateResponse.getStatus());

        assertEquals(2, warehouse.getPassEvents().size());
    }

    @Test
    public void gateWrongOrderTest() {
        Response response = managerTarget.path("/manager/create").request()
                .header("user_id", "1")
                .header("time_begin", "2019-12-31")
                .header("time_end", "2021-12-31").get();
        response.close();
        assertEquals(201, response.getStatus());

        Response gateResponse = gateTarget.path("/gate/exit")
                .request()
                .header("user_id", "1")
                .get();
        gateResponse.close();
        assertEquals(400, gateResponse.getStatus());
    }

    @Test
    public void gateWrongUserTest() {

        Response gateResponse = gateTarget.path("/gate/enter")
                .request()
                .header("user_id", "1")
                .get();
        gateResponse.close();
        assertEquals(400, gateResponse.getStatus());


        gateResponse = gateTarget.path("/gate/exit")
                .request()
                .header("user_id", "1")
                .get();
        gateResponse.close();
        assertEquals(400, gateResponse.getStatus());
    }

    @Test
    public void getStatisticsTest() throws Exception {

        Response response = managerTarget.path("/manager/create").request()
                .header("user_id", "1")
                .header("time_begin", "2019-12-31")
                .header("time_end", "2021-12-31").get();
        response.close();
        assertEquals(201, response.getStatus());


        response = gateTarget.path("/gate/enter")
                .request()
                .header("user_id", "1")
                .get();
        response.close();
        assertEquals(200, response.getStatus());


        response = gateTarget.path("/gate/exit")
                .request()
                .header("user_id", "1")
                .get();
        response.close();
        assertEquals(200, response.getStatus());


        Thread.sleep(4000L);
        response = statsTarget.path("/stats/common")
                .request()
                .get();
        assertEquals(200, response.getStatus());
        String s = response.readEntity(String.class);
        JsonNode stats = new ObjectMapper().readTree(s);
        response.close();
        assertEquals(1, stats.get("avgEntry").asInt());

    }
}
