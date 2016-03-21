package com.nik7.upgcraft.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import java.util.Random;

public class BlocFluidUpgCActiveLava extends BlocFluidUpgC {
    public BlocFluidUpgCActiveLava(Fluid fluid) {
        super(fluid, Material.lava, "ActiveLava");
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

        if (this.canFlowInto(world, pos.down())) {
            if (this.blockMaterial == Material.lava && world.getBlockState(pos.down()).getBlock().getMaterial() == Material.water) {
                world.setBlockState(pos.down(), Blocks.stone.getDefaultState());
                this.triggerMixEffects(world, pos.down());

            }
        }
        super.updateTick(world, pos, state, rand);

    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.checkForMixing(worldIn, pos, state);
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }


    public boolean checkForMixing(World worldIn, BlockPos pos, IBlockState state) {
        if (this.blockMaterial == Material.lava) {
            boolean flag = false;

            for (EnumFacing enumfacing : EnumFacing.values()) {
                if (enumfacing != EnumFacing.DOWN && worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getMaterial() == Material.water) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                Integer integer = state.getValue(LEVEL);

                if (integer == 0) {
                    worldIn.setBlockState(pos, Blocks.obsidian.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }

                if (integer <= 4) {
                    worldIn.setBlockState(pos, Blocks.cobblestone.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }
            }
        }

        return false;
    }

    protected void triggerMixEffects(World worldIn, BlockPos pos) {
        double d0 = (double) pos.getX();
        double d1 = (double) pos.getY();
        double d2 = (double) pos.getZ();
        worldIn.playSoundEffect(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D, "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

        for (int i = 0; i < 8; ++i) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D);
        }
    }
}
