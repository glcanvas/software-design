package com.nduginets.softwaredesign.clock;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class MutableClock extends Clock {

    private Clock clock;

    public MutableClock(Clock clock) {
        this.clock = clock;
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return clock.withZone(zone);
    }

    @Override
    public Instant instant() {
        return clock.instant();
    }

    public void delay(long interval, TimeUnit unit) {
        long secondsInterval = unit.toSeconds(interval);
        clock = Clock.offset(clock, Duration.ofSeconds(secondsInterval));

    }
}
