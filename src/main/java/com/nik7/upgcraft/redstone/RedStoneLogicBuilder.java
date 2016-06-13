package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.item.ItemBlockRedLogicComponent;
import com.nik7.upgcraft.util.INBTTagProvider;
import com.nik7.upgcraft.util.NBTTagHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
                int roataion = 0;

                if (itemStack.hasTagCompound()) {
                    NBTTagCompound tagCompound = itemStack.getTagCompound();
                    if (tagCompound.hasKey("roataion")) {
                        roataion = tagCompound.getInteger("roataion");

                        if (roataion < 0)
                            roataion = 0;
                        else if (roataion > 3)
                            roataion %= 4;
                    }
                }

                boolean usefulElement = false;

                /*cleaning: - if the output points to nowhere remove the element - */
                switch (roataion) {
                    case 0:
                        usefulElement = getUpElement(index) != null;
                        break;
                    case 1:
                        usefulElement = getRightElement(index) != null;
                        break;
                    case 2:
                        usefulElement = getDownElement(index) != null;
                        break;
                    case 3:
                        usefulElement = getLeftElemnt(index) != null;
                        break;
                }

                if (usefulElement) {
                    TempElement tempElement = null;

                    if (redElemnt == itemUpgCANDComponent) {

                        tempElement = new TempElement().setContent(this.index, RedstoneSimpleLogicElement.AND.getNewComponent());

                    } else if (redElemnt == itemUpgCORComponent) {

                        tempElement = new TempElement().setContent(this.index, RedstoneSimpleLogicElement.OR.getNewComponent());

                    } else if (redElemnt == itemUpgCNOTComponent) {
                        tempElement = new TempElement().setContent(this.index, RedstoneSimpleLogicElement.NOT.getNewComponent());

                    } else if (redElemnt instanceof ItemBlockRedLogicComponent) {
                        ItemBlockRedLogicComponent complexElement = (ItemBlockRedLogicComponent) redElemnt;
                        tempElement = new TempElement().setContent(this.index, complexElement.getRedstoneComplexLogicElement(itemStack));
                    }

                    if (tempElement != null) {

                        tempElement.setRoataion(roataion);
                        this.tempElementMap.put(this.index, tempElement);
                    }
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
        int rotation = 0;
        IRedstoneLogicElement element;

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setInteger("index", this.i);
            NBTTagHelper.writeRedstoneLogicElement(this.element, tag);

        }

        public TempElement setContent(int i, IRedstoneLogicElement element) {

            if (element == null)
                return null;

            this.i = i;
            this.element = element;

            return this;
        }

        public TempElement setRoataion(int rotation) {
            this.rotation = rotation;
            return this;
        }

        @Override
        public TempElement readFomNBT(NBTTagCompound tag) {
            this.i = tag.getInteger("index");
            this.element = NBTTagHelper.readRedstoneLogicElement(tag);
            return this;
        }
    }

}
