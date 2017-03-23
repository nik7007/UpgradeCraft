package com.nik7.upgcraft.init;


import com.nik7.upgcraft.item.ItemClayIngot;
import com.nik7.upgcraft.item.ItemUpgC;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    private ModItems() {
    }

    public static ItemUpgC clayIngot;

    public static void init() {
        clayIngot = new ItemClayIngot();
        register();
    }

    private static void register() {
        register(clayIngot);
    }

    private static void register(ItemUpgC item) {
        GameRegistry.register(item);
    }

}
