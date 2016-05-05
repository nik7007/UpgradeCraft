package com.nik7.upgcraft.item;

import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.registry.CustomCraftingExperience;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

public class ItemBlockClayFluidTank extends ItemBlockFluidTank implements CustomCraftingExperience {

    public ItemBlockClayFluidTank(Block block) {
        super(block, new UpgCFluidTank(Capacity.SMALL_TANK));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String s = "Hardened";
        if (stack.getItemDamage() >= 2)
            return this.getUnlocalizedName() + "." + s;
        else return this.getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean advanced) {
        int metaData = itemStack.getItemDamage();
        if (metaData == 1 || metaData == 3) {
            list.add(I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.hollow"));
        }

        List<String> hiddenInformation = new LinkedList<>();
        if (metaData < 2) {
            hiddenInformation.add(TextFormatting.DARK_AQUA + I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.to.be.cooked.t"));
            hiddenInformation.add(TextFormatting.DARK_AQUA + I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.to.be.cooked.b"));
        }

        if (metaData > 1) {
            addFluidInformation(hiddenInformation, itemStack);
        }

        ItemUpgC.addHiddenInformation(list, hiddenInformation);
    }


    public FluidStack getFluid(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        if (itemStack.getMetadata() < 2 || !itemStack.hasTagCompound()) {
            return null;
        } else {
            return super.getFluid(itemStack);

        }
    }

    public int getCapacity(ItemStack itemStack) {

        if (itemStack == null)
            return 0;

        if (itemStack.getMetadata() < 2) {
            return 0;
        }
        return super.getCapacity(itemStack);
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
        return super.fill(container, resource, doFill);
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

        return super.drain(container, maxDrain, doDrain);
    }

    @Override
    public float getCustomCraftingExperience(ItemStack item) {
        return 0.8f;
    }

}
