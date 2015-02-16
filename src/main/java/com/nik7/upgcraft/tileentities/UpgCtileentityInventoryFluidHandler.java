package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.network.DescriptionHandler;
import com.nik7.upgcraft.util.LogHelper;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public abstract class UpgCtileentityInventoryFluidHandler extends TileEntity implements IFluidHandler, ISidedInventory {

    protected FluidTank tank;
    //Inventory
    protected ItemStack[] itemStacks;
    protected String customName;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);

        //itemStack
        NBTTagList nbttaglist = tag.getTagList("Items", 10);
        this.itemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbtTagCompound.getByte("Slot");

            if (b0 >= 0 && b0 < this.itemStacks.length) {
                this.itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
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

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tank.writeToNBT(tag);

        //itemStack
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.itemStacks.length; ++i) {
            if (this.itemStacks[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                this.itemStacks[i].writeToNBT(nbtTagCompound);
                nbttaglist.appendTag(nbtTagCompound);
            }
        }

        tag.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName()) {
            tag.setString("CustomName", this.customName);
        }

    }

    protected void updateModBlock() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        Block block = this.worldObj.getBlock(xCoord, yCoord, zCoord);
        this.worldObj.notifyBlockChange(xCoord, yCoord, zCoord, block);
    }

    @Override
    public abstract void updateEntity();

    //Fluid method
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource != null) {
            if (this.canFill(from, resource.getFluid())) {
                updateModBlock();
                return tank.fill(resource, doFill);
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {

        if ((resource == null || !resource.isFluidEqual(tank.getFluid()))) {
            return null;
        }

        if (canDrain(from, resource.getFluid())) {
            updateModBlock();
            return tank.drain(resource.amount, doDrain);

        } else return null;

    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {

        if (canDrain(from, null)) {
            updateModBlock();
            return tank.drain(maxDrain, doDrain);
        } else return null;

    }

    @Override
    public abstract boolean canFill(ForgeDirection from, Fluid fluid);

    @Override
    public abstract boolean canDrain(ForgeDirection from, Fluid fluid);

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{tank.getInfo()};

    }

    public FluidStack getFluid() {
        return this.tank.getFluid();
    }


    //Inventory method

    @Override
    public abstract int[] getAccessibleSlotsFromSide(int side);

    @Override
    public abstract boolean canInsertItem(int slot, ItemStack item, int side);

    @Override
    public abstract boolean canExtractItem(int slot, ItemStack item, int size);

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < getSizeInventory()) {
            return itemStacks[slot];
        } else {
            LogHelper.error("Try to get Stack in slot that doesn't exist!!");
            return null;
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int maxNumber) {
        if (maxNumber == 0 || slot >= getSizeInventory()) {
            return null;
        } else if (itemStacks[slot] == null) {
            return null;
        } else {
            int number;
            ItemStack itemStack = itemStacks[slot];
            if (itemStack.stackSize >= maxNumber) {
                number = maxNumber;
            } else number = itemStack.stackSize;

            itemStacks[slot].stackSize = itemStack.stackSize - number;

            if (itemStacks[slot].stackSize <= 0) {
                itemStacks[slot] = null;
            }

            return new ItemStack(itemStack.getItem(), number, itemStack.getItemDamage());

        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {

        if (itemStacks[slot] != null) {
            ItemStack itemStack = itemStacks[slot];
            itemStacks[slot] = null;
            return itemStack;
        } else
            return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {

        this.itemStacks[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }

    }

    @Override
    public abstract String getInventoryName();

    @Override
    public abstract boolean hasCustomInventoryName();

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
    public abstract boolean isItemValidForSlot(int slot, ItemStack itemStack);
}
