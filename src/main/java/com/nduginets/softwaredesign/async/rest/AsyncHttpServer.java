package com.nduginets.softwaredesign.async.rest;

import com.nduginets.softwaredesign.async.dao.AsyncDataBase;
import com.nduginets.softwaredesign.async.dao.User;
import com.nduginets.softwaredesign.async.rest.handlers.*;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.server.events.HttpServerEventsListener;
import javafx.util.Pair;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import rx.Observable;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class AsyncHttpServer {

    private final ExecutorService service = Executors.newFixedThreadPool(1);

    public AsyncHttpServer(int port) {
        AsyncDataBase instance = AsyncDataBase.createInstance();
        Resolver resolver = new Resolver();
        resolver.register(new CreateUserHandler(instance));
        resolver.register(new CreateItemHandler(instance));
        resolver.register(new ListItemHandler(instance));
        resolver.register(new ListUserItemHandler(instance));

        service.submit(() -> HttpServer.newServer(new InetSocketAddress("localhost", port))
                .start((request, response) -> {
                    final String path = request.getHeader("Accept");
                    final HttpMethod method = request.getHttpMethod();
                    final Handler h = resolver.resolveHandler(path, method);
                    if (h == null) {
                        response.setStatus(HttpResponseStatus.NOT_FOUND);
                        return response;
                    }


                    Observable<String> result;
                    try {
                        request.getContentLength();
                        result = request.getContent()
                                .map(x -> {
                                    ByteBuffer buffer = x.nioBuffer();
                                    byte[] bytes = new byte[buffer.remaining()];
                                    buffer.get(bytes, 0, bytes.length);
                                    return bytes;
                                })
                                .map(b -> h.fetch(path, new String(b)))
                                .flatMap(x -> x);
                    } catch (NumberFormatException e) {
                        result = h.fetch(path, null);
                    }

                    response.setStatus(HttpResponseStatus.OK);
                    return response.writeString(result);
                }).awaitShutdown());
    }

    public void stop() {
        service.shutdown();
    }

}