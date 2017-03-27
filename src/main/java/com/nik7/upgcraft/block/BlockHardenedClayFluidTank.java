package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentity.TileEntityHardenedClayFluidTank;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockHardenedClayFluidTank extends BlockFluidTank {

    public BlockHardenedClayFluidTank() {
        super("hardenedclayfluidtank", Material.CLAY);
        this.setHardness(2.5f);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityHardenedClayFluidTank();
    }
}
