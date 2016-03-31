package com.nik7.upgcraft.jei.fluidinfuser;


import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FluidInfuserJEI extends BlankRecipeWrapper {

    @Nonnull
    private final ItemStack infuseInput;
    @Nonnull
    private final ItemStack meltInput;
    @Nonnull
    private final ItemStack output;
    @Nonnull
    private final FluidStack fluid;

    @Nullable
    private final String experienceString;

    public FluidInfuserJEI(@Nonnull ItemStack infuseInput, @Nonnull ItemStack meltInput, @Nonnull ItemStack output, @Nonnull FluidStack fluid, float experience) {
        this.infuseInput = infuseInput;
        this.meltInput = meltInput;
        this.output = output;
        this.fluid = fluid;

        if (experience > 0.0) {
            experienceString = Translator.translateToLocalFormatted("gui.jei.category.smelting.experience", experience);
        } else {
            experienceString = null;
        }

    }

    @Override
    @Nonnull
    public List getInputs() {
        List<ItemStack> inputs = new ArrayList<ItemStack>();
        inputs.add(infuseInput);
        inputs.add(meltInput);
        return inputs;
    }

    @Override
    @Nonnull
    public List<FluidStack> getFluidInputs() {
        return Collections.singletonList(fluid);
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
            fontRendererObj.drawString(experienceString, recipeWidth - stringWidth + 5, 0, Color.gray.getRGB());
        }
    }

}
