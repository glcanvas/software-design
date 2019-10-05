package com.nduginets.softwaredesign.apiclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ApiTest {
    private static final Instant time = Instant.ofEpochSecond(10);
    private static final int hours = 4;
    private static final String query = "A";

    private final ObjectMapper mapper = new ObjectMapper();
    private Api apiMock;
    private Api apiStub;

    @Rule
    private final WireMockServer wireMockServer = new WireMockServer(8089);

    @BeforeEach
    public void initMock() {
        ApiClient clientMock = Mockito.mock(ApiClient.class);
        Mockito.when(clientMock.getNews(query, time, hours))
                .thenReturn(createArrayNode());
        apiMock = new Api(clientMock);
    }

    @Test
    public void mockTest() {
        Histogram h = apiMock.createHistogram(query, time, hours);
        for (int i = 1; i <= hours; i++) {
            Assertions.assertEquals(1, h.getValue(i));
        }
    }

    @Test
    public void stubTest() {
        wireMockServer.start();
        wireMockServer.
                stubFor(
                        get(urlEqualTo("/news?start_time=10&end_time=14410&q=A&access_token=stub&v=5.101"))
                                .willReturn(aResponse()
                                        .withBody(createRootNode())));
        ApiClient clientStub = ApiClient.buildApiClient("http://localhost:8089/news", "stub");
        apiStub = new Api(clientStub);
        Histogram h = apiStub.createHistogram(query, time, hours);
        for (int i = 1; i <= hours; i++) {
            Assertions.assertEquals(1, h.getValue(i));
        }
    }

    private ArrayNode createArrayNode() {
        ArrayNode nodes = mapper.createArrayNode();
        for (int i = 1; i <= hours; i++) {
            ObjectNode node = mapper.createObjectNode();
            node.put("date", time.getEpochSecond() + TimeUnit.HOURS.toSeconds(i));
            nodes.add(node);
        }
        return nodes;
    }

    private String createRootNode() {
        ObjectNode root = mapper.createObjectNode();
        ObjectNode items = mapper.createObjectNode();
        items.putArray("items").addAll(createArrayNode());
        root.put("response", items);
        return root.toString();
    }
}
