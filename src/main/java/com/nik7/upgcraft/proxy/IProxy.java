package com.nik7.upgcraft.proxy;

import java.io.File;

public interface IProxy {

    public abstract void initClientConfiguration(File configFile);

    public abstract void registerTileEntities();

    public abstract void initRenderingAndTextures();

    public abstract void registerEventHandlers();

    public abstract void registerKeybindings();
}
