package com.nik7.upgcraft.nei;


import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.nik7.upgcraft.client.gui.inventory.GuiFluidFurnace;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.reference.Texture;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.util.Map;

public class FluidFurnaceRecipeHandler extends TemplateRecipeHandler {

    public class CachedFluidFurnaceRecipe extends CachedRecipe {

        public PositionedStack input;
        public PositionedStack output;

        public PositionedFluidTank positionedFluidTank;

        public CachedFluidFurnaceRecipe(ItemStack input, ItemStack output) {
            this.input = new PositionedStack(input, 51, 6);
            this.output = new PositionedStack(output, 111, 24);
            positionedFluidTank = new PositionedFluidTank(new FluidStack(FluidRegistry.getFluid("lava"), 1), Capacity.INTERNAL_FLUID_TANK_TR1, 21, 49, 16, 32);
            positionedFluidTank.setConsPerTick(true);
            positionedFluidTank.setTicks(20);
        }

        @Override
        public PositionedStack getIngredient() {
            this.randomRenderPermutation(input, FluidFurnaceRecipeHandler.this.cycleticks / 20);
            return input;
        }

        @Override
        public PositionedStack getResult() {
            this.randomRenderPermutation(output, FluidFurnaceRecipeHandler.this.cycleticks / 20);
            return output;
        }
    }

    @Override
    public void drawExtras(int recipe) {

        PositionedFluidTank fluidStack = ((CachedFluidFurnaceRecipe) arecipes.get(recipe)).positionedFluidTank;

        if (fluidStack != null) {
            fluidStack.draw();
        }
        GuiDraw.changeTexture(getGuiTexture());
        this.drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
        this.drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
    }

    @Override
    public java.util.List<String> handleTooltip(GuiRecipe gui, java.util.List<String> currenttip, int recipe) {
        currenttip = super.handleTooltip(gui, currenttip, recipe);

        PositionedFluidTank fluidStack = ((CachedFluidFurnaceRecipe) arecipes.get(recipe)).positionedFluidTank;
        return fluidStack.handleTooltip(gui, currenttip, recipe);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getRecipeID())) {
            Map recipes = FurnaceRecipes.smelting().getSmeltingList();

            for (Object o : recipes.entrySet()) {
                Map.Entry recipe = (Map.Entry) o;
                this.arecipes.add(new CachedFluidFurnaceRecipe((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue()));

            }

        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Map recipes = FurnaceRecipes.smelting().getSmeltingList();

        for (Object o : recipes.entrySet()) {
            Map.Entry recipe = (Map.Entry) o;
            if (NEIServerUtils.areStacksSameType((ItemStack) recipe.getValue(), ingredient)) {
                arecipes.add(new CachedFluidFurnaceRecipe((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue()));
            }
        }

    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Map recipes = FurnaceRecipes.smelting().getSmeltingList();

        for (Object o : recipes.entrySet()) {
            Map.Entry recipe = (Map.Entry) o;
            if (NEIServerUtils.areStacksSameType((ItemStack) recipe.getValue(), result)) {
                this.arecipes.add(new CachedFluidFurnaceRecipe((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue()));
            }
        }

    }

    public String getRecipeID() {
        return Reference.MOD_ID + ":" + Names.Blocks.FLUID_FURNACE;
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiFluidFurnace.class;
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(72, 23, 25, 16), getRecipeID()));
    }

    @Override
    public String getGuiTexture() {
        return Texture.GUI.FLUID_FURNACE;
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.nei." + Reference.MOD_ID + ":" + Names.Blocks.FLUID_FURNACE);
    }

}
