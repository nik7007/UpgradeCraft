package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.client.render.tileentity.TileEntityRendererTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityWoodenFluidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.io.File;

public class ClientProxy extends CommonProxy {

    @Override
    public void initClientConfiguration(File configFile) {

    }

    @Override
    public void initRenderingAndTextures() {

        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtileentityWoodenFluidTank.class, new TileEntityRendererTank());

    }

    @Override
    public void registerKeybindings() {

    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
