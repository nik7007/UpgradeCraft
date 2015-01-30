package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.inventory.UpgCTank;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.TileFluidHandler;

public class UpgCtileentityTank extends TileFluidHandler {

    public UpgCtileentityTank adjacentTankYPos = null;
    public UpgCtileentityTank adjacentTankYNeg = null;
    private boolean canBeDouble = false;
    private boolean isDouble = false;

    public boolean isCanBeDouble() {
        return canBeDouble;
    }

    public void setCanBeDouble(boolean canBeDouble) {
        this.canBeDouble = canBeDouble;
    }

    public UpgCTank getTank() {
        return (UpgCTank) tank;
    }

    void setTank(FluidTank tank) {
        this.tank = tank;
    }

    private void updateModBlock() {
        Block block = this.worldObj.getBlock(xCoord, yCoord, zCoord);
        this.worldObj.notifyBlockChange(xCoord, yCoord, zCoord, block);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int result = super.fill(from, resource, doFill);

        if (result > 0)
            updateModBlock();

        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        FluidStack origin = getTank().getFluid();
        FluidStack result = super.drain(from, resource, doDrain);

        if (origin != result)
            updateModBlock();

        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack origin = getTank().getFluid();
        FluidStack result = super.drain(from, maxDrain, doDrain);

        if (origin != result)
            updateModBlock();

        return result;
    }

    public void updateEntity() {

        this.adjacentTankYNeg = null;
        this.adjacentTankYPos = null;

        if (isCanBeDouble()) {

            World world = this.getWorldObj();

            TileEntity tileEntity = world.getTileEntity(xCoord, yCoord + 1, zCoord);

            if (tileEntity != null && tileEntity instanceof UpgCtileentityTank) {
                adjacentTankYPos = (UpgCtileentityTank) tileEntity;
            } else {
                tileEntity = world.getTileEntity(xCoord, yCoord - 1, zCoord);
                if (tileEntity != null && tileEntity instanceof UpgCtileentityTank) {
                    adjacentTankYNeg = (UpgCtileentityTank) tileEntity;
                }
            }


        }
        isDouble = !(adjacentTankYNeg == null && adjacentTankYPos == null);

    }

    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();

        if (canBeDouble)
            isDouble = false;

    }

    public boolean hasAdjacentTank() {
        return isDouble;
    }

    public float getCapacity() {
        return this.tank.getCapacity();
    }

    public FluidStack getFluid() {
        return this.getTank().getFluid();
    }

    public boolean isEmpty() {
        return getFluid() == null;
    }

}
