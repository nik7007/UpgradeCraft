package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.IFluidIO;
import com.nik7.upgcraft.registry.FluidInfuserRegister;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IInteractionObject;

import javax.annotation.Nonnull;

public class TileEntityFluidInfuser extends TileEntityInventoryAndFluidHandler implements ITickable, IInteractionObject, IFluidIO {

    public static final int MELT = 0;
    public static final int INFUSE = 1;
    public static final int OUTPUT = 2;

    private ItemStack resultWorking = ItemStack.EMPTY;


    public TileEntityFluidInfuser() {
        super(EnumCapacity.MACHINE_CAPACITY, 3);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("resultWorking")) {
            this.resultWorking = new ItemStack(tag.getCompoundTag("resultWorking"));
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (this.resultWorking != ItemStack.EMPTY) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            this.resultWorking.writeToNBT(tagCompound);
            tag.setTag("resultWorking", tagCompound);

        }

        return tag;
    }


    @Override
    public void syncTileEntity() {
        markDirty();
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {

        return new int[]{MELT, INFUSE, OUTPUT};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == OUTPUT;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {

        if (index == MELT) {
            return FluidInfuserRegister.isInputCorrect(this.getFluid(), stack, this.getStackInSlot(INFUSE));
        } else if (index == INFUSE) {
            return FluidInfuserRegister.isInputCorrect(this.getFluid(), this.getStackInSlot(MELT), stack);
        }

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
    public void update() {

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
    public boolean canFill() {
        return FluidInfuserRegister.isInputCorrect(this.getFluid(), this.getStackInSlot(MELT), this.getStackInSlot(INFUSE));
    }

    @Override
    public boolean canDrain() {
        return true;
    }
}
