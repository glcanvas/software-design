package com.nduginets.softwaredesign.calculator.tokens;

import com.nduginets.softwaredesign.calculator.visitors.TokenVisitor;

public abstract class BracketToken implements Token {

    public abstract BracketType getType();

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    public static enum BracketType{
      OPEN,
      CLOSE;
    };
}
