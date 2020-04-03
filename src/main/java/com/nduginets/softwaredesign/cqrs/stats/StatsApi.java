package com.nduginets.softwaredesign.cqrs.stats;

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
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class StatsApi {

    private final int port;
    private final PassCommandExecutor passCommandExecutor;
    private final UserCommandExecutor userCommandExecutor;
    private final Warehouse warehouse;
    private final ExecutorService executor;
    private final AtomicBoolean isAlive = new AtomicBoolean(true);

    private HttpServer server;

    private final ConcurrentMap<Integer, PassCommand> latestCommand = new ConcurrentHashMap<>();
    private final ConcurrentMap<LocalDate, Integer> entryByDay = new ConcurrentHashMap<>();
    private final AtomicLong entrySum = new AtomicLong(0);
    private final AtomicLong entryTimeSum = new AtomicLong(0);

    public StatsApi(int port, Warehouse warehouse) {
        this.port = port;
        this.warehouse = warehouse;
        this.passCommandExecutor = new PassCommandExecutor();
        this.userCommandExecutor = new UserCommandExecutor();

        executor = Executors.newSingleThreadExecutor();
        executor.submit(this::metricUpdater);
    }

    private void metricUpdater() {
        Instant now = Instant.EPOCH;
        while (isAlive.get()) {
            List<PassCommand> actualCommands = warehouse.getPassEventsFrom(now)
                    .stream()
                    .map(x -> new PassMapper().map(x))
                    .collect(Collectors.toList());
            now = Instant.now();

            for (PassCommand command : actualCommands) {
                if (command.getPassType() == PassCommand.PassType.ENTER) {
                    latestCommand.put(command.getUserId(), command);
                } else {
                    PassCommand enterCommand = latestCommand.get(command.getUserId());
                    entrySum.incrementAndGet();
                    long delta = command.getActionTime().toEpochMilli() - enterCommand.getActionTime().toEpochMilli();
                    entryTimeSum.addAndGet(delta);
                    LocalDate date = LocalDateTime.ofInstant(command.getActionTime(), ZoneOffset.UTC).toLocalDate();
                    entryByDay.computeIfPresent(date, (x, y) -> y + 1);
                    entryByDay.putIfAbsent(date, 1);
                }

            }

            try {
                Thread.sleep(2 * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void close() {
        server.stop(1);
        isAlive.set(false);
        executor.shutdown();
    }

    public HttpServer initServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/stats/day", handler -> {
                if (!isGet(handler)) {
                    return;
                }
                LocalDate date = getTime(handler, "day");
                if (date == null) {
                    return;
                }
                String response = String.format("{\"entry\":\"%s\"}", entryByDay.getOrDefault(date, 0));
                handler.sendResponseHeaders(200, response.getBytes().length);
                handler.getResponseBody().write(response.getBytes());
            });
            server.createContext("/stats/common", handler -> {
                if (!isGet(handler)) {
                    return;
                }
                long avgEntity = entrySum.get() / (entryByDay.size() == 0 ? 1 : entryByDay.size());
                long avgTime = entryTimeSum.get() / (entryByDay.size() == 0 ? 1 : entryByDay.size());
                String response = String.format("{\"avgEntry\":\"%s\", \"avgTime\":\"%s\"}", avgEntity, avgTime);
                handler.sendResponseHeaders(200, response.getBytes().length);
                handler.getResponseBody().write(response.getBytes());
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
