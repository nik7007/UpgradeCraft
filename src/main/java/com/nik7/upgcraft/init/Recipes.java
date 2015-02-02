package com.nik7.upgcraft.init;


import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

    public static void init() {

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockSlimyLog), "sss", "sps", "sss", 's', "slimeball", 'p',"plankWood"));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockWoodenLiquidTank),"sss","s s","sss",'s',new ItemStack(ModBlocks.blockSlimyLog));

    }
}