package com.nduginets.softwaredesign.clock;

import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class EventsStatisticsImpl implements EventsStatistics {

    private final Clock clock;
    private long totalCount = 0;
    private final Map<String, List<Instant>> holder = new HashMap<>();


    public EventsStatisticsImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void incEvent(String name) {
        Instant eventTime = clock.instant();
        if (holder.containsKey(name)) {
            holder.get(name).add(eventTime);
        } else {
            List<Instant> times = new ArrayList<>();
            times.add(eventTime);
            holder.put(name, times);
        }
        totalCount++;
    }

    @Override
    public double getEventStatisticByName(String name) {
        Map<String, Double> result = rpsHour();
        Double rpm = result.get(name);

        return rpm == null ? 0 : rpm;
    }

    @Override
    public double getAllEventStatistic() {
        Map<String, Double> result = rpsHour();
        Optional<Double> totalCount = result.values().stream().reduce((x, y) -> x + y);
        return totalCount
                .map(tc -> tc / (result.size() + 1))
                .orElse(0.0);
    }

    @Override
    public void printStatistic() {
        Map<String, Double> result = composeStatistics(Instant.EPOCH);
        for (AbstractMap.Entry<String, Double> e : result.entrySet()) {
            System.out.printf("%s: %.10f\n", e.getKey(), e.getValue());
        }
    }

    private Map<String, Double> rpsHour() {
        Instant currentTime = clock.instant().minusSeconds(TimeUnit.HOURS.toSeconds(1));
        return composeStatistics(currentTime);
    }

    private Map<String, Double> composeStatistics(Instant currentInstant) {
        Map<String, Double> result = new HashMap<>();
        for (AbstractMap.Entry<String, List<Instant>> entry : holder.entrySet()) {
            int cnt = 0;
            for (Instant i : entry.getValue()) {
                // minus one hour
                if (currentInstant.compareTo(i) <= 0) {
                    cnt++;
                }
            }
            result.put(entry.getKey(), cnt / (double) 60);
        }
        return result;
    }

}
