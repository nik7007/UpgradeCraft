package com.nik7.upgcraft.fluids;


import net.minecraftforge.fluids.FluidStack;

public interface IFluidIO {

    boolean canFill(FluidStack fluidStack);

    boolean canDrain(FluidStack fluidStack);
}
