package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.reference.Capacity;
import net.minecraft.tileentity.TileEntity;

public class UpgCtileentityWoodenTankSmall extends UpgCtileentityTank {

    public UpgCtileentityWoodenTankSmall() {
        super(Capacity.SMALL_TANK, true);
    }

    @Override
    protected boolean tileEntityInstanceOf(TileEntity tileEntity) {
        return tileEntity instanceof UpgCtileentityWoodenTankSmall;
    }
}
