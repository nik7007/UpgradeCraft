package com.nik7.upgcraft.util;


import com.nik7.upgcraft.redstone.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTTagHelper {

    private final static short REDSTONE_LOGIC_ELEMENT_S = 0;
    private final static short REDSTONE_LOGIC_ELEMENT_C = 1;

    public static void writeInventoryToNBT(ItemStack[] inventory, NBTTagCompound tag) {

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < inventory.length; ++i) {
            if (inventory[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(nbtTagCompound);
                nbttaglist.appendTag(nbtTagCompound);
            }
        }

        tag.setTag("Items", nbttaglist);
    }

    public static void readInventoryFromNBT(ItemStack[] inventory, NBTTagCompound tag) {

        NBTTagList nbttaglist = tag.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbtTagCompound.getByte("Slot");

            if (b0 >= 0 && b0 < inventory.length) {
                inventory[b0] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
            }
        }

    }

    public static void setShortArray(NBTTagCompound tag, short[] array) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setShort("length", (short) array.length);
        for (int i = 0; i < array.length; i++) {
            tagCompound.setShort("v" + i, array[i]);
        }
        tag.setTag("shortArray", tagCompound);
    }

    public static short[] getShortArray(NBTTagCompound tag) {
        NBTTagCompound tagCompound = tag.getCompoundTag("shortArray");
        short l = tagCompound.getShort("length");
        short[] array = new short[l];
        for (short i = 0; i < l; i++) {
            array[i] = tagCompound.getShort("v" + i);
        }
        return array;
    }

    public static void writeRedstoneLogicElement(IRedstoneLogicElement e, NBTTagCompound tag) {

        if (e instanceof RedstoneSimpleLogicElement)
            tag.setShort("RedstoneElement", REDSTONE_LOGIC_ELEMENT_S);
        else if (e instanceof RedstoneComplexLogicElement) {
            tag.setShort("RedstoneElement", REDSTONE_LOGIC_ELEMENT_C);
            NBTTagHelper.setShortArray(tag, e.getInputsPort());
            tag.setShort("outputPort", e.getOutputPort());
            tag.setInteger("elements_number", ((RedstoneComplexLogicElement) e).getElementsNumber());
            tag.setInteger("connections_number", ((RedstoneComplexLogicElement) e).getConnectionsNumber());
        }

        tag.setString("ExpressionType", e.getExpressionType().toString());
        tag.setInteger("TickTocomplete", e.getTickToComplete());
        e.writeToNBT(tag);

    }

    public static IRedstoneLogicElement readRedstoneLogicElement(NBTTagCompound tagCompound) {
        ExpressionType type = ExpressionType.getType(tagCompound.getString("ExpressionType"));
        int tickTocomplete = tagCompound.getInteger("TickTocomplete");
        short redstoneElement = tagCompound.getShort("RedstoneElement");

        IRedstoneLogicElement e;

        if (redstoneElement == REDSTONE_LOGIC_ELEMENT_S)
            e = new RedstoneSimpleLogicElement(type, tickTocomplete);
        else {
            short[] inputs = NBTTagHelper.getShortArray(tagCompound);
            short output = tagCompound.getShort("outputPort");

            int eN = tagCompound.getInteger("elements_number");
            int cN = tagCompound.getInteger("column_dimension");
            e = new RedstoneComplexLogicElement(tickTocomplete, inputs, output, eN, cN);

        }

        e.readFomNBT(tagCompound);

        return e;
    }


    public static void writeRedstoneLogicExecutor(RedstoneLogicExecutor logicExecutor, NBTTagCompound tagCompound){

        int elementsNumber = logicExecutor.getElementsNumber();
        int connectionsNumber = logicExecutor.getConnectionsNumber();
        short[] inputsPort = logicExecutor.getInputsPort();
        short outputPort = logicExecutor.getOutputPort();

        tagCompound.setInteger("elementsNumber", elementsNumber);
        tagCompound.setInteger("connectionsNumber", connectionsNumber);
        NBTTagHelper.setShortArray(tagCompound, inputsPort);
        tagCompound.setShort("outputPort", outputPort);

        NBTTagCompound executorTag = new NBTTagCompound();
        logicExecutor.writeToNBT(executorTag);
        tagCompound.setTag("executorTag", executorTag);
    }

    public static RedstoneLogicExecutor readRedstoneLogicExecutor(NBTTagCompound tagCompound){

        int elementsNumber = tagCompound.getInteger("elementsNumber");
        int connectionsNumber = tagCompound.getInteger("connectionsNumber");
        short[] inputsPort = NBTTagHelper.getShortArray(tagCompound);
        short outputPort = tagCompound.getShort("outputPort");
        RedstoneLogicExecutor redstoneLogicExecutor = new RedstoneLogicExecutor(elementsNumber, connectionsNumber, inputsPort, outputPort);
        return redstoneLogicExecutor.readFomNBT(tagCompound.getCompoundTag("executorTag"));
    }
}
