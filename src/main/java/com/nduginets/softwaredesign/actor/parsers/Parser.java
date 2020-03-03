package com.nduginets.softwaredesign.actor.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public interface Parser {
    ObjectMapper MAPPER = new ObjectMapper();

    default JsonNode parseResponse(String response) {
        try {
            return MAPPER.readTree(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    List<String> getInformation(JsonNode node);

    String name();
}
