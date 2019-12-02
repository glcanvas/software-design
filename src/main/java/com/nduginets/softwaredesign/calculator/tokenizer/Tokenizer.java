package com.nduginets.softwaredesign.calculator.tokenizer;

import com.nduginets.softwaredesign.calculator.tokens.Token;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private TokenizerState state;
    private final List<Token> tokens;

    public Tokenizer() {
        this.state = new SymbolTokenizerState();
        this.tokens = new ArrayList<>();
    }


    public void flush() {
        this.state = new SymbolTokenizerState();
        this.tokens.clear();
    }


    void processCharacter(char c) {
        state.startProcessing(this, c);
    }

    public void processInput(String input) {
        for (int i = 0; i < input.length(); i++) {
            this.processCharacter(input.charAt(i));
        }

        state.endProcessing(this);
        state = new EofTokenizerState();
    }

    void setState(TokenizerState state) {
        this.state = state;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
