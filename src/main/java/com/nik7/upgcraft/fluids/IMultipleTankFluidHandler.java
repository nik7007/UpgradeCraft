package com.nik7.upgcraft.fluids;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;


public interface IMultipleTankFluidHandler extends IFluidHandler {

    int getTanksNumber();

    int fill(int tank, EnumFacing from, FluidStack resource, boolean doFill);

    FluidStack drain(int tank, EnumFacing from, FluidStack resource, boolean doDrain);

    FluidStack drain(int tank, EnumFacing from, int maxDrain, boolean doDrain);

    boolean canFill(int tank, EnumFacing from, Fluid fluid);

    boolean canDrain(int tank, EnumFacing from, Fluid fluid);

    FluidTankInfo[] getTankInfo(int tank, EnumFacing from);
}
