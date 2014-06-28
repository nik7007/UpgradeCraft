package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {

    public void registerTileEntities() {

        GameRegistry.registerTileEntity(UpgCtileentityTank.class, "LiquidTankEntity");
    }
}
