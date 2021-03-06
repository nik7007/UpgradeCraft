package com.nik7.upgcraft.jei;


import com.nik7.upgcraft.client.gui.inventory.GuiFluidFurnace;
import com.nik7.upgcraft.client.gui.inventory.GuiFluidInfuser;
import com.nik7.upgcraft.client.gui.inventory.GuiThermoFluidFurnace;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import com.nik7.upgcraft.inventory.ContainerFluidInfuser;
import com.nik7.upgcraft.inventory.ContainerThermoFluidFurnace;
import com.nik7.upgcraft.jei.fluidfurnace.FluidFurnaceCategory;
import com.nik7.upgcraft.jei.fluidfurnace.FluidFurnaceHandler;
import com.nik7.upgcraft.jei.fluidfurnace.FluidFurnaceMaker;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserCategory;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserHandler;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserMaker;
import com.nik7.upgcraft.jei.thermosmelting.ThermoSmeltingCategory;
import com.nik7.upgcraft.jei.thermosmelting.ThermoSmeltingHandler;
import com.nik7.upgcraft.jei.thermosmelting.ThermoSmeltingMaker;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
public class UpgCPlugin extends BlankModPlugin {

    public static IJeiHelpers jeiHelper;

    @Override
    public void register(@Nonnull IModRegistry registry) {
        jeiHelper = registry.getJeiHelpers();

        registry.addRecipeCategories(new FluidInfuserCategory(), new FluidFurnaceCategory(), new ThermoSmeltingCategory());
        registry.addRecipeHandlers(new FluidInfuserHandler(), new FluidFurnaceHandler(), new ThermoSmeltingHandler());

        registry.addRecipeClickArea(GuiFluidInfuser.class, 88, 32, 34, 18, ModBlocks.blockUpgCFluidInfuser.getUnlocalizedName());
        registry.addRecipeClickArea(GuiFluidFurnace.class, 78, 32, 28, 23, ModBlocks.blockUpgCFluidFurnace.getUnlocalizedName());
        registry.addRecipeClickArea(GuiThermoFluidFurnace.class, 78, 32, 28, 23, ModBlocks.blockUpgCThermoFluidFurnace.getUnlocalizedName());

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        recipeTransferRegistry.addRecipeTransferHandler(ContainerFluidInfuser.class, ModBlocks.blockUpgCFluidInfuser.getUnlocalizedName(), 0, 2, 3, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerFluidFurnace.class, ModBlocks.blockUpgCFluidFurnace.getUnlocalizedName(), 0, 1, 2, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerThermoFluidFurnace.class, ModBlocks.blockUpgCThermoFluidFurnace.getUnlocalizedName(), 0, 1, 2, 36);

        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockUpgCFluidInfuser),ModBlocks.blockUpgCFluidInfuser.getUnlocalizedName());
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockUpgCFluidFurnace),ModBlocks.blockUpgCFluidFurnace.getUnlocalizedName());
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockUpgCThermoFluidFurnace),ModBlocks.blockUpgCThermoFluidFurnace.getUnlocalizedName());


        registry.addRecipes(FluidInfuserMaker.getRecipes());
        registry.addRecipes(FluidFurnaceMaker.getRecipes());
        registry.addRecipes(ThermoSmeltingMaker.getRecipes());
    }

}
