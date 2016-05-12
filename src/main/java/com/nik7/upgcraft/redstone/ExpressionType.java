package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.INBTTagProvider;
import net.minecraft.nbt.NBTTagCompound;

public enum ExpressionType implements INBTTagProvider<ExpressionType> {
    AND("&&"),
    OR("||"),
    NOT("!"),
    CUSTOM("custom");

    private final String expression;

    ExpressionType(String expression) {
        this.expression = expression;
    }


    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setString("ExpressionType", expression);
    }

    @Override
    public ExpressionType getFomNBT(NBTTagCompound nbt) {
        return getType(nbt.getString("ExpressionType"));
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
