package com.nik7.upgcraft.block;


import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockClayFluidTank extends BlockFluidTank {
    public BlockClayFluidTank() {
        super("clayfluidtank", Material.CLAY);
        this.setHardness(1.5f);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
