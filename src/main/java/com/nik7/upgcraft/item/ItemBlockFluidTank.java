package com.nik7.upgcraft.item;


import com.nik7.upgcraft.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockFluidTank extends ItemBlock {

    public ItemBlockFluidTank(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        int meta = stack.getItemDamage();
        if((meta % 2) ==1){
            tooltip.add(I18n.translateToLocal("tooltip." + Reference.RESOURCE_PREFIX + "tank.glassed"));
        }
    }
}
