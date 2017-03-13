package com.nik7.upgcraft.block;


import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.reference.GUIs;
import com.nik7.upgcraft.tileentity.TileEntityFluidHandler;
import com.nik7.upgcraft.tileentity.TileEntityFluidInfuser;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFluidInfuser extends BlockOrientable implements ITileEntityProvider {

    public static final PropertyEnum<Status> STATUS = PropertyEnum.create("status", Status.class);


    public BlockFluidInfuser() {
        super(Material.IRON, "fluidinfuser");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(STATUS, Status.OFF));
        this.setHardness(3f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, STATUS);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileEntityFluidInfuser) {
            if (((TileEntityFluidInfuser) te).isWorking()) {
                FluidStack fluid = ((TileEntityFluidInfuser) te).getFluid();
                if (fluid != null) {
                    Status status;
                    if (fluid.getFluid().getTemperature(fluid) >= 300) {
                        status = Status.WORKING_HOT;
                    } else status = Status.WORKING_COOL;
                    return state.withProperty(STATUS, status);
                }
            } else state.withProperty(STATUS, Status.OFF);

        }
        return state;
    }

    public void changeWorkingStatus(World world, BlockPos pos, IBlockState state, boolean working, boolean isHot) {

        Status status = state.getValue(STATUS);

        if (status == Status.OFF && working) {
            if (isHot) {
                status = Status.WORKING_HOT;
            } else {
                status = Status.WORKING_COOL;
            }
            TileEntity te = world.getTileEntity(pos);
            world.setBlockState(pos, state.withProperty(STATUS, status));

            if (te != null) {
                te.validate();
                world.setTileEntity(pos, te);
            }
        } else if (status != Status.OFF && !working) {

            TileEntity te = world.getTileEntity(pos);
            world.setBlockState(pos, state.withProperty(STATUS, Status.OFF));

            if (te != null) {
                te.validate();
                world.setTileEntity(pos, te);
            }

        } else if (working && isHot && status != Status.WORKING_HOT) {


            TileEntity te = world.getTileEntity(pos);
            world.setBlockState(pos, state.withProperty(STATUS, Status.WORKING_HOT));

            if (te != null) {
                te.validate();
                world.setTileEntity(pos, te);
            }
        } else if (working && !isHot && status != Status.WORKING_COOL) {

            TileEntity te = world.getTileEntity(pos);
            world.setBlockState(pos, state.withProperty(STATUS, Status.WORKING_COOL));

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


    public enum Status implements IStringSerializable {
        OFF("off"),
        WORKING_HOT("working_hot"),
        WORKING_COOL("working_cool");

        private String name;


        Status(String name) {
            this.name = name;
        }


        @Override
        public String getName() {
            return this.name;
        }
    }

}
