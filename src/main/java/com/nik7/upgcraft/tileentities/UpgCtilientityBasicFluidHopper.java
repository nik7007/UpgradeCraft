package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.BlockUpgCBasicFluidHopper;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.util.LogHelper;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.Random;

public class UpgCtilientityBasicFluidHopper extends UpgCtileentityTank {

    private final static int MAX_TICKS = 150;

    protected int speed = Capacity.Speed.BASIC_FLUID_HOPPER_SPEED;
    private final Random random = new Random();
    private int tick = random.nextInt(MAX_TICKS) + 1;

    public UpgCtilientityBasicFluidHopper() {
        super(Capacity.FLUID_HOPPER_TANK, false);

    }

    @Override
    public void update() {

        IBlockState blockState = worldObj.getBlockState(pos);

        if (!worldObj.isRemote) {


            EnumFacing facing = blockState.getValue(BlockUpgCBasicFluidHopper.FACING);
            boolean hasToWork = worldObj.isBlockIndirectlyGettingPowered(pos) == 0;


            if (hasToWork) {

                int lastFluidAmount = tank.getFluidAmount();
                fillFromUp();

                if (lastFluidAmount > 0)
                    autoDrain(facing);
            }


        }

        changeBlockState(blockState);


    }

    protected void changeBlockState(IBlockState blockState) {
        if (tick <= 0) {
            tick = random.nextInt(MAX_TICKS) + 1;
            boolean hasToBurn = !blockState.getValue(BlockUpgCBasicFluidHopper.BURNED) && isFluidHot();
            if (hasToBurn) {
                BlockUpgCBasicFluidHopper block = (BlockUpgCBasicFluidHopper) WorldHelper.getBlock(worldObj, pos);
                block.burnFluidHopper(worldObj, pos, blockState);
            }

        } else tick--;
    }

    private boolean isFluidHot() {

        FluidStack fluidStack = tank.getFluid();

        return fluidStack != null && fluidStack.getFluid().getTemperature(fluidStack) > 300 + 273;

    }


    private void fillFromUp() {

        BlockPos newPos = pos.up();

        if (worldObj.getTileEntity(newPos) instanceof IFluidHandler) {
            IFluidHandler fluidHandler = (IFluidHandler) worldObj.getTileEntity(newPos);
            FluidStack fluidStack;
            if ((fluidStack = fluidHandler.drain(EnumFacing.DOWN, 1, false)) != null) {

                fluidStack = new FluidStack(fluidStack, speed);

                int cacity = tank.fill(fluidStack, false);
                if (cacity > 0) {
                    fluidStack = fluidHandler.drain(EnumFacing.DOWN, cacity, true);
                    super.fill(EnumFacing.UP, fluidStack, true);

                }
            }


        }

    }

    private void autoDrain(EnumFacing direction) {

        if (getFluidAmount() > 0) {
            BlockPos newPos = pos.add(direction.getDirectionVec());
            // BlockPos newPos = new BlockPos(this.pos.getX() + direction.getFrontOffsetX(), this.pos.getY() + direction.getFrontOffsetY(), this.pos.getZ() + direction.getFrontOffsetZ());
            if (worldObj.getTileEntity(newPos) instanceof IFluidHandler) {
                IFluidHandler fluidHandler = (IFluidHandler) worldObj.getTileEntity(newPos);

                FluidStack fluidStack = new FluidStack(this.getFluid(), speed);
                int amount;
                if ((amount = fluidHandler.fill(direction.getOpposite(), fluidStack, false)) > 0) {
                    fluidStack = this.drain(direction, amount, true);
                    if (amount != fluidHandler.fill(direction.getOpposite(), fluidStack, true)) {
                        LogHelper.error("Something wrong appends: Fluid is transferring wrong!!");
                    }
                }

            }
        }

    }

    @Override
    protected boolean canMerge(TileEntity tileEntity) {
        return false;
    }

}
