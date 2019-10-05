package com.nduginets.softwaredesign.apiclient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Histogram {

    private final String name;
    private final List<Integer> values;
    private final int times;

    public Histogram(String name, int times) {
        this.name = name;
        this.values = new ArrayList<>(times + 1);
        this.times = times + 1;
        for (int i = 0; i <= times; i++) {
            values.add(0);
        }
    }

    public void setValue(int time, int value) {
        check(time);
        values.set(time, value);
    }

    public int getValue(int time) {
        check(time);
        return values.get(time);
    }

    public String getName() {
        return name;
    }

    private void check(int time) {
        if (time <= 0 || time > times) {
            throw new ArrayIndexOutOfBoundsException(time);
        }
    }

    @Override
    public String toString() {
        List<String> array = IntStream.range(1, times).boxed().map(t -> String.format("[t%d:c%d]", t, values.get(t))).collect(Collectors.toList());
        return "Histogram{" +
                "name='" + name + '\'' +
                ", values=" + String.join(",", array) +
                '}';
    }
}
