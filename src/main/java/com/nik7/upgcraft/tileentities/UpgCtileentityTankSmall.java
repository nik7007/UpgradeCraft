package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.tank.UpgCTank;
import com.nik7.upgcraft.reference.Capacity;
import net.minecraft.tileentity.TileEntity;

public class UpgCtileentityTankSmall extends UpgCtileentityTank {

    public UpgCtileentityTankSmall() {
        super();
        setTank(new UpgCTank(Capacity.SMALL_WOODEN_TANK));
        setCanBeDouble(true);

    }

    @Override
    protected boolean tileEntityInstanceOf(TileEntity tileEntity) {
        return tileEntity instanceof UpgCtileentityTankSmall;
    }
}
