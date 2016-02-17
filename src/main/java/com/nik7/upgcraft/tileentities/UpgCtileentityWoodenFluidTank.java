package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import net.minecraft.tileentity.TileEntity;

public class UpgCtileentityWoodenFluidTank extends UpgCtileentityFluidTank {

    public UpgCtileentityWoodenFluidTank() {
        super(Capacity.SMALL_TANK, true);
    }

    public boolean isFluidHot() {
        return ((UpgCFluidTank) tank).isFluidHot();
    }

    @Override
    protected boolean canMerge(TileEntity tileEntity) {
        return tileEntity instanceof UpgCtileentityWoodenFluidTank;
    }
}
