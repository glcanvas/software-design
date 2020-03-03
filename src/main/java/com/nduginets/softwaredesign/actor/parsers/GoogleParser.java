package com.nduginets.softwaredesign.actor.parsers;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleParser implements Parser {
    @Override
    public List<String> getInformation(JsonNode node) {
        try {
            List<String> result = new ArrayList<>();
            for (JsonNode currentAnswer : node.get("google")) {
                result.add(currentAnswer.get("answer").asText());
            }
            return result;
        } catch (RuntimeException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public String name() {
        return "google";
    }

}