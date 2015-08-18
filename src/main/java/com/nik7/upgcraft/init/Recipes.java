package com.nik7.upgcraft.init;


import com.nik7.upgcraft.registry.FluidInfuserRegister;
import com.nik7.upgcraft.registry.TermoSmeltingRegister;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

    public static void init() {

        //Crafting recipes
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockSlimyLog), "sss", "sps", "sss", 's', "slimeball", 'p', "plankWood"));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockWoodenLiquidTank, 1, 0), "sss", "s s", "sss", 's', new ItemStack(ModBlocks.blockSlimyLog));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockWoodenLiquidTank, 1, 1), "sss", "sgs", "sss", 'g', "blockGlass", 's', new ItemStack(ModBlocks.blockSlimyLog)));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockClayLiquidTank, 1, 0), "ccc", "c c", "ccc", 'c', new ItemStack(ModItems.itemClayeyIronIngot));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockUpgCEnderTank), "ooo", "oeo", "ooo", 'o', new ItemStack(ModBlocks.blockUpgCSlimyObsidian), 'e', new ItemStack(Items.ender_eye));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockClayLiquidTank, 1, 1), "ccc", "cgc", "ccc", 'g', "blockGlass", 'c', new ItemStack(ModItems.itemClayeyIronIngot)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockFluidBasicHopper), "i i", "iti", " i ", 'i', "ingotIron", 't', new ItemStack(ModBlocks.blockWoodenLiquidTank)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockFluidFurnace), "ifi", "iti", "iii", 'i', "ingotIron", 't', new ItemStack(ModBlocks.blockWoodenLiquidTank, 1, 1), 'f', new ItemStack(Blocks.furnace)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockFluidInfuse), "iii", "i i", "ifi", 'i', "ingotIron", 'f', new ItemStack(ModBlocks.blockFluidFurnace)));

        //Fluid Infuser recipes
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("lava"), 200), new ItemStack(ModBlocks.blockSlimyLog), new ItemStack(Items.slime_ball, 2), 10, new ItemStack(Blocks.planks), 200);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("lava"), 400), new ItemStack(ModBlocks.blockUpgCSlimyObsidian), new ItemStack(Items.slime_ball, 4), 20, new ItemStack(Blocks.obsidian), 400);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("water"), 250), new ItemStack(ModItems.itemClayeyIronIngot), new ItemStack(Items.clay_ball), 50, new ItemStack(Items.iron_ingot), 400);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("water"), 4 * 250), new ItemStack(ModItems.itemClayeyIronIngot, 4), new ItemStack(Blocks.clay), 4 * 50, new ItemStack(Items.iron_ingot, 4), 400);
        TermoSmeltingRegister.addRecipe(new ItemStack(ModBlocks.blockClayLiquidTank, 1, 0), new ItemStack(ModBlocks.blockClayLiquidTank, 1, 2), 1100, 400);
        TermoSmeltingRegister.addRecipe(new ItemStack(ModBlocks.blockClayLiquidTank, 1, 1), new ItemStack(ModBlocks.blockClayLiquidTank, 1, 3), 1100, 400);
    }
}
