package com.nik7.upgcraft;

import com.nik7.upgcraft.config.SystemConfig;
import com.nik7.upgcraft.handler.ConfigurationHandler;
import com.nik7.upgcraft.handler.GuiHandler;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.init.ModItems;
import com.nik7.upgcraft.init.Recipes;
import com.nik7.upgcraft.network.DescriptionHandler;
import com.nik7.upgcraft.network.NetworkHandler;
import com.nik7.upgcraft.proxy.IProxy;
import com.nik7.upgcraft.reference.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;


@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class UpgradeCraft {

    @Mod.Instance(Reference.MOD_ID)
    public static UpgradeCraft instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        //config
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());

        //Network
        NetworkHandler.init();
        DescriptionHandler.init();

        //fluid, item, block
        ModBlocks.init();
        ModItems.init();

        //FluidContainerRegistry.registerFluidContainer(ModFluids.ActiveLava, new ItemStack(ModItems.itemActiveLavaBucket), new ItemStack(Items.bucket));


    }

    @Mod.EventHandler
    public void Init(FMLInitializationEvent event) {

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        SystemConfig.applyConfig();

        proxy.registerTileEntities();
        proxy.initRenderingAndTextures();
        proxy.registerEventHandlers();
        Recipes.init();


        FMLInterModComms.sendMessage("Waila", "register", "com.nik7.upgcraft.waila.Waila.callbackRegister");

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        SystemConfig.applyConfig();

    }
}
