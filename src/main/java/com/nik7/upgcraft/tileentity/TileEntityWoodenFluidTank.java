package com.nik7.upgcraft.tileentity;

import com.nik7.upgcraft.fluids.EnumCapacity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWoodenFluidTank extends TileEntityFluidTank {


    public TileEntityWoodenFluidTank() {
        super(EnumCapacity.BASIC_CAPACITY);
    }

    @Override
    protected boolean canMerge(TileEntity te) {
        return te != null && te instanceof TileEntityWoodenFluidTank;
    }
}
