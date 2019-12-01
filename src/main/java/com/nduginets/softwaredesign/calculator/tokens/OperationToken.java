package com.nduginets.softwaredesign.calculator.tokens;

import com.nduginets.softwaredesign.calculator.visitors.TokenVisitor;

import java.util.function.BiFunction;

public class OperationToken implements Token {

    private final int priority;
    private final BiFunction<Integer, Integer, Integer> operation;
    private final String textOperation;

    private OperationToken(int priority, BiFunction<Integer, Integer, Integer> operation, String textOperation) {
        this.priority = priority;
        this.operation = operation;
        this.textOperation = textOperation;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    public String textRepresentation() {
        return textOperation;
    }

    public int getPriority() {
        return priority;
    }

    public BiFunction<Integer, Integer, Integer> getOperation() {
        return operation;
    }

    public static OperationToken plus() {
        return new OperationToken(0, (x, y) -> x + y, "+");
    }

    public static OperationToken minus() {
        return new OperationToken(0, (x, y) -> x - y, "-");
    }

    public static OperationToken mull() {
        return new OperationToken(1, (x, y) -> x * y, "*");
    }

    public static OperationToken div() {
        return new OperationToken(1, (x, y) -> x / y, "/");
    }
}
