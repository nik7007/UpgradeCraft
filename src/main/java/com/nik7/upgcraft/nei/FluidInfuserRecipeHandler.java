package com.nik7.upgcraft.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.nik7.upgcraft.client.gui.inventory.GuiFluidInfuser;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.registry.FluidInfuser.FluidInfuserRecipe;
import com.nik7.upgcraft.registry.FluidInfuserRegister;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FluidInfuserRecipeHandler extends TemplateRecipeHandler {

    public class CachedFluidInfuserRecipe extends CachedRecipe {

        public List<PositionedStack> inputs;
        public PositionedStack output;

        public PositionedFluidTank positionedFluidTank;

        public CachedFluidInfuserRecipe(FluidInfuserRecipe recipe) {

            ItemStack toMelt = recipe.getInputs().getToMelt();
            ItemStack toInfuse = recipe.getInputs().getToInfuse();
            ItemStack result = recipe.getResult();

            positionedFluidTank = new PositionedFluidTank(recipe.getFluidStack(), Capacity.INTERNAL_FLUID_TANK_TR1, 11, 45, 16, 32);
            inputs = Arrays.asList(new PositionedStack(toMelt, 41, 6), new PositionedStack(toInfuse, 71, 6));
            output = new PositionedStack(result, 131, 24);


        }

        @Override
        public List<PositionedStack> getIngredients() {
            for (PositionedStack positionedStack : inputs) {
                this.randomRenderPermutation(positionedStack, FluidInfuserRecipeHandler.this.cycleticks / 20);
            }
            return inputs;
        }

        @Override
        public PositionedStack getResult() {
            this.randomRenderPermutation(output, FluidInfuserRecipeHandler.this.cycleticks / 20);
            return output;
        }

    }


    @Override
    public void drawExtras(int recipe) {

        PositionedFluidTank fluidStack = ((CachedFluidInfuserRecipe) arecipes.get(recipe)).positionedFluidTank;

        if (fluidStack != null) {
            fluidStack.draw();
        }
        GuiDraw.changeTexture(getGuiTexture());
        this.drawProgressBar(41, 25, 176, 0, 15, 15, 20, 3);
        this.drawProgressBar(94, 23, 176, 14, 25, 16, 60, 0);
    }

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
        currenttip = super.handleTooltip(gui, currenttip, recipe);

        PositionedFluidTank fluidStack = ((CachedFluidInfuserRecipe) arecipes.get(recipe)).positionedFluidTank;
        return fluidStack.handleTooltip(gui, currenttip, recipe);
    }


    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (HashSet<FluidInfuserRecipe> hashSet : FluidInfuserRegister.getRecipes()) {

            for (FluidInfuserRecipe recipe : hashSet) {
                if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getResult(), result)) {
                    arecipes.add(new CachedFluidInfuserRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getRecipeID())) {
            for (HashSet<FluidInfuserRecipe> hashSet : FluidInfuserRegister.getRecipes()) {

                for (FluidInfuserRecipe recipe : hashSet) {
                    arecipes.add(new CachedFluidInfuserRecipe(recipe));

                }
            }

        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    public String getRecipeID() {
        return Reference.MOD_ID + ":" + Names.Blocks.FLUID_INFUSE;
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {

        for (HashSet<FluidInfuserRecipe> hashSet : FluidInfuserRegister.getRecipes()) {

            for (FluidInfuserRecipe recipe : hashSet) {
                if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, recipe.getInputs().getToMelt()) || NEIServerUtils.areStacksSameTypeCrafting(ingredient, recipe.getInputs().getToInfuse())) {
                    arecipes.add(new CachedFluidInfuserRecipe(recipe));
                }
            }
        }

    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(67, 23, 50, 16), getRecipeID()));
    }


    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiFluidInfuser.class;
    }

    @Override
    public String getGuiTexture() {
        return Texture.GUI.FLUID_INFUSER;
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.nei." + Reference.MOD_ID + ":" + Names.Blocks.FLUID_INFUSE);
    }
}
