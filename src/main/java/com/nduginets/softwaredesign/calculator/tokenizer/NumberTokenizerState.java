package com.nduginets.softwaredesign.calculator.tokenizer;

import com.nduginets.softwaredesign.calculator.tokens.NumberToken;

public class NumberTokenizerState implements TokenizerState {

    private int currentNumber = 0;

    @Override
    public void process(Tokenizer tokenizer, char c) {
        if (Character.isDigit(c)) {
            currentNumber *= 10;
            currentNumber += (c - '0');
        } else {
            tokenizer.getTokens().add(new NumberToken(currentNumber));
            tokenizer.setState(new StartTokenizerState());
            tokenizer.process(c);
        }
    }

    @Override
    public void endProcessing(Tokenizer tokenizer) {
        tokenizer.getTokens().add(new NumberToken(currentNumber));
    }
}
