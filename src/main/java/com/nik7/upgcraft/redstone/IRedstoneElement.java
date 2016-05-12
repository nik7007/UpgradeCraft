package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.INBTTagProvider;

public interface IRedstoneElement extends INBTTagProvider<IRedstoneElement> {

    void initID();

    int getID();

    short getPosition();

    void setPosition(short position);

    boolean getOutput();

    Connection[] getConnections();

    void setInput(int ID, boolean value);

    void exec();
}
