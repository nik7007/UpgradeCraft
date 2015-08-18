package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.client.render.item.*;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderBasicFluidHopper;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderEnderHopper;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderFluidMachine;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRendererTank;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.tileentities.UpgCtilientityEnderHopper;
import com.nik7.upgcraft.tileentities.UpgCtilientityFluidHopper;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import java.io.File;

public class ClientProxy extends CommonProxy {
    @Override
    public void initClientConfiguration(File configFile) {

    }

    @Override
    public void initRenderingAndTextures() {

        Render.Ids.FLUID_TANK = RenderingRegistry.getNextAvailableRenderId();
        Render.Ids.BASIC_FLUID_HOPPER = RenderingRegistry.getNextAvailableRenderId();
        Render.Ids.ENDER_HOPPER = RenderingRegistry.getNextAvailableRenderId();
        Render.Ids.FLUID_MACHINE = RenderingRegistry.getNextAvailableRenderId();

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockWoodenLiquidTank), new ItemRenderWoodenTank());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockClayLiquidTank), new ItemRenderClayTank());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockUpgCEnderTank), new ItemRenderEnderTank());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockFluidFurnace), new itemRenderFluidFurnace());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockFluidInfuse), new itemRenderFluidInfuser());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockUpgCTermoFluidFurnace), new itemRenderTermoFluidFurnace());

        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtileentityTank.class, new TileEntityRendererTank());
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtilientityFluidHopper.class, new TileEntityRenderBasicFluidHopper());
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtilientityEnderHopper.class, new TileEntityRenderEnderHopper());
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtileentityInventoryFluidHandler.class, new TileEntityRenderFluidMachine());

    }

    @Override
    public void registerKeybindings() {

    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
