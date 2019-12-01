package com.nduginets.softwaredesign.calculator.tokens;

import com.nduginets.softwaredesign.calculator.visitors.TokenVisitor;

public class NumberToken implements Token {

    private final int n;

    public NumberToken(int n) {
        this.n = n;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String textRepresentation() {
        return Integer.toString(n);
    }

    public int getN() {
        return n;
    }
}
