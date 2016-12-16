package com.nik7.upgcraft.waila;

import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import com.nik7.upgcraft.reference.Reference;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.List;

public class WailaFluidTankHandler implements IWailaDataProvider {
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        TileEntity fluidTank = accessor.getTileEntity();
        UpgCFluidTank tank = null;


        if (fluidTank.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            tank = (UpgCFluidTank) fluidTank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
        }

        if (tank != null) {
            int capacity = tank.getCapacity();
            if (capacity > 0) {

                String fluidName = I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.empty");
                int amount = tank.getFluidAmount();
                FluidStack fluidStack = tank.getFluid();

                if (amount > 0 && fluidStack != null) {

                    fluidName = fluidStack.getLocalizedName();
                    currenttip.add(fluidName);
                    currenttip.add(amount + "/" + capacity + " mB");

                } else
                    currenttip.add(I18n.translateToLocal(TextFormatting.DARK_GRAY + fluidName));
            }

        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        return null;
    }
}
