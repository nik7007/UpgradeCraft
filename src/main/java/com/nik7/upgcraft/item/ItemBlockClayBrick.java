package com.nik7.upgcraft.item;


import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockClayBrick extends ItemBlock {

    public ItemBlockClayBrick(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String c = "";
        if ((stack.getItemDamage() % 2) == 1)
            c = ".cooked";
        if (stack.getItemDamage() >= 2)
            c = c + ".squared";
        return this.getUnlocalizedName() + c;
    }

}
