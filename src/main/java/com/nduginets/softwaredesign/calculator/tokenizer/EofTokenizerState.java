package com.nduginets.softwaredesign.calculator.tokenizer;

public class EofTokenizerState implements TokenizerState {
    @Override
    public void process(Tokenizer tokenizer, char c) {
        throw new IllegalStateException("Eof accepted, can't parse any values");
    }
}
