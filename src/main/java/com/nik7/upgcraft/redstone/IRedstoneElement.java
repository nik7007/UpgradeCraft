package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.INBTTagProvider;

public interface IRedstoneElement extends INBTTagProvider<IRedstoneElement> {

    void initID();

    int getID();


    boolean getOutput();

    void setConnection(IConnection connection, short port);

    IConnection[] getConnections();

    short[] getInputsPort();

    short getOutputPort();

    void exec();
}
