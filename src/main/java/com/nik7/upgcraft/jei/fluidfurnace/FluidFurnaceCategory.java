package com.nik7.upgcraft.jei.fluidfurnace;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.jei.UpgCPlugin;
import com.nik7.upgcraft.reference.Texture;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;

import javax.annotation.Nonnull;

public class FluidFurnaceCategory implements IRecipeCategory {

    @Nonnull
    protected final IDrawableAnimated flame;
    @Nonnull
    protected final IDrawableAnimated arrow;


    @Nonnull
    private final IDrawable background = UpgCPlugin.jeiHelper.getGuiHelper().createDrawable(new ModelResourceLocation(Texture.GUI.FLUID_FURNACE), 55, 16, 82, 54);


    public FluidFurnaceCategory() {

        ModelResourceLocation backgroundLocation = new ModelResourceLocation(Texture.GUI.FLUID_FURNACE);

        IDrawableStatic flameDrawable = UpgCPlugin.jeiHelper.getGuiHelper().createDrawable(backgroundLocation, 176, 0, 14, 14);
        flame = UpgCPlugin.jeiHelper.getGuiHelper().createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic arrowDrawable = UpgCPlugin.jeiHelper.getGuiHelper().createDrawable(backgroundLocation, 176, 14, 24, 17);
        arrow = UpgCPlugin.jeiHelper.getGuiHelper().createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);


    }

    @Nonnull
    @Override
    public String getUid() {
        return ModBlocks.blockUpgCFluidFurnace.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return ModBlocks.blockUpgCFluidFurnace.getLocalizedName();
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft) {
        flame.draw(minecraft, 2, 20);
        arrow.draw(minecraft, 24, 18);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {

        recipeLayout.getItemStacks().init(0, true, 0, 0);
        recipeLayout.getItemStacks().init(1, false, 60, 18);

        if (recipeWrapper instanceof FluidFurnaceJEI) {

            recipeLayout.getItemStacks().set(0, recipeWrapper.getInputs());
            recipeLayout.getItemStacks().set(1, recipeWrapper.getOutputs());
        }

    }
}
