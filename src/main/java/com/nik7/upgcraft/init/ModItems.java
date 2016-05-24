package com.nik7.upgcraft.init;

import com.nik7.upgcraft.item.ItemUpgC;
import com.nik7.upgcraft.item.ItemUpgCClayIngot;
import com.nik7.upgcraft.item.ItemUpgCRedLogicComponent;
import com.nik7.upgcraft.reference.Reference;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.nik7.upgcraft.redstone.ExpressionType.*;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {

    public static ItemUpgC itemUpgCClayIngot;
    public static ItemUpgC itemUpgCANDComponent;
    public static ItemUpgC itemUpgCORComponent;
    public static ItemUpgC itemUpgCNOTComponent;
    public static ItemUpgC itemUpgCWireComponent;

    public static void init() {
        itemUpgCClayIngot = new ItemUpgCClayIngot();
        itemUpgCANDComponent = new ItemUpgCRedLogicComponent("ANDComponent", AND);
        itemUpgCORComponent = new ItemUpgCRedLogicComponent("ORComponent", OR);
        itemUpgCNOTComponent = new ItemUpgCRedLogicComponent("NOTComponent", NOT);
        itemUpgCWireComponent = new ItemUpgCRedLogicComponent("WireComponent", WIRE);

        registerItem(itemUpgCClayIngot);

        registerItem(itemUpgCANDComponent);
        registerItem(itemUpgCORComponent);
        registerItem(itemUpgCNOTComponent);
        registerItem(itemUpgCWireComponent);


    }

    private static void registerItem(ItemUpgC item) {
        GameRegistry.register(item.getRegistryName() == null ? item.setRegistryName(item.getName()) : item);
    }


}
