package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiWithFluid extends GuiContainer {

    public GuiWithFluid(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    protected void renderFluidWithToolTip(FluidStack fluid, int maxFluidAmount, int x, int y, int mazX, int maxY, int mouseX, int mouseY, int level) {

        renderFluidWithToolTipEX(fluid, maxFluidAmount, x, y, mazX, maxY, mouseX, mouseY, level, false, true);
    }

    protected void renderFluidToolTipAndTemp(FluidStack fluid, int maxFluidAmount, int x, int y, int mazX, int maxY, int mouseX, int mouseY, int level) {
        renderFluidWithToolTipEX(fluid, maxFluidAmount, x, y, mazX, maxY, mouseX, mouseY, level, true, false);
    }

    protected void renderFluidWithToolTipAndTemp(FluidStack fluid, int maxFluidAmount, int x, int y, int mazX, int maxY, int mouseX, int mouseY, int level) {

        renderFluidWithToolTipEX(fluid, maxFluidAmount, x, y, mazX, maxY, mouseX, mouseY, level, true, true);
    }

    protected void renderFluidWithToolTipEX(FluidStack fluid, int maxFluidAmount, int x, int y, int mazX, int maxY, int mouseX, int mouseY, int level, boolean temp, boolean renderFluid) {

        int amount = 0;
        String fluidName = I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluid.df.name");
        int temperature = 0;

        if (fluid != null && fluid.amount > 0) {
            if (renderFluid)
                renderFluid(fluid.getFluid(), x, y, mazX, level);
            amount = fluid.amount;
            fluidName = fluid.getLocalizedName();
            if (temp)
                temperature = fluid.getFluid().getTemperature(fluid);
        }

        List<String> text = new ArrayList<>();
        text.add(TextFormatting.RESET + fluidName);
        text.add(TextFormatting.GRAY + "" + amount + " / " + maxFluidAmount + " mB");

        if (temp)
            text.add(TextFormatting.AQUA + I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":machine.temperature") + ": " + TextFormatting.RESET + temperature + " K");

        drawToolTip(text, x, y, mazX, maxY, mouseX, mouseY);


    }

    protected void drawToolTip(List<String> text, int x, int y, int mazX, int maxY, int mouseX, int mouseY) {
        int posX = mouseX - this.guiLeft;
        int posY = mouseY - this.guiTop;


        if ((posX > x && posX < (mazX + x)) && (posY < y && posY > (y - maxY - 2))) {

            drawHoveringText(text, posX, posY, mc.fontRendererObj);

        }
    }

    protected void renderFluid(Fluid fluid, int x, int y, int mazX, int level) {

        TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());

        if(fluidStillSprite == null)
            fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();

        int color = fluid.getColor();

        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        GL11.glColor4f(r, g, b, 1.0F);

        this.drawTexturedModalRect(x, y - level, fluidStillSprite, mazX, level);
    }

    protected void renderFluid(Fluid fluid, int x, int y, int level) {
        renderFluid(fluid, x, y, 16, level);

    }

}
