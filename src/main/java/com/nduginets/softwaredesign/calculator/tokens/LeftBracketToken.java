package com.nduginets.softwaredesign.calculator.tokens;

public class LeftBracketToken extends BracketToken {
    public String textRepresentation() {
        return "Left";
    }

    @Override
    public BracketType getType() {
        return BracketType.OPEN;
    }
}
