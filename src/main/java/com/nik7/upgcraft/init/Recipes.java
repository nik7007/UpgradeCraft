package com.nik7.upgcraft.init;


import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

    public static void init() {

        //Crafting recipes
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUpgCSlimyLog), "sss", "sps", "sss", 's', "slimeball", 'p', "plankWood"));

        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockUpgCWoodenFluidTank, 1, 0), "sss", "s s", "sss", 's', new ItemStack(ModBlocks.blockUpgCSlimyLog));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUpgCWoodenFluidTank, 1, 1), "sss", "sgs", "sss", 'g', "blockGlass", 's', new ItemStack(ModBlocks.blockUpgCSlimyLog)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUpgCBasicFluidHopper), "i i", "iti", " i ", 'i', "ingotIron", 't', new ItemStack(ModBlocks.blockUpgCWoodenFluidTank)));

        /*//Fluid Infuser recipes
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("lava"), 200), new ItemStack(ModBlocks.blockSlimyLog), new ItemStack(Items.slime_ball, 2), 10, new ItemStack(Blocks.planks), 200);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("lava"), 400), new ItemStack(ModBlocks.blockUpgCSlimyObsidian), new ItemStack(Items.slime_ball, 4), 20, new ItemStack(Blocks.obsidian), 400);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("water"), 250), new ItemStack(ModItems.itemClayeyIronIngot), new ItemStack(Items.clay_ball), 50, new ItemStack(Items.iron_ingot), 400);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("water"), 4 * 250), new ItemStack(ModItems.itemClayeyIronIngot, 4), new ItemStack(Blocks.clay), 4 * 50, new ItemStack(Items.iron_ingot, 4), 400);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("water"), 4 * 250), new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 0), new ItemStack(Blocks.clay), 4 * 50, new ItemStack(ModBlocks.blockWoodenLiquidTank, 1, 0), 250);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("water"), 4 * 250), new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 1), new ItemStack(Blocks.clay), 4 * 50, new ItemStack(ModBlocks.blockWoodenLiquidTank, 1, 1), 250);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("lava"), 4 * 250), new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 4), new ItemStack(Blocks.iron_block), 500, new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 2), 500);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("lava"), 4 * 250), new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 5), new ItemStack(Blocks.iron_block), 500, new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 3), 500);

        //Termo Smelting recipes
        TermoSmeltingRegister.addRecipe(new ItemStack(ModBlocks.blockClayLiquidTank, 1, 0), new ItemStack(ModBlocks.blockClayLiquidTank, 1, 2), 1100, 400);
        TermoSmeltingRegister.addRecipe(new ItemStack(ModBlocks.blockClayLiquidTank, 1, 1), new ItemStack(ModBlocks.blockClayLiquidTank, 1, 3), 1100, 400);
        TermoSmeltingRegister.addRecipe(new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 0), new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 2), 2000, 300);
        TermoSmeltingRegister.addRecipe(new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 1), new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 3), 2000, 300);
    */
    }
}
