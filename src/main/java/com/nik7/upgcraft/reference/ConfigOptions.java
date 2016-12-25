package com.nik7.upgcraft.reference;


import net.minecraftforge.fluids.Fluid;

public class ConfigOptions {

    public static final int FUNNEL_CAPACITY = 5 * Fluid.BUCKET_VOLUME;
    public static final int BASIC_FUNNEL_SPEED = 50;
    //CAPACITY
    public static int CONFIG_CAPACITY ;
    public static int BASIC_CAPACITY = CONFIG_CAPACITY * Fluid.BUCKET_VOLUME;
    public static int DOUBLE_CAPACITY = 2 * BASIC_CAPACITY;
    //BURN
    public static boolean WOODEN_TANK_BURN;
    public static boolean WOODEN_FLUID_HOPPER_BURN;

    public static void reloadOptions() {
        BASIC_CAPACITY = CONFIG_CAPACITY * Fluid.BUCKET_VOLUME;
        DOUBLE_CAPACITY = 2 * BASIC_CAPACITY;
    }


}
