package com.nik7.upgcraft.jei.fluidinfuser;


import com.nik7.upgcraft.util.TranslatorUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FluidInfuserJEI extends BlankRecipeWrapper {

    private final List<List<ItemStack>> inputs;
    private final ItemStack output;
    private final FluidStack fluid;
    private final String experienceString;

    public FluidInfuserJEI(List<ItemStack> melt, List<ItemStack> infuse, ItemStack output, FluidStack fluid, float experience) {
        this.output = output;
        this.fluid = fluid;

        this.inputs = new ArrayList<>();
        this.inputs.add(melt);
        this.inputs.add(infuse);

        if (experience > 0.0) {
            this.experienceString = TranslatorUtil.translateToLocalFormatted("gui.jei.category.smelting.experience", experience);
        } else {
            this.experienceString = null;
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients) {

        ingredients.setInputLists(ItemStack.class, this.inputs);
        ingredients.setInput(FluidStack.class, this.fluid);
        ingredients.setOutput(ItemStack.class, this.output);

    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (this.experienceString != null) {
            FontRenderer fontRendererObj = minecraft.fontRendererObj;
            int stringWidth = fontRendererObj.getStringWidth(this.experienceString);
            fontRendererObj.drawString(this.experienceString, recipeWidth - stringWidth - 5, 5, Color.gray.getRGB());
        }
    }
}
