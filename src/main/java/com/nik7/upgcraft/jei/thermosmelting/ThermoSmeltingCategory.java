package com.nik7.upgcraft.jei.thermosmelting;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.jei.RecipeCategory;
import com.nik7.upgcraft.jei.UpgCPlugin;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.reference.Texture;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import javax.annotation.Nonnull;

import static com.nik7.upgcraft.tileentities.UpgCtileentityThermoFluidFurnace.MAX_TEMPERATURE;

public class ThermoSmeltingCategory extends RecipeCategory {

    private static final String BACKGROUND_GUI = Reference.MOD_ID + ":" + Texture.GUI.TEXTURE_GUI_LOCATION + "thermofurnace.png";
    private static final ModelResourceLocation BACKGROUND = new ModelResourceLocation(BACKGROUND_GUI);

    @Nonnull
    protected final IDrawableAnimated arrow;
    @Nonnull
    protected IDrawable flame;


    public ThermoSmeltingCategory() {

        super(ModBlocks.blockUpgCThermoFluidFurnace, UpgCPlugin.jeiHelper.getGuiHelper().createDrawable(BACKGROUND, 11, 16, 146, 54));


        IDrawableStatic arrowDrawable = UpgCPlugin.jeiHelper.getGuiHelper().createDrawable(BACKGROUND, 176, 14, 24, 17);
        arrow = UpgCPlugin.jeiHelper.getGuiHelper().createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        flame.draw(minecraft, 44, 22);
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft) {
        arrow.draw(minecraft, 67, 12);

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {

        recipeLayout.getItemStacks().init(0, true, 43, 3);
        recipeLayout.getItemStacks().init(1, false, 103, 11);

        if (recipeWrapper instanceof ThermoSmeltingJEI) {

            recipeLayout.getItemStacks().set(0, recipeWrapper.getInputs());
            recipeLayout.getItemStacks().set(1, recipeWrapper.getOutputs());

            int h = (int) (14f * (((float) ((ThermoSmeltingJEI) recipeWrapper).getTemperature()) / (float) MAX_TEMPERATURE));

            flame = UpgCPlugin.jeiHelper.getGuiHelper().createDrawable(BACKGROUND, 176, 14 - h, 14, h, 14 - h, 0, 0, 0);

        }

    }
}
