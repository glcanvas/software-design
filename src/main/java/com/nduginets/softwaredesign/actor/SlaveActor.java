package com.nduginets.softwaredesign.actor;

import akka.actor.AbstractActor;
import com.nduginets.softwaredesign.actor.communications.ActorRequest;
import com.nduginets.softwaredesign.actor.communications.ActorResponse;
import com.nduginets.softwaredesign.actor.parsers.Parser;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class SlaveActor extends AbstractActor {

    private final Parser parser;
        private final WebTarget target;

    public SlaveActor(Parser parser, String pathToRequest) {
        this.parser = parser;
        target = ResteasyClientBuilder.newClient()
                .target(pathToRequest);

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActorRequest.class, this::parseRequest)
                .build();
    }

    private void parseRequest(ActorRequest request) {
        Response response = target.path(request.getRequestName()).request().get();
        String responseString = response.readEntity(String.class);
        ActorResponse actorResponse = new ActorResponse(parser.name(),
                parser.getInformation(parser.parseResponse(responseString)));
        sender().forward(actorResponse, context());
    }
}
