package com.nik7.upgcraft.item;

import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class InitItems {

    public static ItemUpgC itemDragonSword = new ItemDragonSword();
    public static ItemUpgC itemConcentratedEnderPearl = new ItemConcentratedEnderPearl();

    public static void init() {

        GameRegistry.registerItem(itemDragonSword, Reference.MOD_ID + "Item" + Names.Items.DRAGON_SWORD);
        GameRegistry.registerItem(itemConcentratedEnderPearl, Reference.MOD_ID + "Item" + Names.Items.CONCENTRATED_ENDER_PEARL);
    }
}
