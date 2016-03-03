package com.nik7.upgcraft.tank;

import com.nik7.upgcraft.fluid.FluidWithExtendedProperties;
import com.nik7.upgcraft.reference.Capacity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

public class UpgCEnderFluidTank extends FluidTank {

    protected FluidStack fluid;

    public UpgCEnderFluidTank() {
        super(0);
    }
    //protected int capacity;
    //protected TileEntity tile;

    /*public UpgCEnderFluidTank(int capacity)
    {
        this(null, capacity);
    }

    public UpgCEnderFluidTank(FluidStack stack, int capacity)
    {
        this.fluid = stack;
        this.capacity = capacity;
    }

    public UpgCEnderFluidTank(Fluid fluid, int amount, int capacity)
    {
        this(new FluidStack(fluid, amount), capacity);
    }*/

    public UpgCEnderFluidTank readFromNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("Empty")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
            setFluid(fluid);
        } else {
            setFluid(null);
        }
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (fluid != null) {
            fluid.writeToNBT(nbt);
        } else {
            nbt.setString("Empty", "");
        }
        return nbt;
    }

    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
    }

    public void setCapacity(int capacity) {
        Capacity.SMALL_TANK = capacity;
    }

    /* IFluidTank */
    @Override
    public FluidStack getFluid() {
        return fluid;
    }

    @Override
    public int getFluidAmount() {
        if (fluid == null) {
            return 0;
        }
        return fluid.amount;
    }

    @Override
    public int getCapacity() {
        return Capacity.SMALL_TANK;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null) {
            return 0;
        }

        if (getFluid() != null && getFluid().getFluid() instanceof FluidWithExtendedProperties) {
            FluidWithExtendedProperties fluidWithEP = (FluidWithExtendedProperties) getFluid().getFluid();
            if (fluidWithEP.hasExtendedFill(getFluid()))
                return fluidWithEP.fill(this, resource, doFill);
        }

        if (!doFill) {
            if (fluid == null) {
                return Math.min(Capacity.SMALL_TANK, resource.amount);
            }

            if (!fluid.isFluidEqual(resource)) {
                return 0;
            }

            return Math.min(Capacity.SMALL_TANK - fluid.amount, resource.amount);
        }

        if (fluid == null) {
            fluid = new FluidStack(resource, Math.min(Capacity.SMALL_TANK, resource.amount));

            /*if (tile != null)
            {
                FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(), this, fluid.amount));
            }*/
            return fluid.amount;
        }

        if (!fluid.isFluidEqual(resource)) {
            return 0;
        }
        int filled = Capacity.SMALL_TANK - fluid.amount;

        if (resource.amount < filled) {
            fluid.amount += resource.amount;
            filled = resource.amount;
        } else {
            fluid.amount = Capacity.SMALL_TANK;
        }

       /* if (tile != null)
        {
            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(), this, filled));
        }*/
        return filled;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (fluid == null) {
            return null;
        }

        if (getFluid() != null && getFluid().getFluid() instanceof FluidWithExtendedProperties) {
            FluidWithExtendedProperties fluidWithEP = (FluidWithExtendedProperties) getFluid().getFluid();

            if (fluidWithEP.hasExtendedDrain(getFluid()))
                return fluidWithEP.drain(this, maxDrain, doDrain);
        }

        int drained = maxDrain;
        if (fluid.amount < drained) {
            drained = fluid.amount;
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain) {
            fluid.amount -= drained;
            if (fluid.amount <= 0) {
                fluid = null;
            }

            /*if (tile != null)
            {
                FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, tile.getWorld(), tile.getPos(), this, drained));
            }*/
        }
        return stack;
    }
}