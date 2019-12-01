package com.nduginets.softwaredesign.calculator.tokens;

import com.nduginets.softwaredesign.calculator.visitors.TokenVisitor;

public interface Token {
    void accept(TokenVisitor visitor);

    String textRepresentation();
}
