package com.nik7.upgcraft.util;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.opengl.GL11;

public class RenderHelper {

    public static void fluidRender(float fillPercentage, Fluid fluid, float xMin, float yMin, float zMin, float xMaz, float maxY, float zMax, boolean top, boolean renderDown) {


        if (fillPercentage > 0 && maxY > 0) {

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glRotatef(-180F, 0.0F, 0.0F, 1.0F);

            if (!top)
                GL11.glTranslatef(-0.5F, -1.5F, -0.5F);
            else

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

            if (fillPercentage > 1) {
                fillPercentage = 1.05F;
            }

            float height = (maxY - yMin) * fillPercentage + yMin;

            IIcon texture = fluid.getStillIcon();
            final int color;

            if (texture != null) {
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                color = fluid.getColor();
            } else {
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("missingno");
                color = 0xFFFFFFFF;
            }

            final double uMin = texture.getMinU();
            final double uMax = texture.getMaxU();
            final double vMin = texture.getMinV();
            final double vMax = texture.getMaxV();

            final double vHeight = vMax - vMin;

            final float r = (color >> 16 & 0xFF) / 255.0F;
            final float g = (color >> 8 & 0xFF) / 255.0F;
            final float b = (color & 0xFF) / 255.0F;


            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setColorOpaque_F(r, g, b);


            //north
            t.addVertexWithUV(xMaz, yMin, zMin, uMax, vMin);
            t.addVertexWithUV(xMin, yMin, zMin, uMin, vMin);
            t.addVertexWithUV(xMin, height, zMin, uMin, vMin + (vHeight * height));
            t.addVertexWithUV(xMaz, height, zMin, uMax, vMin + (vHeight * height));

            //south
            t.addVertexWithUV(xMaz, yMin, zMax, uMin, vMin);
            t.addVertexWithUV(xMaz, height, zMax, uMin, vMin + (vHeight * height));
            t.addVertexWithUV(xMin, height, zMax, uMax, vMin + (vHeight * height));
            t.addVertexWithUV(xMin, yMin, zMax, uMax, vMin);

            //east
            t.addVertexWithUV(xMaz, yMin, zMin, uMin, vMin);
            t.addVertexWithUV(xMaz, height, zMin, uMin, vMin + (vHeight * height));
            t.addVertexWithUV(xMaz, height, zMax, uMax, vMin + (vHeight * height));
            t.addVertexWithUV(xMaz, yMin, zMax, uMax, vMin);

            //west
            t.addVertexWithUV(xMin, yMin, zMax, uMin, vMin);
            t.addVertexWithUV(xMin, height, zMax, uMin, vMin + (vHeight * height));
            t.addVertexWithUV(xMin, height, zMin, uMax, vMin + (vHeight * height));
            t.addVertexWithUV(xMin, yMin, zMin, uMax, vMin);

            //up
            if (height <= maxY) {
                t.addVertexWithUV(xMaz, height, zMax, uMax, vMin);
                t.addVertexWithUV(xMaz, height, zMin, uMin, vMin);
                t.addVertexWithUV(xMin, height, zMin, uMin, vMax);
                t.addVertexWithUV(xMin, height, zMax, uMax, vMax);
            }


            //down
            if (renderDown) {
                t.addVertexWithUV(xMaz, yMin, zMin, uMax, vMin);
                t.addVertexWithUV(xMaz, yMin, zMax, uMin, vMin);
                t.addVertexWithUV(xMin, yMin, zMax, uMin, vMax);
                t.addVertexWithUV(xMin, yMin, zMin, uMax, vMax);
            }

            t.draw();
            GL11.glEnable(GL11.GL_LIGHTING);

        }


    }

    public static void renderFluidinGUI(float fillPercentage, Fluid fluid, float xMin, float yMin, float z, float xMaz, float maxY) {


        if (fillPercentage > 0 && maxY > 0) {

            float height = (maxY - yMin) * fillPercentage + yMin;

            IIcon texture = fluid.getStillIcon();
            final int color;

            if (texture != null) {
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                color = fluid.getColor();
            } else {
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("missingno");
                color = 0xFFFFFFFF;
            }

            final double uMin = texture.getMinU();
            final double uMax = texture.getMaxU();
            final double vMin = texture.getMinV();
            final double vMax = texture.getMaxV();

            final double vHeight = vMax - vMin;

            final float r = (color >> 16 & 0xFF) / 255.0F;
            final float g = (color >> 8 & 0xFF) / 255.0F;
            final float b = (color & 0xFF) / 255.0F;


            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setColorOpaque_F(r, g, b);

            t.addVertexWithUV(xMaz, yMin, z, uMin, vMin);
            t.addVertexWithUV(xMaz, height, z, uMin, vMin + (vHeight * height));
            t.addVertexWithUV(xMaz, height, z, uMax, vMin + (vHeight * height));
            t.addVertexWithUV(xMaz, yMin, z, uMax, vMin);

            t.draw();


        }


    }


}
