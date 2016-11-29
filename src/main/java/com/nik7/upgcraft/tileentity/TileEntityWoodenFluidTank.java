package com.nik7.upgcraft.tileentity;

import com.nik7.upgcraft.fluids.EnumCapacity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityWoodenFluidTank extends TileEntityFluidTank {

    private final static int WOOD_BURN_TEMPERATURE = 300 + 273;

    public TileEntityWoodenFluidTank() {
        super(EnumCapacity.BASIC_CAPACITY);
    }

    public boolean isFluidHot() {
        FluidStack fluid = this.getFluid();

        if (fluid == null)
            return false;
        else {
            return fluid.getFluid().getTemperature(fluid) > WOOD_BURN_TEMPERATURE;
        }
    }

    @Override
    protected boolean canMerge(TileEntity te) {
        return te != null && te instanceof TileEntityWoodenFluidTank;
    }
}
