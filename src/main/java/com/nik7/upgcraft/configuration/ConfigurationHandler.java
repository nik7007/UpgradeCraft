package com.nik7.upgcraft.configuration;

import com.nik7.upgcraft.block.InitBlocks;
import net.minecraftforge.common.config.Configuration;

import java.io.File;


public class ConfigurationHandler {

    public static void init(File configFile) {

        Configuration configuration = new Configuration(configFile);
        boolean basic_Wooden_Block_Flammability = true;

        try {
            configuration.load();

            basic_Wooden_Block_Flammability = configuration.get("Flammability", "basic_Wooden_Block_Flammability", true, "Set to false to make fireproof Wooden Liquid Tank, and Slimy Log.").getBoolean(true);
        } catch (Exception e) {

        } finally {

            configuration.save();
        }

    }
}
