package com.nik7.upgcraft.jei.fluidinfuser;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.jei.RecipeHandler;

public class FluidInfuserHandler extends RecipeHandler<FluidInfuserJEI> {

    public FluidInfuserHandler() {
        super(ModBlocks.blockFluidInfuser);
    }

    @Override
    public Class<FluidInfuserJEI> getRecipeClass() {
        return FluidInfuserJEI.class;
    }

    @Override
    public boolean isRecipeValid(FluidInfuserJEI recipe) {
        return true;
    }
}
