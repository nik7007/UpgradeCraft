package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTankWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityFluidFurnace extends TileEntitySynchronizable implements ISidedInventory, IInteractionObject {

    private static final int[] SLOTS_TOP_SIDE = new int[]{0};
    private static final int[] SLOTS_BOTTOM = new int[]{1};
    protected final NonNullList<ItemStack> inventory; // input : 0 - output : 1
    private IItemHandler inputInventory;
    private IItemHandler outputInventory;
    private UpgCFluidTank fluidTank;
    private UpgCFluidTankWrapper inputTank;
    private UpgCFluidTankWrapper outputTank;
    private String customName;

    public TileEntityFluidFurnace() {
        this.fluidTank = new UpgCFluidTank(EnumCapacity.MACHINE_CAPACITY, this);
        this.inputTank = new UpgCFluidTankWrapper(this.fluidTank, false, true);
        this.outputTank = new UpgCFluidTankWrapper(this.fluidTank, true, false);
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
        this.inputInventory = new SidedInvWrapper(this, EnumFacing.UP);
        this.outputInventory = new SidedInvWrapper(this, EnumFacing.DOWN);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.fluidTank.readFromNBT(tag);
        ItemStackHelper.loadAllItems(tag, this.inventory);

        if (tag.hasKey("CustomName", 8)) {
            this.customName = tag.getString("CustomName");
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        this.fluidTank.writeToNBT(tag);
        ItemStackHelper.saveAllItems(tag, this.inventory);
        if (this.hasCustomName()) {
            tag.setString("CustomName", this.customName);
        }
        return tag;
    }

    @Override
    public void syncTileEntity() {

    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        if (side != EnumFacing.DOWN)
            return SLOTS_TOP_SIDE;
        else return SLOTS_BOTTOM;
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, @Nonnull EnumFacing direction) {
        return !(index >= 1 || direction == EnumFacing.DOWN) && this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
        return index == 1 && direction == EnumFacing.DOWN;
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int index) {
        return this.inventory.get(index);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq((double) this.getPos().getX() + 0.5D, (double) this.getPos().getY() + 0.5D, (double) this.getPos().getZ() + 0.5D) <= 64.0D;
    }


    @Override
    public void openInventory(@Nonnull EntityPlayer player) {

    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {

        if (index >= 1)
            return false;
        else {
            ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(stack);
            return !smeltResult.isEmpty();

        }
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    public void setCustomInventoryName(String customName) {
        this.customName = customName;
    }

    @Override
    @Nullable
    public String getName() {
        return this.hasCustomName() ? this.customName : null;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }


    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public String getGuiID() {
        return null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (facing != null) {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                if (facing == EnumFacing.DOWN)
                    return (T) this.outputInventory;
                else return (T) this.inputInventory;
            if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                if (facing == EnumFacing.DOWN)
                    return (T) this.outputTank;
                else return (T) this.inputTank;
        }
        return super.getCapability(capability, facing);
    }

}
