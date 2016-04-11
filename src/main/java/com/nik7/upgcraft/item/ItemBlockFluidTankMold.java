package com.nik7.upgcraft.item;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.registry.CustomCraftingExperience;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

public class ItemBlockFluidTankMold extends ItemBlock implements CustomCraftingExperience {

    public ItemBlockFluidTankMold(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean advanced) {
        int metaData = itemStack.getItemDamage();

        List<String> hiddenInformation = new LinkedList<>();

        hiddenInformation.add(I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":fluidtankmold.content") + ":");

        if (metaData % 2 != 0)
            hiddenInformation.add("-" + Blocks.glass.getLocalizedName());

        if (metaData < 2)
            hiddenInformation.add("-" + ModBlocks.blockUpgCSlimyLog.getLocalizedName());

        if (metaData > 5)
            hiddenInformation.add("-" + I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":fluidtankmold.iron"));

        if (hiddenInformation.size() <= 1)
            hiddenInformation.add("<" + I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":fluidtankmold.empty") + ">");

        ItemUpgC.addHiddenInformation(list, hiddenInformation);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public float getCustomCraftingExperience(ItemStack item) {

        float value = item.getItemDamage() / 5f;
        return 0.65f * value + 0.35f;
    }
}
