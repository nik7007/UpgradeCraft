package com.nik7.upgcraft.handler;

import com.nik7.upgcraft.reference.ConfigOptions;
import com.nik7.upgcraft.reference.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ConfigurationHandler {

    public static Configuration configuration;

    public static void init(File configFile) {
        // Create the configuration object from the given configuration file
        if (ConfigurationHandler.configuration == null) {
            ConfigurationHandler.configuration = new Configuration(configFile);
            ConfigurationHandler.loadConfiguration();
        }
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
    }

    private static void loadConfiguration() {

        ConfigOptions.WOODEN_TANK_BURN = configuration.getBoolean("basic Wooden Block Flammability", Configuration.CATEGORY_GENERAL, true, "Set to false to make fireproof Wooden Liquid Tank.");
        ConfigOptions.WOODEN_FLUID_HOPPER_BURN = configuration.getBoolean("basic Fluid Hopper can burn", Configuration.CATEGORY_GENERAL, true, "Set to false to not allow Basic Fluid Hopper to burn");
        ConfigOptions.CONFIG_CAPACITY = configuration.getInt("basic tank capacity", Configuration.CATEGORY_GENERAL, 27, 24, 32, "Basic capacity Tank custom value. This will not change the capacity of the enderTanks.");

        ConfigOptions.reloadOptions();

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfiguration();
        }
    }
}

