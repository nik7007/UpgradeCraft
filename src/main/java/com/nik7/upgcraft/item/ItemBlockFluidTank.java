package com.nik7.upgcraft.item;


import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockFluidTank extends ItemBlock{

    public ItemBlockFluidTank(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
