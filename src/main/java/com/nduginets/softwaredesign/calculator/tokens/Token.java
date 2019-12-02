package com.nduginets.softwaredesign.calculator.tokens;

public interface Token {

    String textRepresentation();

    TokenType getToken();

    enum TokenType {
        BRACKET,
        NUMBER,
        OPERATION
    }
}
