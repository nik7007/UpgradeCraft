package com.nik7.upgcraft.item;

import com.nik7.upgcraft.block.BlockUpgCClayFluidTank;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.registry.FluidInfuser.CustomCraftingExperience;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

public class ItemBlockClayFluidTank extends ItemBlock implements IFluidContainerItem, CustomCraftingExperience {

    public ItemBlockClayFluidTank(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String s = "Hardened";
        if (stack.getItemDamage() >= 2)
            return this.getUnlocalizedName() + "." + s;
        else return this.getUnlocalizedName();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean advanced) {
        int metaData = itemStack.getItemDamage();
        if (metaData == 1 || metaData == 3) {
            list.add(StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.hollow"));
        }

        List<String> hiddenInformation = new LinkedList<String>();
        if (metaData < 2) {
            hiddenInformation.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.to.be.cooked.t"));
            hiddenInformation.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.to.be.cooked.b"));
        }

        if (metaData > 1) {
            FluidStack fluidStack = getFluid(itemStack);
            int amount = 0;
            String fluidName = StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.df.name");

            if (fluidStack != null) {
                amount = fluidStack.amount;
                fluidName = fluidStack.getLocalizedName();

            }
            hiddenInformation.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.name") + ": " + EnumChatFormatting.RESET + fluidName);
            hiddenInformation.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.amount") + ": " + EnumChatFormatting.RESET + amount + "/" + getCapacity(itemStack) + " mB");

        }

        ItemUpgC.addHiddenInformation(list, hiddenInformation);
    }

    public FluidStack getFluid(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        if (itemStack.getMetadata() < 2 || !itemStack.hasTagCompound()) {
            return null;
        } else {
            return FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound());

        }
    }

    public int getCapacity(ItemStack itemStack) {

        if (itemStack == null)
            return 0;

        if (itemStack.getMetadata() < 2) {
            return 0;
        }
        Block block = this.getBlock();

        return ((BlockUpgCClayFluidTank) block).getCapacity();
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if (container == null || resource == null)
            return 0;
        if (container.getMetadata() < 2)
            return 0;
        if (resource.getFluid() == null)
            return 0;
        if (resource.amount <= 0)
            return 0;
        FluidStack fluidInside = null;
        int amount = 0;

        if (container.hasTagCompound())
            fluidInside = FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        if (fluidInside != null) {
            if (!fluidInside.equals(resource)) return 0;
            amount = fluidInside.amount;
            if (amount >= getCapacity(container))
                return 0;

        }
        int oldAmount = amount;
        amount += resource.amount;

        amount = Math.min(amount, getCapacity(container));
        FluidStack fluidStack = new FluidStack(resource, amount);

        if (doFill)
            container.setTagCompound(fluidStack.writeToNBT(new NBTTagCompound()));

        return fluidStack.amount - oldAmount;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if (container == null)
            return null;
        if (container.getMetadata() < 2)
            return null;
        if (maxDrain <= 0)
            return null;
        if (!container.hasTagCompound())
            return null;
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        if (fluidStack == null)
            return null;

        int amount = fluidStack.amount;
        int drained;
        if (amount < maxDrain)
            drained = amount;
        else drained = maxDrain;

        if (doDrain) {
            if (drained == amount)
                container.setTagCompound(null);
            else {
                FluidStack fluid = new FluidStack(fluidStack, amount - drained);
                container.setTagCompound(fluid.writeToNBT(new NBTTagCompound()));
            }
        }

        return new FluidStack(fluidStack, drained);
    }

    @Override
    public float getCustomCraftingExperience(ItemStack item) {
        return 0.8f;
    }

}
