package com.nik7.upgcraft.jei.fluidfurnace;


import com.nik7.upgcraft.block.BlockUpgC;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.jei.RecipeHandler;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class FluidFurnaceHandler extends RecipeHandler<FluidFurnaceJEI> {


    public FluidFurnaceHandler() {
        super( ModBlocks.blockUpgCFluidFurnace);
    }

    @Nonnull
    @Override
    public Class<FluidFurnaceJEI> getRecipeClass() {
        return FluidFurnaceJEI.class;
    }

    @Override
    public boolean isRecipeValid(@Nonnull FluidFurnaceJEI recipe) {
        return recipe.getInputs().size() == 1 && recipe.getOutputs().size() == 1;
    }
}
