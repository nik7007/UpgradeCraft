package com.nik7.upgcraft.block;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.tileentity.TileEntityFluidFurnace;
import com.nik7.upgcraft.tileentity.TileEntityFluidHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFluidFurnace extends BlockOrientable implements ITileEntityProvider {

    public static final PropertyBool IS_WORKING = PropertyBool.create("is_working");

    public BlockFluidFurnace() {
        super(Material.ROCK, "fluidfurnace");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(IS_WORKING, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, IS_WORKING);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (!worldIn.isRemote) {
            playerIn.openGui(UpgradeCraft.instance, GUIs.FLUID_FURNACE.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

        TileEntity te = world.getTileEntity(pos);
        boolean isWorking = false;

        if (te instanceof TileEntityFluidFurnace)
            isWorking = ((TileEntityFluidFurnace) te).isWorking();

        return state.withProperty(IS_WORKING, isWorking);
    }

    public void changeWorkingStatus(World world, BlockPos pos, IBlockState state, boolean workingStatus) {

        if (state.getValue(IS_WORKING) != workingStatus) {
            TileEntity te = world.getTileEntity(pos);
            world.setBlockState(pos, state.withProperty(IS_WORKING, workingStatus));

            if (te != null) {
                te.validate();
                world.setTileEntity(pos, te);
            }

        }

    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        if (stack.hasDisplayName()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityFluidFurnace) {
                ((TileEntityFluidFurnace) te).setCustomInventoryName(stack.getDisplayName());
            }
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos pos, Random rand) {

        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileEntityFluidFurnace) {
            if (((TileEntityFluidFurnace) te).isWorking()) {
                spawnParticles(world, pos, rand, EnumParticleTypes.SMOKE_NORMAL, EnumParticleTypes.FLAME);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityFluidFurnace) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFluidFurnace) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityFluidHandler)
            return ((TileEntityFluidHandler) te).getFluidLight();

        return 0;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFluidFurnace();
    }
}
