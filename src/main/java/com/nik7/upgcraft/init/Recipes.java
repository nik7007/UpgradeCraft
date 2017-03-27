package com.nik7.upgcraft.init;

import com.nik7.upgcraft.registry.FluidInfuserRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

    public static void init() {

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockSlimyLog), "sss", "sps", "sss", 's', "slimeball", 'p', "plankWood"));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockWoodenFluidTank, 1, 0), "sss", "s s", "sss", 's', new ItemStack(ModBlocks.blockSlimyLog));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockWoodenFluidTank, 1, 1), "sss", "sgs", "sss", 'g', "blockGlass", 's', new ItemStack(ModBlocks.blockSlimyLog)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockBasicFunnel), "ibi", "iti", " i ", 'i', "ingotIron", 'b', new ItemStack(Blocks.IRON_BARS), 't', new ItemStack(ModBlocks.blockWoodenFluidTank)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockFluidFurnace), "ifi", "iti", "iii", 'i', "ingotIron", 't', new ItemStack(ModBlocks.blockWoodenFluidTank, 1, 1), 'f', new ItemStack(Blocks.FURNACE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockFluidInfuser), "iii", "i i", "ifi", 'i', "ingotIron", 'f', new ItemStack(ModBlocks.blockFluidFurnace)));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockClayBrick, 1, 0), "ii", "ii", 'i', new ItemStack(ModItems.itemClayIngot));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockClayBrick, 4, 2), "bb", "bb", 'b', new ItemStack(ModBlocks.blockClayBrick, 1, 0));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockStairsClayBrick, 4, 0), "b  ", "bb ", "bbb", 'b', new ItemStack(ModItems.itemClayIngot, 1, 0));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockStairsCookedClayBrick, 4, 0), "b  ", "bb ", "bbb", 'b', new ItemStack(ModBlocks.blockClayBrick, 1, 1));

        //Fluid Infuser recipes
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.LAVA, 200), new ItemStack(ModBlocks.blockSlimyLog), new ItemStack(Items.SLIME_BALL, 2), 10, new ItemStack(Blocks.PLANKS), 200);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.LAVA, 4 * 200), new ItemStack(ModBlocks.blockSlimyLog, 4), new ItemStack(Blocks.SLIME_BLOCK, 1), 4 * 10, new ItemStack(Blocks.PLANKS, 4), 200);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.WATER, 250), new ItemStack(ModItems.itemClayIngot), new ItemStack(Items.CLAY_BALL), 50, new ItemStack(Items.BRICK), 400);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.WATER, 4 * 250), new ItemStack(ModItems.itemClayIngot, 4), new ItemStack(Blocks.CLAY), 4 * 50, new ItemStack(Items.BRICK, 4), 400);
    }

}
