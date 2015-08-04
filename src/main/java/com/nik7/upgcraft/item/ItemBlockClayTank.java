package com.nik7.upgcraft.item;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.LinkedList;
import java.util.List;

public class ItemBlockClayTank extends ItemBlock implements IFluidContainerItem {

    public ItemBlockClayTank(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String s = "Hardened";
        if (stack.getItemDamage() >= 2)
            return this.getUnlocalizedName() + "." + s;
        else return this.getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        int metaData = itemStack.getItemDamage();
        if (metaData == 1 || metaData == 3) {
            list.add(StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.hollow"));
        }
        List hiddenInformation = new LinkedList();
        if (metaData < 2) {
            hiddenInformation.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.tobecooked.t"));
            hiddenInformation.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.tobecooked.b"));
        }

        if (metaData > 1) {
            FluidStack fluidStack = getFluid(itemStack);
            int amount = 0;
            String fluidName = StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluiddfname");

            if (fluidStack != null) {
                amount = fluidStack.amount;
                fluidName = fluidStack.getLocalizedName();

            }
            hiddenInformation.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidname") + ": " + EnumChatFormatting.RESET + fluidName);
            hiddenInformation.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidamount") + ": " + EnumChatFormatting.RESET + amount + "/" + getCapacity(itemStack) + " mB");

        }

        ItemUpgC.addHiddenInformation(list, hiddenInformation);

    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {

        int metaData = itemStack.getItemDamage();
        if (metaData < 2)
            return;
        //capacity = 0;
        if (!itemStack.hasTagCompound())
            itemStack.stackTagCompound = new NBTTagCompound();
    }

    @Override
    public FluidStack getFluid(ItemStack container) {

        int metaData = container.getItemDamage();
        if (metaData < 2)
            return null;
        int capacity = Capacity.SMALL_TANK;

        if (capacity == 0)
            return null;

        if (!container.hasTagCompound())
            return null;
        else {
            return FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        }
    }

    @Override
    public int getCapacity(ItemStack container) {

        int metaData = container.getItemDamage();
        if (metaData < 2)
            return 0;

        return Capacity.SMALL_TANK;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {

        int metaData = container.getItemDamage();
        if (metaData < 2)
            return 0;

        int capacity = getCapacity(container);

        if (capacity == 0)
            return 0;

        if (resource == null) {
            return 0;
        }

        FluidStack fluid = null;

        if (!container.hasTagCompound()) {
            container.stackTagCompound = new NBTTagCompound();
        } else fluid = FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        if (!doFill) {
            if (fluid == null) {
                return Math.min(capacity, resource.amount);
            }

            if (!fluid.isFluidEqual(resource)) {
                return 0;
            }

            return Math.min(capacity - fluid.amount, resource.amount);
        }

        if (fluid == null) {
            fluid = new FluidStack(resource, Math.min(capacity, resource.amount));

            fluid.writeToNBT(container.getTagCompound());
            return fluid.amount;
        }

        if (!fluid.isFluidEqual(resource)) {
            return 0;
        }
        int filled = capacity - fluid.amount;

        if (resource.amount < filled) {
            fluid.amount += resource.amount;
            filled = resource.amount;
        } else {
            fluid.amount = capacity;
        }
        fluid.writeToNBT(container.getTagCompound());
        return filled;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {

        int metaData = container.getItemDamage();

        if (metaData < 2)
            return null;
        int capacity = getCapacity(container);

        if (capacity == 0)
            return null;

        FluidStack fluid = null;

        if (!container.hasTagCompound()) {
            container.stackTagCompound = new NBTTagCompound();
        } else fluid = FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        if (fluid == null)
            return null;

        int drained = maxDrain;
        if (fluid.amount < drained) {
            drained = fluid.amount;
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain) {
            fluid.amount -= drained;
            if (fluid.amount <= 0) {
                fluid = null;
            }
            if (fluid != null)
                fluid.writeToNBT(container.getTagCompound());
            else {
                NBTTagCompound tag = container.getTagCompound();
                if (tag.hasKey("FluidName"))
                    tag.removeTag("FluidName");
                if (tag.hasKey("Amount"))
                    tag.removeTag("Amount");
                if (tag.hasKey("Tag"))
                    tag.removeTag("Tag");
            }
        }
        return stack;
    }

}

