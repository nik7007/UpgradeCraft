package com.nik7.upgcraft.jei.thermosmelting;


import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThermoSmeltingJEI extends BlankRecipeWrapper {

    @Nonnull
    private final ItemStack input;
    @Nonnull
    private final ItemStack output;
    @Nullable
    private final String experienceString;
    @Nullable
    private final int temperature;

    public ThermoSmeltingJEI(@Nonnull ItemStack input, @Nonnull ItemStack output, float experience, int temperature) {
        this.input = input;
        this.output = output;

        if (experience > 0.0) {
            experienceString = Translator.translateToLocalFormatted("gui.jei.category.smelting.experience", experience);
        } else {
            experienceString = null;
        }
        this.temperature = temperature;
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
            fontRendererObj.drawString(experienceString, recipeWidth - stringWidth - 15, -1, Color.gray.getRGB());
        }

    }

    public int getTemperature() {
        return temperature;
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {

        if (mouseY >= 23 && mouseY <= 36 && mouseX >= 43 && mouseX <= 57) {
            List<String> tooltip = new ArrayList<>();
            tooltip.add(temperature + "K");

            return tooltip;

        } else return null;
    }
}
