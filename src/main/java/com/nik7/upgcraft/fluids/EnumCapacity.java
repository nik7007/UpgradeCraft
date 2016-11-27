package com.nik7.upgcraft.fluids;


import com.nik7.upgcraft.reference.ConfigOptions;

public enum EnumCapacity {

    BASIC_CAPACITY,
    DOUBLE_CAPACITY,
    FLUID_HOPPER_CAPACITY;

    public static int getCapacity(EnumCapacity capacity){
        switch (capacity) {
            case BASIC_CAPACITY:
                return ConfigOptions.BASIC_CAPACITY;
            case DOUBLE_CAPACITY:
                return ConfigOptions.DOUBLE_CAPACITY;
            case FLUID_HOPPER_CAPACITY:
                return ConfigOptions.FLUID_HOPPER_CAPACITY;
            default:
                return 0;
        }
    }

}
