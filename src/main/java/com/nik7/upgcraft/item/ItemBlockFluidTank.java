package com.nik7.upgcraft.item;


import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;

public abstract class ItemBlockFluidTank extends ItemBlock implements IFluidContainerItem {


    private final UpgCFluidTank tank;

    protected ItemBlockFluidTank(Block block, UpgCFluidTank tank) {
        super(block);
        this.tank = tank;
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        if (getFluid(stack) != null)
            return 1;
        else return super.getItemStackLimit(stack);
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
        return tank.getCapacity();
    }

    protected void addFluidInformation(List<String> information, ItemStack itemStack) {

        if (information != null) {

            FluidStack fluidStack = getFluid(itemStack);
            int amount = 0;
            String fluidName = I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.df.name");

            if (fluidStack != null) {
                amount = fluidStack.amount;
                fluidName = fluidStack.getLocalizedName();

            }
            information.add(TextFormatting.AQUA + I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.name") + ": " + TextFormatting.RESET + fluidName);
            information.add(TextFormatting.AQUA + I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.amount") + ": " + TextFormatting.RESET + amount + "/" + getCapacity(itemStack) + " mB");
        }

    }

    private void setFluidIntoContainer(ItemStack container) {
        FluidStack newFluid = tank.getFluid();
        if (newFluid != null) {
            container.setTagCompound(newFluid.writeToNBT(new NBTTagCompound()));
        } else container.setTagCompound(null);
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        FluidStack fluidStack = null;

        if (container.hasTagCompound())
            fluidStack = FluidStack.loadFluidStackFromNBT(container.getTagCompound());

        tank.setFluid(fluidStack);

        int result = tank.fill(resource, doFill);

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

        tank.setFluid(fluidStack);

        FluidStack result = tank.drain(maxDrain, doDrain);

        if (doDrain) {
            setFluidIntoContainer(container);
        }

        return result;
    }
}
