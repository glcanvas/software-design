package com.nduginets.softwaredesign.apiclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ApiClient {
    private static final String QUERY = "q";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String VERSION = "v";
    private static final String VERSION_NUMBER = "5.101";
    private static final String RESPONSE = "response";
    private static final String ITEMS = "items";
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";

    private final String accessToken;
    private final Client client;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String host;

    public static ApiClient buildVkApiClient(String accessToken) {
        return new ApiClient("https://api.vk.com/method/newsfeed.search", accessToken);
    }

    public static ApiClient buildApiClient(String host, String accessToken) {
        return new ApiClient(host, accessToken);
    }

    private ApiClient(String host, String accessToken) {
        this.host = Objects.requireNonNull(host, "host");
        this.accessToken = Objects.requireNonNull(accessToken, "accessToken");
        this.client = ResteasyClientBuilder.newClient();
    }

    public ArrayNode getNews(String query, Instant from, int hours) {
        WebTarget target = client.target(host);
        target = target.queryParam(START_TIME, from.getEpochSecond());
        long secondsFromStart = TimeUnit.HOURS.toSeconds(hours);
        target = target.queryParam(END_TIME, from.plusSeconds(secondsFromStart).getEpochSecond());

        Response response = target
                .queryParam(QUERY, query)
                .queryParam(ACCESS_TOKEN, accessToken)
                .queryParam(VERSION, VERSION_NUMBER)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        JsonNode json = toJsonNode(response.readEntity(String.class));
        response.close();
        return (ArrayNode) json
                .get(RESPONSE)
                .get(ITEMS);

    }

    private JsonNode toJsonNode(String jsonString) {
        try {
            return mapper.readTree(jsonString);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
