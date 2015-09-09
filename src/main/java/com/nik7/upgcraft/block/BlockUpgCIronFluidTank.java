package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockUpgCIronFluidTank extends BlockUpgCTank {

    public BlockUpgCIronFluidTank() {
        super(Material.iron);
        this.setBlockName(Names.Blocks.IRON_LIQUID_TANK);
        this.setHardness(2.8f);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
        this.setStepSound(soundTypeStone);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {

        if (world.getBlock(x, y - 1, z) == this)
            if (world.getBlock(x, y - 2, z) == this)
                return false;
        if (world.getBlock(x, y + 1, z) == this)
            if (world.getBlock(x, y + 2, z) == this)
                return false;
        return !(world.getBlock(x, y + 1, z) == this && world.getBlock(x, y - 1, z) == this);

    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return null;
    }
}
