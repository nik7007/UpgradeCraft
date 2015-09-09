package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tank.UpgCActiveTank;
import net.minecraft.tileentity.TileEntity;

public class UpgCtileentityTankIron extends UpgCtileentityTank {

    public UpgCtileentityTankIron() {
        super(2 * Capacity.SMALL_TANK, UpgCActiveTank.class, true);
    }


    @Override
    protected boolean canMerge(TileEntity tileEntity) {

        return tileEntity instanceof UpgCtileentityTankIron;
    }
}
