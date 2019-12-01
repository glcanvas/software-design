package com.nduginets.softwaredesign.calculator.visitors;

import com.nduginets.softwaredesign.calculator.tokens.BracketToken;
import com.nduginets.softwaredesign.calculator.tokens.NumberToken;
import com.nduginets.softwaredesign.calculator.tokens.OperationToken;

public class PrintVisitor implements TokenVisitor {

    @Override
    public void visit(BracketToken token) {
        System.out.print(token.textRepresentation() + " ");
    }

    @Override
    public void visit(NumberToken token) {
        System.out.printf("Number: %s ", token.textRepresentation());
    }

    @Override
    public void visit(OperationToken token) {
        System.out.printf("Operation: %s ", token.textRepresentation());
    }

}
