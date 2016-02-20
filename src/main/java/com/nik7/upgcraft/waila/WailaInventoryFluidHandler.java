package com.nik7.upgcraft.waila;


import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
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

public class WailaInventoryFluidHandler implements IWailaDataProvider {
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

        int capacity;
        FluidStack fluidStack;
        int amount = 0;
        UpgCtileentityInventoryFluidHandler inventoryFluid;
        String fluidName = StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.df.name");

        inventoryFluid = (UpgCtileentityInventoryFluidHandler) accessor.getTileEntity();
        if (inventoryFluid != null) {
            capacity = inventoryFluid.getCapacity(inventoryFluid.getTankToShow());
            fluidStack = inventoryFluid.getFluid(inventoryFluid.getTankToShow());

            if (fluidStack != null) {
                amount = fluidStack.amount;
                fluidName = fluidStack.getLocalizedName();

            }

            currenttip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.name") + ": " + EnumChatFormatting.RESET + fluidName);
            currenttip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.amount") + ": " + EnumChatFormatting.RESET + amount + "/" + capacity + " mB");
        }


        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        UpgCtileentityInventoryFluidHandler inventoryFluid = (UpgCtileentityInventoryFluidHandler) te;
        FluidStack fluidStack = inventoryFluid.getFluid(inventoryFluid.getTankToShow());
        if (fluidStack != null)
            fluidStack.writeToNBT(tag);

        return tag;
    }
}
