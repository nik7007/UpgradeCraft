package com.nik7.upgcraft.jei.fluidinfuser;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.jei.RecipeCategory;
import com.nik7.upgcraft.jei.UpgCPlugin;
import com.nik7.upgcraft.reference.ConfigOptions;
import com.nik7.upgcraft.reference.Texture;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FluidInfuserCategory extends RecipeCategory<FluidInfuserJEI> {


    public FluidInfuserCategory() {
        super(ModBlocks.blockFluidInfuser, UpgCPlugin.jeiHelper.getGuiHelper().createDrawable(new ModelResourceLocation(Texture.GUI.FLUID_INFUSER), 3, 16, 169, 54));
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FluidInfuserJEI recipeWrapper, IIngredients ingredients) {

        recipeLayout.getItemStacks().init(0, true, 48, 3);
        recipeLayout.getItemStacks().init(1, true, 78, 3);

        recipeLayout.getItemStacks().init(2, false, 138, 20);

        recipeLayout.getFluidStacks().init(3, true, 12, 10, 16, 32, ConfigOptions.MACHINE_CAPACITY, true, null);

        recipeLayout.getItemStacks().set(ingredients);
        recipeLayout.getFluidStacks().set(ingredients);

    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return new ArrayList<>();
    }
}
