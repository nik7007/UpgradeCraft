package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Names;
import net.minecraft.block.material.Material;

public class BlockWoodenLiquidTank extends BlockUpgC {

    public BlockWoodenLiquidTank() {
        super(Material.wood);
        this.setBlockName(Names.Blocks.WOODENLIQUIDTANK);
        this.setHardness(2.5f);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f); //chest block bounds
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
