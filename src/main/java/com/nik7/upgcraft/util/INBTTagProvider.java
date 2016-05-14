package com.nik7.upgcraft.util;


import net.minecraft.nbt.NBTTagCompound;

public interface INBTTagProvider<T> {

    void writeToNBT(NBTTagCompound tag);

    T readFomNBT(NBTTagCompound tag);

}
