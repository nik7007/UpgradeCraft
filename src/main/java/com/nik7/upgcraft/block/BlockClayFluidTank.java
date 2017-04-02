package com.nik7.upgcraft.block;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.tileentity.TileEntityClayFluidTank;
import com.nik7.upgcraft.tileentity.TileEntityHardenedClayFluidTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockClayFluidTank extends BlockFluidTank {
    public BlockClayFluidTank() {
        super("clayfluidtank", Material.CLAY);
        this.setHardness(1.5f);
    }

    public void cookTank(IBlockState state, World world, BlockPos pos) {

        boolean isGlassed = state.getValue(GLASSED);
        IBlockState hardenerClayTank = ModBlocks.blockHardenedClayFluidTank.getDefaultState().withProperty(GLASSED, isGlassed);

        TileEntity te = world.getTileEntity(pos);
        FluidStack fluidStack = null;
        if (te instanceof TileEntityClayFluidTank) {
            fluidStack = ((TileEntityClayFluidTank) te).getFluid();
            ((TileEntityClayFluidTank) te).separateTank();
        }

        world.setBlockState(pos, hardenerClayTank);
        TileEntityHardenedClayFluidTank fluidTank = new TileEntityHardenedClayFluidTank();

        if (fluidStack != null) {
            fluidTank.setFluid(fluidStack);
        }
        fluidTank.validate();
        world.setTileEntity(pos, fluidTank);
        fluidTank.findAdjFluidTank();

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos pos, Random rand) {

        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileEntityClayFluidTank) {
            if (((TileEntityClayFluidTank) te).isCooking()) {
                spawnParticles(world, pos, rand, EnumParticleTypes.SMOKE_NORMAL);
                world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
            }

        }

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityClayFluidTank();
    }
}
