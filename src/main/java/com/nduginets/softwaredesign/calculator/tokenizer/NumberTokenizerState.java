package com.nduginets.softwaredesign.calculator.tokenizer;

import com.nduginets.softwaredesign.calculator.tokens.NumberToken;

class NumberTokenizerState implements TokenizerState {

    private int currentNumber = 0;

    @Override
    public void startProcessing(Tokenizer tokenizer, char c) {
        if (Character.isDigit(c)) {
            currentNumber *= 10;
            currentNumber += (c - '0');
        } else {
            tokenizer.getTokens().add(new NumberToken(currentNumber));
            tokenizer.setState(new SymbolTokenizerState());
            tokenizer.processCharacter(c);
        }
    }

    @Override
    public void endProcessing(Tokenizer tokenizer) {
        tokenizer.getTokens().add(new NumberToken(currentNumber));
    }
}
