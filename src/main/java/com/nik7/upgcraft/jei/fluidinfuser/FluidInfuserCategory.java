package com.nik7.upgcraft.jei.fluidinfuser;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.jei.RecipeCategory;
import com.nik7.upgcraft.jei.UpgCPlugin;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Texture;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class FluidInfuserCategory extends RecipeCategory {

    @Nonnull
    protected final IDrawableAnimated bubble;
    @Nonnull
    protected final IDrawableAnimated arrow;


    public FluidInfuserCategory() {

        super(ModBlocks.blockUpgCFluidInfuser, UpgCPlugin.jeiHelper.getGuiHelper().createDrawable(new ModelResourceLocation(Texture.GUI.FLUID_INFUSER), 11, 16, 146, 54));

        ModelResourceLocation backgroundLocation = new ModelResourceLocation(Texture.GUI.FLUID_INFUSER);

        IDrawableStatic bubbleDrawable = UpgCPlugin.jeiHelper.getGuiHelper().createDrawable(backgroundLocation, 177, 0, 15, 14);
        bubble = UpgCPlugin.jeiHelper.getGuiHelper().createAnimatedDrawable(bubbleDrawable, 60, IDrawableAnimated.StartDirection.BOTTOM, false);

        IDrawableStatic arrowDrawable = UpgCPlugin.jeiHelper.getGuiHelper().createDrawable(backgroundLocation, 177, 14, 24, 17);
        this.arrow = UpgCPlugin.jeiHelper.getGuiHelper().createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);


    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft) {

        bubble.draw(minecraft, 36, 20);
        arrow.draw(minecraft, 89, 18);

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {

        recipeLayout.getItemStacks().init(0, true, 34, 0);
        recipeLayout.getItemStacks().init(1, true, 64, 0);
        recipeLayout.getItemStacks().init(2, false, 124, 18);
        recipeLayout.getFluidStacks().init(3, true, 5, 8, 16, 32, Capacity.INTERNAL_FLUID_TANK_TR1, true, null);

        if (recipeWrapper instanceof FluidInfuserJEI) {

            FluidInfuserJEI fluidInfuserJEI = (FluidInfuserJEI) recipeWrapper;
            recipeLayout.getItemStacks().set(0, (ItemStack) fluidInfuserJEI.getInputs().get(1));
            recipeLayout.getItemStacks().set(1, (ItemStack) fluidInfuserJEI.getInputs().get(0));
            recipeLayout.getItemStacks().set(2, fluidInfuserJEI.getOutputs());
            recipeLayout.getFluidStacks().set(3, fluidInfuserJEI.getFluidInputs());

        }

    }
}
