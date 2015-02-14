package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tank.UpgCTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class UpgCtileentityFluidFurnace extends UpgCtileentityInventoryFluidHandler {

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    private static final int BURN_TIME = 200;

    private static final int[] input = {0};
    private static final int[] output = {1};

    public int burningTime = 0;
    public int progress = 0;
    public int fluidLevel = 0;
    private int capacity;

    public UpgCtileentityFluidFurnace() {
        this.tank = new UpgCTank(Capacity.INTERNAL_FLUID_TANK_TR1);
        this.itemStacks = new ItemStack[2];
        this.capacity = Capacity.INTERNAL_FLUID_TANK_TR1;

    }

    @SideOnly(Side.CLIENT)
    public int getFluidLevelScaled(int scaleFactor) {
        return scaleFactor * fluidLevel / capacity;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scaleFactor) {
        return this.progress * scaleFactor / BURN_TIME;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scaleFactor) {

        return this.burningTime * scaleFactor / 20;
    }

    //this may be useful in future
    /*public boolean isBurning() {
        return this.progress > 0 && burningTime > 0;
    }*/


    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        this.burningTime = tag.getShort("burningTime");
        this.progress = tag.getShort("progress");
        this.fluidLevel = tag.getInteger("fluidLevel");

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setShort("burningTime", (short) this.burningTime);
        tag.setShort("progress", (short) this.progress);
        tag.setInteger("fluidLevel", fluidLevel);

    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int result = super.fill(from, resource, doFill);

        this.fluidLevel += result;

        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {

        FluidStack result = super.drain(from, resource, doDrain);

        if (result != null) {
            this.fluidLevel -= result.amount;
        }

        return result;

    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack result = super.drain(from, maxDrain, doDrain);

        if (result != null) {
            this.fluidLevel -= result.amount;
        }

        return result;
    }

    private boolean canSmelt() {

        if (tank.getFluid() != null && this.itemStacks[INPUT] != null) {
            if (tank.getFluid().amount >= 1) {

                ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.itemStacks[INPUT]);

                if (itemstack == null) return false;
                if (this.itemStacks[OUTPUT] == null) return true;
                if (!this.itemStacks[OUTPUT].isItemEqual(itemstack)) return false;
                int result = itemStacks[OUTPUT].stackSize + itemstack.stackSize;
                return result <= getInventoryStackLimit() && result <= this.itemStacks[OUTPUT].getMaxStackSize();

            }

        }
        return false;
    }

    private void burning() {
        if (burningTime <= 0) {
            this.tank.drain(1, true);
            fluidLevel--;
            burningTime = 20;
        } else burningTime--;
    }

    public void smeltItem() {

        ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.itemStacks[0]);

        if (this.itemStacks[OUTPUT] == null) {
            this.itemStacks[OUTPUT] = itemstack.copy();
        } else if (this.itemStacks[OUTPUT].getItem() == itemstack.getItem()) {
            this.itemStacks[OUTPUT].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
        }

        --this.itemStacks[INPUT].stackSize;

        if (this.itemStacks[INPUT].stackSize <= 0) {
            this.itemStacks[INPUT] = null;
        }

    }

    private void updateModBlock() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        Block block = this.worldObj.getBlock(xCoord, yCoord, zCoord);
        this.worldObj.notifyBlockChange(xCoord, yCoord, zCoord, block);
    }

    @Override
    public void updateEntity() {

        boolean toUpdate = false;

        if (canSmelt()) {
            burning();
            if (!worldObj.isRemote) {

                progress++;

                if (progress == BURN_TIME) {
                    progress = 0;
                    smeltItem();
                    toUpdate = true;
                }

                if (toUpdate) {
                    updateModBlock();
                    this.markDirty();
                }

            }


        } else if (itemStacks[INPUT] == null) {
            progress = 0;
        }


    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {

        if ((from != ForgeDirection.UP || from != ForgeDirection.DOWN)) {
            if ((fluid == null)) {
                return true;
            } else if (fluid.getBlock() == Blocks.lava) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {

        return from == ForgeDirection.DOWN;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {

        if (side == 0)
            return output;
        else
            return input;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {

        return slot == INPUT && side != 0;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int size) {

        if (slot == INPUT) {
            return false;
        } else if (slot == OUTPUT && size == 0) {
            return true;
        }

        return false;
    }

    @Override
    public String getInventoryName() {
        if (hasCustomInventoryName())
            return customName;
        else
            return Names.Inventory.UPGC_FLUID_FURNACE;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return this.customName != null && this.customName.length() > 0;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return slot != OUTPUT;
    }
}
