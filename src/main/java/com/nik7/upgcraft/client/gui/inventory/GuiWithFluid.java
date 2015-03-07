package com.nik7.upgcraft.client.gui.inventory;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class GuiWithFluid extends GuiContainer {


    public GuiWithFluid(Container container) {
        super(container);
    }

    protected void renderFluid(Fluid fluid, int x, int y, int level) {
        IIcon icon = fluid.getIcon();

        int color = fluid.getColor();

        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        GL11.glColor4f(r, g, b, 1.0F);

        this.drawTexturedModelRectFromIcon(x, y - level, icon, 16, level);
    }
}
