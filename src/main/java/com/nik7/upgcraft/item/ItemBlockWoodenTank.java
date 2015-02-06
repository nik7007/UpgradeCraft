package com.nik7.upgcraft.item;

import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemBlockWoodenTank extends ItemBlock {

    public ItemBlockWoodenTank(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        int metaData = itemStack.getItemDamage();
        if (metaData == 1) {
            list.add(StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":woodentank.hollow"));
        }
    }

}
