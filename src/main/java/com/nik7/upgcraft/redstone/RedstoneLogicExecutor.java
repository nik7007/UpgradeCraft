package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.NBTTagHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import static com.nik7.upgcraft.util.NBTTagHelper.REDSTONE_LOGIC_ELEMENT_C;
import static com.nik7.upgcraft.util.NBTTagHelper.REDSTONE_LOGIC_ELEMENT_S;

public class RedstoneLogicExecutor implements IRedstoneLogicGeneralElement {

    private final static short REDSTONE_CONNECTION_ELEMENT = (short) (Math.max(REDSTONE_LOGIC_ELEMENT_C, REDSTONE_LOGIC_ELEMENT_S) + 1);

    private final int ELEMENTS_NUMBER;
    private final int CONNECTIONS_NUMBER;
    //private final IRedstoneElement[][] REDSTONE_ELEMENT; //[r][c]

    private final IRedstoneLogicElement[] elements;
    private final IRedstoneConnectionElement[] connections;

    private int tickToComplete = 0;

    private final RedstoneIOConnectionElement[] ioConnectionElements;
    private final short[] inputsPort;
    private final short outputPort;

    public RedstoneLogicExecutor(IRedstoneLogicElement[] elements, IRedstoneConnectionElement[] connections, RedstoneIOConnectionElement[] ioConnectionElements, short[] inputsPort, short outputPort) {

        this.elements = elements;
        this.connections = connections;

        this.ELEMENTS_NUMBER = elements.length;
        this.CONNECTIONS_NUMBER = connections.length;

        this.ioConnectionElements = ioConnectionElements;
        this.inputsPort = inputsPort;
        this.outputPort = outputPort;

        int tTC = findMinTick(elements);
        tickToComplete = tTC == Integer.MAX_VALUE ? 0 : tTC;
    }

    public RedstoneLogicExecutor(int elementsNumber, int connectionsNumber, short[] inputsPort, short outputPort) {

        this.elements = new IRedstoneLogicElement[elementsNumber];
        this.connections = new IRedstoneConnectionElement[connectionsNumber];

        this.ELEMENTS_NUMBER = elementsNumber;
        this.CONNECTIONS_NUMBER = connectionsNumber;

        this.inputsPort = inputsPort;
        this.outputPort = outputPort;
        this.ioConnectionElements = new RedstoneIOConnectionElement[inputsPort.length + 1];
    }

    public int getElementsNumber() {
        return this.ELEMENTS_NUMBER;
    }

    public int getConnectionsNumber() {
        return this.CONNECTIONS_NUMBER;
    }

    private static int findMinTick(IRedstoneLogicElement[] elements) {

        int min = Integer.MAX_VALUE;

        for (IRedstoneLogicElement e : elements) {
            if (e != null) {
                if (min < e.getTickToComplete())
                    min = e.getTickToComplete();
            }

        }

        return min;

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {


        NBTTagList redElemetsTag = new NBTTagList();
        for (IRedstoneLogicElement e : elements) {
            NBTTagCompound eTag = new NBTTagCompound();
            NBTTagHelper.writeRedstoneLogicElement(e, eTag); //e.writeToNBT(eTag);
            redElemetsTag.appendTag(eTag);
        }
        tag.setTag("elements", redElemetsTag);

        NBTTagList redConnectionsTag = new NBTTagList();
        for (IRedstoneConnectionElement c : connections) {
            NBTTagCompound cTag = new NBTTagCompound();
            c.writeToNBT(cTag);
            redConnectionsTag.appendTag(cTag);
        }
        tag.setTag("connections", redConnectionsTag);

        for (int i = 0; i < ioConnectionElements.length + 1; i++) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            ioConnectionElements[i].writeToNBT(tagCompound);
            tag.setTag("ioConnectionElements" + i, tagCompound);
        }


    }


    @Override
    public RedstoneLogicExecutor readFomNBT(NBTTagCompound tag) {

        NBTTagList redElemetsTag = tag.getTagList("elements", 10);
        for (int i = 0; i < redElemetsTag.tagCount(); i++) {
            this.elements[i] = NBTTagHelper.readRedstoneLogicElement(redElemetsTag.getCompoundTagAt(i));
        }

        NBTTagList redConnectionsTag = tag.getTagList("connections", 10);
        for (int i = 0; i < redConnectionsTag.tagCount(); i++) {
            this.connections[i] = (new RedstoneConnectionElement()).readFomNBT(redConnectionsTag.getCompoundTagAt(i));
        }


        for (int i = 0; i < ioConnectionElements.length + 1; i++) {
            NBTTagCompound tagCompound = tag.getCompoundTag("ioConnectionElements" + i);
            ioConnectionElements[i].readFomNBT(tagCompound);
        }

        return this;
    }

    @Override
    public void exec() {

        for (IRedstoneElement e : ioConnectionElements) {
            e.exec();
        }

        for (IRedstoneConnectionElement c : connections)
            c.exec();

        for (IRedstoneLogicElement e : elements)
            e.exec();


    }

    @Override
    public boolean getOutput() {
        return this.ioConnectionElements[outputPort].getOutput();
    }


    @Override
    public void setConnection(IRedstoneConnectionElement connection, short port) {
        throw new RuntimeException("You cannot change connection in this class!!");
    }

    public void setInput(boolean value, short port) {

        if (port <= inputsPort.length - 1) {
            this.ioConnectionElements[port].setInput(value, port);
        }

    }

    @Override
    public RedstoneIOConnectionElement[] getConnections() {
        return this.ioConnectionElements;
    }

    @Override
    public short[] getInputsPort() {
        return this.inputsPort;
    }

    @Override
    public short getOutputPort() {
        return this.outputPort;
    }

    @Override
    public int getTickToComplete() {
        return this.tickToComplete;
    }
}
