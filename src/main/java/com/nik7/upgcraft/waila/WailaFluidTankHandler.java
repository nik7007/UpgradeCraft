package com.nik7.upgcraft.waila;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidTank;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

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
        if (accessor.getBlock() != ModBlocks.blockUpgCEnderFluidTank) {

            String fluidName = StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.df.name");

            if (accessor.getBlock() == ModBlocks.blockUpgCBasicFluidHopper) {
                if (itemStack.getMetadata() > 7) {
                    currenttip.add(EnumChatFormatting.ITALIC + "(" + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":fluid.hopper.burned") + ")");
                }
            }

            int capacity;
            FluidStack fluidStack;
            int amount = 0;
            UpgCtileentityFluidTank tank;


            tank = (UpgCtileentityFluidTank) accessor.getTileEntity();
            if (tank != null) {
                capacity = tank.getCapacity();
                fluidStack = tank.getFluid();

                if (fluidStack != null) {
                    amount = fluidStack.amount;
                    fluidName = fluidStack.getLocalizedName();

                }

                currenttip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.name") + ": " + EnumChatFormatting.RESET + fluidName);
                currenttip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.amount") + ": " + EnumChatFormatting.RESET + amount + "/" + capacity + " mB");
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
