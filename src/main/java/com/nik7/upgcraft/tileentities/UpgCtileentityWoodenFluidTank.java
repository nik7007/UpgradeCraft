package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import net.minecraft.tileentity.TileEntity;

public class UpgCtileentityWoodenFluidTank extends UpgCtileentityTank {

    public UpgCtileentityWoodenFluidTank() {
        super(Capacity.SMALL_TANK, true);
    }

    @Override
    protected boolean canMerge(TileEntity tileEntity) {
        return tileEntity instanceof UpgCtileentityWoodenFluidTank;
    }
}
