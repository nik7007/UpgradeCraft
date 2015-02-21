package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockUpgCFluidInfuser extends BlockUpgC implements ITileEntityProvider {

    public BlockUpgCFluidInfuser()
    {
        super(Material.iron);
        setBlockName(Names.Blocks.FLUID_INFUSE);
    }


    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new UpgCtileentityFluidInfuser();
    }
}
