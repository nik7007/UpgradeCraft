package com.nik7.upgcraft.item;

import com.nik7.upgcraft.CreativeTab.CreativeTab;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.StringHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUpgC extends Item {

    public ItemUpgC() {
        super();
        this.maxStackSize = 1;
        this.setNoRepair();
        this.setCreativeTab(CreativeTab.UPGC_TAB);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", StringHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

}
