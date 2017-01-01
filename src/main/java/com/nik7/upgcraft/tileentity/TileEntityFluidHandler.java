package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTankWrapper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityFluidHandler extends TileEntitySynchronizable implements IFluidHandler {

    protected final Class<? extends UpgCFluidTank> tankClass;
    protected UpgCFluidTankWrapper fluidTank;
    private int oldLight;

    public TileEntityFluidHandler(EnumCapacity capacity) {
        this(capacity, null);
    }

    public TileEntityFluidHandler(EnumCapacity capacity, Class<? extends UpgCFluidTank> tankClass) {
        this.tankClass = tankClass;
        this.fluidTank = new UpgCFluidTankWrapper(capacity, tankClass, this);
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

    public int getFluidLight() {

        FluidStack fluidStack = this.fluidTank.getFluid();

        if (fluidStack == null)
            return 0;
        return fluidStack.getFluid().getLuminosity(this.fluidTank.getFluid());
    }

    public FluidStack getFluid() {
        return this.fluidTank.getFluid() == null ? null : this.fluidTank.getFluid().copy();
    }


    protected void updateLight() {
        int light = this.getFluidLight();

        if (this.oldLight != light) {
            getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos());
            this.oldLight = light;
        }
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
