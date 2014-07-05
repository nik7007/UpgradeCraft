package com.nik7.upgcraft.inventory;

import com.nik7.upgcraft.util.LogHelper;
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

        return result;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt = super.writeToNBT(nbt);

        nbt.setBoolean("toHot", toHot);

        return nbt;
    }


}
