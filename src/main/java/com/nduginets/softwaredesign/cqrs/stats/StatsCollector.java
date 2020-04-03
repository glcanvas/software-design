package com.nduginets.softwaredesign.cqrs.stats;

import com.nduginets.softwaredesign.cqrs.warehouse.PassEvent;
import com.nduginets.softwaredesign.cqrs.warehouse.UserEvent;
import com.nduginets.softwaredesign.cqrs.warehouse.Warehouse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class StatsCollector {

    private final Warehouse warehouse;
    private final int delayMillis;
    private final ExecutorService service;

    private final AtomicBoolean alive = new AtomicBoolean(true);
    private final AtomicReference<Instant> lastTime;


    public StatsCollector(Warehouse warehouse, Instant lastTime, int delayMillis) {
        this.warehouse = warehouse;
        this.delayMillis = delayMillis;
        this.service = Executors.newFixedThreadPool(1);
        this.lastTime = new AtomicReference<>(lastTime);
    }

    public void start() {
        service.submit(() -> {
            List<PassEvent> passEventList = new ArrayList<>();
            List<UserEvent> userEventList = new ArrayList<>();

            while (alive.get()) {


                List<PassEvent> currentPassList = warehouse.getPassEventsFrom(lastTime.get());

                passEventList.addAll(warehouse.getPassEventsFrom(lastTime.get()));
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        });
    }

}
