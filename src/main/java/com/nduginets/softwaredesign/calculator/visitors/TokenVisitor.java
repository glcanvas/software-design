package com.nduginets.softwaredesign.calculator.visitors;

import com.nduginets.softwaredesign.calculator.tokens.BracketToken;
import com.nduginets.softwaredesign.calculator.tokens.NumberToken;
import com.nduginets.softwaredesign.calculator.tokens.OperationToken;
import com.nduginets.softwaredesign.calculator.tokens.Token;

import java.util.List;

public interface TokenVisitor {
    void acceptToken(BracketToken token);

    void acceptToken(NumberToken token);

    void acceptToken(OperationToken token);

    default void iterateOverTokens(List<Token> tokens) {
        for (Token t : tokens) {
            if (t instanceof BracketToken) {
                acceptToken((BracketToken) t);
            }
            if (t instanceof NumberToken) {
                acceptToken((NumberToken) t);
            }
            if (t instanceof OperationToken) {
                acceptToken((OperationToken) t);
            }
        }

    }
}
