package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.INBTTagProvider;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public final class RedstoneConnectionElement implements IRedstoneConnectionElement {

    private final List<ConnectionElement> inputs = new ArrayList<>();
    private final List<ConnectionElement> outputs = new ArrayList<>();

    private static boolean addConnectionToList(List<ConnectionElement> list, IRedstoneLogicElement redstoneElement, short port) {
        ConnectionElement element = new ConnectionElement(redstoneElement, port);
        return list.add(element);
    }

    private static boolean removeConnectionToList(List<ConnectionElement> list, IRedstoneLogicElement redstoneElement, short port) {
        for (ConnectionElement e : list) {
            if (e.getRedstoneElement().getID() == redstoneElement.getID() && e.getPort() == port) {
                return list.remove(e);
            }

        }
        return false;

    }


    @Override
    public boolean addInput(IRedstoneLogicElement redstoneElement, short port) {
        return addConnectionToList(inputs, redstoneElement, port);
    }

    @Override
    public boolean removeInput(IRedstoneLogicElement redstoneElement, short port) {
        return removeConnectionToList(inputs, redstoneElement, port);
    }

    @Override
    public void setInputValue(IRedstoneLogicElement redstoneElement, short port, boolean value) {

        for (ConnectionElement e : inputs) {
            if (e.getRedstoneElement().getID() == redstoneElement.getID() && e.getPort() == port) {
                e.setValue(value);
                exec();
                return;
            }

        }
        throw new RuntimeException("Impossible to find redstoneElement ID='" + redstoneElement.getID() + "' and port='" + port + "'");
    }

    @Override
    public boolean getOutputValue(IRedstoneLogicElement redstoneElement, short port) {

        for (ConnectionElement e : outputs) {
            if (e.getRedstoneElement().getID() == redstoneElement.getID() && e.getPort() == port) {
                return e.getValue();
            }

        }
        throw new RuntimeException("Impossible to find redstoneElement ID='" + redstoneElement.getID() + "' and port='" + port + "'");
    }

    @Override
    public boolean addOutput(IRedstoneLogicElement redstoneElement, short port) {
        return addConnectionToList(outputs, redstoneElement, port);
    }

    @Override
    public boolean removeOutput(IRedstoneLogicElement redstoneElement, short port) {
        return removeConnectionToList(outputs, redstoneElement, port);
    }

    @Override
    public void exec() {

        boolean value = false;

        if (inputs.size() > 0) {
            //OR behavior
            for (ConnectionElement e : inputs) {
                value = e.getValue();
                if (value) {
                    break;
                }
            }
        }

        for (ConnectionElement cE : outputs) {
            cE.setValue(value);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        tag.setInteger("inputsNumber", inputs.size());

        for (int i = 0; i < inputs.size(); i++) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            inputs.get(i).writeToNBT(tagCompound);
            tag.setTag("InputConnectionElement" + i, tagCompound);
        }

        tag.setInteger("outpusNumber", outputs.size());
        for (int o = 0; o < outputs.size(); o++) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            outputs.get(o).writeToNBT(tagCompound);
            tag.setTag("OutputConnectionElement" + o, tagCompound);
        }
    }

    @Override
    public IRedstoneConnectionElement readFomNBT(NBTTagCompound tag) {

        int inputsNumber = tag.getInteger("inputsNumber");

        for (int i = 0; i < inputsNumber; i++) {
            NBTTagCompound tagCompound = tag.getCompoundTag("InputConnectionElement" + i);
            ConnectionElement e = new ConnectionElement();
            e.readFomNBT(tagCompound);
            inputs.add(e);
        }

        int outpusNumber = tag.getInteger("outpusNumber");
        for (int o = 0; o < outpusNumber; o++) {
            NBTTagCompound tagCompound = tag.getCompoundTag("OutputConnectionElement" + o);
            ConnectionElement e = new ConnectionElement();
            e.readFomNBT(tagCompound);
            outputs.add(e);
        }

        exec();

        return this;
    }

    private static final class ConnectionElement implements INBTTagProvider<ConnectionElement> {
        private IRedstoneLogicElement redstoneElement;
        private short port;
        private boolean value;

        public ConnectionElement(IRedstoneLogicElement redstoneElement, short port) {
            this.redstoneElement = redstoneElement;
            this.port = port;
        }

        public ConnectionElement() {
        }

        public IRedstoneLogicElement getRedstoneElement() {
            return redstoneElement;
        }

        public void setRedstoneElement(IRedstoneLogicElement redstoneElement) {
            this.redstoneElement = redstoneElement;
        }

        public short getPort() {
            return port;
        }

        public void setPort(short port) {
            this.port = port;
        }

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }


        @Override
        public void writeToNBT(NBTTagCompound tag) {

            tag.setInteger("redstoneElement", this.redstoneElement.getID());
            tag.setShort("port", this.port);
            tag.setBoolean("value", this.value);

        }

        @Override
        public ConnectionElement readFomNBT(NBTTagCompound tag) {
            int id = tag.getInteger("redstoneElement");
            this.redstoneElement = RedstoneUpgC.getRedstoneElemet(id);
            this.port = tag.getShort("port");
            this.value = tag.getBoolean("value");

            return this;
        }
    }

}
