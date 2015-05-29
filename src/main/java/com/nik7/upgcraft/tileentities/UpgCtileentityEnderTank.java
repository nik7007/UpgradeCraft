package com.nik7.upgcraft.tileentities;

import net.minecraft.tileentity.TileEntity;

public class UpgCtileentityEnderTank extends UpgCtileentityTank {

    public UpgCtileentityEnderTank() {
        super(0, false);
    }

    @Override
    protected boolean canMerge(TileEntity tileEntity) {
        return false;
    }

}
