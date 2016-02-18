package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tank.UpgCEPFluidTank;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

public class UpgCtilientityFluidHopper extends UpgCtilientityBasicFluidHopper {

    private int tick = 0;

    public UpgCtilientityFluidHopper() {
        super(Capacity.FLUID_HOPPER_TANK, UpgCEPFluidTank.class);
        this.speed = Capacity.Speed.FLUID_HOPPER_SPEED;
    }

    @Override
    protected void calculateSpeed(IBlockState blockState) {
    }

    @Override
    protected void changeBlockState(IBlockState blockState) {
    }

    @Override
    protected boolean hasToWork(IBlockState blockState) {
        return (worldObj.isBlockIndirectlyGettingPowered(pos) == 0);
    }

    @Override
    public void update() {
        super.update();

        if (worldObj != null && !worldObj.isRemote) {

            if ((tick %= 20) == 0 && hasToWork(null)) {

                Block block = WorldHelper.getBlock(worldObj, pos.up());
                if (block != null)
                    worldBlockFluidHandler(block, pos.up());
            }
            tick++;
        }
    }

    private void worldBlockFluidHandler(Block block, BlockPos pos) {

        FluidStack fluidStack = null;

        if (block instanceof IFluidBlock) {
            IFluidBlock fluidBlock = (IFluidBlock) block;

            if (fluidBlock.canDrain(worldObj, pos)) {
                fluidStack = new FluidStack(fluidBlock.getFluid(), FluidContainerRegistry.BUCKET_VOLUME);

            }

        } else if (block instanceof BlockLiquid) {
            int meta = block.getMetaFromState(worldObj.getBlockState(pos));
            if (meta == 0) {
                String name = block.getUnlocalizedName();
                name = name.replaceAll("tile.", "");
                Fluid fluid = FluidRegistry.getFluid(name);
                if (fluid != null) {
                    fluidStack = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
                }
            }
        }

        if (fluidStack != null) {
            int amount = this.fill(EnumFacing.UP, fluidStack, false);

            if (amount == FluidContainerRegistry.BUCKET_VOLUME) {
                this.fill(EnumFacing.UP, fluidStack, true);
                worldObj.setBlockToAir(pos);
            }
        }
    }

}
