package com.nik7.upgcraft.inventory;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class UpgCTank extends FluidTank {

    private boolean toHot = false;

    public UpgCTank(int capacity) {

        super(capacity);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        int result = super.fill(resource, doFill);
        if (doFill && result > 0) {
            this.setToHot(resource.getFluid().getTemperature() > (300 + 273));
        }


        return result;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        FluidStack result = super.drain(maxDrain, doDrain);

        if (fluid == null)
            this.setToHot(false);

        return result;
    }

    public boolean isToHot() {
        return toHot;
    }

    public void setToHot(boolean toHot) {
        this.toHot = toHot;
    }

    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {
        FluidTank result = super.readFromNBT(nbt);

        toHot = nbt.getBoolean("toHot");
        capacity = nbt.getInteger("capacity");

        return result;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt = super.writeToNBT(nbt);

        nbt.setBoolean("toHot", toHot);
        nbt.setInteger("capacity", capacity);

        return nbt;
    }

    @Override
    public boolean equals(Object tank) {
        if (tank == null) {
            return false;
        }
        if (!(tank instanceof UpgCTank)) {
            return false;
        }

        UpgCTank upgCTank = (UpgCTank) tank;

        return this.getFluid() == null && upgCTank.getFluid() == null || !(this.getFluid() == null || upgCTank.getFluid() == null) && this.getFluid().isFluidStackIdentical(upgCTank.getFluid());

    }

}
