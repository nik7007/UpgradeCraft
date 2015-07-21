package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.block.BlockUpgCBasicFluidHopper;
import com.nik7.upgcraft.item.ItemUpgCPersonalInformation;
import com.nik7.upgcraft.network.DescriptionHandler;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.util.CoolDownProvider;
import com.nik7.upgcraft.util.ItemHelper;
import com.nik7.upgcraft.util.LogHelper;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;

public class UpgCtilientityEnderHopper extends TileEntity implements IHopper, ISidedInventory, CoolDownProvider {

    private ItemStack inventory[] = new ItemStack[8]; //slot: 0 {up personal information} - slot: 1 {fuel} - slots:[2-6] {inventory} - slot: 7 {exit personal information}
    private String customName = Names.Inventory.UPGC_ENDER_HOPPER;
    private int transferCoolDown;

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

        tag.setInteger("TransferCooldown", this.transferCoolDown);
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

        this.transferCoolDown = tag.getInteger("TransferCooldown");

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


    @Override
    public void updateEntity() {

        if (this.worldObj != null && !this.worldObj.isRemote) {
            --this.transferCoolDown;

            if (!this.checkTransferCoolDown()) {
                this.setTransferCoolDown(0);
                this.transfer();
            }
        }

    }

    public boolean transfer() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (!this.checkTransferCoolDown() && BlockUpgCBasicFluidHopper.isNotPowered(this.getBlockMetadata())) {
                boolean flag = false;

                if (!this.isEmpty()) {
                    flag = this.pushItem();
                }

                if (!this.isFull()) {
                    flag = addItemsToHopper() || flag;
                }

                if (flag) {
                    this.setTransferCoolDown(8);
                    this.markDirty();
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private boolean addItemsToHopper() {
        IInventory inventory = getInventoryAbove();

        if (inventory != null) {
            byte side = 0;

            if (ItemHelper.inventoryIsEmpty(inventory, side)) {
                return false;
            }

            if (inventory instanceof ISidedInventory) {
                ISidedInventory isidedinventory = (ISidedInventory) inventory;
                int[] slots = isidedinventory.getAccessibleSlotsFromSide(side);

                for (int slot1 : slots) {
                    if (putItemIntoHopper(inventory, slot1, side)) {
                        return true;
                    }
                }
            } else {
                int i = inventory.getSizeInventory();

                for (int j = 0; j < i; ++j) {
                    if (putItemIntoHopper(inventory, j, side)) {
                        return true;
                    }
                }
            }
        } else {
            EntityItem entityitem = ItemHelper.getEntityItem(this.getWorldObj(), this.getXPos(), this.getYPos() + 1.0D, this.getZPos());

            if (entityitem != null) {
                return ItemHelper.putEntityItemInInventory(this, entityitem);
            }
        }

        return false;
    }

    private IInventory getInventoryAbove() {

        if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) instanceof BlockEnderChest) {

            if (this.inventory[0] != null && this.inventory[0].getItem() instanceof ItemUpgCPersonalInformation) {
                EntityPlayer player = ItemUpgCPersonalInformation.getPlayer(this.inventory[0], getWorldObj());

                if (player != null) {
                    return getEnderChestInventory(player);
                } else return null;

            } else return null;

        }

        return ItemHelper.getInventoryForCoordinates(this.getWorldObj(), this.getXPos(), this.getYPos() + 1.0D, this.getZPos());
    }

    private boolean putItemIntoHopper(IInventory inventory, int slot, int side) {
        ItemStack itemstack = inventory.getStackInSlot(slot);

        if (itemstack != null && ItemHelper.canExtractItem(inventory, itemstack, slot, side)) {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = ItemHelper.putItemInInventory(this, inventory.decrStackSize(slot, 1), 1);

            if (itemstack2 == null || itemstack2.stackSize == 0) {
                inventory.markDirty();
                return true;
            }

            inventory.setInventorySlotContents(slot, itemstack1);
        }

        return false;
    }

    private boolean pushItem() {
        IInventory inventory = this.getInventoryToPush();

        if (inventory == null) {
            return false;
        } else {
            int side = Facing.oppositeSide[BlockHopper.getDirectionFromMetadata(this.getBlockMetadata())];

            if (ItemHelper.inventoryIsFull(inventory, side)) {
                return false;
            } else {
                int[] slots = getAccessibleSlotsFromSide(0);
                for (int slot : slots) {
                    if (this.getStackInSlot(slot) != null) {
                        ItemStack itemstack = this.getStackInSlot(slot).copy();
                        ItemStack itemstack1 = ItemHelper.putItemInInventory(inventory, this.decrStackSize(slot, 1), side);

                        if (itemstack1 == null || itemstack1.stackSize == 0) {
                            inventory.markDirty();
                            return true;
                        }

                        this.setInventorySlotContents(slot, itemstack);
                    }
                }

                return false;
            }
        }
    }

    private IInventory getInventoryToPush() {
        int meta = BlockHopper.getDirectionFromMetadata(this.getBlockMetadata());

        int dX = Facing.offsetsXForSide[meta];
        int dY = Facing.offsetsYForSide[meta];
        int dZ = Facing.offsetsZForSide[meta];

        if (worldObj.getBlock(xCoord + dX, yCoord + dY, zCoord + dZ) instanceof BlockEnderChest) {

            if (this.inventory[7] != null && this.inventory[7].getItem() instanceof ItemUpgCPersonalInformation) {
                EntityPlayer player = ItemUpgCPersonalInformation.getPlayer(this.inventory[7], getWorldObj());

                if (player != null) {
                    return getEnderChestInventory(player);
                } else return null;

            } else return null;
        }

        return ItemHelper.getInventoryForCoordinates(this.getWorldObj(), (double) (this.xCoord + dX), (double) (this.yCoord + dY), (double) (this.zCoord + dZ));
    }

    private boolean isEmpty() {

        int[] slots = getAccessibleSlotsFromSide(0);

        for (int slot : slots) {
            if (this.inventory[slot] != null) {
                return false;
            }
        }

        return true;
    }

    private boolean isFull() {

        int[] slots = getAccessibleSlotsFromSide(0);

        for (int slot : slots) {
            if (this.inventory[slot] == null || this.inventory[slot].stackSize != this.inventory[slot].getMaxStackSize()) {
                return false;
            }
        }

        return true;
    }


    private IInventory getEnderChestInventory(EntityPlayer player) {
        if (player == null)
            return null;
        return player.getInventoryEnderChest();

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
    public ItemStack decrStackSize(int slot, int amount) {

        if (slot < 0 || slot >= getSizeInventory() || amount < 1)
            return null;

        if (this.inventory[slot] != null) {
            ItemStack itemstack;

            if (this.inventory[slot].stackSize <= amount) {
                itemstack = this.inventory[slot];
                this.inventory[slot] = null;
                return itemstack;
            } else {
                itemstack = this.inventory[slot].splitStack(amount);

                if (this.inventory[slot].stackSize == 0) {
                    this.inventory[slot] = null;
                }

                return itemstack;
            }
        } else {
            return null;
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

        return hasCustomInventoryName() ? customName : Names.Inventory.UPGC_ENDER_HOPPER;
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
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{2, 3, 4, 5, 6};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        return isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        return isItemValidForSlot(slot, itemStack);
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public void setTransferCoolDown(int transferCoolDown) {
        this.transferCoolDown = transferCoolDown;
    }

    public boolean checkTransferCoolDown() {
        return this.transferCoolDown > 0;
    }
}
