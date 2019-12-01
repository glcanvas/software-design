package com.nduginets.softwaredesign.calculator.tokens;

public class RightBracketToken extends BracketToken {
    public String textRepresentation() {
        return "Right";
    }

    @Override
    public BracketType getType() {
        return BracketType.CLOSE;
    }
}
