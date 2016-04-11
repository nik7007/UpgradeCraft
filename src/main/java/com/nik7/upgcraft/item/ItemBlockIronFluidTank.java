package com.nik7.upgcraft.item;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tank.UpgCEPFluidTank;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;


public class ItemBlockIronFluidTank extends ItemBlock implements IFluidContainerItem {

    private final UpgCEPFluidTank fluidTank;

    public ItemBlockIronFluidTank(Block block) {
        super(block);
        this.setHasSubtypes(true);
        fluidTank = new UpgCEPFluidTank(getCapacity(null));
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean advanced) {
        int metaData = itemStack.getItemDamage();
        if (metaData == 1) {
            list.add(I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.hollow"));
        }
        List hiddenInformation = new LinkedList();

        FluidStack fluidStack = getFluid(itemStack);
        int amount = 0;
        String fluidName = I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluiddfname");

        if (fluidStack != null) {
            amount = fluidStack.amount;
            fluidName = fluidStack.getLocalizedName();

        }
        hiddenInformation.add(TextFormatting.AQUA + I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidname") + ": " + TextFormatting.RESET + fluidName);
        hiddenInformation.add(TextFormatting.AQUA + I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidamount") + ": " + TextFormatting.RESET + amount + "/" + getCapacity(itemStack) + " mB");

        ItemUpgC.addHiddenInformation(list, hiddenInformation);
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        if (container == null)
            return null;
        if (!container.hasTagCompound()) {
            return null;
        } else {
            return FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        }
    }

    @Override
    public int getCapacity(ItemStack container) {
        return Capacity.SMALL_TANK * 2;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        FluidStack fluidStack = null;

        if (container.hasTagCompound())
            fluidStack = FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        fluidTank.setFluid(fluidStack);

        int result = fluidTank.fill(resource, doFill);

        if (doFill) {
            setFluidIntoContainer(container);
        }

        return result;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        FluidStack fluidStack = null;

        if (container.hasTagCompound())
            fluidStack = FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        fluidTank.setFluid(fluidStack);

        FluidStack result = fluidTank.drain(maxDrain, doDrain);

        if (doDrain) {
            setFluidIntoContainer(container);
        }

        return result;
    }

    private void setFluidIntoContainer(ItemStack container) {
        FluidStack newFluid = fluidTank.getFluid();
        if (newFluid != null) {
            container.setTagCompound(newFluid.writeToNBT(new NBTTagCompound()));
        } else container.setTagCompound(null);
    }
}
