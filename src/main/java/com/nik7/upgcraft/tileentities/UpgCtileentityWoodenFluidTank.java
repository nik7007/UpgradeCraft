package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

public class UpgCtileentityWoodenFluidTank extends UpgCtileentityTank {

    public UpgCtileentityWoodenFluidTank() {
        super(Capacity.SMALL_TANK, true);
    }

    public boolean isFluidHot(){

        FluidStack fluidStack = tank.getFluid();

        if(fluidStack!=null)
            return fluidStack.getFluid().getTemperature(fluidStack) > 300 + 273;

        return false;

    }

    @Override
    protected boolean canMerge(TileEntity tileEntity) {
        return tileEntity instanceof UpgCtileentityWoodenFluidTank;
    }
}
