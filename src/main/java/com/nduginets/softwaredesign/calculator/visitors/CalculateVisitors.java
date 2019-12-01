package com.nduginets.softwaredesign.calculator.visitors;

import com.nduginets.softwaredesign.calculator.tokens.BracketToken;
import com.nduginets.softwaredesign.calculator.tokens.NumberToken;
import com.nduginets.softwaredesign.calculator.tokens.OperationToken;

import java.util.Stack;

public class CalculateVisitors implements TokenVisitor {

    private final Stack<Integer> stack = new Stack<>();

    @Override
    public void visit(BracketToken token) {
        throw new IllegalStateException("Calculated state can't contains brackets");
    }

    @Override
    public void visit(NumberToken token) {
        stack.add(token.getN());
    }

    @Override
    public void visit(OperationToken token) {
        if (stack.size() < 2) {
            throw new IllegalStateException("Operation must apply two elements");
        }
        int a = stack.pop();
        int b = stack.pop();

        int result = token.getOperation().apply(a, b);
        stack.add(result);
    }

    public int getResult() {
        return stack.pop();
    }

}
