package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tank.UpgCTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class UpgCtileentityFluidFurnace extends UpgCtileentityInventoryFluidHandler {

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    private static final int BURN_TIME = 200;

    private static final int[] input = {INPUT};
    private static final int[] output = {OUTPUT};

    public int burningTime = 0;
    public int progress = 0;

    public boolean isActive = false;

    public UpgCtileentityFluidFurnace() {
        this.tank = new UpgCTank[]{new UpgCTank(Capacity.INTERNAL_FLUID_TANK_TR1, this)};
        this.itemStacks = new ItemStack[2];
        this.capacity = Capacity.INTERNAL_FLUID_TANK_TR1;

    }

    @SideOnly(Side.CLIENT)
    public float getFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) fluidLevel / capacity;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scaleFactor) {
        return this.progress * scaleFactor / BURN_TIME;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scaleFactor) {

        return this.burningTime * scaleFactor / 20;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        this.burningTime = tag.getShort("burningTime");
        this.progress = tag.getShort("progress");
        this.fluidLevel = tag.getInteger("fluidLevel");
        this.isActive = tag.getBoolean("active");

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setShort("burningTime", (short) this.burningTime);
        tag.setShort("progress", (short) this.progress);
        tag.setInteger("fluidLevel", fluidLevel);
        tag.setBoolean("active", isActive);
    }

    @Override
    public void writeToPacket(ByteBuf buf) {

        buf.writeInt(fluidLevel);
        buf.writeBoolean(isActive);
    }

    @Override
    public void readFromPacket(ByteBuf buf) {

        this.fluidLevel = buf.readInt();
        this.isActive = buf.readBoolean();
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);


        if (this.fluidLevel > 0) {
            this.tank[0].setFluid(new FluidStack(FluidRegistry.getFluid("lava"), fluidLevel));
        } else this.tank[0].setFluid(null);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int result = super.fill(from, resource, 0, doFill);

        if (!worldObj.isRemote) {
            this.fluidLevel = this.tank[0].getFluidAmount();
        }

        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {

        FluidStack result = super.drain(from, resource, 0, doDrain);

        if (result != null && !worldObj.isRemote) {
            this.fluidLevel = this.tank[0].getFluidAmount();
        }

        return result;

    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack result = super.drain(from, maxDrain, 0, doDrain);

        if (result != null && !worldObj.isRemote) {
            this.fluidLevel = this.tank[0].getFluidAmount();
        }

        return result;
    }

    private boolean canSmelt() {

        if (tank[0] != null && tank[0].getFluid() != null && this.itemStacks[INPUT] != null) {
            if (tank[0].getFluid().amount >= 1) {

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
            this.tank[0].drain(1, true);
            this.fluidLevel = this.tank[0].getFluidAmount();
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


    @Override
    public void updateEntity() {

        if (!worldObj.isRemote) {

            boolean toUpdate = false;

            if (canSmelt()) {
                this.isActive = true;
                this.burning();

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


            } else {
                this.isActive = false;
            }
            if (itemStacks[INPUT] == null) {
                progress = 0;
                this.isActive = false;
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
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return super.getTankInfo(from, 0);
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
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return slot != OUTPUT;
    }
}
