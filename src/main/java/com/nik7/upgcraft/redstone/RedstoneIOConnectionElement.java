package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

import static com.nik7.upgcraft.redstone.RedstoneConnectionElement.ConnectionElement;
import static com.nik7.upgcraft.redstone.RedstoneConnectionElement.ConnectionElement.addConnectionToList;
import static com.nik7.upgcraft.redstone.RedstoneConnectionElement.ConnectionElement.removeConnectionToList;

public class RedstoneIOConnectionElement implements IRedstoneConnectionElement {

    public static final short OUTPUT_TYPE = 0;
    public static final short INPUT_TYPE = 1;

    private final List<ConnectionElement> elements = new ArrayList<>();
    private final short connectionType;

    public RedstoneIOConnectionElement(short connectionType) {
        if (connectionType == OUTPUT_TYPE || connectionType == INPUT_TYPE)
            this.connectionType = connectionType;
        else throw new RuntimeException("'" + connectionType + "' is not a valid connection type for IO");
    }


    @Override
    public boolean addInput(IRedstoneLogicElement redstoneElement, short port) {
        return connectionType == INPUT_TYPE && addConnectionToList(elements, redstoneElement, port);
    }

    @Override
    public boolean removeInput(IRedstoneLogicElement redstoneElement, short port) {
        if(connectionType == INPUT_TYPE)
        {
            return removeConnectionToList(elements,redstoneElement,port);
        }
        return false;
    }

    @Override
    public void setInputValue(IRedstoneLogicElement redstoneElement, short port, boolean value) {

    }

    @Override
    public boolean getOutputValue(IRedstoneLogicElement redstoneElement, short port) {
        return false;
    }

    @Override
    public boolean addOutput(IRedstoneLogicElement redstoneElement, short port) {
        return false;
    }

    @Override
    public boolean removeOutput(IRedstoneLogicElement redstoneElement, short port) {
        return false;
    }

    @Override
    public void exec() {

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public IRedstoneElement readFomNBT(NBTTagCompound tag) {
        return null;
    }
}
