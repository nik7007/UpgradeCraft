package com.nik7.upgcraft.block;

import com.nik7.upgcraft.tileentity.TileEntityFluidHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockFluidContainer extends BlockUpgC implements ITileEntityProvider {

    protected BlockFluidContainer(Material blockMaterial, String blockName) {
        super(blockMaterial, blockName);
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityFluidHandler) {
            TileEntityFluidHandler fluidTank = (TileEntityFluidHandler) te;
            float percentage = fluidTank.getFillPercentage();

            int result = (int) (percentage * 15f);

            if (percentage == 1)
                result = 15;
            else if (result == 15)
                result = 14;
            if (result == 0 && percentage > 0)
                result = 1;

            return result;

        }
        return 0;
    }
}
