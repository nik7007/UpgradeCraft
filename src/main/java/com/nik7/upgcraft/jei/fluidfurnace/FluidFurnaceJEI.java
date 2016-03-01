package com.nik7.upgcraft.jei.fluidfurnace;


import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.util.Translator;
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
    private final ItemStack input;
    @Nonnull
    private final ItemStack output;
    @Nullable
    private final String experienceString;

    public FluidFurnaceJEI(@Nonnull ItemStack input, @Nonnull ItemStack output, float experience) {
        this.input = input;
        this.output = output;

        if (experience > 0.0) {
            experienceString = Translator.translateToLocalFormatted("gui.jei.category.smelting.experience", experience);
        } else {
            experienceString = null;
        }
    }

    @Override
    @Nonnull
    public List getInputs() {
        return Collections.singletonList(input);
    }

    @Override
    @Nonnull
    public List getOutputs() {
        return Collections.singletonList(output);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (experienceString != null) {
            FontRenderer fontRendererObj = minecraft.fontRendererObj;
            int stringWidth = fontRendererObj.getStringWidth(experienceString);
            fontRendererObj.drawString(experienceString, recipeWidth - stringWidth, 0, Color.gray.getRGB());
        }
    }

}
