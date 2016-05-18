package com.nik7.upgcraft.redstone;


public interface IRedstoneLogicGeneralElement extends IRedstoneElement {

    boolean getOutput();

    void setConnection(IRedstoneConnectionElement connection, short port);

    IRedstoneConnectionElement[] getConnections();

    short[] getInputsPort();

    short getOutputPort();

    int getTickToComplete();

}
