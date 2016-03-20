package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.BlockUpgCBasicFluidHopper;
import com.nik7.upgcraft.config.SystemConfig;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import com.nik7.upgcraft.util.LogHelper;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.Random;

public class UpgCtilientityBasicFluidHopper extends UpgCtileentityFluidTank {

    private final static int MAX_TICKS = 150;

    protected int speed = Capacity.Speed.BASIC_FLUID_HOPPER_SPEED;
    private final Random random = new Random();
    private int tick = random.nextInt(MAX_TICKS) + 1;

    public UpgCtilientityBasicFluidHopper() {
        super(Capacity.FLUID_HOPPER_TANK, false);

    }

    protected UpgCtilientityBasicFluidHopper(int capacity, Class<? extends UpgCFluidTank> TankClass) {
        super(capacity, false, TankClass);
    }

    @Override
    public void update() {

        IBlockState blockState = worldObj.getBlockState(pos);

        if (!worldObj.isRemote) {
            calculateSpeed(blockState);
            EnumFacing facing = blockState.getValue(BlockUpgCBasicFluidHopper.FACING);
            if (hasToWork(blockState)) {

                int lastFluidAmount = tank.getFluidAmount();
                fillFromUp();

                if (lastFluidAmount > 0)
                    autoDrain(facing);
            }


        }

        changeBlockState(blockState);


    }

    protected void calculateSpeed(IBlockState blockState) {
        boolean burned = blockState.getValue(BlockUpgCBasicFluidHopper.BURNED);
        if (burned && speed == Capacity.Speed.BASIC_FLUID_HOPPER_SPEED)
            speed += speed / 10;
        else if (!burned) speed = Capacity.Speed.BASIC_FLUID_HOPPER_SPEED;
    }

    protected boolean hasToWork(IBlockState blockState) {
        boolean isBurned = blockState.getValue(BlockUpgCBasicFluidHopper.BURNED);
        return (worldObj.isBlockIndirectlyGettingPowered(pos) == 0) || isBurned;
    }

    protected void changeBlockState(IBlockState blockState) {

        if (!SystemConfig.getInstance().basicFluidHopperCanBurn)
            return;

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
            if (worldObj.getTileEntity(newPos) instanceof IFluidHandler) {
                IFluidHandler fluidHandler = (IFluidHandler) worldObj.getTileEntity(newPos);

                FluidStack fluidStack = new FluidStack(this.getFluid(), speed);
                int amount;
                if ((amount = fluidHandler.fill(direction.getOpposite(), fluidStack, false)) > 0) {
                    fluidStack = this.drain(direction, amount, true);
                    if (fluidStack.amount != fluidHandler.fill(direction.getOpposite(), fluidStack, true)) {
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
