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

    private final BlockPos upPosition = getPos().up();

    public TileEntityFunnel() {
        super(EnumCapacity.FUNNEL_CAPACITY);
    }


    private void fillFromUp(int speed) {
        TileEntity te = worldObj.getTileEntity(upPosition);
        IFluidHandler fluidHandler = null;

        if (te instanceof IFluidHandler)
            fluidHandler = (IFluidHandler) te;
        else if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
            fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);

        if (fluidHandler != null) {
            FluidStack checkFluid;
            if ((checkFluid = fluidHandler.drain(speed, false)) != null) {
                int realSpeed = this.fluidTank.fill(checkFluid, false);
                if (realSpeed > 0) {
                    this.fluidTank.fill(fluidHandler.drain(realSpeed, true), true);
                }
            }
        }

    }

    @Override
    public void update() {

    }


    @Override
    public void syncTileEntity() {
    }

}
