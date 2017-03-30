package com.nik7.upgcraft.item;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.capability.UpgCFluidHandlerItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBlockHardenedClayFluidTank extends ItemBlockFluidTank {

    public ItemBlockHardenedClayFluidTank(Block block) {
        super(block);
    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt) {

        return new UpgCFluidHandlerItemStack(EnumCapacity.BASIC_CAPACITY, stack);
    }

}
