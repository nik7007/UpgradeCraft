package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.tileentities.UpgCtilientityBasicFluidHopper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockUpgCBasicFluidHopper extends BlockUpgC implements ITileEntityProvider {

    public static final PropertyBool BURNED = PropertyBool.create("burned");
    public static final PropertyBool IS_SIDE_FACING = PropertyBool.create("isFacingSide");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");


    public BlockUpgCBasicFluidHopper() {
        super(Material.iron, "BasicFluidHopper");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(BURNED, false).withProperty(IS_SIDE_FACING, false));
    }


    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, BURNED, IS_SIDE_FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getIndex();
        boolean burned = state.getValue(BURNED);
        if (burned) {
            int toOR = 8;
            boolean facingSide = state.getValue(IS_SIDE_FACING);
            if (facingSide)
                toOR = 24;
            meta |= toOR;

        }
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        int extraValue = meta >> 3;

        meta &= 7;

        IBlockState blockState = this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));

        if (extraValue != 0) {
            blockState = blockState.withProperty(BURNED, true);
            if (extraValue > 1) {
                blockState = blockState.withProperty(IS_SIDE_FACING, true);
            }
        }


        return blockState;
    }

    public void burnFluidHopper(World world, BlockPos pos, IBlockState state) {

        boolean isBurned = state.getValue(BURNED);

        if (!isBurned) {
            EnumFacing enumfacing = state.getValue(FACING);
            IBlockState blockState = state.withProperty(BURNED, true);

            blockState = blockState.withProperty(IS_SIDE_FACING, enumfacing != EnumFacing.NORTH);
            world.setBlockState(pos, blockState);

        }

    }



    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing enumfacing = facing.getOpposite();

        int extraValue = meta >> 3;

        IBlockState blockState = this.getDefaultState();

        if (extraValue != 0) {
            blockState = blockState.withProperty(BURNED, true);
            if (extraValue > 1) {
                blockState = blockState.withProperty(IS_SIDE_FACING, true);
                enumfacing = placer.getHorizontalFacing().getOpposite();
            } else {
                enumfacing = EnumFacing.DOWN;
            }
        }

        if (enumfacing == EnumFacing.UP) {
            enumfacing = EnumFacing.DOWN;
        }


        return blockState.withProperty(FACING, enumfacing);
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {

        UpgCtileentityTank tank = (UpgCtileentityTank) worldIn.getTileEntity(pos);
        int capacity = tank.getCapacity();
        int fluidAmount = tank.getFluidAmount();
        int comparator = (int) (tank.getFillPercentage() * 15.0f);

        //In this way the output is max only if is tank(s) is (are) totally full
        if (comparator == 15 && fluidAmount < capacity)
            comparator--;

        return comparator;

    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new UpgCtilientityBasicFluidHopper();
    }
}
