package com.nik7.upgcraft.redstone;


public enum ExpressionType {
    AND("&&"),
    OR("||"),
    NOT("!"),
    CUSTOM("custom");

    private final String expression;

    ExpressionType(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return expression;
    }

    public static ExpressionType getType(String expression) {

        for (ExpressionType type : ExpressionType.values())
            if (type.expression.equals(expression))
                return type;
        throw new RuntimeException(String.format("Invalid expression: '%s'", expression));
    }

    public boolean isBooleanExpression() {
        return this != CUSTOM;
    }
}
