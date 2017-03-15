package com.nik7.upgcraft.registry.recipes;


import com.nik7.upgcraft.registry.ICraftingExperience;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public final class RecipeExperience {

    private RecipeExperience() {

    }

    public static float getExperience(ItemStack itemStack) {

        float expProbability = 0;

        if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            ICraftingExperience craftingExperience = null;

            if (item instanceof ICraftingExperience) {
                craftingExperience = (ICraftingExperience) item;
            } else if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();
                if (block instanceof ICraftingExperience)
                    craftingExperience = (ICraftingExperience) block;
            }

            if (craftingExperience != null)
                expProbability = craftingExperience.getCraftingExperience(itemStack);
        }

        return expProbability;
    }

}
