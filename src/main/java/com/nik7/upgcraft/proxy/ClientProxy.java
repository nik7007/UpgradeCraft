package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.client.render.item.ItemRendererWoodenTank;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderBasicFluidHopper;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRendererWoodenTank;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.RenderIds;
import com.nik7.upgcraft.tileentities.UpgCtileentityTankSmall;
import com.nik7.upgcraft.tileentities.UpgCtilientityFluidHopper;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import java.io.File;

public class ClientProxy extends CommonProxy {
    @Override
    public void initClientConfiguration(File configFile) {

    }

    @Override
    public void initRenderingAndTextures() {

        RenderIds.WOODEN_FLUID_TANK = RenderingRegistry.getNextAvailableRenderId();
        RenderIds.BASIC_FLUID_HOPPER = RenderingRegistry.getNextAvailableRenderId();

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockWoodenLiquidTank), new ItemRendererWoodenTank());
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtileentityTankSmall.class, new TileEntityRendererWoodenTank());
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtilientityFluidHopper.class, new TileEntityRenderBasicFluidHopper());

    }

    @Override
    public void registerEventHandlers() {

    }

    @Override
    public void registerKeybindings() {

    }
}
