package com.nduginets.softwaredesign.calculator.tokens;

public class NumberToken implements Token {

    private final int n;

    public NumberToken(int n) {
        this.n = n;
    }

    @Override
    public String textRepresentation() {
        return Integer.toString(n);
    }

    public int getN() {
        return n;
    }

    public TokenType getToken() {
        return TokenType.NUMBER;
    }

}
