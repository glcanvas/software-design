package com.nduginets.softwaredesign.calculator.tokenizer;

public interface TokenizerState {

    default void startProcessing(Tokenizer tokenizer, char c) {
        throw new IllegalStateException("Not implemented");
    }

    default void endProcessing(Tokenizer tokenizer) {}
}

