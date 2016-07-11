package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.INBTTagProvider;

public interface IRedstoneElement extends INBTTagProvider<IRedstoneElement> {

    /**
     * Execute the task for the current tick
     */
    void exec();

    /**
     * ReSet stored values and other parameters
     */
    void reSet();
}
