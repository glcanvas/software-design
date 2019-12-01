package com.nduginets.softwaredesign.clock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

public class ClockTest {
    private MutableClock clock;
    private EventsStatistics statistics;

    @BeforeEach
    public void init() {
        clock = new MutableClock(Clock.systemUTC());
        statistics = new EventsStatisticsImpl(clock);
    }


    @Test
    public void simpleTest() {
        statistics.incEvent("a");
        clock.delay(2, TimeUnit.MINUTES);
        statistics.incEvent("a");
        clock.delay(4, TimeUnit.MINUTES);
        Assertions.assertTrue(statistics.getEventStatisticByName("a") < 0.035);
    }

    @Test
    public void simpleHourTest() {

        for(int i = 0; i < 60; i++) {
            statistics.incEvent("a");
            clock.delay(40, TimeUnit.SECONDS);
        }
        Assertions.assertEquals(statistics.getEventStatisticByName("a"), 1.0);
    }
}
