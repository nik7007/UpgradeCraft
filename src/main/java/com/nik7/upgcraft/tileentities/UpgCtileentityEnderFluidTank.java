package com.nik7.upgcraft.tileentities;


import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

public class UpgCtileentityEnderFluidTank extends UpgCtileentityFluidTank {


    public UpgCtileentityEnderFluidTank() {
        super(0, false);
    }

    public void setFluidTank(FluidTank fluidTank) {
        this.tank = fluidTank;
    }

    @Override
    protected boolean canMerge(TileEntity tileEntity) {
        return false;
    }
}
