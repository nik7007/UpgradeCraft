package com.nik7.upgcraft.block;


import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class SlimyLog extends BlockUpgC {

    public SlimyLog() {
        super(Material.WOOD, "slimylog");
        this.setHardness(2.0F);
        this.slipperiness = 1.05F;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 7;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 15;
    }
}
