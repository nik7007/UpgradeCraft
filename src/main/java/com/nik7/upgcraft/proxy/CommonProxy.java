package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.tileentities.WoodenLiquidTankEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(WoodenLiquidTankEntity.class, "WoodenLiquidTankEntity");
    }
}
