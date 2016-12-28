package com.nik7.upgcraft.fluids.tank;


import com.nik7.upgcraft.fluids.capability.FluidTankProperties;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class UpgCFluidTankWrapper implements IUpgCFluidTank {

    private final IFluidTankProperties[] tankProperties;
    private UpgCFluidTank internalTank;
    private boolean canDrain;
    private boolean canFill;


    public UpgCFluidTankWrapper(UpgCFluidTank internalTank, boolean canDrain, boolean canFill) {
        this.internalTank = internalTank;
        this.canDrain = canDrain;
        this.canFill = canFill;
        this.tankProperties = new IFluidTankProperties[]{new FluidTankProperties(this)};
    }

    @Override
    public IUpgCFluidTank readFromNBT(NBTTagCompound nbt) {
        this.internalTank.readFromNBT(nbt);
        return this;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        this.internalTank.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {

        return tankProperties;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

        if (this.canFill)
            return this.internalTank.fill(resource, doFill);
        else return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {

        if (this.canDrain)
            return this.internalTank.drain(resource, doDrain);
        else return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (this.canDrain)
            return this.internalTank.drain(maxDrain, doDrain);
        else return null;
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        return this.internalTank.getFluid();
    }

    @Override
    public int getFluidAmount() {
        return this.internalTank.getFluidAmount();
    }

    @Override
    public int getCapacity() {
        return this.internalTank.getCapacity();
    }

    @Override
    public FluidTankInfo getInfo() {
        return this.internalTank.getInfo();
    }

    @Override
    public boolean canFill() {
        return this.canFill;
    }

    @Override
    public boolean canDrain() {
        return this.canDrain;
    }
}
