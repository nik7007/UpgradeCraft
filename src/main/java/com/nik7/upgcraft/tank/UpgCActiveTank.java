package com.nik7.upgcraft.tank;


import com.nik7.upgcraft.fluid.ActiveLava;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;

public class UpgCActiveTank extends UpgCTank {

    public UpgCActiveTank(int capacity, TileEntity tile) {
        super(capacity, tile);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

        if (resource == null) {
            return 0;
        }

        if (!(resource.getFluid() instanceof ActiveLava)) {
            return super.fill(resource, doFill);
        } else {

            int result = fillActiveLava(resource, doFill);

            if (doFill && result > 0) {
                this.setToHot(resource.getFluid().getTemperature() > (300 + 273));
            }

            return result;

        }
    }

    private int fillActiveLava(FluidStack resource, boolean doFill) {

        if (!doFill) {
            if (fluid == null) {
                return Math.min(capacity, resource.amount);
            }

            if (!(fluid.getFluid() == resource.getFluid())) {
                return 0;
            }

            return Math.min(capacity - fluid.amount, resource.amount);
        }

        if (fluid == null) {
            fluid = new FluidStack(resource, Math.min(capacity, resource.amount));

            if (tile != null) {
                FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, this, fluid.amount));
            }
            return fluid.amount;
        }

        if (!(fluid.getFluid() == resource.getFluid())) {
            return 0;
        }
        int filled = capacity - fluid.amount;

        if (resource.amount < filled) {
            fluid.amount += resource.amount;
            filled = resource.amount;
        } else {
            fluid.amount = capacity;
        }

        if (tile != null) {
            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, this, filled));
        }

        int active = fluid.tag != null && fluid.tag.hasKey(ActiveLava.ACTIVE_VALUE) ? fluid.tag.getInteger(ActiveLava.ACTIVE_VALUE) : 0;
        int activeR = resource.tag != null && resource.tag.hasKey(ActiveLava.ACTIVE_VALUE) ? resource.tag.getInteger(ActiveLava.ACTIVE_VALUE) : 0;

        int newActive = (int) (((float) active * capacity + activeR * filled) / filled + capacity);

        if (fluid.tag == null)
            fluid.tag = new NBTTagCompound();
        else if (fluid.tag.hasKey(ActiveLava.ACTIVE_VALUE))
            fluid.tag.removeTag(ActiveLava.ACTIVE_VALUE);

        fluid.tag.setInteger(ActiveLava.ACTIVE_VALUE, newActive);

        return filled;

    }

}
