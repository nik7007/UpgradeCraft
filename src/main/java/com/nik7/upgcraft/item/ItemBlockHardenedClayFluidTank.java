package com.nik7.upgcraft.item;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.capability.UpgCFluidHandlerItemStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockHardenedClayFluidTank extends ItemBlockFluidTank {

    public ItemBlockHardenedClayFluidTank(Block block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);

        int capacity = 0, amount = 0;
        IFluidHandlerItem tank = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

        if (tank != null) {
            capacity = tank.getTankProperties()[0].getCapacity();
            FluidStack fluid = tank.getTankProperties()[0].getContents();
            if (fluid != null) {
                amount = fluid.amount;
            }
        }
        tooltip.add(amount + "/" + capacity + "mB");

    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt) {

        return new UpgCFluidHandlerItemStack(EnumCapacity.BASIC_CAPACITY, stack);
    }

}
