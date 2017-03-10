package com.nik7.upgcraft.fluids.tank;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.IFluidIO;
import com.nik7.upgcraft.fluids.capability.FluidTankProperties;
import com.nik7.upgcraft.tileentity.TileEntityFluidHandler;
import com.nik7.upgcraft.tileentity.TileEntityFluidTank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public class UpgCFluidTankWrapper implements IUpgCFluidTank, IFluidIO {

    private final IFluidTankProperties[] tankProperties;
    private final Class<? extends UpgCFluidTank> TankClass;
    private UpgCFluidTank internalTank;
    private boolean canDrain;
    private boolean canFill;
    private IFluidIO tileIFluidTank = null;


    public UpgCFluidTankWrapper(UpgCFluidTank internalTank, boolean canDrain, boolean canFill) {
        this.internalTank = internalTank;
        this.TankClass = internalTank.getClass();
        this.canDrain = canDrain;
        this.canFill = canFill;
        this.tankProperties = new IFluidTankProperties[]{new FluidTankProperties(this)};
    }

    public UpgCFluidTankWrapper(EnumCapacity capacity, TileEntityFluidHandler... tileEntities) {
        this(capacity, null, true, true, tileEntities);
    }

    public UpgCFluidTankWrapper(EnumCapacity capacity, boolean canDrain, boolean canFill, TileEntityFluidHandler... tileEntities) {
        this(capacity, null, canDrain, canFill, tileEntities);
    }

    public UpgCFluidTankWrapper(EnumCapacity capacity, Class<? extends UpgCFluidTank> tankClass, TileEntityFluidHandler... tileEntities) {
        this(capacity, tankClass, true, true, tileEntities);
    }

    public UpgCFluidTankWrapper(EnumCapacity capacity, Class<? extends UpgCFluidTank> tankClass, boolean canDrain, boolean canFill, TileEntityFluidHandler... tileEntities) {
        this.TankClass = tankClass;
        this.internalTank = createTank(capacity, tileEntities);
        this.canFill = canFill;
        this.canDrain = canDrain;
        this.tankProperties = new IFluidTankProperties[]{new FluidTankProperties(this)};

        if (tileEntities != null && tileEntities.length == 1) {
            if (tileEntities[0] instanceof IFluidIO)
                this.tileIFluidTank = (IFluidIO) tileEntities[0];
        }
    }

    @Override
    public IUpgCFluidTank readFromNBT(NBTTagCompound nbt) {
        this.internalTank.readFromNBT(nbt);
        return this;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        this.internalTank.writeToNBT(nbt);
        return nbt;
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

        return tankProperties;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

        if (this.canFill(resource))
            return this.internalTank.fill(resource, doFill);
        else return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {

        if (this.canDrain(resource))
            return this.internalTank.drain(resource, doDrain);
        else return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (this.canDrain())
            return this.internalTank.drain(maxDrain, doDrain);
        else return null;
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        return this.internalTank.getFluid();
    }

    @Override
    public int getFluidAmount() {
        return this.internalTank.getFluidAmount();
    }

    @Override
    public int getCapacity() {
        return this.internalTank.getCapacity();
    }

    @Override
    public FluidTankInfo getInfo() {
        return this.internalTank.getInfo();
    }

    @Override
    public boolean canFill() {
        return this.canFill;
    }

    @Override
    public boolean canDrain() {
        return this.canDrain;
    }

    public UpgCFluidTank getInternalTank() {
        return this.internalTank;
    }

    @Override
    public boolean canFill(FluidStack fluidStack) {
        if (this.tileIFluidTank != null)
            return this.tileIFluidTank.canFill(fluidStack);
        return this.canFill();
    }

    @Override
    public boolean canDrain(FluidStack fluidStack) {
        if (this.tileIFluidTank != null)
            return this.tileIFluidTank.canDrain(fluidStack);
        return this.canDrain();
    }
}
