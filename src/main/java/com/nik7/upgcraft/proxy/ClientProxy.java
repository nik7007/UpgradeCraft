package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.client.render.item.ItemStackRender;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderFluidHopper;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRendererTank;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.tileentities.UpgCtilientityBasicFluidHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.io.File;

import static com.nik7.upgcraft.init.ModBlocks.*;

public class ClientProxy extends CommonProxy {

    @Override
    public void initClientConfiguration(File configFile) {

    }

    @Override
    public void initRenderingAndTextures() {

        this.registerItemAndBlockRender();
        this.registerTileEntitySpecialRender();

    }

    private void registerItemAndBlockRender() {

        ItemModelMesher modelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

        modelMesher.register(Item.getItemFromBlock(blockUpgCSlimyLog), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + blockUpgCSlimyLog.getName(), "inventory"));

        Item twItem = Item.getItemFromBlock(blockUpgCWoodenFluidTank);
        modelMesher.register(twItem, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + blockUpgCWoodenFluidTank.getName(), "inventory"));
        modelMesher.register(twItem, 1, new ModelResourceLocation(Reference.MOD_ID + ":" + blockUpgCWoodenFluidTank.getName(), "inventory"));

        Item bFHItem = Item.getItemFromBlock(blockUpgCBasicFluidHopper);

        ModelResourceLocation resLocSTD = new ModelResourceLocation(Reference.MOD_ID + ":" + blockUpgCBasicFluidHopper.getName(), "inventory");
        ModelResourceLocation res8 = new ModelResourceLocation(Reference.MOD_ID + ":" + blockUpgCBasicFluidHopper.getName() + "BurnedDown", "inventory");
        ModelResourceLocation res24 = new ModelResourceLocation(Reference.MOD_ID + ":" + blockUpgCBasicFluidHopper.getName() + "BurnedSide", "inventory");
        ModelBakery.registerItemVariants(bFHItem, resLocSTD, res8, res24);

        modelMesher.register(bFHItem, 0, resLocSTD);
        modelMesher.register(bFHItem, 8, res8);
        modelMesher.register(bFHItem, 24, res24);

        TileEntityItemStackRenderer.instance = new ItemStackRender(TileEntityItemStackRenderer.instance);

    }

    private void registerTileEntitySpecialRender() {
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtileentityTank.class, new TileEntityRendererTank());
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtilientityBasicFluidHopper.class, new TileEntityRenderFluidHopper());
    }

    @Override
    public void registerKeybindings() {

    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
