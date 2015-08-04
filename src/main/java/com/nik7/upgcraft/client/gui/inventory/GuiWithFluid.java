package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiWithFluid extends GuiContainer {


    public GuiWithFluid(Container container) {
        super(container);
    }

    protected void renderFluidWithToolTip(FluidStack fluid, int maxFluidAmount, int x, int y, int mazX, int maxY, int mouseX, int mouseY, int level) {

        renderFluid(fluid.getFluid(), x, y, mazX, level);

        int posX = mouseX - this.guiLeft;
        int posY = mouseY - this.guiTop;

        if ((posX > x && posX < (mazX + x)) && (posY < y && posY > (y - maxY - 2))) {
            List<String> strings = new ArrayList<String>();

            strings.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidname") + ": " + EnumChatFormatting.RESET + fluid.getLocalizedName());
            strings.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.fluidamount") + ": " + EnumChatFormatting.RESET + fluid.amount + "/" + maxFluidAmount + " mB");

            drawHoveringText(strings, posX, posY, mc.fontRenderer);

        }


    }

    protected void renderFluid(Fluid fluid, int x, int y, int mazX, int level) {

        IIcon icon = fluid.getIcon();

        int color = fluid.getColor();

        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        GL11.glColor4f(r, g, b, 1.0F);

        this.drawTexturedModelRectFromIcon(x, y - level, icon, mazX, level);
    }

    protected void renderFluid(Fluid fluid, int x, int y, int level) {
        renderFluid(fluid, x, y, 16, level);

    }

}
