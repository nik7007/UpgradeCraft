package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.handler.PlayerEventHandler;


public abstract class CommonProxy implements IProxy {

    public void registerTileEntities() {


    }

    public void registerEventHandlers() {

        PlayerEventHandler.init();

    }
}
