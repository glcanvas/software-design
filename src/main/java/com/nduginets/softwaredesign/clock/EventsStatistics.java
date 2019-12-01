package com.nduginets.softwaredesign.clock;

public interface EventsStatistics {

    void incEvent(String name);

    double getEventStatisticByName(String name);

    double getAllEventStatistic();

    void printStatistic();
}
