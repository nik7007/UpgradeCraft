package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tank.UpgCEPFluidTank;
import net.minecraft.tileentity.TileEntity;

public class UpgCtileentityIronFluidTank extends UpgCtileentityFluidTank {


    public UpgCtileentityIronFluidTank() {
        super(Capacity.SMALL_TANK * 2, true, UpgCEPFluidTank.class);
    }

    @Override
    protected boolean canMerge(TileEntity tileEntity) {
        return tileEntity instanceof UpgCtileentityIronFluidTank;
    }
}
