package com.nik7.upgcraft.config;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Capacity;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class SystemConfig {
    private static SystemConfig ourInstance = new SystemConfig();

    public boolean basicWoodenBlockFlammability;

    public int basicTankCapacity;

    public static SystemConfig getInstance() {
        return ourInstance;
    }

    private SystemConfig() {
    }

    public static void applyConfig() {

        Capacity.SMALL_TANK = getInstance().basicTankCapacity * FluidContainerRegistry.BUCKET_VOLUME;

        ConfigValue value = new ConfigValue("basicTankCapacity", (new Integer(Capacity.SMALL_TANK)).toString());

        ((ConfigurableObject) ModBlocks.blockUpgCWoodenFluidTank).appliedConfig(new ConfigValue("basicWoodenBlockFlammability", (Boolean.valueOf(getInstance().basicWoodenBlockFlammability)).toString()), value);
        /*((ConfigurableObject) ModBlocks.blockClayLiquidTank).appliedConfig(value);*/

    }

    public static class ConfigValue {

        public String configName;
        public String value;

        public ConfigValue(String configName, String value) {
            this.value = value;
            this.configName = configName;
        }
    }
}
