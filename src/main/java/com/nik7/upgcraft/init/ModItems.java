package com.nik7.upgcraft.init;


import com.nik7.upgcraft.item.ItemClayIngot;
import com.nik7.upgcraft.item.ItemUpgC;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static ItemUpgC itemClayIngot;

    private ModItems() {
    }

    public static void init() {
        itemClayIngot = new ItemClayIngot();
        register();
    }

    private static void register() {
        register(itemClayIngot);
    }

    private static void register(ItemUpgC item) {
        GameRegistry.register(item);
    }

}
