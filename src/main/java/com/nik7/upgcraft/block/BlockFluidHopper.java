package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.UpgCraftFluidHopper;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class BlockFluidHopper extends BlockUpgCTank {


    public BlockFluidHopper() {
        super(Material.iron);
        setBlockName(Names.Blocks.FLUID_HOPPER);
    }

    public int onBlockPlaced(World world, int x, int y, int z, int hitX, int hitY, int hitZ, int meta)
    {
        int newMeta = Facing.oppositeSide[hitX];

        if(newMeta==1)
        {
            newMeta = 0;
        }

        return newMeta;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new UpgCraftFluidHopper();
    }
}
