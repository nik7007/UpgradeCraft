package com.nik7.upgcraft.fluids.capability;


import com.nik7.upgcraft.fluids.tank.IUpgCFluidTank;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class FluidTankProperties implements IFluidTankProperties {

    private final IUpgCFluidTank fluidTank;

    public FluidTankProperties(IUpgCFluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Nullable
    @Override
    public FluidStack getContents() {
        return fluidTank.getFluid() == null ? null : fluidTank.getFluid().copy();
    }

    @Override
    public int getCapacity() {
        return fluidTank.getCapacity();
    }

    @Override
    public boolean canFill() {
        return fluidTank.canFill();
    }

    @Override
    public boolean canDrain() {
        return fluidTank.canDrain();
    }

    @Override
    public boolean canFillFluidType(FluidStack fluidStack) {
        return canFill();
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluidStack) {
        return canDrain();
    }
}
