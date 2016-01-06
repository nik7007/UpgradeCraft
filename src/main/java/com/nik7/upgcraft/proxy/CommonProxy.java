package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.handler.PlayerEventHandler;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentities.UpgCtileentityWoodenFluidTank;
import net.minecraftforge.fml.common.registry.GameRegistry;


public abstract class CommonProxy implements IProxy {

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(UpgCtileentityWoodenFluidTank.class, Reference.RESOURCE_PREFIX + "UpgCtileentityWoodenFluidTank");
    }

    public void registerEventHandlers() {

        PlayerEventHandler.init();

    }
}
