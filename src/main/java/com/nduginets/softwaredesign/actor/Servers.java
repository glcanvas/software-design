package com.nduginets.softwaredesign.actor;

import com.nduginets.softwaredesign.actor.parsers.BingParser;
import com.nduginets.softwaredesign.actor.parsers.GoogleParser;
import com.nduginets.softwaredesign.actor.parsers.Parser;
import com.nduginets.softwaredesign.actor.parsers.YandexParser;

public enum Servers {
    YANDEX(new YandexParser()), GOOGLE(new GoogleParser()), BING(new BingParser());

    private final Parser parser;

    Servers(Parser parser) {
        this.parser = parser;
    }

    public Parser getParser() {
        return parser;
    }
}
