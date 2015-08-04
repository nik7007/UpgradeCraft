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
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FluidInfuserRecipeHandler extends TemplateRecipeHandler {

    public class CachedFluidInfuserRecipe extends CachedRecipe {

        public List<PositionedStack> inputs;

        public PositionedStack output;

        public FluidStack fluidStack;

        public CachedFluidInfuserRecipe(FluidInfuserRecipe recipe) {

            ItemStack toMelt = recipe.getInputs().getToMelt();
            ItemStack toInfuse = recipe.getInputs().getToInfuse();
            ItemStack result = recipe.getResult();
            fluidStack = recipe.getFluidStack();

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

        FluidStack fluidStack = ((CachedFluidInfuserRecipe) arecipes.get(recipe)).fluidStack;

        if (fluidStack != null) {
            Fluid fluid = fluidStack.getFluid();
            if (fluid != null) {
                int level = Math.round(((float) fluidStack.amount / (float) Capacity.INTERNAL_FLUID_TANK_TR1) * 100);

                if (level == 0 && fluidStack.amount != 0)
                    level = 1;
                if (level > 32)
                    level = 32;

                IIcon icon = fluid.getIcon();

                int color = fluid.getColor();

                GuiDraw.changeTexture(TextureMap.locationBlocksTexture);

                GL11.glColor3ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));
                GL11.glDisable(GL11.GL_BLEND);

                GuiDraw.gui.drawTexturedModelRectFromIcon(11, 45 - level, icon, 16, level);

                GL11.glEnable(GL11.GL_BLEND);
            }
        }
        GuiDraw.changeTexture(getGuiTexture());
        this.drawProgressBar(41, 25, 176, 0, 15, 15, 20, 3);
        this.drawProgressBar(94, 23, 176, 14,25, 16, 60, 0);
    }

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
        currenttip = super.handleTooltip(gui, currenttip, recipe);
        FluidStack fluidStack = ((CachedFluidInfuserRecipe) arecipes.get(recipe)).fluidStack;
        Point offset = gui.getRecipePosition(recipe);
        Point mousepos = GuiDraw.getMousePosition();
        Point relMouse = new Point(mousepos.x - (gui.width - 176) / 2 - offset.x, mousepos.y - (gui.height - 164) / 2 - offset.y);

        if ((relMouse.x >= 11 && relMouse.x <= 11 + 16) && (relMouse.y < 45 && relMouse.y > 45 - 35)) {
            currenttip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidname") + ": " + EnumChatFormatting.RESET + fluidStack.getLocalizedName());
            currenttip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidamount") + ": " + EnumChatFormatting.RESET + fluidStack.amount + " mB");
        }


        return currenttip;
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
        transferRects.add(new RecipeTransferRect(new Rectangle(67, 23, 50,16), getRecipeID()));
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
