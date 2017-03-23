package com.nik7.upgcraft.item;


import com.nik7.upgcraft.registry.ICraftingExperience;
import net.minecraft.item.ItemStack;

public class ItemClayIngot extends ItemUpgC implements ICraftingExperience {


    public ItemClayIngot() {
        super("ClayIngot");
    }

    @Override
    public float getCraftingExperience(ItemStack item) {
        return 1f;
    }
}
