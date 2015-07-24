package com.nik7.upgcraft.item;


import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.LinkedList;
import java.util.List;

public class ItemBlockClayTank extends ItemBlock {

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
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        int metaData = itemStack.getItemDamage();
        if (metaData == 1 || metaData == 3) {
            list.add(StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.hollow"));
        }
        if (metaData < 2) {
            List hiddenInformation = new LinkedList();

            hiddenInformation.add("");
            hiddenInformation.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.tobecooked.t"));
            hiddenInformation.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.tobecooked.b"));

            ItemUpgC.addHiddenInformation(list, hiddenInformation);

        }

    }

}
