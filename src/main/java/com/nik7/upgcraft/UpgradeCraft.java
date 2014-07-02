package com.nik7.upgcraft;

import com.nik7.upgcraft.block.InitBlocks;
import com.nik7.upgcraft.configuration.ConfigurationHandler;
import com.nik7.upgcraft.item.InitItems;
import com.nik7.upgcraft.proxy.IProxy;
import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class UpgradeCraft {

    @Mod.Instance(Reference.MOD_ID)
    public static UpgradeCraft instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        //config
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        //item, block
        InitBlocks.init();
        InitItems.init();

    }

    @Mod.EventHandler
    public void Init(FMLInitializationEvent event) {

        proxy.registerTileEntities();

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
