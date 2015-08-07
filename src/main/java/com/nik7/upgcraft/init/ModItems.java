package com.nik7.upgcraft.init;

import com.nik7.upgcraft.item.*;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {

    public final static Item itemDragonSword = new ItemDragonSword();
    public final static Item itemConcentratedEnderPearl = new ItemConcentratedEnderPearl();
    public final static Item itemClayeyIronIngot = new ItemClayIronIngot();
    public final static Item itemUpgCPersonalInformation  = new ItemUpgCPersonalInformation();
    public final static Item itemActiveLavaBucket = new ItemActiveLavaBucket();

    public static void init() {

        GameRegistry.registerItem(itemDragonSword, Reference.MOD_ID + "Item" + Names.Items.DRAGON_SWORD);
        GameRegistry.registerItem(itemConcentratedEnderPearl, Reference.MOD_ID + "Item" + Names.Items.CONCENTRATED_ENDER_PEARL);
        GameRegistry.registerItem(itemClayeyIronIngot, Reference.MOD_ID + "Item" + Names.Items.CLAY_IRON_INGOT);
        GameRegistry.registerItem(itemUpgCPersonalInformation, Reference.MOD_ID + "Item" + Names.Items.PERSONAL_INFORMATION);
        GameRegistry.registerItem(itemActiveLavaBucket, Reference.MOD_ID + "Item" + Names.Items.ACTIVE_LAVA_BUCKET);
    }
}
