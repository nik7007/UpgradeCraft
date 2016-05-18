package com.nik7.upgcraft.redstone;


public interface IRedstoneLogicElement extends IRedstoneLogicGeneralElement {

    void initID();

    int getID();

    ExpressionType getExpressionType();


}
