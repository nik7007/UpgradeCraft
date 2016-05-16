package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.INBTTagProvider;

public interface IRedstoneElement extends INBTTagProvider<IRedstoneElement> {
    void exec();
}
