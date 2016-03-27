package com.nik7.upgcraft.fluid;


import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface FluidWithExtendedProperties<P> {

    void createExtendedProperties(FluidStack fluidStack, P properties);

    P getExtendedProperties(FluidStack fluidStack);

    boolean hasExtendedDrain(FluidStack fluidStack);

    boolean hasToUseExtendedDrain(FluidStack fluidStack);

    boolean hasExtendedFill(FluidStack fluidStack);

    boolean hasToUseExtendedFill(FluidStack fluidStack);

    FluidStack drain(IFluidTank fluidTank, int maxDrain, boolean doDrain);

    int fill(IFluidTank fluidTank, FluidStack resource, boolean doFill);

    void writeToByteBuf(FluidStack resource, PacketBuffer buf);

    void readFromByteBuf(FluidStack resource, PacketBuffer buf);

}
