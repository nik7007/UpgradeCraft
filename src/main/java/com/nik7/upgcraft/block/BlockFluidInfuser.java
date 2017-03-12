package com.nik7.upgcraft.block;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.tileentity.TileEntityFluidHandler;
import com.nik7.upgcraft.tileentity.TileEntityFluidInfuser;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFluidInfuser extends BlockOrientable implements ITileEntityProvider {


    public BlockFluidInfuser() {
        super(Material.IRON, "fluidinfuser");
        this.setHardness(3f);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (!worldIn.isRemote) {
            playerIn.openGui(UpgradeCraft.instance, GUIs.FLUID_INFUSER.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        if (stack.hasDisplayName()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityFluidInfuser) {
                ((TileEntityFluidInfuser) te).setCustomInventoryName(stack.getDisplayName());
            }
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityFluidInfuser) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFluidInfuser) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos pos, Random rand) {

        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileEntityFluidInfuser) {
            if (((TileEntityFluidInfuser) te).isWorking()) {
                final FluidStack fluid = ((TileEntityFluidInfuser) te).getFluid();
                if (fluid != null) {
                    EnumParticleTypes[] particles;

                    if (fluid.getFluid().getTemperature(fluid) >= 300)
                        particles = new EnumParticleTypes[]{EnumParticleTypes.SMOKE_NORMAL, EnumParticleTypes.FLAME};
                    else particles = new EnumParticleTypes[]{EnumParticleTypes.SMOKE_NORMAL};
                    spawnParticles(world, pos, rand, particles);
                }
            }
        }
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
        return new TileEntityFluidInfuser();
    }
}
