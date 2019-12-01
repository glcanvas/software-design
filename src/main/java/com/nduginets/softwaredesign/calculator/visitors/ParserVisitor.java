package com.nduginets.softwaredesign.calculator.visitors;

import com.nduginets.softwaredesign.calculator.tokens.BracketToken;
import com.nduginets.softwaredesign.calculator.tokens.NumberToken;
import com.nduginets.softwaredesign.calculator.tokens.OperationToken;
import com.nduginets.softwaredesign.calculator.tokens.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParserVisitor implements TokenVisitor {

    private final Stack<Token> operations = new Stack<>();
    private final List<Token> numbers = new ArrayList<>();

    @Override
    public void visit(BracketToken token) {
        switch (token.getType()) {
            case OPEN:
                operations.add(token);
                break;
            case CLOSE:
                while (!operations.isEmpty()) {
                    Token lastToken = operations.peek();
                    if (lastToken instanceof BracketToken && ((BracketToken) lastToken).getType() == BracketToken.BracketType.OPEN) {
                        operations.pop();
                        break;
                    }
                    if (lastToken instanceof OperationToken) {
                        numbers.add(lastToken);
                        operations.pop();
                        continue;
                    }
                    throw new IllegalStateException("Wrong order");
                }
                break;
        }
    }

    @Override
    public void visit(NumberToken token) {
        numbers.add(token);
    }

    @Override
    public void visit(OperationToken token) {
        while (!operations.isEmpty()) {
            Token lastToken = operations.peek();
            if (lastToken instanceof OperationToken && token.getPriority() <= ((OperationToken) lastToken).getPriority()) {
                numbers.add(lastToken);
                operations.pop();
            } else {
                break;
            }
        }
        operations.add(token);
    }

    public List<Token> tokens() {
        while (!operations.isEmpty()) {
            Token lastToken = operations.peek();
            if (lastToken instanceof OperationToken) {
                numbers.add(lastToken);
                operations.pop();
            } else {
                throw new IllegalStateException("No matching closing bracket");
            }
        }
        return numbers;
    }
}
