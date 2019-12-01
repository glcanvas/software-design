package com.nduginets.softwaredesign.calculator.tokenizer;

public interface TokenizerState {

    void process(Tokenizer tokenizer, char c);

    default void endProcessing(Tokenizer tokenizer) {
    }
}

