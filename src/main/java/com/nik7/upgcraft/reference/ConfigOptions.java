package com.nik7.upgcraft.reference;


import net.minecraftforge.fluids.Fluid;

public final class ConfigOptions {


    public static final int FUNNEL_CAPACITY = 5 * Fluid.BUCKET_VOLUME;
    public static final int BASIC_FUNNEL_SPEED = 50;
    //TEMP
    public final static int WOOD_BURN_TEMPERATURE = 300 + 273;
    public final static int CLAY_COOK = 1000 + 273;
    //CAPACITY
    public static int CONFIG_CAPACITY;
    public static int BASIC_CAPACITY = CONFIG_CAPACITY * Fluid.BUCKET_VOLUME;
    public static int DOUBLE_CAPACITY = 2 * BASIC_CAPACITY;
    public static int MACHINE_CAPACITY = BASIC_CAPACITY - FUNNEL_CAPACITY;
    //BURN
    public static boolean WOODEN_TANK_BURN;
    public static boolean WOODEN_FLUID_HOPPER_BURN;

    private ConfigOptions() {
    }

    public static void reloadOptions() {
        BASIC_CAPACITY = CONFIG_CAPACITY * Fluid.BUCKET_VOLUME;
        DOUBLE_CAPACITY = 2 * BASIC_CAPACITY;
        MACHINE_CAPACITY = BASIC_CAPACITY - FUNNEL_CAPACITY;
    }


}
