package com.nduginets.softwaredesign.calculator;

import com.nduginets.softwaredesign.calculator.tokenizer.Tokenizer;
import com.nduginets.softwaredesign.calculator.tokens.Token;
import com.nduginets.softwaredesign.calculator.visitors.CalculateVisitors;
import com.nduginets.softwaredesign.calculator.visitors.ParserVisitor;
import com.nduginets.softwaredesign.calculator.visitors.PrintVisitor;
import com.nduginets.softwaredesign.calculator.visitors.TokenVisitor;

import java.util.List;

public class CalcMain {
    public static void main(String[] args) {
        String input = "1+2*3";

        Tokenizer tokenizer = new Tokenizer();
        tokenizer.process(input);


        ParserVisitor parserVisitor = new ParserVisitor();
        parserVisitor.visit(tokenizer.getTokens());
        List<Token> polishTokens = parserVisitor.tokens();

        TokenVisitor printVisitor = new PrintVisitor();
        printVisitor.visit(polishTokens);

        CalculateVisitors calculateVisitors = new CalculateVisitors();
        calculateVisitors.visit(polishTokens);
        System.out.println();
        System.out.println(calculateVisitors.getResult());

    }
}
