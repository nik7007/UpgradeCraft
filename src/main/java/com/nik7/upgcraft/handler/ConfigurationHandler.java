package com.nik7.upgcraft.handler;

import com.nik7.upgcraft.config.SystemConfig;
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
            loadConfiguration();
        }

    }

    private static void loadConfiguration() {

        SystemConfig.getInstance().basicWoodenBlockFlammability = configuration.getBoolean("basic Wooden Block Flammability", Configuration.CATEGORY_GENERAL, true, "Set to false to make fireproof Wooden Liquid Tank, and Slimy Log.");

        SystemConfig.getInstance().basicTankCapacity = configuration.getInt("basic tank capacity", Configuration.CATEGORY_GENERAL, 27, 24, 32, "Basic capacity Tank custom value. This will not change the capacity of the enderTanks.");

        if (configuration.hasChanged()) {
            configuration.save();
            SystemConfig.applyConfig();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfiguration();
        }
    }
}
