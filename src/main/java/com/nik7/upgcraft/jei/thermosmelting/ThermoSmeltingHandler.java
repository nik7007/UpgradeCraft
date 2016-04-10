package com.nik7.upgcraft.jei.thermosmelting;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.jei.RecipeHandler;

import javax.annotation.Nonnull;

public class ThermoSmeltingHandler extends RecipeHandler<ThermoSmeltingJEI> {


    public ThermoSmeltingHandler() {
        super(ModBlocks.blockUpgCThermoFluidFurnace);
    }

    @Nonnull
    @Override
    public Class<ThermoSmeltingJEI> getRecipeClass() {
        return ThermoSmeltingJEI.class;
    }

    @Override
    public boolean isRecipeValid(@Nonnull ThermoSmeltingJEI recipe) {
        return recipe.getInputs().size() == 1 && recipe.getOutputs().size() >= 1;
    }
}
