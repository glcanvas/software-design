package com.nduginets.softwaredesign.actor.communications;

import java.util.Objects;

public class ActorRequest {

    private final String requestName;

    public ActorRequest(String requestName) {
        this.requestName = requestName;
    }

    public String getRequestName() {
        return requestName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorRequest that = (ActorRequest) o;
        return Objects.equals(requestName, that.requestName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestName);
    }

    @Override
    public String toString() {
        return "ActorRequest{" +
                "requestName='" + requestName + '\'' +
                '}';
    }
}
