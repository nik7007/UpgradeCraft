package com.nik7.upgcraft.nei;


import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.nik7.upgcraft.client.gui.inventory.GuiTermoFluidFurnace;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.registry.TermoSmelting.TermoSmeltingRecipe;
import com.nik7.upgcraft.registry.TermoSmeltingRegister;
import com.nik7.upgcraft.tileentities.UpgCtileentityTermoFluidFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

public class TermoFluidFurnaceRecipeHandler extends TemplateRecipeHandler {

    public class CachedTermoFluidFurnaceRecipe extends CachedRecipe {

        public PositionedStack input;
        public PositionedStack output;
        public boolean termoSmelting;
        public int temp;

        public CachedTermoFluidFurnaceRecipe(ItemStack input, ItemStack output, boolean termoSmelting) {
            this.input = new PositionedStack(input, 50, 9);
            this.output = new PositionedStack(output, 110, 17);
            this.termoSmelting = termoSmelting;
        }

        public CachedTermoFluidFurnaceRecipe(ItemStack input, ItemStack output, boolean termoSmelting, int temp) {
            this(input, output, termoSmelting);
            this.temp = temp;
        }

        @Override
        public PositionedStack getIngredient() {
            this.randomRenderPermutation(input, TermoFluidFurnaceRecipeHandler.this.cycleticks / 20);
            return input;
        }

        @Override
        public PositionedStack getResult() {
            this.randomRenderPermutation(output, TermoFluidFurnaceRecipeHandler.this.cycleticks / 20);
            return output;
        }
    }

    @Override
    public void drawExtras(int recipe) {

        GuiDraw.changeTexture(getGuiTexture());

        CachedTermoFluidFurnaceRecipe cachedTermoFluidFurnaceRecipe = ((CachedTermoFluidFurnaceRecipe) arecipes.get(recipe));

        float temp;
        if (cachedTermoFluidFurnaceRecipe.termoSmelting)
            temp = cachedTermoFluidFurnaceRecipe.temp / (float) UpgCtileentityTermoFluidFurnace.MAX_TEMPERATURE;
        else
            temp = UpgCtileentityTermoFluidFurnace.MIN_OPERATION_TEMP / (float) UpgCtileentityTermoFluidFurnace.MAX_TEMPERATURE;

        temp = 1 - temp;

        this.drawProgressBar(51, 27, 176, 0, 14, 14, temp, 7);
        this.drawProgressBar(73, 16, 176, 14, 24, 16, 54, 0);
    }

    @Override
    public java.util.List<String> handleTooltip(GuiRecipe gui, java.util.List<String> currenttip, int recipe) {
        currenttip = super.handleTooltip(gui, currenttip, recipe);


        return handleTemperatureToolTip(gui, currenttip, recipe, 51, 42, 14, 13);
    }

    private java.util.List<String> handleTemperatureToolTip(GuiRecipe gui, java.util.List<String> currenttip, int recipe, int x, int y, int w, int h) {
        Point offset = gui.getRecipePosition(recipe);
        Point mousepos = GuiDraw.getMousePosition();
        Point relMouse = new Point(mousepos.x - (gui.width - 176) / 2 - offset.x, mousepos.y - (gui.height - 164) / 2 - offset.y);
        if ((relMouse.x >= x && relMouse.x <= x + w) && (relMouse.y < y && relMouse.y > y - h - 3)) {

            EnumChatFormatting color;
            CachedTermoFluidFurnaceRecipe cachedTermoFluidFurnaceRecipe = ((CachedTermoFluidFurnaceRecipe) arecipes.get(recipe));

            float temp;
            if (cachedTermoFluidFurnaceRecipe.termoSmelting)
                temp = cachedTermoFluidFurnaceRecipe.temp;
            else
                temp = UpgCtileentityTermoFluidFurnace.MIN_OPERATION_TEMP;

            if (temp <= 273 + 25)
                color = EnumChatFormatting.BLUE;
            else if (temp <= UpgCtileentityTermoFluidFurnace.MAX_TEMPERATURE / 2)
                color = EnumChatFormatting.DARK_RED;
            else
                color = EnumChatFormatting.GOLD;

            currenttip.add(color + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":machine.temperature") + ": " + EnumChatFormatting.RESET + (int) temp + "K");
        }
        return currenttip;
    }


    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getRecipeID())) {
            Map recipes = FurnaceRecipes.smelting().getSmeltingList();

            for (Object o : recipes.entrySet()) {
                Map.Entry recipe = (Map.Entry) o;
                this.arecipes.add(new CachedTermoFluidFurnaceRecipe((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue(), false));

            }
            Collection<TermoSmeltingRecipe> termoSmeltingRecipes = TermoSmeltingRegister.getRecipes();

            for (TermoSmeltingRecipe r : termoSmeltingRecipes) {
                this.arecipes.add(new CachedTermoFluidFurnaceRecipe(r.getInput(), r.getOutput(), true, (int) r.getTemperature()));
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
            if (NEIServerUtils.areStacksSameType((ItemStack) recipe.getKey(), ingredient)) {
                arecipes.add(new CachedTermoFluidFurnaceRecipe((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue(), false));
            }
        }

        Collection<TermoSmeltingRecipe> termoSmeltingRecipes = TermoSmeltingRegister.getRecipes();

        for (TermoSmeltingRecipe r : termoSmeltingRecipes) {

            if (NEIServerUtils.areStacksSameTypeCrafting(r.getInput(), ingredient)) {
                arecipes.add(new CachedTermoFluidFurnaceRecipe(r.getInput(), r.getOutput(), true, (int) r.getTemperature()));
            }
        }

    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Map recipes = FurnaceRecipes.smelting().getSmeltingList();

        for (Object o : recipes.entrySet()) {
            Map.Entry recipe = (Map.Entry) o;
            if (NEIServerUtils.areStacksSameType((ItemStack) recipe.getValue(), result)) {
                this.arecipes.add(new CachedTermoFluidFurnaceRecipe((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue(), false));
            }
        }

        Collection<TermoSmeltingRecipe> termoSmeltingRecipes = TermoSmeltingRegister.getRecipes();

        for (TermoSmeltingRecipe r : termoSmeltingRecipes) {

            if (NEIServerUtils.areStacksSameTypeCrafting(r.getOutput(), result)) {
                arecipes.add(new CachedTermoFluidFurnaceRecipe(r.getInput(), r.getOutput(), true, (int) r.getTemperature()));
            }
        }

    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(72, 23, 25, 16), getRecipeID()));
    }

    @Override
    public String getGuiTexture() {
        return Texture.GUI.TERMO_FURNACE;
    }

    public String getRecipeID() {
        return Reference.MOD_ID + ":" + Names.Blocks.TERMO_FLUID_FURNACE;
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiTermoFluidFurnace.class;
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.nei." + Reference.MOD_ID + ":" + Names.Blocks.TERMO_FLUID_FURNACE);
    }

}
