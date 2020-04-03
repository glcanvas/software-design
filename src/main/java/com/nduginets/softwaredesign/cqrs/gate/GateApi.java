package com.nduginets.softwaredesign.cqrs.gate;

import com.nduginets.softwaredesign.cqrs.command.PassCommand;
import com.nduginets.softwaredesign.cqrs.command.UserCommand;
import com.nduginets.softwaredesign.cqrs.executor.PassCommandExecutor;
import com.nduginets.softwaredesign.cqrs.executor.UserCommandExecutor;
import com.nduginets.softwaredesign.cqrs.mapper.PassMapper;
import com.nduginets.softwaredesign.cqrs.mapper.UserMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.nduginets.softwaredesign.cqrs.warehouse.Warehouse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

public class GateApi {

    private final int port;

    private final PassCommandExecutor passCommandExecutor;
    private final UserCommandExecutor userCommandExecutor;
    private final Warehouse warehouse;

    public GateApi(int port, Warehouse warehouse) {
        this.port = port;
        this.warehouse = warehouse;
        this.passCommandExecutor = new PassCommandExecutor();
        this.userCommandExecutor = new UserCommandExecutor();
    }

    public HttpServer initServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/gate/enter", handler -> {
                if (!isGet(handler)) {
                    return;
                }
                PassCommand enter = buildCommand(handler, PassCommand.PassType.ENTER);
                if (enter == null) {
                    return;
                }

                if (!isValid(handler, enter)) {
                    return;
                }
                warehouse.insertPassEvent(new PassMapper().map(enter));
                handler.sendResponseHeaders(200, -1);
                handler.close();
            });
            server.createContext("/gate/exit", handler -> {
                if (!isGet(handler)) {
                    return;
                }
                PassCommand exit = buildCommand(handler, PassCommand.PassType.EXIT);
                if (exit == null) {
                    return;
                }

                if (!isValid(handler, exit)) {
                    return;
                }
                warehouse.insertPassEvent(new PassMapper().map(exit));
                handler.sendResponseHeaders(200, -1);
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


    private PassCommand buildCommand(HttpExchange handler, PassCommand.PassType type) throws IOException {
        try {
            String userIdStr = handler.getRequestHeaders().getFirst("user_id");
            int userId = Integer.parseInt(userIdStr);
            return new PassCommand(userId, Instant.now(), type);
        } catch (Exception e) {
            handler.sendResponseHeaders(400, -1);
            handler.close();
            return null;
        }
    }

    private boolean isValid(HttpExchange handler, PassCommand passCommand) throws IOException {
        UserCommand lastUserState = userCommandExecutor.fetchToLastState(
                warehouse.getUserEventsForUser(passCommand.getUserId())
                        .stream()
                        .map(x -> new UserMapper().map(x))
                        .collect(Collectors.toList()));

        PassCommand lastPassState = passCommandExecutor.fetchToLastState(
                warehouse.getPassEventsForUser(passCommand.getUserId())
                        .stream()
                        .map(x -> new PassMapper().map(x))
                        .collect(Collectors.toList()));

        LocalDate currentDay = LocalDateTime.ofInstant(passCommand.getActionTime(), ZoneOffset.UTC).toLocalDate();

        if (lastUserState == null || currentDay.isBefore(lastUserState.getTimeBegin()) ||
                currentDay.isAfter(lastUserState.getTimeEnd())) {
            handler.sendResponseHeaders(400, -1);
            handler.close();
            return false;
        }

        if (lastPassState == null && passCommand.getPassType() == PassCommand.PassType.ENTER) {
            return true;
        }

        if (lastPassState == null || lastPassState.getPassType() == passCommand.getPassType()) {
            handler.sendResponseHeaders(400, -1);
            handler.close();
            return false;
        }
        return true;

    }
}
