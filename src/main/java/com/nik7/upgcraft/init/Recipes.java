package com.nik7.upgcraft.init;


import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

    public static void init() {

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockSlimyLog), "sss", "sps", "sss", 's', "slimeball", 'p',"plankWood"));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockWoodenLiquidTank,1,0),"sss","s s","sss",'s',new ItemStack(ModBlocks.blockSlimyLog));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockWoodenLiquidTank,1,1),"sss","sgs","sss",'g',"blockGlass",'s',new ItemStack(ModBlocks.blockSlimyLog)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockFluidBasicHopper),"i i","iti"," i ", 'i',"ingotIron",'t', new ItemStack(ModBlocks.blockWoodenLiquidTank)));

    }
}
