package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedList;
import java.util.List;

public class RedstoneLogicExecuter implements IRedstoneLogicGeneralElement {

    private final static short REDSTONE_LOGIC_ELEMENT_S = 0;
    private final static short REDSTONE_LOGIC_ELEMENT_C = 1;
    private final static short REDSTONE_CONNECTION_ELEMENT = 2;

    private final short ROW_DIMENSION;
    private final short COLUMN_DIMENSION;
    private final IRedstoneElement[][] REDSTONE_ELEMENT; //[r][c]
    private int tickToComplete = 0;

    public RedstoneLogicExecuter(IRedstoneElement[][] redstoneElements) {
        this.REDSTONE_ELEMENT = redstoneElements;
        this.ROW_DIMENSION = (short) redstoneElements.length;
        if (this.ROW_DIMENSION > 0)
            this.COLUMN_DIMENSION = (short) redstoneElements[0].length;
        else this.COLUMN_DIMENSION = 0;
    }

    public RedstoneLogicExecuter(short row_dimension, short column_dimension) {
        ROW_DIMENSION = row_dimension;
        COLUMN_DIMENSION = column_dimension;
        REDSTONE_ELEMENT = new IRedstoneElement[ROW_DIMENSION][COLUMN_DIMENSION];
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
                        else if (e instanceof RedstoneComplexLogicElement)
                            tagCompound.setShort("RedstoneElement", REDSTONE_LOGIC_ELEMENT_C);

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

    }


    @Override
    public RedstoneLogicExecuter readFomNBT(NBTTagCompound tag) {

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
                    else
                        e = new RedstoneComplexLogicElement(tickTocomplete);

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

    // TODO: 18/05/2016
    @Override
    public boolean getOutput() {
        return false;
    }

    // TODO: 18/05/2016
    @Override
    public void setConnection(IRedstoneConnectionElement connection, short port) {

    }

    // TODO: 18/05/2016
    @Override
    public IRedstoneConnectionElement[] getConnections() {
        return new IRedstoneConnectionElement[0];
    }

    // TODO: 18/05/2016
    @Override
    public short[] getInputsPort() {
        return new short[0];
    }

    // TODO: 18/05/2016
    @Override
    public short getOutputPort() {
        return 0;
    }

    // TODO: 18/05/2016
    @Override
    public int getTickToComplete() {
        return 0;
    }
}
