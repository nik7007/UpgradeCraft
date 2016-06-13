package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.item.ItemBlockRedLogicComponent;
import com.nik7.upgcraft.util.INBTTagProvider;
import com.nik7.upgcraft.util.NBTTagHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

import static com.nik7.upgcraft.init.ModItems.*;

public class RedStoneLogicBuilder implements INBTTagProvider<RedStoneLogicBuilder> {

    private ItemStack[] inventory;
    private int index = 0;
    private final short row;
    private final short column;
    private RedstoneLogicExecutor redstoneLogicExecutor;
    private Map<Integer, TempElement> tempElementMap = new HashMap<>();

    public RedStoneLogicBuilder(short row, short column) {

        if (row % 2 != 1 || column % 2 != 1)
            throw new RuntimeException("Row and column must be odd!");

        this.row = row;
        this.column = column;
        inventory = new ItemStack[row * column];
    }


    @Override
    public void writeToNBT(NBTTagCompound tag) {

        NBTTagHelper.writeInventoryToNBT(this.inventory, tag);
        tag.setInteger("index", this.index);
        if (this.redstoneLogicExecutor != null) {
            NBTTagCompound executorTagCompound = new NBTTagCompound();
            short rowDimension = this.redstoneLogicExecutor.getRowDimension();
            short columnDimension = this.redstoneLogicExecutor.getColumnDimension();
            short[] inputsPort = this.redstoneLogicExecutor.getInputsPort();
            short outputPort = this.redstoneLogicExecutor.getOutputPort();

            executorTagCompound.setShort("rowDimension", rowDimension);
            executorTagCompound.setShort("columnDimension", columnDimension);
            NBTTagHelper.setShortArray(executorTagCompound, inputsPort);
            executorTagCompound.setShort("outputPort", outputPort);

            NBTTagCompound executorTag = new NBTTagCompound();
            this.redstoneLogicExecutor.writeToNBT(executorTag);
            executorTagCompound.setTag("executorTag", executorTag);

            tag.setTag("executorTagCompound", executorTagCompound);


        }
    }

    @Override
    public RedStoneLogicBuilder readFomNBT(NBTTagCompound tag) {

        NBTTagHelper.readInventoryFromNBT(this.inventory, tag);
        this.index = tag.getInteger("index");

        if (tag.hasKey("executorTagCompound")) {
            NBTTagCompound executorTagCompound = tag.getCompoundTag("executorTagCompound");

            short rowDimension = executorTagCompound.getShort("rowDimension");
            short columnDimension = executorTagCompound.getShort("columnDimension");
            short[] inputsPort = NBTTagHelper.getShortArray(executorTagCompound);
            short outputPort = executorTagCompound.getShort("outputPort");
            RedstoneLogicExecutor redstoneLogicExecutor = new RedstoneLogicExecutor(rowDimension, columnDimension, inputsPort, outputPort);
            this.redstoneLogicExecutor = redstoneLogicExecutor.readFomNBT(executorTagCompound.getCompoundTag("executorTag"));
        }

        return this;
    }

    public void setInventory(ItemStack[] inventory) {

        if (this.inventory.length == inventory.length) {
            System.arraycopy(inventory, 0, this.inventory, 0, this.inventory.length);
        }


    }

    public void exec() {

        for (; this.index < this.inventory.length; this.index++) {

            ItemStack itemStack = this.inventory[index];

            if (itemStack != null) {
                Item redElemnt = itemStack.getItem();

                // TODO: 13/06/2016
                if (redElemnt == itemUpgCANDComponent) {

                } else if (redElemnt == itemUpgCORComponent) {

                } else if (redElemnt == itemUpgCNOTComponent) {

                } else if (redElemnt instanceof ItemBlockRedLogicComponent) {

                }

            }
        }

    }

    public int getIndex() {
        return this.index;
    }

    private ItemStack getUpElement(int index) {
        int i = index - column;
        if (i >= 0) {
            return this.inventory[i];
        } else return null;
    }

    private ItemStack getDownElement(int index) {
        int i = index + column;

        if (i < this.inventory.length) {
            return this.inventory[i];
        }
        return null;

    }

    private ItemStack getLeftElemnt(int index) {

        int i = index - 1;
        if (i > 0) {
            if (index % column != 0) {
                return this.inventory[i];
            }
        }
        return null;

    }

    private ItemStack getRightElement(int index) {

        int i = index + 1;
        if (i < column * row) {
            if (i % column != 0) {
                return this.inventory[i];
            }
        }
        return null;

    }

    public RedstoneLogicExecutor getRedstoneLogicExecutor() {

        if (this.redstoneLogicExecutor != null) {
            RedstoneLogicExecutor result = this.redstoneLogicExecutor;
            this.redstoneLogicExecutor = null;
            return result;

        } else return null;

    }

    private class TempElement implements INBTTagProvider<TempElement> {

        int i;
        IRedstoneLogicElement element;

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setInteger("index", this.i);
            NBTTagHelper.writeRedstoneLogicElement(this.element, tag);

        }

        @Override
        public TempElement readFomNBT(NBTTagCompound tag) {
            this.i = tag.getInteger("index");
            this.element = NBTTagHelper.readRedstoneLogicElement(tag);
            return this;
        }
    }

}
