package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import net.minecraft.tileentity.TileEntity;

public class UpgCtileentityClayFluidTank extends UpgCtileentityFluidTank {


    public UpgCtileentityClayFluidTank() {
        super(Capacity.SMALL_TANK, true);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    protected boolean canMerge(TileEntity tileEntity) {

        int myMeta = this.getBlockMetadata();
        int otherMeta = tileEntity.getBlockMetadata();

        boolean result = (myMeta < 2 && otherMeta < 2) || (myMeta >= 2 && otherMeta >= 2);

        return result && tileEntity instanceof UpgCtileentityClayFluidTank;
    }
}
