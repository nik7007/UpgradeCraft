package com.nik7.upgcraft.fluids.tank;

import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.capability.FluidTankProperties;
import com.nik7.upgcraft.tileentity.TileEntitySynchronizable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class UpgCFluidTank implements IUpgCFluidTank {

    protected IFluidTankProperties[] tankProperties;
    protected boolean canFill = true;
    protected boolean canDrain = true;
    protected TileEntitySynchronizable[] tileEntities;
    private FluidStack fluidStack;
    private EnumCapacity capacity;

    public UpgCFluidTank(EnumCapacity capacity) {
        this.capacity = capacity;
        this.fluidStack = null;
        this.tankProperties = new IFluidTankProperties[]{new FluidTankProperties(this)};
    }

    public UpgCFluidTank(EnumCapacity capacity, TileEntitySynchronizable... tileEntities) {
        this(capacity);
        this.tileEntities = tileEntities;
    }

    @Override
    public IUpgCFluidTank readFromNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("Empty")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
            this.fluidStack = fluid;
        } else {
            this.fluidStack = null;
        }

        EnumCapacity capacity = this.capacity.readFromNBT(nbt);
        if (capacity != EnumCapacity.ERROR_CAPACITY)
            this.capacity = capacity;
        return this;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (this.getFluid() != null) {
            this.getFluid().writeToNBT(nbt);
        } else {
            nbt.setString("Empty", "");
        }
        capacity.writeToNBT(nbt);
        return nbt;
    }


    @Nullable
    @Override
    public FluidStack getFluid() {
        return this.fluidStack;
    }

    @Override
    public int getFluidAmount() {
        return this.getFluid() == null ? 0 : this.getFluid().amount;
    }

    public EnumCapacity getEnumCapacity() {
        return this.capacity;
    }

    @Override
    public int getCapacity() {
        return EnumCapacity.getCapacity(this.getEnumCapacity());

    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this.getFluid(), this.getCapacity());
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {

        return this.tankProperties;
    }

    public boolean canFill() {
        return this.canFill;
    }

    public boolean canDrain() {
        return this.canDrain;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null)
            return 0;
        else if (!this.tankProperties[0].canFillFluidType(resource))
            return 0;
        else if (this.getFluid() != null && !this.getFluid().isFluidEqual(resource))
            return 0;
        else if (this.getFluidAmount() >= this.getCapacity())
            return 0;
        else {
            int resourceAmount = resource.amount;
            int amount = this.getFluidAmount();
            int capacity = this.getCapacity();

            int result;

            if (resourceAmount + amount <= capacity) {
                result = resourceAmount;
            } else {
                result = capacity - amount;
            }
            if (doFill) {
                if (this.fluidStack != null)
                    this.fluidStack.amount += result;
                else this.fluidStack = new FluidStack(resource, result);
                if (this.tileEntities != null) {
                    for (TileEntitySynchronizable te : this.tileEntities) {
                        FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(this.fluidStack, te.getWorld(), te.getPos(), this, this.fluidStack.amount));
                        if (result > 0)
                            te.syncTileEntity();
                    }
                }
            }

            return result;
        }

    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {

        if (resource == null)
            return null;
        else if (!this.tankProperties[0].canDrainFluidType(resource))
            return null;
        else if (this.getFluid() == null)
            return null;
        else if (!this.getFluid().isFluidEqual(resource))
            return null;
        else {
            return drain(resource.amount, doDrain);
        }
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {

        if (this.getFluid() == null)
            return null;
        else if (!this.tankProperties[0].canDrainFluidType(new FluidStack(this.getFluid(), maxDrain)))
            return null;
        else if (maxDrain <= 0)
            return null;
        else {
            int amount = this.getFluidAmount();
            int drained;
            boolean toBeNull = false;
            if (maxDrain >= amount) {
                drained = amount;
                toBeNull = true;
            } else drained = maxDrain;

            FluidStack result = new FluidStack(this.getFluid(), drained);

            if (doDrain) {
                if (toBeNull)
                    this.fluidStack = null;
                else this.fluidStack.amount -= drained;
                if (tileEntities != null) {
                    for (TileEntitySynchronizable te : this.tileEntities) {
                        FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(this.fluidStack, te.getWorld(), te.getPos(), this, drained));
                        te.syncTileEntity();
                    }
                }
            }

            return result;

        }
    }
}
