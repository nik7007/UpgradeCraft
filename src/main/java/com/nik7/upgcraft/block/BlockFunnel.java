package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentity.TileEntityFunnel;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFunnel extends BlockFluidContainer {

    public static final PropertyDirection FACING = BlockHopper.FACING;

    protected BlockFunnel(String name) {
        super(Material.IRON, name);
        this.setHardness(3f);
    }

    public BlockFunnel() {
        this("funnel");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }


    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing enumfacing = facing.getOpposite();

        if (enumfacing == EnumFacing.UP) {
            enumfacing = EnumFacing.DOWN;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing == EnumFacing.UP) {
            enumfacing = EnumFacing.DOWN;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FACING)).getIndex();
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFunnel();
    }
}
