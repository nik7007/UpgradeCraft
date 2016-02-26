package com.nik7.upgcraft.jei.fluidinfuser;


import com.nik7.upgcraft.init.ModBlocks;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class FluidInfuserHandler implements IRecipeHandler<FluidInfuserJEI> {
    @Nonnull
    @Override
    public Class<FluidInfuserJEI> getRecipeClass() {
        return FluidInfuserJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return ModBlocks.blockUpgCFluidInfuser.getName();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull FluidInfuserJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull FluidInfuserJEI recipe) {
        return recipe.getFluidInputs().size() == 1 && recipe.getInputs().size() == 2 && recipe.getOutputs().size() == 1;
    }
}
