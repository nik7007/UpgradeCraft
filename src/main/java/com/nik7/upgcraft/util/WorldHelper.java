package com.nik7.upgcraft.util;


import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WorldHelper {

    public static Block getBlock(IBlockAccess worldIn, BlockPos pos) {

        return worldIn.getBlockState(pos).getBlock();

    }

}