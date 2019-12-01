package com.nduginets.softwaredesign.calculator.tokenizer;

import com.nduginets.softwaredesign.calculator.tokens.Token;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private TokenizerState state;
    private final List<Token> tokens;

    public Tokenizer() {
        this.state = new StartTokenizerState();
        this.tokens = new ArrayList<>();
    }


    public void flush() {
        this.state = new StartTokenizerState();
        this.tokens.clear();
    }


    void process(char c) {
        state.process(this, c);
    }

    public void process(String input) {
        for (int i = 0; i < input.length(); i++) {
            this.process(input.charAt(i));
        }

        state.endProcessing(this);
        state = new EofTokenizerState();
    }

    TokenizerState getState() {
        return state;
    }

    void setState(TokenizerState state) {
        this.state = state;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
