package com.nik7.upgcraft.item;


import com.nik7.upgcraft.creativetab.CreativeTab;
import com.nik7.upgcraft.reference.Reference;
import net.minecraft.item.Item;

public class ItemUpgC extends Item {

    public ItemUpgC(String itemName) {
        super();
        this.setRegistryName(itemName);
        this.setUnlocalizedName(Reference.RESOURCE_PREFIX + itemName);
        this.setCreativeTab(CreativeTab.UPGC_TAB);
    }
}
