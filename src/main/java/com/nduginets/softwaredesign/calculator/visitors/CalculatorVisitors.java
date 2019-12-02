package com.nduginets.softwaredesign.calculator.visitors;

import com.nduginets.softwaredesign.calculator.tokens.BracketToken;
import com.nduginets.softwaredesign.calculator.tokens.NumberToken;
import com.nduginets.softwaredesign.calculator.tokens.OperationToken;

import java.util.Stack;

public class CalculatorVisitors implements TokenVisitor {

    private final Stack<Integer> stack = new Stack<>();

    @Override
    public void acceptToken(BracketToken token) {
        throw new IllegalStateException("Calculated state can't contains brackets");
    }

    @Override
    public void acceptToken(NumberToken token) {
        stack.add(token.getN());
    }

    @Override
    public void acceptToken(OperationToken token) {
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
