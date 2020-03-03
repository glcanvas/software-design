package com.nduginets.softwaredesign.apiclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ApiTest {
    private static final Instant startTime = Instant.ofEpochSecond(10);
    private static final int hours = 4;
    private static final Instant endTime = startTime.plusSeconds(TimeUnit.HOURS.toSeconds(4));
    private static final String query = "A";
    private static final WireMockServer stubServer = new WireMockServer(8089);

    private final ObjectMapper mapper = new ObjectMapper();

    private Api apiStub;
    private Api apiMock;

    @BeforeEach
    public void initStubServer() {
        String testUrl = String.format("/news?start_time=%d&end_time=%d&q=A&access_token=stub&v=5.101",
                startTime.getEpochSecond(),
                endTime.getEpochSecond());
        stubServer.start();
        stubServer.
                stubFor(
                        get(urlEqualTo(testUrl))
                                .willReturn(aResponse()
                                        .withBody(createRootNode())));
        ApiClient clientStub = ApiClient.buildApiClient("http://localhost:8089/news", "stub");
        apiStub = new Api(clientStub);
    }

    @BeforeEach
    public void initMock() {
        ApiClient clientMock = Mockito.mock(ApiClient.class);
        Mockito.when(clientMock.getNews(query, startTime, hours))
                .thenReturn(createArrayNode());
        apiMock = new Api(clientMock);
    }

    @AfterEach
    public void stopStubServer() {
        stubServer.stop();
    }

    @Test
    public void mockTest() {
        Histogram h = apiMock.createHistogram(query, startTime, hours);
        for (int i = 1; i <= hours; i++) {
            Assertions.assertEquals(1, h.getValue(i));
        }
    }

    @Test
    public void stubTest() {
        Histogram h = apiStub.createHistogram(query, startTime, hours);
        for (int i = 1; i <= hours; i++) {
            Assertions.assertEquals(1, h.getValue(i));
        }
    }

    private ArrayNode createArrayNode() {
        ArrayNode nodes = mapper.createArrayNode();
        for (int i = 1; i <= hours; i++) {
            ObjectNode node = mapper.createObjectNode();
            node.put("date", startTime.getEpochSecond() + TimeUnit.HOURS.toSeconds(i));
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