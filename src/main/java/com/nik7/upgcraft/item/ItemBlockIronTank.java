package com.nik7.upgcraft.item;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tank.UpgCActiveTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.LinkedList;
import java.util.List;

public class ItemBlockIronTank extends ItemBlock implements IFluidContainerItem {


    public ItemBlockIronTank(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        int metaData = itemStack.getItemDamage();
        if (metaData == 1) {
            list.add(StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.hollow"));
        }
        List hiddenInformation = new LinkedList();

        FluidStack fluidStack = getFluid(itemStack);
        int amount = 0;
        String fluidName = StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluiddfname");

        if (fluidStack != null) {
            amount = fluidStack.amount;
            fluidName = fluidStack.getLocalizedName();

        }
        hiddenInformation.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidname") + ": " + EnumChatFormatting.RESET + fluidName);
        hiddenInformation.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidamount") + ": " + EnumChatFormatting.RESET + amount + "/" + getCapacity(itemStack) + " mB");

        ItemUpgC.addHiddenInformation(list, hiddenInformation);

    }

    @Override
    public FluidStack getFluid(ItemStack container) {

        if (!container.hasTagCompound())
            return null;
        else {
            return FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        }
    }

    @Override
    public int getCapacity(ItemStack container) {
        return 2 * Capacity.SMALL_TANK;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        UpgCActiveTank tank = new UpgCActiveTank(2 * Capacity.SMALL_TANK);

        FluidStack oldFluid = null;

        if (container.hasTagCompound())
            oldFluid = FluidStack.loadFluidStackFromNBT(container.getTagCompound());


        tank.setFluid(oldFluid);

        int result = tank.fill(resource, doFill);

        FluidStack newFluid = tank.getFluid();

        if (!container.hasTagCompound())
            container.stackTagCompound = new NBTTagCompound();

        newFluid.writeToNBT(container.getTagCompound());


        return result;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {

        UpgCActiveTank tank = new UpgCActiveTank(2 * Capacity.SMALL_TANK);

        FluidStack oldFluid = null;

        if (container.hasTagCompound())
            oldFluid = FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        tank.setFluid(oldFluid);

        FluidStack result = tank.drain(maxDrain, doDrain);

        FluidStack newFluid = tank.getFluid();

        if (!container.hasTagCompound())
            container.stackTagCompound = new NBTTagCompound();

        if (newFluid != null)
            newFluid.writeToNBT(container.getTagCompound());
        else container.stackTagCompound = null;


        return result;
    }
}
