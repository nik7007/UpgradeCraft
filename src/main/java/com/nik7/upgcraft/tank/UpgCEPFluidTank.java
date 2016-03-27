package com.nik7.upgcraft.tank;


import com.nik7.upgcraft.fluid.FluidWithExtendedProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

public class UpgCEPFluidTank extends UpgCFluidTank {

    public UpgCEPFluidTank(int capacity, TileEntity tile) {
        super(capacity, tile);
    }

    public UpgCEPFluidTank(int capacity) {
        super(capacity);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

        if (getFluid() != null && getFluid().getFluid() instanceof FluidWithExtendedProperties) {
            FluidWithExtendedProperties fluidWithEP = (FluidWithExtendedProperties) getFluid().getFluid();
            if (fluidWithEP.hasExtendedFill(getFluid()) && fluidWithEP.hasToUseExtendedFill(getFluid()))
                return fluidWithEP.fill(this, resource, doFill);
        }
        return super.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {

        if (getFluid() != null && getFluid().getFluid() instanceof FluidWithExtendedProperties) {
            FluidWithExtendedProperties fluidWithEP = (FluidWithExtendedProperties) getFluid().getFluid();

            if (fluidWithEP.hasExtendedDrain(getFluid()) && fluidWithEP.hasToUseExtendedDrain(getFluid()))
                return fluidWithEP.drain(this, maxDrain, doDrain);
        }
        return super.drain(maxDrain, doDrain);

    }
}
