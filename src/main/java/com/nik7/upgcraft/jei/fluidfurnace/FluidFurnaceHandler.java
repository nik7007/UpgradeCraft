package com.nik7.upgcraft.jei.fluidfurnace;

import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.jei.RecipeHandler;

import javax.annotation.Nonnull;

public class FluidFurnaceHandler extends RecipeHandler<FluidFurnaceJEI> {


    public FluidFurnaceHandler() {
        super(ModBlocks.blockFluidFurnace);
    }

    @Nonnull
    @Override
    public Class<FluidFurnaceJEI> getRecipeClass() {
        return FluidFurnaceJEI.class;
    }

    @Override
    public boolean isRecipeValid(@Nonnull FluidFurnaceJEI recipe) {
        return !recipe.getInputs().isEmpty() && recipe.getOutputs().size() >= 1;
    }
}