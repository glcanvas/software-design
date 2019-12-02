package com.nduginets.softwaredesign.calculator.tokens;

public abstract class BracketToken implements Token {

    public abstract BracketType getType();

    public TokenType getToken() {
        return TokenType.BRACKET;
    }

    public enum BracketType {
        OPEN,
        CLOSE;
    }
}
