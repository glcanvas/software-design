package com.nduginets.softwaredesign.apiclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Api {
    public static final String DATE = "date";

    private final ApiClient client;

    public Api(ApiClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    public Histogram createHistogram(String query, Instant from, int hours) {
        if (hours < 1 || hours > 24) {
            throw new IllegalStateException("hours must be in [1, 24]");
        }
        Instant time = from != null ? from : Instant.now();
        ArrayNode nodes = client.getNews(query, time, hours);

        long firstHour = TimeUnit.SECONDS.toHours(time.getEpochSecond());
        Histogram histogram = new Histogram(query, hours);
        for (JsonNode node : nodes) {
            long postSeconds = node.get(DATE).longValue();
            long postHour = TimeUnit.SECONDS.toHours(postSeconds);
            int actualHour = (int) (postHour - firstHour);
            int requests = histogram.getValue(actualHour);
            histogram.setValue(actualHour, requests + 1);
        }
        return histogram;
    }

    public static void main(String[] args) {
        ApiClient client = ApiClient.buildVkApiClient("9e411d0a9e411d0a9e411d0ae79e2c220099e419e411d0ac3d992df8a0658293fa6771f");
        Api api = new Api(client);
        Histogram h = api.createHistogram("hello", Instant.now().minusSeconds(TimeUnit.DAYS.toSeconds(30)), 20);
        System.err.println(h);
    }
}
