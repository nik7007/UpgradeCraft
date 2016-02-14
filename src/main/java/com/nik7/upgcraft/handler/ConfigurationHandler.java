package com.nik7.upgcraft.handler;

import com.nik7.upgcraft.config.SystemConfig;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

        SystemConfig.getInstance().basicWoodenFluidTankFlammability = configuration.getBoolean("basic Wooden Block Flammability", Configuration.CATEGORY_GENERAL, true, "Set to false to make fireproof Wooden Liquid Tank.");
        SystemConfig.getInstance().basicFluidHopperCanBurn = configuration.getBoolean("basic Fluid Hopper can burn", Configuration.CATEGORY_GENERAL, true, "Set to false to not allow Basic Fluid Hopper to burn");
        SystemConfig.getInstance().basicTankCapacity = configuration.getInt("basic tank capacity", Configuration.CATEGORY_GENERAL, 27, 24, 32, "Basic capacity Tank custom value. This will not change the capacity of the enderTanks.");

        if (configuration.hasChanged()) {
            configuration.save();
            try {
                SystemConfig.applyConfig();
            } catch (NullPointerException e) {
                LogHelper.info("First time create Config!");
            }
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfiguration();
        }
    }
}
