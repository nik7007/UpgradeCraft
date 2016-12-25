package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentity.TileEntityBasicFunnel;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBasicFunnel extends BlockFunnel {

    public static final PropertyBool BURNED = PropertyBool.create("burned");

    public BlockBasicFunnel() {
        super("basicfunnel");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(BURNED, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, BURNED);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        int extraValue = meta >> 3;

        if (extraValue == 0)
            return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        else {
            EnumFacing enumfacing;
            if (extraValue == 1) {
                enumfacing = EnumFacing.DOWN;

            } else {
                enumfacing = placer.getHorizontalFacing().getOpposite();
            }
            return this.getDefaultState().withProperty(FACING, enumfacing);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int extraValue = meta >> 3;
        if (extraValue == 0) {
            return super.getStateFromMeta(meta);
        } else {
            int realMeta = meta ^ 8;
            return super.getStateFromMeta(realMeta);
        }

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (!state.getValue(BURNED))
            return super.getMetaFromState(state);
        else {

            int meta = super.getMetaFromState(state);
            meta |= 8;

            return meta;

        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBasicFunnel();
    }

}
