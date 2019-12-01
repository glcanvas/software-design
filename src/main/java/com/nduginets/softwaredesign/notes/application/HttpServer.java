package com.nduginets.softwaredesign.notes.application;

import io.vertx.core.Vertx;
import org.apache.log4j.BasicConfigurator;
import org.jboss.resteasy.plugins.server.vertx.VertxRequestHandler;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;

import java.util.Properties;

public class HttpServer extends AbstractApplication {
    public static final String HOST = "host";
    public static final String PORT = "port";

    private final RestApi[] servers;
    private final String host;
    private final int port;
    private Vertx vertx;
    private io.vertx.core.http.HttpServer httpServer;


    public HttpServer(Properties properties, RestApi... servers) {
        this.servers = servers;
        this.host = properties.getProperty(HOST);
        this.port = Integer.parseInt(properties.getProperty(PORT));
    }

    @Override
    public void onStart() throws Exception {
        BasicConfigurator.configure();
        VertxResteasyDeployment deployment = new VertxResteasyDeployment();

        deployment.start();
        for (RestApi server : servers) {
            deployment.getRegistry().addSingletonResource(server);
        }

        vertx = Vertx.vertx();
        httpServer = vertx.createHttpServer();
        httpServer.requestHandler(new VertxRequestHandler(vertx, deployment));
        httpServer.listen(port, host);
    }

    @Override
    public void onClose() throws Exception {
        vertx.close();
        httpServer.close();
    }
}
