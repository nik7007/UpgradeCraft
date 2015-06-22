package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.item.ItemUpgCPersonalInformation;
import com.nik7.upgcraft.network.DescriptionHandler;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.util.LogHelper;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;

public class UpgCtilientityEnderHopper extends TileEntity implements IHopper, ISidedInventory {

    private ItemStack inventory[] = new ItemStack[8]; //slot: 0 {up personal information} - slot: 1 {fuel} - slots:[2-6] {inventory} - slot: 7 {exit personal information}
    private String customName = Names.Inventory.UPGC_ENDER_HOPPER;

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbtTagCompound);
                nbttaglist.appendTag(nbtTagCompound);
            }
        }

        tag.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName()) {
            tag.setString("CustomName", this.customName);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList nbttaglist = tag.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbtTagCompound.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.length) {
                this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
            }
        }

        if (tag.hasKey("CustomName")) {
            this.customName = tag.getString("CustomName");
        }

    }

    @Override
    public Packet getDescriptionPacket() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(xCoord);
        buf.writeInt(yCoord);
        buf.writeInt(zCoord);
        writeToPacket(buf);
        return new FMLProxyPacket(buf, DescriptionHandler.CHANNEL);
    }

    public void writeToPacket(ByteBuf buf) {

    }

    public void readFromPacket(ByteBuf buf) {

    }

    void getItemStackFromTop() {

        IInventory inventory = null;

        if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) instanceof BlockEnderChest) {

            if (this.inventory[1].getItem() instanceof ItemUpgCPersonalInformation) {
                EntityPlayer player = ItemUpgCPersonalInformation.getPlayer(this.inventory[1], getWorldObj());

                if (player != null)
                    inventory = getEnderChestInventory(this.inventory[1]);

            }

        }

    }

    //TODO: ISidedInventory version

    private ItemStack putItemInInventory(IInventory inventory, ItemStack itemStack) {

        return putItemInInventory(inventory, itemStack, 0);

    }

    private ItemStack putItemInInventory(IInventory inventory, ItemStack itemStack, int start) {


        for (int i = start; itemStack != null && i < inventory.getSizeInventory(); i++) {

            if (inventory.getStackInSlot(i) == null || inventory.isItemValidForSlot(i, itemStack)) {

                int internalSize = inventory.getStackInSlot(i) == null ? 0 : inventory.getStackInSlot(i).stackSize;

                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (itemStack.stackSize + internalSize > max) {

                    inventory.setInventorySlotContents(i, new ItemStack(itemStack.getItem(), max, itemStack.getItemDamage()));
                    return putItemInInventory(inventory, new ItemStack(itemStack.getItem(), itemStack.stackSize + internalSize - max, itemStack.getItemDamage()), i + 1);
                } else {
                    inventory.setInventorySlotContents(i, new ItemStack(itemStack.getItem(), itemStack.stackSize + internalSize, itemStack.getItemDamage()));
                    return null;
                }
            }

        }

        return itemStack;
    }

    private IInventory getEnderChestInventory(ItemStack personalInformation) {

        if (!(personalInformation.getItem() instanceof ItemUpgCPersonalInformation))
            return null;
        EntityPlayer player = ItemUpgCPersonalInformation.getPlayer(personalInformation, this.getWorldObj());
        if (player == null)
            return null;
        return player.getInventoryEnderChest();

    }

    //TODO: ISidedInventory version
    private ItemStack getItemsFromInventory(ISidedInventory sidedInventory, int howMany, int side){

        if(sidedInventory == null || howMany == 0)
            return null;

        return null;
    }

    private ItemStack getItemsFromInventory(IInventory inventory, int howMany) {

        if (inventory == null || howMany == 0)
            return null;

        int inventorySize = inventory.getSizeInventory();
        int i;
        ItemStack itemStack = null;

        for (i = 0; i < inventorySize; i++)
            if ((itemStack = inventory.getStackInSlot(i)) != null) break;

        if (itemStack == null)
            return null;

        return getItemsFromInventory(inventory, howMany, i);


    }

    private ItemStack getItemsFromInventory(IInventory inventory, int howMany, int slot) {

        if (inventory == null || howMany == 0)
            return null;

        int stackSize = inventory.getInventoryStackLimit();

        ItemStack itemStack = inventory.getStackInSlot(slot);

        if (itemStack == null)
            return null;

        int slotLength = itemStack.stackSize;

        if (howMany < stackSize)
            stackSize = howMany;

        if (stackSize >= slotLength) {
            inventory.setInventorySlotContents(slot, null);
            stackSize = slotLength;
        } else
            inventory.setInventorySlotContents(slot, new ItemStack(itemStack.getItem(), itemStack.stackSize - stackSize, itemStack.getItemDamage()));

        return new ItemStack(itemStack.getItem(), stackSize, itemStack.getItemDamage());

    }


    @Override
    public double getXPos() {
        return (double) this.xCoord;
    }

    @Override
    public double getYPos() {
        return (double) this.yCoord;
    }

    @Override
    public double getZPos() {
        return (double) this.zCoord;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < getSizeInventory()) {
            return inventory[slot];
        } else {
            LogHelper.error("Try to get Stack in slot that doesn't exist!!");
            return null;
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int maxNumber) {
        if (maxNumber == 0 || slot >= getSizeInventory()) {
            return null;
        } else if (inventory[slot] == null) {
            return null;
        } else {
            int number;
            ItemStack itemStack = inventory[slot];
            if (itemStack.stackSize >= maxNumber) {
                number = maxNumber;
            } else number = itemStack.stackSize;

            inventory[slot].stackSize = itemStack.stackSize - number;

            if (inventory[slot].stackSize <= 0) {
                inventory[slot] = null;
            }

            return new ItemStack(itemStack.getItem(), number, itemStack.getItemDamage());

        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (inventory[slot] != null) {
            ItemStack itemStack = inventory[slot];
            inventory[slot] = null;
            return itemStack;
        } else
            return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {

        this.inventory[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }

    }

    @Override
    public String getInventoryName() {

        if (hasCustomInventoryName())
            return customName;
        else
            return Names.Inventory.UPGC_ENDER_HOPPER;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return this.customName != null && this.customName.length() > 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return ItemStack.areItemStacksEqual(inventory[slot], itemStack) && inventory[slot].stackSize < inventory[slot].getMaxStackSize();
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{2,3,4,5,6};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        return false;
    }
}
