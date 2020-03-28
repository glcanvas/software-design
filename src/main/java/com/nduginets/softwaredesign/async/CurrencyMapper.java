package com.nduginets.softwaredesign.async;

public final class CurrencyMapper {

    public static double map(String type, double p) {
        if (type.equals("USD")) {
            return p;
        }
        if (type.equals("EUR")) {
            return p * 1.5;
        }
        return p;
    }
}
