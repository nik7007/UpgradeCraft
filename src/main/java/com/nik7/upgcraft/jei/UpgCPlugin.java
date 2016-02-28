package com.nik7.upgcraft.jei;


import com.nik7.upgcraft.client.gui.inventory.GuiFluidInfuser;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.inventory.ContainerFluidInfuser;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserCategory;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserHandler;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserMaker;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;

import javax.annotation.Nonnull;

@JEIPlugin
public class UpgCPlugin extends BlankModPlugin {

    public static IJeiHelpers jeiHelper;

    @Override
    public void register(@Nonnull IModRegistry registry) {
        jeiHelper = registry.getJeiHelpers();

        registry.addRecipeCategories(new FluidInfuserCategory());
        registry.addRecipeHandlers(new FluidInfuserHandler());

        registry.addRecipeClickArea(GuiFluidInfuser.class, 88, 32, 34, 18, ModBlocks.blockUpgCFluidInfuser.getName());

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        recipeTransferRegistry.addRecipeTransferHandler(ContainerFluidInfuser.class, ModBlocks.blockUpgCFluidInfuser.getName(), 0, 2, 3, 36);

        registry.addRecipes(FluidInfuserMaker.getRecipes());
    }

}
