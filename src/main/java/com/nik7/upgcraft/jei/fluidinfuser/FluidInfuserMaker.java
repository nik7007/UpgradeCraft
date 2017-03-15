package com.nik7.upgcraft.jei.fluidinfuser;


import com.nik7.upgcraft.jei.UpgCPlugin;
import com.nik7.upgcraft.registry.FluidInfuserRegister;
import com.nik7.upgcraft.registry.recipes.FluidInfuserRecipe;
import com.nik7.upgcraft.registry.recipes.RecipeExperience;
import net.minecraft.item.ItemStack;

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

            ItemStack result = recipe.getResult();

            recipes.add(new FluidInfuserJEI(UpgCPlugin.jeiHelper.getStackHelper().getSubtypes(recipe.getToMelt()), UpgCPlugin.jeiHelper.getStackHelper().getSubtypes(recipe.getToInfuse()), result, recipe.getFluidStack(), RecipeExperience.getExperience(result)));

        }

        return recipes;

    }

}
