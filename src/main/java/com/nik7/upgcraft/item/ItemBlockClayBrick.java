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
        String c = "Cooked";
        if (stack.getItemDamage() == 1)
            return this.getUnlocalizedName() + "." + c;
        else return this.getUnlocalizedName();
    }
}
