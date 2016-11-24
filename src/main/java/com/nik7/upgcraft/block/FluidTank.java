package com.nik7.upgcraft.block;


import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FluidTank extends BlockUpgC implements ITileEntityProvider {

    public FluidTank(Material blockMaterial, String blockName) {
        super(blockMaterial, blockName);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return null;
    }
}
