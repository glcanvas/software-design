package com.nduginets.softwaredesign.calculator.tokenizer;

import com.nduginets.softwaredesign.calculator.tokens.LeftBracketToken;
import com.nduginets.softwaredesign.calculator.tokens.OperationToken;
import com.nduginets.softwaredesign.calculator.tokens.RightBracketToken;

public class StartTokenizerState implements TokenizerState {
    @Override
    public void process(Tokenizer tokenizer, char c) {
        switch (c) {
            case '(':
                tokenizer.getTokens().add(new LeftBracketToken());
                return;
            case ')':
                tokenizer.getTokens().add(new RightBracketToken());
                return;
            case '+':
                tokenizer.getTokens().add(OperationToken.plus());
                return;
            case '-':
                tokenizer.getTokens().add(OperationToken.minus());
                return;
            case '/':
                tokenizer.getTokens().add(OperationToken.div());
                return;
            case '*':
                tokenizer.getTokens().add(OperationToken.mull());
                return;
        }

        if(Character.isWhitespace(c)) {
            return;
        }

        if(Character.isDigit(c)) {
         tokenizer.setState(new NumberTokenizerState());
         tokenizer.process(c);
         return;
        }

        throw new IllegalStateException("Unrecognized character: " + c);
    }

}
