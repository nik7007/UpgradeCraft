package com.nik7.upgcraft.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

    public static void init() {

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockSlimyLog), "sss", "sps", "sss", 's', "slimeball", 'p', "plankWood"));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockWoodenFluidTank, 1, 0), "sss", "s s", "sss", 's', new ItemStack(ModBlocks.blockSlimyLog));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockWoodenFluidTank, 1, 1), "sss", "sgs", "sss", 'g', "blockGlass", 's', new ItemStack(ModBlocks.blockSlimyLog)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockBasicFunnel), "ibi", "iti", " i ", 'i', "ingotIron", 'b', new ItemStack(Blocks.IRON_BARS), 't', new ItemStack(ModBlocks.blockWoodenFluidTank)));
    }

}
