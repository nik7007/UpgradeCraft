package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentity.TileEntityWoodenFluidTank;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WoodenFluidTank extends FluidTank {


    public WoodenFluidTank() {
        super("woodenfluidtank");
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityWoodenFluidTank();
    }
}
