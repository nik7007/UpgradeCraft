package com.nik7.upgcraft.item;


import com.nik7.upgcraft.registry.FluidInfuser.FluidInfuserItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCSlimyLog extends ItemBlock implements FluidInfuserItem {

    public ItemBlockCSlimyLog(Block block) {
        super(block);
    }

    @Override
    public float getInfusingExperience(ItemStack item) {
        return 0.6F;
    }
}
