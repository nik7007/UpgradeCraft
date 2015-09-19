package com.nik7.upgcraft.item;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.registry.FluidInfuser.CustomCraftingExperience;
import net.minecraft.item.ItemStack;

public class ItemClayIronIngot extends ItemUpgC implements CustomCraftingExperience {

    public ItemClayIronIngot() {
        super();
        this.setUnlocalizedName(Names.Items.CLAY_IRON_INGOT);
        this.setTextureName(Texture.Items.CLAY_IRON_INGOT);

    }

    @Override
    public float getCustomCraftingExperience(ItemStack item) {
        return 1F;
    }
}
