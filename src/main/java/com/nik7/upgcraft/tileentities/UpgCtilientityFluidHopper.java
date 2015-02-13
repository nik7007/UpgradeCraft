package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.block.BlockUpgCBasicFluidHopper;
import com.nik7.upgcraft.tank.UpgCTank;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class UpgCtilientityFluidHopper extends UpgCtileentityTank {

    protected int speed = Capacity.Speed.BASIC_FLUID_HOPPER_SPEED;

    public UpgCtilientityFluidHopper() {
        super();
        this.setTank(new UpgCTank(Capacity.FLUID_HOPPER_TANK));

    }

    @Override
    public void updateEntity() {
        int meta = this.getBlockMetadata();
        if (worldObj != null && !this.worldObj.isRemote && BlockUpgCBasicFluidHopper.isNotPowered(meta)) {
            fillFromUp();
            int dir = BlockUpgCBasicFluidHopper.getDirectionFromMetadata(meta);
            ForgeDirection forgeDirection = ForgeDirection.getOrientation(dir);
            autoDrain(forgeDirection);
        }

    }

    private void fillFromUp() {

        if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IFluidHandler) {
            IFluidHandler fluidHandler = (IFluidHandler) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
            FluidStack fluidStack;
            if ((fluidStack = fluidHandler.drain(ForgeDirection.DOWN, 1, false)) != null) {

                fluidStack = new FluidStack(fluidStack, speed);

                int cacity = getTank().fill(fluidStack, false);
                if (cacity > 0) {
                    fluidStack = fluidHandler.drain(ForgeDirection.DOWN, cacity, true);
                    super.fill(ForgeDirection.UP, fluidStack, true);

                }
            }


        }

    }

    private void autoDrain(ForgeDirection direction) {

        if (!this.isEmpty()) {
            if (worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ) instanceof IFluidHandler) {
                IFluidHandler fluidHandler = (IFluidHandler) worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

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
    protected boolean tileEntityInstanceOf(TileEntity tileEntity) {
        return false;
    }
}
