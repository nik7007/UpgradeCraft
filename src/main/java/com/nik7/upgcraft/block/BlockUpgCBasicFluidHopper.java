package com.nik7.upgcraft.block;


import com.nik7.upgcraft.tileentities.UpgCtileentityFluidTank;
import com.nik7.upgcraft.tileentities.UpgCtilientityBasicFluidHopper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockUpgCBasicFluidHopper extends BlockUpgC implements ITileEntityProvider {

    private Random random = new Random();

    public static final PropertyBool BURNED = PropertyBool.create("burned");
    public static final PropertyBool IS_SIDE_FACING = PropertyBool.create("isfacingside");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");


    public BlockUpgCBasicFluidHopper() {
        super(Material.iron, "BasicFluidHopper");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(BURNED, false).withProperty(IS_SIDE_FACING, false));
        this.setHardness(3f);
    }

    protected BlockUpgCBasicFluidHopper(Material material, String name) {
        super(material, name);
    }

    public String getHarvestTool(IBlockState state) {
        boolean b = state.getValue(BURNED);
        boolean i = state.getValue(IS_SIDE_FACING);

        String result = super.getHarvestTool(state.withProperty(BURNED, false).withProperty(IS_SIDE_FACING, false));
        state.withProperty(BURNED, b).withProperty(IS_SIDE_FACING, i);

        return result;
    }


    protected String originalGetHarvestTool(IBlockState state) {
        return super.getHarvestTool(state);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, BURNED, IS_SIDE_FACING);
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
            TileEntity tileentity = world.getTileEntity(pos);

            EnumFacing enumfacing = state.getValue(FACING);
            IBlockState blockState = state.withProperty(BURNED, true);

            blockState = blockState.withProperty(IS_SIDE_FACING, enumfacing != EnumFacing.DOWN);
            world.setBlockState(pos, blockState);

            if (world.isRemote)
                spawnParticles(world, pos, random, EnumParticleTypes.SMOKE_LARGE);

            //world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), "fire.fire", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);

            if (tileentity != null) {
                tileentity.validate();
                world.setTileEntity(pos, tileentity);
            }

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
                enumfacing = placer.getHorizontalFacing();
            } else {
                enumfacing = EnumFacing.DOWN;
            }
        }

        if (enumfacing == EnumFacing.UP) {
            enumfacing = EnumFacing.DOWN;
        }


        return blockState.withProperty(FACING, enumfacing);
    }

    public int damageDropped(IBlockState state) {
        int meta = this.getMetaFromState(state);
        meta &= 24;
        return meta;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {

        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 8));
        list.add(new ItemStack(this, 1, 24));

    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {

        UpgCtileentityFluidTank tank = (UpgCtileentityFluidTank) worldIn.getTileEntity(pos);
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
