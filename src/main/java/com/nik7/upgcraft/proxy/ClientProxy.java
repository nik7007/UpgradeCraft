package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.block.BlockUpgC;
import com.nik7.upgcraft.client.render.item.ItemStackRender;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderFluidHopper;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderFluidMachine;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRendererFluidTank;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
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
        modelMesher.register(twItem, 0, createLocation(blockUpgCWoodenFluidTank));
        modelMesher.register(twItem, 1, createLocation(blockUpgCWoodenFluidTank));

        Item bFHItem = Item.getItemFromBlock(blockUpgCBasicFluidHopper);

        ModelResourceLocation resLocSTD = createLocation(blockUpgCBasicFluidHopper);
        ModelResourceLocation res8 = createLocation(blockUpgCBasicFluidHopper, "BurnedDown");
        ModelResourceLocation res24 = createLocation(blockUpgCBasicFluidHopper, "BurnedSide");
        ModelBakery.registerItemVariants(bFHItem, resLocSTD, res8, res24);

        modelMesher.register(bFHItem, 0, resLocSTD);
        modelMesher.register(bFHItem, 8, res8);
        modelMesher.register(bFHItem, 24, res24);

        modelMesher.register(Item.getItemFromBlock(blockUpgCFluidFurnace), 0, createLocation(blockUpgCFluidFurnace));
        modelMesher.register(Item.getItemFromBlock(blockUpgCFluidInfuser), 0, createLocation(blockUpgCFluidInfuser));

        Item clayTank = Item.getItemFromBlock(blockUpgCClayFluidTank);
        modelMesher.register(clayTank, 0, createLocation(blockUpgCClayFluidTank));
        modelMesher.register(clayTank, 1, createLocation(blockUpgCClayFluidTank));
        modelMesher.register(clayTank, 2, createLocation(blockUpgCClayFluidTank));
        modelMesher.register(clayTank, 3, createLocation(blockUpgCClayFluidTank));


        TileEntityItemStackRenderer.instance = new ItemStackRender(TileEntityItemStackRenderer.instance);

    }

    private ModelResourceLocation createLocation(BlockUpgC block, String variant) {

        return new ModelResourceLocation(Reference.MOD_ID + ":" + block.getName() + variant, "inventory");
    }

    private ModelResourceLocation createLocation(BlockUpgC block) {

        return createLocation(block, "");
    }

    private void registerTileEntitySpecialRender() {
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtileentityFluidTank.class, new TileEntityRendererFluidTank());
        ClientRegistry.bindTileEntitySpecialRenderer(UpgCtilientityBasicFluidHopper.class, new TileEntityRenderFluidHopper());
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
