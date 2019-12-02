package com.nduginets.softwaredesign.calculator;

import com.nduginets.softwaredesign.calculator.tokenizer.Tokenizer;
import com.nduginets.softwaredesign.calculator.tokens.Token;
import com.nduginets.softwaredesign.calculator.visitors.CalculatorVisitors;
import com.nduginets.softwaredesign.calculator.visitors.ParserVisitor;
import com.nduginets.softwaredesign.calculator.visitors.PrintVisitor;
import com.nduginets.softwaredesign.calculator.visitors.TokenVisitor;

import java.util.List;

public class CalcMain {
    public static void main(String[] args) {
        String input = "1+2*3";

        Tokenizer tokenizer = new Tokenizer();
        tokenizer.processInput(input);


        ParserVisitor parserVisitor = new ParserVisitor();
        parserVisitor.iterateOverTokens(tokenizer.getTokens());
        List<Token> polishTokens = parserVisitor.getParsedTokens();

        TokenVisitor printVisitor = new PrintVisitor();
        printVisitor.iterateOverTokens(polishTokens);

        CalculatorVisitors calculatorVisitors = new CalculatorVisitors();
        calculatorVisitors.iterateOverTokens(polishTokens);
        System.out.println();
        System.out.println(calculatorVisitors.getResult());

    }
}
