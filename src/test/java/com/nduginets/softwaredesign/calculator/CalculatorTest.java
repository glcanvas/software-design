package com.nduginets.softwaredesign.calculator;

import com.nduginets.softwaredesign.calculator.tokenizer.Tokenizer;
import com.nduginets.softwaredesign.calculator.visitors.CalculatorVisitors;
import com.nduginets.softwaredesign.calculator.visitors.ParserVisitor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CalculatorTest {

    private Tokenizer tokenizer;
    private ParserVisitor parserVisitor;
    private CalculatorVisitors calculatorVisitors;

    @BeforeEach
    public void init() {
        tokenizer = new Tokenizer();
        parserVisitor = new ParserVisitor();
        calculatorVisitors = new CalculatorVisitors();
    }

    @Test
    public void simpleTest() {
        tokenizer.processInput("1");
        parserVisitor.iterateOverTokens(tokenizer.getTokens());
        calculatorVisitors.iterateOverTokens(parserVisitor.getParsedTokens());
        Assertions.assertEquals(1, calculatorVisitors.getResult());
    }

    @Test
    public void singleOperationsTest() {
        tokenizer.processInput("1 + 2");
        parserVisitor.iterateOverTokens(tokenizer.getTokens());
        calculatorVisitors.iterateOverTokens(parserVisitor.getParsedTokens());
        Assertions.assertEquals(3, calculatorVisitors.getResult());
    }

    @Test
    public void bracketOperationsTest() {
        tokenizer.processInput("(1 + 2) * 2");
        parserVisitor.iterateOverTokens(tokenizer.getTokens());
        calculatorVisitors.iterateOverTokens(parserVisitor.getParsedTokens());
        Assertions.assertEquals(6, calculatorVisitors.getResult());
    }

    @Test
    public void hardBracketOperationTest() {
        tokenizer.processInput("(1 + 2) * (1 + 1)");
        parserVisitor.iterateOverTokens(tokenizer.getTokens());
        calculatorVisitors.iterateOverTokens(parserVisitor.getParsedTokens());
        Assertions.assertEquals(6, calculatorVisitors.getResult());
    }
}
