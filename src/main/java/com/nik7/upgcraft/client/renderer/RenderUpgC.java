package com.nik7.upgcraft.client.renderer;

import com.nik7.upgcraft.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUpgC {

    public static void initRenderingAndTextures() {
        RenderUpgC.initModel(ModBlocks.slimyLog);
        RenderUpgC.initModel(ModBlocks.fluidTank);
    }

    private static void initModel(Block block) {
        initModel(Item.getItemFromBlock(block));
    }

    private static void initModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, createModelResourceLocation(item));
    }

    private static ModelResourceLocation createModelResourceLocation(IForgeRegistryEntry forgeRegistryEntry) {
        return new ModelResourceLocation(forgeRegistryEntry.getRegistryName(), "inventory");
    }


}
