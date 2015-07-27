package com.nik7.upgcraft.proxy;

import net.minecraft.entity.player.EntityPlayer;

import java.io.File;

public class ServerProxy extends CommonProxy {
    @Override
    public void initClientConfiguration(File configFile) {

    }

    @Override
    public void initRenderingAndTextures() {

    }

    @Override
    public void registerKeybindings() {

    }

    @Override
    public EntityPlayer getClientPlayer() {
        return null;
    }
}
