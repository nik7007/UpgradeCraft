package com.nik7.upgcraft.jei;


import com.nik7.upgcraft.block.BlockUpgC;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;


public abstract class RecipeHandler<T extends IRecipeWrapper> implements IRecipeHandler<T> {

    @Nonnull
    private final BlockUpgC modBlock;

    public RecipeHandler(@Nonnull BlockUpgC modBlock) {
        this.modBlock = modBlock;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return modBlock.getUnlocalizedName();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull T recipe) {
        return recipe;
    }

}
