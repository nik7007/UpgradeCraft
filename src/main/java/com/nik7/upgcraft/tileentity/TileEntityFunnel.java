package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityFunnel extends TileEntityFluidHandler implements ITickable {

    public TileEntityFunnel() {
        super(EnumCapacity.FUNNEL_CAPACITY);
    }


    private IFluidHandler getFluidHandler(BlockPos blockPos, EnumFacing facing) {

        IFluidHandler fluidHandler = null;
        BlockPos otherPos = blockPos.add(facing.getDirectionVec());
        TileEntity te = worldObj.getTileEntity(otherPos);

        if (te != null) {
            if (te instanceof IFluidHandler)
                fluidHandler = (IFluidHandler) te;
            else if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()))
                fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
        }

        return fluidHandler;
    }

    private void transferFluid(IFluidHandler toFill, IFluidHandler toDrain, int maxAmount) {
        if (toFill != toDrain && toFill != null && toDrain != null) {
            FluidStack checkFluid;
            if ((checkFluid = toDrain.drain(maxAmount, false)) != null) {
                int realSpeed = toFill.fill(checkFluid, false);
                if (realSpeed > 0) {
                    toFill.fill(toDrain.drain(realSpeed, true), true);
                }
            }
        }
    }

    private void fillFromUp(int speed) {
        IFluidHandler fluidHandler = getFluidHandler(getPos(), EnumFacing.UP);

        if (fluidHandler != null) {
            transferFluid(this.fluidTank, fluidHandler, speed);
        }

    }

    private void autoDrain(int speed, EnumFacing facing) {

        IFluidHandler fluidHandler = getFluidHandler(getPos(), facing);

        if (fluidHandler != null) {
            transferFluid(fluidHandler, this.fluidTank, speed);
        }

    }

    @Override
    public void update() {

    }


    @Override
    public void syncTileEntity() {
    }

}
