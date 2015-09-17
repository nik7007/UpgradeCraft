package com.nik7.upgcraft.item;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.registry.FluidInfuser.FluidInfuserItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class ItemBlockFluidTankMold extends ItemBlock implements FluidInfuserItem {

    public ItemBlockFluidTankMold(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        if (meta < 2)
            return this.field_150939_a.getUnlocalizedName();
        else return this.field_150939_a.getUnlocalizedName() + ".Hardened";
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        int metaData = itemStack.getItemDamage();

        List<String> hiddenInformation = new ArrayList<String>();

        hiddenInformation.add(StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":fluidtankmold.content") + ":");

        if (metaData % 2 != 0)
            hiddenInformation.add("-" + Blocks.glass.getLocalizedName());

        if (metaData < 2)
            hiddenInformation.add("-" + ModBlocks.blockSlimyLog.getLocalizedName());

        if (metaData > 3)
            hiddenInformation.add("-" + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":fluidtankmold.iron"));

        if (hiddenInformation.size() <= 1)
            hiddenInformation.add("<" + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":fluidtankmold.empty") + ">");

        ItemUpgC.addHiddenInformation(list, hiddenInformation);
    }

    @Override
    public float getInfusingExperience(ItemStack item) {

        float value = item.getItemDamage() / 5;
        return 0.85f * value + 0.1f;
    }
}
