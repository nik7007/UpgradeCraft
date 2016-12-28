package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public abstract class TileEntityFluidHandler extends TileEntitySynchronizable implements IFluidHandler {

    protected final EnumCapacity capacity;
    private final Class<? extends UpgCFluidTank> TankClass;
    protected UpgCFluidTank fluidTank;

    public TileEntityFluidHandler(EnumCapacity capacity) {
        this(capacity, null);
    }

    public TileEntityFluidHandler(EnumCapacity capacity, Class<? extends UpgCFluidTank> tankClass) {
        TankClass = tankClass;
        this.capacity = capacity;
        this.fluidTank = createTank(capacity, this);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.fluidTank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        this.fluidTank.writeToNBT(tag);
        return tag;
    }


    protected UpgCFluidTank createTank(EnumCapacity capacity, TileEntityFluidHandler... tileEntities) {
        if (TankClass != null) {
            UpgCFluidTank result;
            try {
                result = TankClass.asSubclass(UpgCFluidTank.class).getConstructor(EnumCapacity.class, TileEntityFluidTank[].class).newInstance(capacity, tileEntities);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return result;

        } else
            return new UpgCFluidTank(capacity, tileEntities);
    }


    @Override
    public IFluidTankProperties[] getTankProperties() {
        return this.fluidTank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return this.fluidTank.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return this.fluidTank.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return this.fluidTank.drain(maxDrain, doDrain);
    }

    public float getFillPercentage() {
        return (float) this.fluidTank.getFluidAmount() / (float) this.fluidTank.getCapacity();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) this.fluidTank;
        return super.getCapability(capability, facing);
    }
}
