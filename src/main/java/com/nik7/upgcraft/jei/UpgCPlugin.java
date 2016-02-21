package com.nik7.upgcraft.jei;


import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserCategory;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserHandler;
import com.nik7.upgcraft.jei.fluidinfuser.FluidInfuserMaker;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

import javax.annotation.Nonnull;

@JEIPlugin
public class UpgCPlugin extends BlankModPlugin {

    public static IJeiHelpers jeiHelper;

    @Override
    public void register(@Nonnull IModRegistry registry) {
        jeiHelper = registry.getJeiHelpers();

        registry.addRecipeCategories(new FluidInfuserCategory());
        registry.addRecipeHandlers(new FluidInfuserHandler());
        registry.addRecipes(FluidInfuserMaker.getRecipes());
    }

}
