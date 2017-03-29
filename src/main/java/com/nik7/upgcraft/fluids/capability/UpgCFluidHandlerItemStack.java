package com.nik7.upgcraft.fluids.capability;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.IUpgCFluidTank;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTankWrapper;
import net.minecraft.item.ItemStack;
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

    private EnumCapacity capacity;
    private IUpgCFluidTank fluidTank;
    private ItemStack container;

    public UpgCFluidHandlerItemStack(EnumCapacity capacity, ItemStack container) {
        this(capacity, container, null);
    }

    public UpgCFluidHandlerItemStack(EnumCapacity capacity, ItemStack container, Class<? extends UpgCFluidTank> tankClass) {
        this.capacity = capacity;
        this.container = container;
        this.fluidTank = new UpgCFluidTankWrapper(capacity, tankClass, null);
        if (container.hasTagCompound() && container.getTagCompound().hasKey(FLUID_NBT_KEY)) {
            this.fluidTank.readFromNBT(container.getTagCompound().getCompoundTag(FLUID_NBT_KEY));
        }
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
        if (container.getCount() != 1)
            return 0;


        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
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
