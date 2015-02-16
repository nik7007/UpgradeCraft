package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.client.render.item.ItemRenderWoodenTank;
import com.nik7.upgcraft.client.render.item.itemRenderFluidFurnace;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderBasicFluidHopper;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderFluidFurnace;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRendererWoodenTank;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.RenderIds;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import com.nik7.upgcraft.tileentities.UpgCtileentityTankSmall;
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

        RenderIds.WOODEN_FLUID_TANK = RenderingRegistry.getNextAvailableRenderId();
        RenderIds.BASIC_FLUID_HOPPER = RenderingRegistry.getNextAvailableRenderId();
        RenderIds.FLUID_FURNACE = RenderingRegistry.getNextAvailableRenderId();

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockWoodenLiquidTank), new ItemRenderWoodenTank());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockFluidFurnace), new itemRenderFluidFurnace());

        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtileentityTankSmall.class, new TileEntityRendererWoodenTank());
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtilientityFluidHopper.class, new TileEntityRenderBasicFluidHopper());
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtileentityFluidFurnace.class, new TileEntityRenderFluidFurnace());

    }

    @Override
    public void registerEventHandlers() {

    }

    @Override
    public void registerKeybindings() {

    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
