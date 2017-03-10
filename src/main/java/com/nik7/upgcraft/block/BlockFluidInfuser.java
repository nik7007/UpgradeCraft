package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentity.TileEntityFluidInfuser;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFluidInfuser extends BlockOrientable implements ITileEntityProvider {


    public BlockFluidInfuser() {
        super(Material.IRON, "fluidinfuser");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFluidInfuser();
    }
}
