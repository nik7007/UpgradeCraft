package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTankWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityFluidFurnace extends TileEntitySynchronizable implements ISidedInventory {

    private static final int[] SLOTS_TOP_SIDE = new int[]{0};
    private static final int[] SLOTS_BOTTOM = new int[]{1};
    protected final ItemStack inventory[]; // input : 0 - output : 1
    protected UpgCFluidTank fluidTank;
    protected UpgCFluidTankWrapper inputTank;
    protected UpgCFluidTankWrapper outputTank;
    private String customName;

    public TileEntityFluidFurnace() {
        this.fluidTank = new UpgCFluidTank(EnumCapacity.MACHINE_CAPACITY, this);
        this.inputTank = new UpgCFluidTankWrapper(this.fluidTank, false, true);
        this.outputTank = new UpgCFluidTankWrapper(this.fluidTank, true, false);
        this.inventory = new ItemStack[2];
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.fluidTank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        this.fluidTank.writeToNBT(tag);
        return tag;
    }

    @Override
    public void syncTileEntity() {

    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side != EnumFacing.DOWN)
            return SLOTS_TOP_SIDE;
        else return SLOTS_BOTTOM;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (index >= 1 || direction == EnumFacing.DOWN)
            return false;
            // TODO: check other thing!!
        else return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }


    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
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

    }

    public void setCustomInventoryName(String customName) {
        this.customName = customName;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : null;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }
}
