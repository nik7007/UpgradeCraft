package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.init.ModItems;
import com.nik7.upgcraft.item.ItemBlockRedLogicComponent;
import com.nik7.upgcraft.util.INBTTagProvider;
import com.nik7.upgcraft.util.NBTTagHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

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
    private int keysMap[];

    private final RedstoneIOConnectionElement IOConnections[];
    private final boolean IOConnectionFound[];

    private final List<RedstoneConnectionElement> connections;

    public RedStoneLogicBuilder(short row, short column) {

        if (row % 2 != 1 || column % 2 != 1)
            throw new RuntimeException("Row and column must be odd!");

        this.row = row;
        this.column = column;

        this.IOup = (column - 1) / 2;
        this.IOdow = (row - 1) * column + this.IOup;
        this.IOleft = row / 2 * column;
        this.IOright = this.IOleft + column - 1;

        this.inventory = new ItemStack[row * column];

        this.IOConnections = new RedstoneIOConnectionElement[4];
        this.IOConnectionFound = new boolean[4];

        connections = new LinkedList<>();
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

        NBTTagList tagList = new NBTTagList();
        int length = this.IOConnections.length;
        boolean needToWrite = false;
        for (int i = 0; i < length; i++) {
            RedstoneIOConnectionElement io = this.IOConnections[i];
            if (io != null) {
                NBTTagCompound elementTag = new NBTTagCompound();
                NBTTagCompound content = new NBTTagCompound();
                elementTag.setInteger("index", i);
                io.writeToNBT(content);
                elementTag.setTag("content", content);
                tagList.appendTag(elementTag);
                needToWrite = true;
            }
        }
        if (needToWrite)
            tag.setTag("IOConnections", tagList);

        if (!this.connections.isEmpty()) {
            tagList = new NBTTagList();
            for (RedstoneConnectionElement c : this.connections) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                c.writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);

            }
            tag.setTag("connections", tagList);
        }

        if (this.keysMap != null && (length = this.keysMap.length) > 0) {
            tag.setIntArray("keysMap", this.keysMap);
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

        if (tag.hasKey("IOConnections")) {
            NBTTagList tagList = tag.getTagList("IOConnections", 10);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound elementTag = tagList.getCompoundTagAt(i);
                NBTTagCompound content = elementTag.getCompoundTag("content");
                int index = elementTag.getInteger("index");
                this.IOConnections[index] = new RedstoneIOConnectionElement();
                this.IOConnections[index].readFomNBT(content);
            }
        }

        if (tag.hasKey("connections")) {
            NBTTagList tagList = tag.getTagList("connections", 10);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound elementTag = tagList.getCompoundTagAt(i);
                RedstoneConnectionElement c = new RedstoneConnectionElement();
                c.readFomNBT(elementTag);
                this.connections.add(c);
            }
        }

        if (tag.hasKey("keysMap")) {
            this.keysMap = tag.getIntArray("keysMap");
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
                    int rotation = getRotation(itemStack);


                    ItemStack usefulElement = null;
                    int usefulIndex = 0;

                /*cleaning: - if the output points to nowhere remove the element - */
                    switch (rotation) {
                        case 0:
                            usefulElement = getUpElement(index);
                            usefulIndex = getUpIndex(index);
                            break;
                        case 1:
                            usefulElement = getRightElement(index);
                            usefulIndex = getRightIndex(index);
                            break;
                        case 2:
                            usefulElement = getDownElement(index);
                            usefulIndex = getDownIndex(index);
                            break;
                        case 3:
                            usefulElement = getLeftElement(index);
                            usefulIndex = getLeftIndex(index);
                            break;
                    }

                /* ... but the last element could point to nowhere */
                    if (usefulElement != null || this.index == this.IOright) {
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

                            tempElement.setRoataion(rotation);
                            if (usefulElement != null)
                                if (usefulElement.getItem() != itemUpgCWireComponent) {
                                    tempElement.needsExtraConnection();
                                    tempElement.needsConnectionTo = usefulIndex;
                                }

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

    private int getRotation(ItemStack itemStack) {
        if (itemStack != null) {
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
            return roataion;
        }
        return -1;
    }

    /**
     * Second phase: find IO connection
     */
    private void phase1() {

        if (this.index < 4) {
            try {

                int IOPosition = 0;
                ItemStack element;
                switch (this.index) {
                    case 0:
                        IOPosition = this.IOdow;
                        break;
                    case 1:
                        IOPosition = this.IOleft;
                        break;
                    case 2:
                        IOPosition = this.IOup;
                        break;
                    case 3:
                        IOPosition = this.IOright;
                        break;
                }

                element = this.inventory[IOPosition];
                if (element != null) {
                    RedstoneIOConnectionElement ioConnectionElement = new RedstoneIOConnectionElement();
                    if (element.getItem() != ModItems.itemUpgCWireComponent) {
                        int rotation = getRotation(element);
                        short port;
                        IRedstoneLogicElement e = tempElementMap.get(IOPosition).element;
                        if (this.index != 3) {
                            if ((port = getInputPort(IOPosition, rotation, e.getExpressionType())) >= 0) {

                                ioConnectionElement.addInput(tempElementMap.get(IOPosition).element, port);

                            } else ioConnectionElement = null;

                        } else {
                            if ((port = getOutPutPort(IOPosition, rotation, e.getExpressionType())) >= 0)
                                ioConnectionElement.addInput(tempElementMap.get(IOPosition).element, port);
                            else ioConnectionElement = null;
                        }

                    } else {
                        for (int i = 0; i < this.IOConnectionFound.length; i++)
                            this.IOConnectionFound[i] = false;

                        findConnection(ioConnectionElement, IOPosition);
                    }

                    for (int i = 0; i < this.IOConnectionFound.length; i++) {
                        if (this.IOConnectionFound[i])
                            this.IOConnections[i] = ioConnectionElement;
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

    private short getInputPort(int posistion, int rotation, ExpressionType type) {

        if (type == ExpressionType.NOT) {

            if (posistion == this.IOup && rotation == 2)
                return 0;
            else if (posistion == this.IOdow && rotation == 0)
                return 0;
            else if (posistion == this.IOleft && rotation == 1)
                return 0;

        } else {
            if (posistion == this.IOup) {
                if (rotation != 0) {
                    return (short) Math.abs(3 - rotation);
                }
            } else if (posistion == this.IOdow) {
                if (rotation != 2) {
                    return (short) Math.abs(1 - rotation);
                }

            } else if (posistion == this.IOleft) {
                if (rotation != 3) {
                    return (short) Math.abs(2 - rotation);
                }
            }

        }

        return -1;
    }

    private short getOutPutPort(int posistion, int rotation, ExpressionType type) {

        if (posistion == this.IOright && rotation == 1)
            if (type == ExpressionType.NOT) {
                return 1;
            } else {
                return 3;
            }

        return -1;
    }

    private void findConnection(IRedstoneConnectionElement connection, int start) {

        if (this.inventory[start] != null) {
            if (start == this.IOdow)
                this.IOConnectionFound[0] = true;
            else if (start == this.IOleft)
                this.IOConnectionFound[1] = true;
            else if (start == this.IOup)
                this.IOConnectionFound[2] = true;
            else if (start == this.IOright)
                this.IOConnectionFound[3] = true;
        }

        this.inventory[start] = null;

        ItemStack elemet = getUpElement(start);
        if (elemet != null) {
            int i = getUpIndex(start);
            if (elemet.getItem() == ModItems.itemUpgCWireComponent)
                findConnection(connection, i);
            else {
                int rotation = getRotation(elemet);
                connectToUp(connection, elemet, i, rotation);
            }
        }

        elemet = getRightElement(start);
        if (elemet != null) {
            int i = getRightIndex(start);
            if (elemet.getItem() == ModItems.itemUpgCWireComponent)
                findConnection(connection, i);
            else {
                int rotation = getRotation(elemet);
                connectToRight(connection, elemet, i, rotation);
            }
        }

        elemet = getDownElement(start);
        if (elemet != null) {
            int i = getDownIndex(start);
            if (elemet.getItem() == ModItems.itemUpgCWireComponent)
                findConnection(connection, i);
            else {
                int rotation = getRotation(elemet);
                connectToDown(connection, elemet, i, rotation);
            }
        }

        elemet = getLeftElement(start);
        if (elemet != null) {
            int i = getLeftIndex(start);
            if (elemet.getItem() == ModItems.itemUpgCWireComponent)
                findConnection(connection, i);
            else {
                int rotation = getRotation(elemet);
                connectToLeft(connection, elemet, i, rotation);
            }
        }

    }

    private void connectToUp(IRedstoneConnectionElement connection, ItemStack element, int elementIndex, int elementRotation) {

        if (element.getItem() == ModItems.itemUpgCNOTComponent) {
            if (elementRotation == 0) {
                connection.addInput(tempElementMap.get(elementIndex).element, (short) 0);
            } else if (elementRotation == 2)
                connection.addOutput(tempElementMap.get(elementIndex).element, (short) 1);
        } else {
            if (elementRotation != 2) {
                connection.addInput(tempElementMap.get(elementIndex).element, (short) Math.abs(1 - elementRotation));
            } else connection.addOutput(tempElementMap.get(elementIndex).element, (short) 3);
        }
    }

    private void connectToRight(IRedstoneConnectionElement connection, ItemStack element, int elementIndex, int elementRotation) {

        if (element.getItem() == ModItems.itemUpgCNOTComponent) {
            if (elementRotation == 1)
                connection.addInput(tempElementMap.get(elementIndex).element, (short) 0);
            else if (elementRotation == 3)
                connection.addOutput(tempElementMap.get(elementIndex).element, (short) 1);
        } else {

            if (elementRotation != 3) {
                connection.addInput(tempElementMap.get(elementIndex).element, (short) Math.abs(2 - elementRotation));
            } else connection.addOutput(tempElementMap.get(elementIndex).element, (short) 3);
        }
    }

    private void connectToDown(IRedstoneConnectionElement connection, ItemStack element, int elementIndex, int elementRotation) {

        if (element.getItem() == ModItems.itemUpgCNOTComponent) {
            if (elementRotation == 2)
                connection.addInput(tempElementMap.get(elementIndex).element, (short) 0);
            else if (elementRotation == 0)
                connection.addOutput(tempElementMap.get(elementIndex).element, (short) 1);
        } else {

            if (elementRotation != 0) {
                connection.addInput(tempElementMap.get(elementIndex).element, (short) Math.abs(3 - elementRotation));
            } else connection.addOutput(tempElementMap.get(elementIndex).element, (short) 3);
        }
    }

    private void connectToLeft(IRedstoneConnectionElement connection, ItemStack element, int elementIndex, int elementRotation) {

        if (element.getItem() == ModItems.itemUpgCNOTComponent) {
            if (elementRotation == 3)
                connection.addInput(tempElementMap.get(elementIndex).element, (short) 0);
            else if (elementRotation == 1)
                connection.addOutput(tempElementMap.get(elementIndex).element, (short) 1);

        } else {
            if (elementRotation != 1) {
                connection.addInput(tempElementMap.get(elementIndex).element, (short) (Math.abs(4 - elementRotation) % 4));

            } else connection.addOutput(tempElementMap.get(elementIndex).element, (short) 3);
        }
    }

    /**
     * Third phase: find all the extra connection
     */
    private void phase2() {

        if (this.index < this.tempElementMap.size()) {

            try {
                if (this.index == 0) {
                    this.keysMap = new int[this.tempElementMap.size()];
                    int i = 0;
                    for (Map.Entry<Integer, TempElement> entry : this.tempElementMap.entrySet()) {
                        this.keysMap[i] = entry.getKey();
                        i++;
                    }
                }

                TempElement tmpElement = this.tempElementMap.get(this.keysMap[this.index]);
                if (tmpElement.needsExtraConnection) {
                    RedstoneConnectionElement connectionElement = new RedstoneConnectionElement();
                    connectionElement.addOutput(tmpElement.element, (short) 3);

                    int elementIndex = tmpElement.i;
                    int connectIndex = tmpElement.needsConnectionTo;
                    int rotation = getRotation(this.inventory[connectIndex]);

                    if (connectIndex < elementIndex) {

                        if (connectIndex == getUpIndex(elementIndex)) {
                            connectToUp(connectionElement, this.inventory[connectIndex], connectIndex, rotation);

                        } else {
                            connectToLeft(connectionElement, this.inventory[connectIndex], connectIndex, rotation);
                        }

                    } else {

                        if (connectIndex == getRightIndex(elementIndex)) {
                            connectToRight(connectionElement, this.inventory[connectIndex], connectIndex, rotation);

                        } else {
                            connectToDown(connectionElement, this.inventory[connectIndex], connectIndex, rotation);
                        }
                    }
                    this.connections.add(connectionElement);
                }

            } finally {
                this.index++;
            }


        } else {
            this.phase++;
            this.index = 0;
            this.keysMap = null;
        }

    }

    /**
     * Fourth phase: find all the last connections
     */
    private void phase3() {
        if (this.index < this.inventory.length) {

            try {

                ItemStack element = this.inventory[this.index];

                if (element != null && element.getItem() == ModItems.itemUpgCWireComponent) {
                    RedstoneConnectionElement connection = new RedstoneConnectionElement();
                    findConnection(connection, this.index);
                    this.connections.add(connection);
                }


            } finally {
                this.index++;
            }


        } else {
            this.phase++;
            this.index = 0;
            this.inventory = null;
        }

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
            case 2:
                phase2();
                break;
            case 3:
                phase3();
                break;
        }
        return this.phase;
    }

    public int getIndex() {
        return this.index;
    }

    private int getUpIndex(int index) {

        int i = index - column;
        if (i >= 0)
            return i;
        return -1;
    }

    private ItemStack getUpElement(int index) {
        int i = getUpIndex(index);
        if (i >= 0) {
            return this.inventory[i];
        } else return null;
    }

    private int getDownIndex(int index) {
        int i = index + column;

        if (i < this.inventory.length)
            return i;
        return -1;
    }

    private ItemStack getDownElement(int index) {
        int i = getDownIndex(index);

        if (i >= 0 && i < this.inventory.length) {
            return this.inventory[i];
        }
        return null;

    }

    private int getLeftIndex(int index) {

        int i = index - 1;
        if (i > 0) {
            if (index % column != 0)
                return i;
        }

        return -1;
    }

    private ItemStack getLeftElement(int index) {

        int i = index - 1;
        if (i > 0) {
            if (index % column != 0) {
                return this.inventory[i];
            }
        }
        return null;

    }

    private int getRightIndex(int index) {

        int i = index + 1;
        if (i < column * row) {
            if (i % column != 0) {
                return i;
            }
        }
        return -1;
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
        int needsConnectionTo;

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setInteger("index", this.i);
            NBTTagHelper.writeRedstoneLogicElement(this.element, tag);
            tag.setBoolean("needsExtraConnection", this.needsExtraConnection);
            if (needsExtraConnection)
                tag.setInteger("needsConnectionTo", this.needsConnectionTo);

        }

        @Override
        public TempElement readFomNBT(NBTTagCompound tag) {
            this.i = tag.getInteger("index");
            this.element = NBTTagHelper.readRedstoneLogicElement(tag);
            this.needsExtraConnection = tag.getBoolean("needsExtraConnection");
            if (tag.hasKey("needsConnectionTo"))
                this.needsConnectionTo = tag.getInteger("needsConnectionTo");
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
