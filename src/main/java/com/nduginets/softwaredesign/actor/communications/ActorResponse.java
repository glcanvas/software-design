package com.nduginets.softwaredesign.actor.communications;

import java.util.List;
import java.util.Objects;

public class ActorResponse {
    private final String name;
    private final List<String> results;

    public ActorResponse(String name, List<String> results) {
        this.name = name;
        this.results = results;
    }

    public String getName() {
        return name;
    }

    public List<String> getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorResponse that = (ActorResponse) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(results, that.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, results);
    }

    @Override
    public String toString() {
        return "ActorResponse{" +
                "name='" + name + '\'' +
                ", results=" + results +
                '}';
    }
}
