package com.nik7.upgcraft.block;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.tileentity.TileEntityClayFluidTank;
import com.nik7.upgcraft.tileentity.TileEntityHardenedClayFluidTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityClayFluidTank();
    }
}
