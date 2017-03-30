package com.nik7.upgcraft.fluids.capability;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.IUpgCFluidTank;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTankWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UpgCFluidHandlerItemStack implements IFluidHandlerItem, ICapabilityProvider {

    public static final String FLUID_NBT_KEY = "Fluid";

    private final IUpgCFluidTank fluidTank;
    private final ItemStack container;

    public UpgCFluidHandlerItemStack(EnumCapacity capacity, ItemStack container) {
        this(capacity, container, null);
    }

    public UpgCFluidHandlerItemStack(EnumCapacity capacity, ItemStack container, Class<? extends UpgCFluidTank> tankClass) {
        this.container = container;
        this.fluidTank = new UpgCFluidTankWrapper(capacity, tankClass, null);
        this.updateTank();
    }

    private void updateTank() {
        if (this.container.hasTagCompound() && this.container.getTagCompound().hasKey(FLUID_NBT_KEY)) {
            this.fluidTank.readFromNBT(container.getTagCompound().getCompoundTag(FLUID_NBT_KEY));
        }
    }

    private void updateContainer() {
        if (this.getFluid() == null) {
            if (this.container.hasTagCompound()) {
                this.container.getTagCompound().removeTag(FLUID_NBT_KEY);
            }
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag(FLUID_NBT_KEY, this.fluidTank.writeToNBT(new NBTTagCompound()));
            this.container.setTagCompound(tag);
        }

    }


    public FluidStack getFluid() {
        return this.fluidTank.getFluid();
    }

    public void setFluid(FluidStack fluid) {
        this.fluidTank.setFluid(fluid);
        this.updateContainer();
    }

    @Nonnull
    @Override
    public ItemStack getContainer() {
        return container;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return this.fluidTank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (this.container.getCount() != 1)
            return 0;

        int result = this.fluidTank.fill(resource, doFill);

        if (doFill && result > 0)
            this.updateContainer();

        return result;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (this.container.getCount() != 1)
            return null;

        FluidStack result = this.fluidTank.drain(resource, doDrain);

        if (doDrain && result != null) {
            this.updateContainer();
        }

        return result;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (this.container.getCount() != 1)
            return null;

        FluidStack result = this.fluidTank.drain(maxDrain, doDrain);

        if (doDrain && result != null) {
            this.updateContainer();
        }

        return null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY ? (T) this : null;
    }

}
