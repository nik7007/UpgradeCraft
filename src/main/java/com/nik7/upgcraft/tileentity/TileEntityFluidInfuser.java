package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.IFluidIO;
import com.nik7.upgcraft.registry.FluidInfuserRegister;
import com.nik7.upgcraft.registry.recipes.FluidInfuserRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;

public class TileEntityFluidInfuser extends TileEntityInventoryAndFluidHandler implements ITickable, IInteractionObject, IFluidIO {

    public static final int MELT = 0;
    public static final int INFUSE = 1;
    public static final int OUTPUT = 2;

    private int tickMelting = 0;
    private int tickInfusing = 0;

    private boolean isWorking = false;

    private ItemStack resultWorking = ItemStack.EMPTY;


    private IItemHandler itemHandler = new SidedInvWrapper(this, EnumFacing.UP);


    public TileEntityFluidInfuser() {
        super(EnumCapacity.MACHINE_CAPACITY, 3);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("resultWorking")) {
            this.resultWorking = new ItemStack(tag.getCompoundTag("resultWorking"));
        }

        this.isWorking = tag.getBoolean("isWorking");

        if (this.isWorking) {

            this.tickMelting = tag.getInteger("tickMelting");
            this.tickInfusing = tag.getInteger("tickInfusing");
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

        tag.setBoolean("isWorking", this.isWorking);

        if (this.isWorking) {
            tag.setInteger("tickMelting", this.tickMelting);
            tag.setInteger("tickInfusing", this.tickInfusing);
        }

        return tag;
    }


    @Override
    public void syncTileEntity() {
        markDirty();
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    private boolean checkAndInitWorking() {
        if (!this.isWorking) {
            FluidInfuserRecipe recipe = FluidInfuserRegister.getRecipe(this.getFluid(), this.getStackInSlot(MELT), this.getStackInSlot(INFUSE));

            if (recipe != null) {

                if (this.getStackInSlot(OUTPUT).isEmpty() || this.getStackInSlot(OUTPUT).getCount() + recipe.getResult().getCount() <= this.getInventoryStackLimit()) {

                    int nMelt = recipe.getToMelt().getCount();
                    int nInfuse = recipe.getToInfuse().getCount();
                    int nFluid = recipe.getFluidStack().amount;
                    if (this.getStackInSlot(MELT).getCount() >= nMelt && this.getStackInSlot(INFUSE).getCount() >= nInfuse && this.getFluid().amount >= nFluid) {

                        this.drain(nFluid, true);
                        this.decrStackSize(MELT, nMelt);
                        this.decrStackSize(INFUSE, nInfuse);

                        this.tickMelting = recipe.getTickToMelt();
                        this.tickInfusing = recipe.getTickToInfuse();
                        this.resultWorking = recipe.getResult();

                        this.isWorking = true;

                    }

                }
            }
        }

        return this.isWorking;
    }


    @Override
    public void update() {

        if (this.checkAndInitWorking()) {
            if (this.tickMelting <= 0) {
                if (this.tickInfusing <= 0) {

                    ItemStack outPut = this.getStackInSlot(OUTPUT);

                    if (outPut.isEmpty()) {
                        this.inventory.set(OUTPUT, this.resultWorking);
                    } else outPut.grow(this.resultWorking.getCount());

                    this.resultWorking = ItemStack.EMPTY;
                    this.isWorking = false;
                    this.tickMelting = 0;
                    this.tickInfusing = 0;

                } else this.tickInfusing--;
            } else this.tickMelting--;
        }

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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) this.itemHandler;

        return super.getCapability(capability, facing);
    }
}
