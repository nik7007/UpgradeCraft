package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.inventory.UpgCTank;
import com.nik7.upgcraft.reference.Capacity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class UpgCraftFluidHopper extends UpgCtileentityTank {

    private int speed = 50;

    public UpgCraftFluidHopper() {
        super();
        this.setTank(new UpgCTank(Capacity.FLUID_HOPPER_TANK));

    }

    @Override
    public void updateEntity() {
        fillFromUp();

    }

    private void fillFromUp() {
        if (worldObj != null) {
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
    }


    @Override
    protected boolean tileEntityInstanceOf(TileEntity tileEntity) {
        return false;
    }
}
