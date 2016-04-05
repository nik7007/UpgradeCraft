package com.nik7.upgcraft.item;


import com.nik7.upgcraft.registry.CustomCraftingExperience;
import net.minecraft.item.ItemStack;

public class ItemUpgCClayIngot extends ItemUpgC implements CustomCraftingExperience {

    public ItemUpgCClayIngot() {
        super("ClayIngot");
    }

    @Override
    public float getCustomCraftingExperience(ItemStack item) {
        return 1F;
    }

}
