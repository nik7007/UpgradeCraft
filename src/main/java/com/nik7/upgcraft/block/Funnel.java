package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentity.TileEntityFunnel;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Funnel extends BlockOrientable implements ITileEntityProvider {

    public Funnel() {
        super(Material.IRON, "Funnel", EnumFacing.DOWN);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFunnel();
    }
}
