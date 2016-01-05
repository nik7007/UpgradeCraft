package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.handler.PlayerEventHandler;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import net.minecraftforge.fml.common.registry.GameRegistry;


public abstract class CommonProxy implements IProxy {

    public void registerTileEntities() {

    }

    public void registerEventHandlers() {

        PlayerEventHandler.init();

    }
}
