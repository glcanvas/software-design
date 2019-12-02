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
            switch (t.getToken()) {
                case BRACKET:
                    acceptToken((BracketToken) t);
                    break;
                case NUMBER:
                    acceptToken((NumberToken) t);
                    break;
                case OPERATION:
                    acceptToken((OperationToken) t);
                    break;
                default:
                    throw new IllegalStateException("Unrecognized token type:" + t.getToken());
            }
        }

    }
}
