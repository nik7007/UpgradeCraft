package com.nik7.upgcraft.fluids;


import com.nik7.upgcraft.nbt.INBTProvider;
import com.nik7.upgcraft.reference.ConfigOptions;
import net.minecraft.nbt.NBTTagCompound;

public enum EnumCapacity implements INBTProvider<EnumCapacity> {

    BASIC_CAPACITY(0),
    DOUBLE_CAPACITY(1),
    FUNNEL_CAPACITY(2),
    MACHINE_CAPACITY(3),
    ERROR_CAPACITY(Integer.MAX_VALUE);

    private final int i;

    EnumCapacity(int i) {
        this.i = i;
    }

    public static int getCapacity(EnumCapacity capacity) {
        switch (capacity) {
            case BASIC_CAPACITY:
                return ConfigOptions.BASIC_CAPACITY;
            case DOUBLE_CAPACITY:
                return ConfigOptions.DOUBLE_CAPACITY;
            case FUNNEL_CAPACITY:
                return ConfigOptions.FUNNEL_CAPACITY;
            case MACHINE_CAPACITY:
                return ConfigOptions.MACHINE_CAPACITY;
            default:
                return 0;
        }
    }

    public static EnumCapacity getDoubleCapacity(EnumCapacity capacity) {
        if (capacity == BASIC_CAPACITY)
            return DOUBLE_CAPACITY;

        return ERROR_CAPACITY;
    }

    @Override
    public EnumCapacity readFromNBT(NBTTagCompound nbt) {
        switch (nbt.getInteger("enum_capacity")) {
            case 0:
                return BASIC_CAPACITY;
            case 1:
                return DOUBLE_CAPACITY;
            case 2:
                return FUNNEL_CAPACITY;
            case 3:
                return MACHINE_CAPACITY;
            default:
                return ERROR_CAPACITY;
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("enum_capacity", this.i);

        return nbt;
    }

}
