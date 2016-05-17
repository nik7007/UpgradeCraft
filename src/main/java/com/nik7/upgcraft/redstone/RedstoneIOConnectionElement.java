package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

import static com.nik7.upgcraft.redstone.RedstoneConnectionElement.ConnectionElement;
import static com.nik7.upgcraft.redstone.RedstoneConnectionElement.ConnectionElement.addConnectionToList;
import static com.nik7.upgcraft.redstone.RedstoneConnectionElement.ConnectionElement.removeConnectionToList;

public final class RedstoneIOConnectionElement implements IRedstoneConnectionElement {

    public static final short OUTPUT_TYPE = 0;
    public static final short INPUT_TYPE = 1;

    private final List<ConnectionElement> elements = new ArrayList<>();
    private final short connectionType;

    public RedstoneIOConnectionElement(short connectionType) {
        if (connectionType == OUTPUT_TYPE || connectionType == INPUT_TYPE)
            this.connectionType = connectionType;
        else throw new RuntimeException("'" + connectionType + "' is not a valid connection type for IO");
    }

    public boolean addElement(IRedstoneLogicElement redstoneElement, short port) {
        return addConnectionToList(elements, redstoneElement, port);
    }

    public boolean removeElement(IRedstoneLogicElement redstoneElement, short port) {
        return removeConnectionToList(elements, redstoneElement, port);
    }

    public void setInput(boolean input) {
        if (connectionType == INPUT_TYPE) {
            for (ConnectionElement e : elements) {
                e.setValue(input);

            }
        } else throw new RuntimeException("This in NOT an input IO connection!");
    }

    public boolean getOutput() {
        if (connectionType == OUTPUT_TYPE) {
            boolean output = false;

            for (ConnectionElement e : elements) {
                output = e.getValue();
                if (output)
                    return output;
            }
            return output;
        } else throw new RuntimeException("This is NOT an output IO connection!!");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("elementNumber", elements.size());
        for (int i = 0; i < elements.size(); i++) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            elements.get(i).writeToNBT(tagCompound);
            tag.setTag("element" + i, tagCompound);
        }

    }

    @Override
    public RedstoneIOConnectionElement readFomNBT(NBTTagCompound tag) {
        int size = tag.getInteger("elementNumber");

        for (int i = 0; i < size; i++) {

            NBTTagCompound tagCompound = tag.getCompoundTag("element" + i);
            ConnectionElement e = new ConnectionElement();
            e.readFomNBT(tagCompound);
            elements.add(e);
            e.getRedstoneElement().setConnection(this, e.getPort());
        }

        return this;
    }

    @Override
    public boolean addInput(IRedstoneLogicElement redstoneElement, short port) {
        if (connectionType == OUTPUT_TYPE)
            throw new RuntimeException("This in NOT an input IO connection!");
        return addElement(redstoneElement, port);
    }

    @Override
    public boolean removeInput(IRedstoneLogicElement redstoneElement, short port) {
        if (connectionType == OUTPUT_TYPE)
            throw new RuntimeException("This in NOT an input IO connection!");
        return removeElement(redstoneElement, port);
    }

    @Override
    public void setInputValue(IRedstoneLogicElement redstoneElement, short port, boolean value) {
        if (connectionType == OUTPUT_TYPE)
            throw new RuntimeException("This in NOT an input IO connection!");
        setInput(value);
    }

    @Override
    public boolean getOutputValue(IRedstoneLogicElement redstoneElement, short port) {
        if (connectionType == INPUT_TYPE)
            throw new RuntimeException("This in NOT an output IO connection!");
        return getOutput();
    }

    @Override
    public boolean addOutput(IRedstoneLogicElement redstoneElement, short port) {
        if (connectionType == INPUT_TYPE)
            throw new RuntimeException("This in NOT an output IO connection!");
        return addElement(redstoneElement, port);
    }

    @Override
    public boolean removeOutput(IRedstoneLogicElement redstoneElement, short port) {
        if (connectionType == INPUT_TYPE)
            throw new RuntimeException("This in NOT an output IO connection!");
        return removeElement(redstoneElement, port);
    }

    @Override
    public void exec() {

    }
}
