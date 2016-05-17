package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedList;
import java.util.List;

public class RedstoneLogicExecuter implements IRedstoneElement {

    private final static short REDSTONE_LOGIC_ELEMENT_S = 0;
    private final static short REDSTONE_LOGIC_ELEMENT_C = 1;
    private final static short REDSTONE_CONNECTION_ELEMENT = 2;

    private final short ROW_DIMENSION;
    private final short COLUMN_DIMENSION;
    private final IRedstoneElement[][] REDSTONE_ELEMENT;

    public RedstoneLogicExecuter(short row_dimension, short column_dimension) {
        ROW_DIMENSION = row_dimension;
        COLUMN_DIMENSION = column_dimension;
        REDSTONE_ELEMENT = new IRedstoneElement[ROW_DIMENSION][COLUMN_DIMENSION];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        IRedstoneElement rE;
        for (int c = 0; c < COLUMN_DIMENSION; c++)
            for (int r = 0; r < ROW_DIMENSION; r++) {
                if ((rE = REDSTONE_ELEMENT[c][r]) != null) {
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
                        tagCompound.setInteger("TickTocomplete", e.getTickTocomplete());
                        e.writeToNBT(tagCompound);

                    } else if (rE instanceof IRedstoneConnectionElement) {
                        tagCompound.setShort("RedstoneElement", REDSTONE_CONNECTION_ELEMENT);
                        rE.writeToNBT(tagCompound);

                    }
                    tag.setTag("REDSTONE_ELEMENT" + c + r, tagCompound);
                }
            }

    }


    @Override
    public RedstoneLogicExecuter readFomNBT(NBTTagCompound tag) {

        List<NBTTagCompound> connections = new LinkedList<>();

        for (int c = 0; c < COLUMN_DIMENSION; c++)
            for (int r = 0; r < ROW_DIMENSION; r++) {
                NBTTagCompound tagCompound = tag.getCompoundTag("REDSTONE_ELEMENT" + c + r);
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

                    REDSTONE_ELEMENT[c][r] = e;
                } else if (redstoneElement == REDSTONE_CONNECTION_ELEMENT)
                    connections.add(tagCompound);
            }

        for (NBTTagCompound tagCompound : connections) {
            int r = tagCompound.getInteger("row");
            int c = tagCompound.getInteger("column");

            IRedstoneConnectionElement e = new RedstoneConnectionElement();
            e.readFomNBT(tagCompound);
            REDSTONE_ELEMENT[c][r] = e;
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
}
