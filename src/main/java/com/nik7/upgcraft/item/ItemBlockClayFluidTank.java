package com.nik7.upgcraft.item;

import com.nik7.upgcraft.block.BlockUpgCClayFluidTank;
import com.nik7.upgcraft.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

public class ItemBlockClayFluidTank extends ItemBlock {

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
            hiddenInformation.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.amount") + ": " + EnumChatFormatting.RESET + amount + "/" + getCapacity() + " mB");

        }

        ItemUpgC.addHiddenInformation(list, hiddenInformation);
    }

    private FluidStack getFluid(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        if (!itemStack.hasTagCompound()) {
            return null;
        } else {
            return FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound());

        }
    }

    private int getCapacity() {
        Block block = this.getBlock();

        return ((BlockUpgCClayFluidTank) block).getCapacity();
    }

}
