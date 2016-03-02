package com.nik7.upgcraft.jei;


import com.nik7.upgcraft.block.BlockUpgC;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.IRecipeCategory;

import javax.annotation.Nonnull;

public abstract class RecipeCategory implements IRecipeCategory {

    private final BlockUpgC block;
    @Nonnull
    private final IDrawable background;


    public RecipeCategory(BlockUpgC block, @Nonnull IDrawable background) {
        this.block = block;
        this.background = background;
    }

    @Nonnull
    @Override
    public String getUid() {
        return block.getUnlocalizedName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return block.getLocalizedName();
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

}
