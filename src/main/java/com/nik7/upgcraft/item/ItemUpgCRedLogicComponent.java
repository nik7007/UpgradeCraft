package com.nik7.upgcraft.item;

import com.nik7.upgcraft.redstone.ExpressionType;

public class ItemUpgCRedLogicComponent extends ItemUpgC {

    private ExpressionType expressionType;

    public ItemUpgCRedLogicComponent(String name, ExpressionType expressionType) {
        super(name);
        this.expressionType = expressionType;
    }

    public ExpressionType getExpressionType() {
        return this.expressionType;
    }
}
