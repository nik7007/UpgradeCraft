package com.nik7.upgcraft.block;


import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFluidInfuser extends BlockOrientable implements ITileEntityProvider {


    protected BlockFluidInfuser() {
        super(Material.IRON, "fluidinfuser");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
