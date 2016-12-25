package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.block.BlockBasicFunnel;
import com.nik7.upgcraft.reference.ConfigOptions;
import net.minecraft.block.state.IBlockState;

public class TileEntityBasicFunnel extends TileEntityFunnel {

    public TileEntityBasicFunnel() {
        super(ConfigOptions.BASIC_FUNNEL_SPEED);
    }

    protected boolean canOperate(IBlockState blockState) {
        return blockState.getValue(BlockBasicFunnel.BURNED) || super.canOperate(blockState);
    }


}
