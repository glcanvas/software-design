package com.nduginets.softwaredesign.actor;

import akka.actor.*;
import com.nduginets.softwaredesign.actor.communications.ActorRequest;
import com.nduginets.softwaredesign.actor.communications.ActorResponse;
import com.nduginets.softwaredesign.actor.communications.TimeOut;

import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MasterActor extends AbstractActorWithTimers {

    private final EnumMap<Servers, String> baseUrl;
    private final Duration timeOutDuration;

    public MasterActor(EnumMap<Servers, String> baseUrl, Duration timeOutDuration) {
        this.baseUrl = baseUrl;
        this.timeOutDuration = timeOutDuration;
    }

    private ActorRef parent = null;
    private boolean sendedResponse = false;

    private final List<ActorRef> slaves = new ArrayList<>();
    private final List<String> responses = new ArrayList<>();
    private int sendedResponses = 0;

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(ActorRequest.class, this::onActorRequest)
                .match(ActorResponse.class, this::onActorResponse)
                .match(TimeOut.class, this::onTimeout)
                .build();
    }

    private void onActorRequest(ActorRequest request) {
        registerSlaves();
        parent = sender();
        sendedResponses = slaves.size();
        slaves.forEach(s -> s.tell(request, self()));
        getTimers().startSingleTimer("TimeOut", new TimeOut(), timeOutDuration);
    }

    private void onActorResponse(ActorResponse response) {
        responses.addAll(response.getResults());
        sendedResponses--;

        if (sendedResponses == 0) {
            sendResponseToParent();
            deregisterSlaves();
        }
    }

    private void sendResponseToParent() {
        if (parent != null) {
            if (!sendedResponse) {
                sendedResponse = true;
                parent.tell(new ActorResponse("master", new ArrayList<>(responses)), context().sender());
            }
        }
    }

    private void onTimeout(TimeOut command) {
        sendResponseToParent();
        deregisterSlaves();
    }

    private void deregisterSlaves() {
        slaves.clear();
        responses.clear();
        sendedResponses = 0;
        sendedResponse = false;
    }

    private void registerSlaves() {
        ActorSystem system = context().system();
        for (Servers server : Servers.values()) {
            baseUrl.get(server);

            slaves.add(system.actorOf(Props.create(SlaveActor.class,
                    () -> new SlaveActor(server.getParser(), baseUrl.get(server)))));
        }

    }

}
