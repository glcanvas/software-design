package com.nduginets.softwaredesign.calculator;

import com.nduginets.softwaredesign.calculator.tokenizer.Tokenizer;
import com.nduginets.softwaredesign.calculator.visitors.CalculateVisitors;
import com.nduginets.softwaredesign.calculator.visitors.ParserVisitor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CalculatorTest {

    private Tokenizer tokenizer;
    private ParserVisitor parserVisitor;
    private CalculateVisitors calculateVisitors;

    @BeforeEach
    public void init() {
        tokenizer = new Tokenizer();
        parserVisitor = new ParserVisitor();
        calculateVisitors = new CalculateVisitors();
    }

    @Test
    public void simpleTest() {
        tokenizer.process("1");
        parserVisitor.visit(tokenizer.getTokens());
        parserVisitor.tokens();
        calculateVisitors.visit(parserVisitor.tokens());
        Assertions.assertEquals(1, calculateVisitors.getResult());
    }

    @Test
    public void singleOperationsTest() {
        tokenizer.process("1 + 2");
        parserVisitor.visit(tokenizer.getTokens());
        parserVisitor.tokens();
        calculateVisitors.visit(parserVisitor.tokens());
        Assertions.assertEquals(3, calculateVisitors.getResult());
    }

    @Test
    public void bracketOperationsTest() {
        tokenizer.process("(1 + 2) * 2");
        parserVisitor.visit(tokenizer.getTokens());
        parserVisitor.tokens();
        calculateVisitors.visit(parserVisitor.tokens());
        Assertions.assertEquals(6, calculateVisitors.getResult());
    }

    @Test
    public void hardBracketOperationTest() {
        tokenizer.process("(1 + 2) * (1 + 1)");
        parserVisitor.visit(tokenizer.getTokens());
        parserVisitor.tokens();
        calculateVisitors.visit(parserVisitor.tokens());
        Assertions.assertEquals(6, calculateVisitors.getResult());
    }
}
