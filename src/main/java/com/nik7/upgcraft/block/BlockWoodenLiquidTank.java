package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockWoodenLiquidTank extends BlockUpgCTank {


    public BlockWoodenLiquidTank() {
        super(Material.wood);
        this.setBlockName(Names.Blocks.WOODENLIQUIDTANK);
        this.setHardness(2.5f);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f); //chest block bounds
        this.setStepSound(soundTypeWood);

        capacity = Capacity.SMALL_WOODEN_TANK;

    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {

        if (world.getBlock(x, y - 1, z) == this)
            if (world.getBlock(x, y - 2, z) == this)
                return false;
        if (world.getBlock(x, y + 1, z) == this)
            if (world.getBlock(x, y + 2, z) == this)
                return false;
        return !(world.getBlock(x, y + 1, z) == this && world.getBlock(x, y - 1, z) == this);

    }




}




