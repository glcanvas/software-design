package com.nduginets.softwaredesign.cqrs.manager;


import com.nduginets.softwaredesign.cqrs.command.PassCommand;
import com.nduginets.softwaredesign.cqrs.command.UserCommand;
import com.nduginets.softwaredesign.cqrs.executor.PassCommandExecutor;
import com.nduginets.softwaredesign.cqrs.executor.UserCommandExecutor;
import com.nduginets.softwaredesign.cqrs.mapper.PassMapper;
import com.nduginets.softwaredesign.cqrs.mapper.UserMapper;
import com.nduginets.softwaredesign.cqrs.warehouse.Warehouse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class ManagerApi {

    private final int port;

    private final PassCommandExecutor passCommandExecutor;
    private final UserCommandExecutor userCommandExecutor;
    private final Warehouse warehouse;

    public ManagerApi(int port, Warehouse warehouse) {
        this.port = port;
        this.warehouse = warehouse;
        this.passCommandExecutor = new PassCommandExecutor();
        this.userCommandExecutor = new UserCommandExecutor();
    }

    public HttpServer initServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/manager/get", handler -> {
                if (!isGet(handler)) {
                    return;
                }

                int userId = getUserId(handler);
                UserCommand lastState = userCommandExecutor.fetchToLastState(
                        warehouse.getUserEventsForUser(userId)
                                .stream()
                                .map(x -> new UserMapper().map(x))
                                .collect(Collectors.toList()));
                if (lastState != null) {
                    String user = String.format("{\"userId\":\"%s\", \"timeEnd\":\"%s\", \"timeBegin\":\"%s\"}",
                            lastState.getUserId(), lastState.getTimeEnd(), lastState.getTimeBegin());
                    handler.sendResponseHeaders(200, user.getBytes().length);
                    handler.getResponseBody().write(user.getBytes());
                } else {
                    handler.sendResponseHeaders(404, -1);
                }
                handler.close();
            });
            server.createContext("/manager/create", handler -> {
                if (!isGet(handler)) {
                    return;
                }
                int userId = getUserId(handler);
                LocalDate timeBegin = getTime(handler, "time_begin");
                LocalDate timeEnd = getTime(handler, "time_end");

                if (userId == -1 || timeBegin == null || timeEnd == null) {
                    return;
                }

                UserCommand command = new UserCommand(userId, Instant.now(), timeBegin, timeEnd);
                warehouse.insertUserEvent(new UserMapper().map(command));
                handler.sendResponseHeaders(201, -1);
                handler.close();
            });
            server.start();
            return server;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isGet(HttpExchange handler) throws IOException {
        if (!"GET".equals(handler.getRequestMethod())) {
            handler.sendResponseHeaders(404, -1);
            handler.close();
            return false;
        }
        return true;
    }

    private int getUserId(HttpExchange handler) throws IOException {
        try {
            String userIdStr = handler.getRequestHeaders().getFirst("user_id");
            return Integer.parseInt(userIdStr);
        } catch (Exception e) {
            handler.sendResponseHeaders(400, -1);
            handler.close();
            return -1;
        }
    }

    private LocalDate getTime(HttpExchange handler, String attribute) throws IOException {

        try {
            return LocalDate.parse(handler.getRequestHeaders().getFirst(attribute));
        } catch (Exception e) {
            handler.sendResponseHeaders(400, -1);
            handler.close();
            return null;
        }
    }
}
