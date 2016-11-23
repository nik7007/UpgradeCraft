package com.nik7.upgcraft.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

    public static void init(){

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.slimyLog), "sss", "sps", "sss", 's', "slimeball", 'p', "plankWood"));

    }

}
