package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.INBTTagProvider;

public interface IConnection extends INBTTagProvider<IConnection> {

    boolean addInput(IRedstoneElement redstoneElement, short port);

    boolean removeInput(IRedstoneElement redstoneElement, short port);

    void setInputValue(IRedstoneElement redstoneElement, short port, boolean value);

    boolean getOutputValue(IRedstoneElement redstoneElement, short port);

    boolean addOutput(IRedstoneElement redstoneElement, short port);

    boolean removeOutput(IRedstoneElement redstoneElement, short port);

    void exec();
}
