package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.item.ItemBlockRedLogicComponent;
import com.nik7.upgcraft.util.INBTTagProvider;
import com.nik7.upgcraft.util.NBTTagHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.Map;

import static com.nik7.upgcraft.init.ModItems.*;

public class RedStoneLogicBuilder implements INBTTagProvider<RedStoneLogicBuilder> {

    private ItemStack[] inventory;
    private int index = 0;
    private int phase = 0;
    private final short row;
    private final short column;
    private final int IOup, IOdow, IOleft, IOright;
    private RedstoneLogicExecutor redstoneLogicExecutor;
    private Map<Integer, TempElement> tempElementMap = new HashMap<>();

    public RedStoneLogicBuilder(short row, short column) {

        if (row % 2 != 1 || column % 2 != 1)
            throw new RuntimeException("Row and column must be odd!");

        this.row = row;
        this.column = column;

        this.IOup = (column - 1) / 2;
        this.IOdow = (row - 1) * column + this.IOup;
        this.IOleft = row / 2 * column;
        this.IOright = this.IOleft + column - 1;

        inventory = new ItemStack[row * column];
    }


    @Override
    public void writeToNBT(NBTTagCompound tag) {

        NBTTagHelper.writeInventoryToNBT(this.inventory, tag);
        tag.setInteger("index", this.index);
        tag.setInteger("phase", this.phase);
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

        if (!this.tempElementMap.isEmpty()) {
            NBTTagList tagList = new NBTTagList();
            for (Map.Entry<Integer, TempElement> entry : this.tempElementMap.entrySet()) {
                NBTTagCompound elementTag = new NBTTagCompound();
                entry.getValue().writeToNBT(elementTag);
                tagList.appendTag(elementTag);
            }

            tag.setTag("tempElementMap", tagList);

        }

    }

    @Override
    public RedStoneLogicBuilder readFomNBT(NBTTagCompound tag) {

        NBTTagHelper.readInventoryFromNBT(this.inventory, tag);
        this.index = tag.getInteger("index");
        this.phase = tag.getInteger("phase");

        if (tag.hasKey("executorTagCompound")) {
            NBTTagCompound executorTagCompound = tag.getCompoundTag("executorTagCompound");

            short rowDimension = executorTagCompound.getShort("rowDimension");
            short columnDimension = executorTagCompound.getShort("columnDimension");
            short[] inputsPort = NBTTagHelper.getShortArray(executorTagCompound);
            short outputPort = executorTagCompound.getShort("outputPort");
            RedstoneLogicExecutor redstoneLogicExecutor = new RedstoneLogicExecutor(rowDimension, columnDimension, inputsPort, outputPort);
            this.redstoneLogicExecutor = redstoneLogicExecutor.readFomNBT(executorTagCompound.getCompoundTag("executorTag"));
        }

        if (tag.hasKey("tempElementMap")) {
            NBTTagList tagList = tag.getTagList("tempElementMap", 10);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound elementTag = tagList.getCompoundTagAt(i);
                TempElement tempElement = new TempElement();
                tempElement.readFomNBT(elementTag);
                this.tempElementMap.put(tempElement.i, tempElement);
            }

        }

        return this;
    }

    public void setInventory(ItemStack[] inventory) {

        if (this.inventory.length == inventory.length) {
            System.arraycopy(inventory, 0, this.inventory, 0, this.inventory.length);
        }


    }

    /**
     * First phase: find all the logic component.
     */
    private void phase0() {
        if (this.index < this.inventory.length) {
            try {
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

                    ItemStack usefulElement = null;

                /*cleaning: - if the output points to nowhere remove the element - */
                    switch (roataion) {
                        case 0:
                            usefulElement = getUpElement(index);
                            break;
                        case 1:
                            usefulElement = getRightElement(index);
                            break;
                        case 2:
                            usefulElement = getDownElement(index);
                            break;
                        case 3:
                            usefulElement = getLeftElemnt(index);
                            break;
                    }

                    if (usefulElement != null) {
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
                            if (usefulElement.getItem() != itemUpgCWireComponent)
                                tempElement.needsExtraConnection();
                            this.tempElementMap.put(this.index, tempElement);
                        }
                    }

                }
            } finally {
                this.index++;
            }
        } else {
            this.phase++;
            this.index = 0;
        }


    }

    /**
     * Second phase: find IO connection
     */
    private void phase1() {



    }

    /**
     * Call it every tick to build the logic function starting from the Itemstacks.
     * <p>
     * return: the current phase of the process.
     */
    public int exec() {
        switch (this.phase) {
            case 0:
                phase0();
                break;
            case 1:
                phase1();
                break;
        }
        return this.phase;
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
        boolean needsExtraConnection = false;

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setInteger("index", this.i);
            NBTTagHelper.writeRedstoneLogicElement(this.element, tag);
            tag.setBoolean("needsExtraConnection", this.needsExtraConnection);

        }

        @Override
        public TempElement readFomNBT(NBTTagCompound tag) {
            this.i = tag.getInteger("index");
            this.element = NBTTagHelper.readRedstoneLogicElement(tag);
            this.needsExtraConnection = tag.getBoolean("needsExtraConnection");
            return this;
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

        public void needsExtraConnection() {
            this.needsExtraConnection = true;
        }


    }

}
