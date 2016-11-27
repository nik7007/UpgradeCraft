package com.nik7.upgcraft.nbt;


import net.minecraft.nbt.NBTTagCompound;

public interface INBTProvider<T> {

    T readFromNBT(NBTTagCompound nbt);

    NBTTagCompound writeToNBT(NBTTagCompound nbt);
}
