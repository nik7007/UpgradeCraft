package com.nik7.upgcraft.handler;

import com.nik7.upgcraft.block.InitBlocks;
import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;


public class ConfigurationHandler {
    public static Configuration configuration;
    public static boolean basic_Wooden_Block_Flammability = true;

    public static void init(File configFile) {
// Create the configuration object from the given configuration file
        if (configuration == null) {
            configuration = new Configuration(configFile);
        }

    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfiguration();
        }
    }

    public void loadConfiguration() {
        basic_Wooden_Block_Flammability = configuration.getBoolean("Flammability", "basic_Wooden_Block_Flammability", true, "Set to false to make fireproof Wooden Liquid Tank, and Slimy Log.");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
