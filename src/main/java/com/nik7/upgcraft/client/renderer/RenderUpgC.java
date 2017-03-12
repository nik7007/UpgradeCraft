package com.nik7.upgcraft.client.renderer;

import com.nik7.upgcraft.block.BlockUpgC;
import com.nik7.upgcraft.client.renderer.tileentity.FluidMachineTESR;
import com.nik7.upgcraft.client.renderer.tileentity.FluidTankTESR;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.tileentity.TileEntityFluidFurnace;
import com.nik7.upgcraft.tileentity.TileEntityFluidInfuser;
import com.nik7.upgcraft.tileentity.TileEntityFluidTank;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUpgC {

    public static void initRenderingAndTextures() {
        RenderUpgC.initModel(ModBlocks.blockSlimyLog);
        RenderUpgC.initFluidTankModel(ModBlocks.blockWoodenFluidTank);
        RenderUpgC.initModel(ModBlocks.blockFluidFurnace);
        RenderUpgC.initModel(ModBlocks.blockFluidInfuser);
        RenderUpgC.initModel(ModBlocks.blockFunnel);

        //basic funnel
        Item basicFunnelItem = Item.getItemFromBlock(ModBlocks.blockBasicFunnel);
        ModelResourceLocation normal = createModelResourceLocation(basicFunnelItem, "normal");
        ModelResourceLocation burned = createModelResourceLocation(basicFunnelItem, "burned");
        ModelResourceLocation burnedSide = createModelResourceLocation(basicFunnelItem, "burned_side");
        ModelBakery.registerItemVariants(basicFunnelItem, normal, burned, burnedSide);
        RenderUpgC.initModel(basicFunnelItem, 0, normal);
        RenderUpgC.initModel(basicFunnelItem, 1, burned);
        RenderUpgC.initModel(basicFunnelItem, 2, burnedSide);

    }

    private static void initFluidTankModel(BlockUpgC fluidTank) {
        Item twItem = Item.getItemFromBlock(fluidTank);

        ModelResourceLocation normal = createModelResourceLocation(twItem);
        ModelResourceLocation glassed = createModelResourceLocation(twItem, "glassed");

        ModelBakery.registerItemVariants(twItem, normal, glassed);

        RenderUpgC.initModel(twItem, 0, normal);
        RenderUpgC.initModel(twItem, 1, glassed);
    }

    private static void initModel(Block block) {
        initModel(Item.getItemFromBlock(block));
    }

    private static void initModel(Item item) {
        initModel(item, 0);
    }

    private static void initModel(Item item, int meta) {
        initModel(item, meta, createModelResourceLocation(item));
    }

    private static void initModel(Item item, int meta, ModelResourceLocation modelResourceLocation) {
        ModelLoader.setCustomModelResourceLocation(item, meta, modelResourceLocation);
    }

    private static ModelResourceLocation createModelResourceLocation(IForgeRegistryEntry forgeRegistryEntry) {
        return createModelResourceLocation(forgeRegistryEntry, null);
    }

    private static ModelResourceLocation createModelResourceLocation(IForgeRegistryEntry forgeRegistryEntry, String variant) {
        String registryName = forgeRegistryEntry.getRegistryName() + (variant == null ? "" : "_" + variant);
        return new ModelResourceLocation(registryName, "inventory");
    }


    public static void registerTileEntitySpecialRender() {

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidTank.class, new FluidTankTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidFurnace.class, new FluidMachineTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidInfuser.class, new FluidMachineTESR());
    }

}
