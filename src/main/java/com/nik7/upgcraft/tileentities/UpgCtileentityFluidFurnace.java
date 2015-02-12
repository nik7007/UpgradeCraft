package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.inventory.UpgCTank;
import com.nik7.upgcraft.reference.Capacity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

public class UpgCtileentityFluidFurnace extends UpgCtileentityInventoryFluidHandler {

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    private static final int BURN_TIME = 200;

    private static final int[] input = {0};
    private static final int[] output = {1};

    private int burningTime = 0;
    private int progress = 0;

    public UpgCtileentityFluidFurnace() {
        this.tank = new UpgCTank(Capacity.INTERNAL_FLUID_TANK_TR1);
        this.itemStacks = new ItemStack[2];

    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scaleFactor) {
        return this.progress * scaleFactor / BURN_TIME;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scaleFactor) {

        return this.burningTime * scaleFactor / 20;
    }

    public boolean isBurning() {
        return this.progress > 0 && burningTime > 0;
    }


    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        this.burningTime = tag.getShort("burningTime");
        this.progress = tag.getShort("progress");

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setShort("burningTime", (short) this.burningTime);
        tag.setShort("progress", (short) this.progress);

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
        //worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
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
            return "container.fluidfurnace";
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
