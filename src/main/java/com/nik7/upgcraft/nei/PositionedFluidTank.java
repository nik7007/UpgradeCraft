package com.nik7.upgcraft.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.GuiRecipe;
import com.nik7.upgcraft.reference.Reference;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class PositionedFluidTank {

    private FluidStack fluidStack;
    private int tankCapacity;
    private int x, y, w, h;

    public PositionedFluidTank(FluidStack fluidStack, int tankCapacity, int x, int y, int w, int h) {
        this.fluidStack = fluidStack;
        this.tankCapacity = tankCapacity;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void draw() {

        if (fluidStack != null) {
            Fluid fluid = fluidStack.getFluid();
            if (fluid != null) {
                int level = Math.round(((float) fluidStack.amount / (float) tankCapacity) * 100);

                if (level == 0 && fluidStack.amount != 0)
                    level = 1;
                if (level > h)
                    level = h;

                IIcon icon = fluid.getIcon();

                int color = fluid.getColor();

                GuiDraw.changeTexture(TextureMap.locationBlocksTexture);

                GL11.glColor3ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));
                GL11.glDisable(GL11.GL_BLEND);

                GuiDraw.gui.drawTexturedModelRectFromIcon(x, y - level, icon, w, level);

                GL11.glEnable(GL11.GL_BLEND);
            }
        }

    }

    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {

        if (fluidStack != null) {
            Point offset = gui.getRecipePosition(recipe);
            Point mousepos = GuiDraw.getMousePosition();
            Point relMouse = new Point(mousepos.x - (gui.width - 176) / 2 - offset.x, mousepos.y - (gui.height - 164) / 2 - offset.y);

            if ((relMouse.x >= x && relMouse.x <= x + w) && (relMouse.y < y && relMouse.y > y - h - 3)) {
                currenttip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidname") + ": " + EnumChatFormatting.RESET + fluidStack.getLocalizedName());
                currenttip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidamount") + ": " + EnumChatFormatting.RESET + fluidStack.amount + " mB");
            }
        }

        return currenttip;
    }
}
