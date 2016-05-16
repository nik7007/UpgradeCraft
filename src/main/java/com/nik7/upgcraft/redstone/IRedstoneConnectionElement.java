package com.nik7.upgcraft.redstone;


public interface IRedstoneConnectionElement extends IRedstoneElement {

    boolean addInput(IRedstoneLogicElement redstoneElement, short port);

    boolean removeInput(IRedstoneLogicElement redstoneElement, short port);

    void setInputValue(IRedstoneLogicElement redstoneElement, short port, boolean value);

    boolean getOutputValue(IRedstoneLogicElement redstoneElement, short port);

    boolean addOutput(IRedstoneLogicElement redstoneElement, short port);

    boolean removeOutput(IRedstoneLogicElement redstoneElement, short port);


}
