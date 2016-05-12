package com.nik7.upgcraft.util;


import net.minecraft.nbt.NBTTagCompound;

public interface INBTTagProvider<T> {

    public void writeToNBT(NBTTagCompound tag);

    public T getFomNBT(NBTTagCompound nbt);

}
