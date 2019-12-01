package com.nduginets.softwaredesign.calculator.visitors;

import com.nduginets.softwaredesign.calculator.tokens.BracketToken;
import com.nduginets.softwaredesign.calculator.tokens.NumberToken;
import com.nduginets.softwaredesign.calculator.tokens.OperationToken;
import com.nduginets.softwaredesign.calculator.tokens.Token;

import java.util.List;

public interface TokenVisitor {
    void visit(BracketToken token);

    void visit(NumberToken token);

    void visit(OperationToken token);

    default void visit(List<Token> tokens) {
        for(Token t: tokens) {
            t.accept(this);
        }
    }
}
