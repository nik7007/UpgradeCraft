package com.nik7.upgcraft.jei.fluidfurnace;

import com.nik7.upgcraft.util.TranslatorUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class FluidFurnaceJEI extends BlankRecipeWrapper {

    @Nonnull
    private final List<List<ItemStack>> input;
    @Nonnull
    private final ItemStack output;
    @Nullable
    private final String experienceString;

    public FluidFurnaceJEI(@Nonnull List<ItemStack> input, @Nonnull ItemStack output, float experience) {
        this.input = Collections.singletonList(input);
        this.output = output;

        if (experience > 0.0) {
            this.experienceString = TranslatorUtil.translateToLocalFormatted("gui.jei.category.smelting.experience", experience);
        } else {
            this.experienceString = null;
        }
    }

    @Nonnull
    public List<List<ItemStack>> getInputs() {
        return this.input;
    }

    @Nonnull
    public List<ItemStack> getOutputs() {
        return Collections.singletonList(output);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, this.input);
        ingredients.setOutput(ItemStack.class, this.output);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (this.experienceString != null) {
            FontRenderer fontRendererObj = minecraft.fontRendererObj;
            int stringWidth = fontRendererObj.getStringWidth(this.experienceString);
            fontRendererObj.drawString(this.experienceString, recipeWidth - stringWidth, 0, Color.gray.getRGB());
        }
    }

}
