package com.nik7.upgcraft.redstone;


public interface IRedstoneLogicElement extends IRedstoneElement {

    void initID();

    int getID();


    boolean getOutput();

    void setConnection(IRedstoneConnectionElement connection, short port);

    IRedstoneConnectionElement[] getConnections();

    short[] getInputsPort();

    short getOutputPort();

    ExpressionType getExpressionType();

    int getTickTocomplete();

}
