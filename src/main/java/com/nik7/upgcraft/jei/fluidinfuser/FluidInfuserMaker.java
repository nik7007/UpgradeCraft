package com.nik7.upgcraft.jei.fluidinfuser;


import com.nik7.upgcraft.jei.UpgCPlugin;
import com.nik7.upgcraft.registry.FluidInfuserRegister;
import com.nik7.upgcraft.registry.recipes.FluidInfuserRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FluidInfuserMaker {

    @Nonnull
    public static List<FluidInfuserJEI> getRecipes() {

        Collection<FluidInfuserRecipe> infuserRecipes = FluidInfuserRegister.getRecipes();

        List<FluidInfuserJEI> recipes = new ArrayList<>();

        for (FluidInfuserRecipe recipe : infuserRecipes) {

            recipes.add(new FluidInfuserJEI(UpgCPlugin.jeiHelper.getStackHelper().getSubtypes(recipe.getToMelt()), UpgCPlugin.jeiHelper.getStackHelper().getSubtypes(recipe.getToInfuse()), recipe.getResult(), recipe.getFluidStack()));

        }

        return recipes;

    }

}
