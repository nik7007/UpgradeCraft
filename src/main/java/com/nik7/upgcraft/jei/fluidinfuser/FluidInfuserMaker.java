package com.nik7.upgcraft.jei.fluidinfuser;


import com.nik7.upgcraft.registry.FluidInfuser.FluidInfuserRecipe;
import com.nik7.upgcraft.registry.FluidInfuser.InputItemStacks;
import com.nik7.upgcraft.registry.FluidInfuserRegister;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FluidInfuserMaker {

    @Nonnull
    public static List<FluidInfuserJEI> getRecipes() {

        ArrayList<FluidInfuserJEI> recipes = new ArrayList<FluidInfuserJEI>();

        for (HashSet<FluidInfuserRecipe> fluidInfuserRecipes : FluidInfuserRegister.getRecipes()) {
            for (FluidInfuserRecipe fluidInfuserRecipe : fluidInfuserRecipes) {
                InputItemStacks inputs = fluidInfuserRecipe.getInputs();

                FluidInfuserJEI recipe = new FluidInfuserJEI(inputs.getToInfuse(), inputs.getToMelt(), fluidInfuserRecipe.getResult(), fluidInfuserRecipe.getFluidStack());
                recipes.add(recipe);
            }

        }


        return recipes;
    }
}
