package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.client.render.item.ItemRendererWoodenTank;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRendererWoodenTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityTankSmall;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import java.io.File;

public class ClientProxy extends CommonProxy {
    @Override
    public void initClientConfiguration(File configFile) {

    }

    @Override
    public void initRenderingAndTextures() {

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockWoodenLiquidTank),new ItemRendererWoodenTank());
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtileentityTankSmall.class, new TileEntityRendererWoodenTank());

    }

    @Override
    public void registerEventHandlers() {

    }

    @Override
    public void registerKeybindings() {

    }
}
