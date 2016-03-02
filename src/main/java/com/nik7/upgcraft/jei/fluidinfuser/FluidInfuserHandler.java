package com.nik7.upgcraft.jei.fluidinfuser;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.jei.RecipeHandler;

import javax.annotation.Nonnull;

public class FluidInfuserHandler extends RecipeHandler<FluidInfuserJEI> {


    public FluidInfuserHandler() {
        super(ModBlocks.blockUpgCFluidInfuser);
    }

    @Nonnull
    @Override
    public Class<FluidInfuserJEI> getRecipeClass() {
        return FluidInfuserJEI.class;
    }


    @Override
    public boolean isRecipeValid(@Nonnull FluidInfuserJEI recipe) {
        return recipe.getFluidInputs().size() == 1 && recipe.getInputs().size() == 2 && recipe.getOutputs().size() == 1;
    }
}
