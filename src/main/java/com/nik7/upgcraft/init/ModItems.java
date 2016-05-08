package com.nik7.upgcraft.init;

import com.nik7.upgcraft.item.ItemUpgC;
import com.nik7.upgcraft.item.ItemUpgCClayIngot;
import com.nik7.upgcraft.reference.Reference;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {

    public static ItemUpgC itemUpgCClayIngot;

    public static void init() {
        itemUpgCClayIngot = new ItemUpgCClayIngot();

        registerItem(itemUpgCClayIngot);

    }

    private static void registerItem(ItemUpgC item) {
        GameRegistry.register(item.getRegistryName() == null ? item.setRegistryName(item.getName()) : item);
    }


}
