package com.nik7.upgcraft.jei.fluidfurnace;


import com.nik7.upgcraft.init.ModBlocks;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class FluidFurnaceHandler implements IRecipeHandler<FluidFurnaceJEI> {
    @Nonnull
    @Override
    public Class<FluidFurnaceJEI> getRecipeClass() {
        return FluidFurnaceJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return ModBlocks.blockUpgCFluidFurnace.getName();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull FluidFurnaceJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull FluidFurnaceJEI recipe) {
        return recipe.getInputs().size() == 1 && recipe.getOutputs().size() == 1;
    }
}
