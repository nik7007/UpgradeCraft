package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedList;
import java.util.List;

public class RedstoneLogicExecutor implements IRedstoneLogicGeneralElement {

    private final static short REDSTONE_LOGIC_ELEMENT_S = 0;
    private final static short REDSTONE_LOGIC_ELEMENT_C = 1;
    private final static short REDSTONE_CONNECTION_ELEMENT = 2;

    private final short ROW_DIMENSION;
    private final short COLUMN_DIMENSION;
    private final IRedstoneElement[][] REDSTONE_ELEMENT; //[r][c]
    private int tickToComplete = 0;

    private final RedstoneIOConnectionElement[] ioConnectionElements;
    private final short[] inputsPort;
    private final short outputPort;

    public RedstoneLogicExecutor(IRedstoneElement[][] redstoneElements, RedstoneIOConnectionElement[] ioConnectionElements, short[] inputsPort, short outputPort) {
        this.REDSTONE_ELEMENT = redstoneElements;
        this.ROW_DIMENSION = (short) redstoneElements.length;
        if (this.ROW_DIMENSION > 0)
            this.COLUMN_DIMENSION = (short) redstoneElements[0].length;
        else this.COLUMN_DIMENSION = 0;

        this.ioConnectionElements = ioConnectionElements;
        this.inputsPort = inputsPort;
        this.outputPort = outputPort;

        int tTC = findMinTick(REDSTONE_ELEMENT);
        tickToComplete = tTC == Integer.MAX_VALUE ? 0 : tTC;
    }

    public RedstoneLogicExecutor(short row_dimension, short column_dimension, short[] inputsPort, short outputPort) {
        ROW_DIMENSION = row_dimension;
        COLUMN_DIMENSION = column_dimension;
        REDSTONE_ELEMENT = new IRedstoneElement[ROW_DIMENSION][COLUMN_DIMENSION];
        this.inputsPort = inputsPort;
        this.outputPort = outputPort;
        this.ioConnectionElements = new RedstoneIOConnectionElement[inputsPort.length + 1];
    }

    public short getRowDimension() {
        return this.ROW_DIMENSION;
    }

    public short getColumnDimension() {
        return this.COLUMN_DIMENSION;
    }

    private static int findMinTick(IRedstoneElement[][] redstoneElements) {

        int min = Integer.MAX_VALUE;

        for (IRedstoneElement[] eV : redstoneElements)
            for (IRedstoneElement e : eV) {
                if (e instanceof IRedstoneLogicGeneralElement) {
                    IRedstoneLogicGeneralElement gE = (IRedstoneLogicGeneralElement) e;
                    if (min < gE.getTickToComplete())
                        min = gE.getTickToComplete();
                }

            }

        return min;

    }

    private void setShortArray(NBTTagCompound tag, short[] array) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setShort("length", (short) array.length);
        for (int i = 0; i < array.length; i++) {
            tagCompound.setShort("v" + i, array[i]);
        }
        tag.setTag("shortArray", tagCompound);
    }

    private short[] getShortArray(NBTTagCompound tag) {
        NBTTagCompound tagCompound = tag.getCompoundTag("shortArray");
        short l = tagCompound.getShort("length");
        short[] array = new short[l];
        for (short i = 0; i < l; i++) {
            array[i] = tagCompound.getShort("v" + i);
        }
        return array;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        IRedstoneElement rE;
        tag.setInteger("tickToComplete", this.tickToComplete);
        for (int r = 0; r < ROW_DIMENSION; r++)
            for (int c = 0; c < COLUMN_DIMENSION; c++) {
                if ((rE = REDSTONE_ELEMENT[r][c]) != null) {
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setInteger("column", c);
                    tagCompound.setInteger("row", r);
                    if (rE instanceof IRedstoneLogicElement) {
                        IRedstoneLogicElement e = (IRedstoneLogicElement) rE;

                        if (e instanceof RedstoneSimpleLogicElement)
                            tagCompound.setShort("RedstoneElement", REDSTONE_LOGIC_ELEMENT_S);
                        else if (e instanceof RedstoneComplexLogicElement) {
                            tagCompound.setShort("RedstoneElement", REDSTONE_LOGIC_ELEMENT_C);
                            setShortArray(tagCompound, e.getInputsPort());
                            tagCompound.setShort("outputPort", e.getOutputPort());
                            tagCompound.setShort("row_dimension", ((RedstoneComplexLogicElement) e).getRowDimension());
                            tagCompound.setShort("column_dimension", ((RedstoneComplexLogicElement) e).getColumnDimension());
                        }

                        tagCompound.setString("ExpressionType", e.getExpressionType().toString());
                        tagCompound.setInteger("TickTocomplete", e.getTickToComplete());
                        e.writeToNBT(tagCompound);

                    } else if (rE instanceof IRedstoneConnectionElement) {
                        tagCompound.setShort("RedstoneElement", REDSTONE_CONNECTION_ELEMENT);
                        rE.writeToNBT(tagCompound);

                    }
                    tag.setTag("REDSTONE_ELEMENT" + r + c, tagCompound);
                }
            }

        for (int i = 0; i < ioConnectionElements.length + 1; i++) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            ioConnectionElements[i].writeToNBT(tagCompound);
            tag.setTag("ioConnectionElements" + i, tagCompound);
        }


    }


    @Override
    public RedstoneLogicExecutor readFomNBT(NBTTagCompound tag) {

        List<NBTTagCompound> connections = new LinkedList<>();
        this.tickToComplete = tag.getInteger("tickToComplete");
        for (int r = 0; r < ROW_DIMENSION; r++)
            for (int c = 0; c < COLUMN_DIMENSION; c++) {
                NBTTagCompound tagCompound = tag.getCompoundTag("REDSTONE_ELEMENT" + r + c);
                short redstoneElement = tagCompound.getShort("RedstoneElement");
                if (redstoneElement == REDSTONE_LOGIC_ELEMENT_S || redstoneElement == REDSTONE_LOGIC_ELEMENT_C) {
                    ExpressionType type = ExpressionType.getType(tagCompound.getString("ExpressionType"));
                    int tickTocomplete = tagCompound.getInteger("TickTocomplete");

                    IRedstoneLogicElement e;

                    if (redstoneElement == REDSTONE_LOGIC_ELEMENT_S)
                        e = new RedstoneSimpleLogicElement(type, tickTocomplete);
                    else {
                        short[] inputs = getShortArray(tagCompound);
                        short output = tagCompound.getShort("outputPort");

                        short rD = tagCompound.getShort("row_dimension");
                        short cD = tagCompound.getShort("column_dimension");
                        e = new RedstoneComplexLogicElement(tickTocomplete, inputs, output, rD, cD);

                    }

                    e.readFomNBT(tagCompound);

                    REDSTONE_ELEMENT[r][c] = e;
                } else if (redstoneElement == REDSTONE_CONNECTION_ELEMENT)
                    connections.add(tagCompound);
            }

        for (NBTTagCompound tagCompound : connections) {
            int r = tagCompound.getInteger("row");
            int c = tagCompound.getInteger("column");

            IRedstoneConnectionElement e = new RedstoneConnectionElement();
            e.readFomNBT(tagCompound);
            REDSTONE_ELEMENT[r][c] = e;
        }

        for (int i = 0; i < ioConnectionElements.length + 1; i++) {
            NBTTagCompound tagCompound = tag.getCompoundTag("ioConnectionElements" + i);
            ioConnectionElements[i].readFomNBT(tagCompound);
        }

        return this;
    }

    @Override
    public void exec() {
        IRedstoneElement rE;
        for (int c = 0; c < COLUMN_DIMENSION; c++)
            for (int r = 0; r < ROW_DIMENSION; r++) {
                if ((rE = REDSTONE_ELEMENT[r][c]) != null)
                    rE.exec();
            }

    }

    @Override
    public boolean getOutput() {
        return this.ioConnectionElements[outputPort].getOutput();
    }


    @Override
    public void setConnection(IRedstoneConnectionElement connection, short port) {
        throw new RuntimeException("You cannot change connection inside this class!!");
    }

    public void setInput(boolean value, short port) {

        if (port <= inputsPort.length - 1) {
            this.ioConnectionElements[port].setInput(value);
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
