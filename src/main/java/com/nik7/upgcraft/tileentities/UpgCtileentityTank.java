package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.UpgCTank;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.TileFluidHandler;

public class UpgCtileentityTank extends TileFluidHandler {


    public UpgCTank getTank() {
        return (UpgCTank) tank;
    }

    public void setTank(FluidTank tank) {
        this.tank = tank;
    }

    private void updateModBlock() {
        Block block = this.worldObj.getBlock(xCoord, yCoord, zCoord);
        this.worldObj.notifyBlockChange(xCoord, yCoord, zCoord, block);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int result = super.fill(from, resource, doFill);
        updateModBlock();
        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        FluidStack result = super.drain(from, resource, doDrain);
        updateModBlock();
        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack result = super.drain(from, maxDrain, doDrain);
        updateModBlock();
        return result;
    }


}
