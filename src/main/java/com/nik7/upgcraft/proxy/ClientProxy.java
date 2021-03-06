package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.block.BlockFluidUpgC;
import com.nik7.upgcraft.block.IBlockUpgC;
import com.nik7.upgcraft.client.render.item.ItemStackRender;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderFluidHopper;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRenderFluidMachine;
import com.nik7.upgcraft.client.render.tileentity.TileEntityRendererFluidTank;
import com.nik7.upgcraft.item.ItemUpgC;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
import com.nik7.upgcraft.tileentities.UpgCtilientityBasicFluidHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.io.File;

import static com.nik7.upgcraft.init.ModBlocks.*;
import static com.nik7.upgcraft.init.ModFluids.blockFluidUpgCActiveLava;
import static com.nik7.upgcraft.init.ModItems.itemUpgCClayIngot;

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

        modelMesher.register(Item.getItemFromBlock(blockUpgCSlimyLog), 0, createLocation(blockUpgCSlimyLog));
        modelMesher.register(Item.getItemFromBlock(blockUpgCSlimyObsidian), 0, createLocation(blockUpgCSlimyObsidian));
        modelMesher.register(Item.getItemFromBlock(blockUpgCEnderFluidTank), 0, createLocation(blockUpgCEnderFluidTank));

        fluidRender(blockFluidUpgCActiveLava);

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
        modelMesher.register(Item.getItemFromBlock(blockUpgCActiveLavaMaker), 0, createLocation(blockUpgCActiveLavaMaker));
        modelMesher.register(Item.getItemFromBlock(blockUpgCThermoFluidFurnace), 0, createLocation(blockUpgCThermoFluidFurnace));

        Item clayTank = Item.getItemFromBlock(blockUpgCClayFluidTank);
        modelMesher.register(clayTank, 0, createLocation(blockUpgCClayFluidTank));
        modelMesher.register(clayTank, 1, createLocation(blockUpgCClayFluidTank));
        modelMesher.register(clayTank, 2, createLocation(blockUpgCClayFluidTank));
        modelMesher.register(clayTank, 3, createLocation(blockUpgCClayFluidTank));

        Item ironTank = Item.getItemFromBlock(blockUpgCIronFluidTank);
        modelMesher.register(ironTank, 0, createLocation(blockUpgCIronFluidTank));
        modelMesher.register(ironTank, 1, createLocation(blockUpgCIronFluidTank));


        modelMesher.register(itemUpgCClayIngot, 0, createLocation(itemUpgCClayIngot));

        modelMesher.register(Item.getItemFromBlock(blockUpgCFluidHopper), 0, createLocation(blockUpgCFluidHopper));

        Item mold = Item.getItemFromBlock(blockUpgCFluidTankMold);
        ModelResourceLocation moldRL = createLocation(blockUpgCFluidTankMold);
        ModelResourceLocation moldCookedRL = createLocation(blockUpgCFluidTankMold, "Cooked");
        ModelBakery.registerItemVariants(mold, moldRL, moldCookedRL);
        modelMesher.register(mold, 0, moldRL);
        modelMesher.register(mold, 1, moldRL);
        modelMesher.register(mold, 4, moldCookedRL);
        modelMesher.register(mold, 5, moldCookedRL);
        modelMesher.register(mold, 6, moldCookedRL);
        modelMesher.register(mold, 7, moldCookedRL);

        Item clayBrick = Item.getItemFromBlock(blockUpgCClayBrick);
        ModelResourceLocation clayBrickRL = createLocation(blockUpgCClayBrick);
        ModelResourceLocation clayBrickCookedRL = createLocation(blockUpgCClayBrick, "Cooked");
        ModelResourceLocation claySquaredBrickRL = createLocation(blockUpgCClayBrick, "Squared");
        ModelResourceLocation claySquaredBrickCookedRL = createLocation(blockUpgCClayBrick, "CookedSquared");
        ModelBakery.registerItemVariants(clayBrick, clayBrickRL, clayBrickCookedRL, claySquaredBrickRL, claySquaredBrickCookedRL);
        modelMesher.register(clayBrick, 0, clayBrickRL);
        modelMesher.register(clayBrick, 1, clayBrickCookedRL);
        modelMesher.register(clayBrick, 2, claySquaredBrickRL);
        modelMesher.register(clayBrick, 3, claySquaredBrickCookedRL);

        modelMesher.register(Item.getItemFromBlock(blockUpgCStairsClayBrick), 0, createLocation(blockUpgCStairsClayBrick));
        modelMesher.register(Item.getItemFromBlock(blockUpgCStairsCookedClayBrick), 0, createLocation(blockUpgCStairsCookedClayBrick));

        TileEntityItemStackRenderer.instance = new ItemStackRender(TileEntityItemStackRenderer.instance);

    }


    private void fluidRender(BlockFluidUpgC block) {

        Item item = Item.getItemFromBlock(block);
        ModelBakery.registerItemVariants(item);
        final ModelResourceLocation location = new ModelResourceLocation(Reference.MOD_ID + ":" + block.getName(), "fluid");

        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return location;
            }
        });
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return location;
            }
        });
    }

    private ModelResourceLocation createLocation(IBlockUpgC block, String variant) {

        return createLocation(Reference.MOD_ID + ":" + block.getName() + variant);
    }

    private ModelResourceLocation createLocation(IBlockUpgC block) {

        return createLocation(block, "");
    }

    private ModelResourceLocation createLocation(ItemUpgC item) {

        return createLocation(item, "");
    }

    private ModelResourceLocation createLocation(ItemUpgC item, String variant) {
        return createLocation(Reference.MOD_ID + ":" + item.getName() + variant);
    }

    private ModelResourceLocation createLocation(String location) {
        return new ModelResourceLocation(location, "inventory");
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
