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

        //Crafting recipes
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUpgCSlimyLog), "sss", "sps", "sss", 's', "slimeball", 'p', "plankWood"));

        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockUpgCWoodenFluidTank, 1, 0), "sss", "s s", "sss", 's', new ItemStack(ModBlocks.blockUpgCSlimyLog));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUpgCWoodenFluidTank, 1, 1), "sss", "sgs", "sss", 'g', "blockGlass", 's', new ItemStack(ModBlocks.blockUpgCSlimyLog)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUpgCBasicFluidHopper), "i i", "iti", " i ", 'i', "ingotIron", 't', new ItemStack(ModBlocks.blockUpgCWoodenFluidTank)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUpgCFluidFurnace), "ifi", "iti", "iii", 'i', "ingotIron", 't', new ItemStack(ModBlocks.blockUpgCWoodenFluidTank, 1, 1), 'f', new ItemStack(Blocks.furnace)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUpgCFluidInfuser), "iii", "i i", "ifi", 'i', "ingotIron", 'f', new ItemStack(ModBlocks.blockUpgCFluidFurnace)));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockUpgCClayFluidTank, 1, 0), "ccc", "c c", "ccc", 'c', new ItemStack(ModItems.itemUpgCClayIngot));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUpgCClayFluidTank, 1, 1), "ccc", "cgc", "ccc", 'g', "blockGlass", 'c', new ItemStack(ModItems.itemUpgCClayIngot)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUpgCFluidHopper), "i i", "iti", " i ", 'i', "ingotIron", 't', new ItemStack(ModBlocks.blockUpgCClayFluidTank, 1, 2)));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockUpgCEnderFluidTank), "ooo", "oeo", "ooo", 'o', new ItemStack(ModBlocks.blockUpgCSlimyObsidian), 'e', new ItemStack(Items.ender_eye));

        //Fluid Infuser recipes
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("lava"), 200), new ItemStack(ModBlocks.blockUpgCSlimyLog), new ItemStack(Items.slime_ball, 2), 10, new ItemStack(Blocks.planks), 200);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("lava"), 4 * 200), new ItemStack(ModBlocks.blockUpgCSlimyLog, 4), new ItemStack(Blocks.slime_block, 1), 4 * 10, new ItemStack(Blocks.planks, 4), 200);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("water"), 250), new ItemStack(ModItems.itemUpgCClayIngot), new ItemStack(Items.clay_ball), 50, new ItemStack(Items.brick), 400);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("water"), 4 * 250), new ItemStack(ModItems.itemUpgCClayIngot, 4), new ItemStack(Blocks.clay), 4 * 50, new ItemStack(Items.brick, 4), 400);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("lava"), 400), new ItemStack(ModBlocks.blockUpgCSlimyObsidian), new ItemStack(Items.slime_ball, 4), 20, new ItemStack(Blocks.obsidian), 400);
        FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("lava"), 2 * 400), new ItemStack(ModBlocks.blockUpgCSlimyObsidian, 2), new ItemStack(Blocks.slime_block), 2 * 20, new ItemStack(Blocks.obsidian, 2), 400);
        /*FluidInfuserRegister.addRecipe(new FluidStack(FluidRegistry.getFluid("water"), 4 * 250), new ItemStack(ModBlocks.blockUpgCFluidTankMold, 1, 0), new ItemStack(Blocks.clay), 4 * 50, new ItemStack(ModBlocks.blockWoodenLiquidTank, 1, 0), 250);
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
