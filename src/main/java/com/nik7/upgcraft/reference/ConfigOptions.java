package com.nik7.upgcraft.reference;


import net.minecraftforge.fluids.Fluid;

public class ConfigOptions {

    //CAPACITY
    public static int CONFIG_CAPACITY ;
    public static int BASIC_CAPACITY = CONFIG_CAPACITY * Fluid.BUCKET_VOLUME;
    public static int DOUBLE_CAPACITY = 2 * BASIC_CAPACITY;

    public  static final int FLUID_HOPPER_CAPACITY = 5 * Fluid.BUCKET_VOLUME;

    public static void reloadOptions(){
        BASIC_CAPACITY = CONFIG_CAPACITY * Fluid.BUCKET_VOLUME;
        DOUBLE_CAPACITY = 2 * BASIC_CAPACITY;
    }


    //BURN
    public static boolean WOODEN_TANK_BURN;
    public static boolean WOODEN_FLUID_HOPPER_BURN;


}
