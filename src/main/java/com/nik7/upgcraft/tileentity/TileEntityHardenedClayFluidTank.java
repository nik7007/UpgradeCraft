package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityHardenedClayFluidTank extends TileEntityFluidTank {

    public TileEntityHardenedClayFluidTank() {
        super(EnumCapacity.BASIC_CAPACITY);
    }

    public void setFluid(FluidStack fluid) {
        this.fluidTank.setFluid(fluid);
    }

    @Override
    protected boolean canMerge(TileEntity te) {
        return te instanceof TileEntityHardenedClayFluidTank;
    }
}
