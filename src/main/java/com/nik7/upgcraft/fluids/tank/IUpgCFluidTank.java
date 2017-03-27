package com.nik7.upgcraft.fluids.tank;


import com.nik7.upgcraft.nbt.INBTProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IUpgCFluidTank extends IFluidTank, IFluidHandler, INBTProvider<IUpgCFluidTank> {

    boolean canFill();

    boolean canDrain();

    void setFluid(FluidStack fluid);
}
