package com.nik7.upgcraft;

import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.proxy.IProxy;
import com.nik7.upgcraft.reference.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class UpgradeCraft {

    @Mod.Instance(Reference.MOD_ID)
    public static UpgradeCraft instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //read config
        //init blocks
        ModBlocks.init();
        //init Items
        //register
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        //register block
        ModBlocks.register();
        //recipes
        //tileEntity
        proxy.registerTileEntities();
        //interModComm

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}

