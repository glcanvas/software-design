package com.nduginets.softwaredesign.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.nduginets.softwaredesign.actor.MasterActor;
import com.nduginets.softwaredesign.actor.Servers;
import com.nduginets.softwaredesign.actor.communications.ActorRequest;
import com.nduginets.softwaredesign.actor.communications.ActorResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scala.concurrent.Await;

import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class ActorTest {

    private static final EnumMap<Servers, String> serversConfig = new EnumMap<>(Servers.class);
    private Duration slavesTimeout;

    private final List<WireMockServer> closeables = new ArrayList<>();

    @BeforeEach
    public void init() {

        serversConfig.put(Servers.BING, "http://127.0.0.1:8080");
        serversConfig.put(Servers.GOOGLE, "http://127.0.0.1:8081");
        serversConfig.put(Servers.YANDEX, "http://127.0.0.1:8082");

    }

    @AfterEach
    public void close() {
        for (WireMockServer server : closeables) {
            server.stop();
        }
    }

    @Test
    public void simpleRequest() {
        slavesTimeout = Duration.ofSeconds(100);
        registerClosable(registerStubServer(8082, "yandex", "test", 0));
        registerClosable(registerStubServer(8081, "google", "test", 0));
        registerClosable(registerStubServer(8080, "bing", "test", 0));
        Assertions.assertEquals(6, makeActorRequest("test", 10000L).size());
    }


    @Test
    public void requestWithTimeout() {
        slavesTimeout = Duration.ofSeconds(1);
        registerClosable(registerStubServer(8082, "yandex", "test", 1000000));
        registerClosable(registerStubServer(8081, "google", "test", 1000000));
        registerClosable(registerStubServer(8080, "bing", "test", 1000000));
        Assertions.assertEquals(0, makeActorRequest("test", 10000L).size());
    }

    private WireMockServer registerStubServer(int portNumber, String responseBody, String requestPattern,
                                              long responseSleep) {
        WireMockServer server = new WireMockServer(portNumber);
        server.addMockServiceRequestListener((request, response) -> {
            try {
                Thread.sleep(responseSleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        server.stubFor(get(urlEqualTo("/" + requestPattern))
                .willReturn(aResponse()
                        .withBody("{\"" + responseBody + "\":[{\"answer\":\"a\"},{\"answer\":\"b\"}]}")));
        server.start();
        return server;
    }

    private List<String> makeActorRequest(String request, long timeOutMillis) {
        ActorSystem system = ActorSystem.create("testSystem");
        ActorRef actor = system.actorOf(Props.create(MasterActor.class, serversConfig, slavesTimeout), "master");
        try {
            ActorResponse response = (ActorResponse) Await.result(Patterns.ask(actor, new ActorRequest(request),
                    timeOutMillis), scala.concurrent.duration.Duration.Inf());
            return response.getResults();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void registerClosable(WireMockServer obj) {
        closeables.add(obj);
    }
}
