package com.nik7.upgcraft.util;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.Random;

public class RenderHelper {

    private static final ResourceLocation endSky = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation endPortal = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random random = new Random(31100L);
    private static final FloatBuffer gl = GLAllocation.createDirectFloatBuffer(16);


    public static void fluidRender(float fillPercentage, Fluid fluid, float xMin, float yMin, float zMin, float xMaz, float maxY, float zMax, boolean top, boolean renderDown) {

        fluidRender(fillPercentage, fluid, xMin, yMin, zMin, xMaz, maxY, zMax, top, renderDown, true);
    }

    public static void fluidRender(float fillPercentage, Fluid fluid, float xMin, float yMin, float zMin, float xMaz, float maxY, float zMax, boolean top, boolean renderDown, boolean renderTop) {


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
            if (renderTop && height <= maxY) {
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

    public static void renderEndPortal(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax, float oldPx, float oldPy, float oldPz) {
        GL11.glDisable(GL11.GL_LIGHTING);
        random.setSeed(31100L);
        float f4 = 0.75F;

        for (int i = 0; i < 15; ++i) {
            GL11.glPushMatrix();
            float f5 = (float) (16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);

            if (i == 0) {
                Minecraft.getMinecraft().renderEngine.bindTexture(endSky);
                f7 = 0.1F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if (i == 1) {
                Minecraft.getMinecraft().renderEngine.bindTexture(endPortal);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                f6 = 0.5F;
            }

            float f8 = (float) (-(yMin + (double) f4));
            float f9 = f8 + ActiveRenderInfo.objectY;
            float f10 = f8 + f5 + ActiveRenderInfo.objectY;
            float f11 = f9 / f10;
            f11 += (float) (yMin + (double) f4);
            GL11.glTranslatef(oldPx, f11, oldPz);

            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);

            GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));

            GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float) (Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float) (i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-oldPx, -oldPz, -oldPy);
            f9 = f8 + ActiveRenderInfo.objectY;
            GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -oldPy);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;

            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);

            //north
            tessellator.addVertex(xMax, yMin, zMin);
            tessellator.addVertex(xMin, yMin, zMin);
            tessellator.addVertex(xMin, yMax, zMin);
            tessellator.addVertex(xMax, yMax, zMin);

            //south
            tessellator.addVertex(xMax, yMin, zMax);
            tessellator.addVertex(xMax, yMax, zMax);
            tessellator.addVertex(xMin, yMax, zMax);
            tessellator.addVertex(xMin, yMin, zMax);

            //east
            tessellator.addVertex(xMax, yMin, zMin);
            tessellator.addVertex(xMax, yMax, zMin);
            tessellator.addVertex(xMax, yMax, zMax);
            tessellator.addVertex(xMax, yMin, zMax);

            //west
            tessellator.addVertex(xMin, yMin, zMax);
            tessellator.addVertex(xMin, yMax, zMax);
            tessellator.addVertex(xMin, yMax, zMin);
            tessellator.addVertex(xMin, yMin, zMin);

            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private static FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
        gl.clear();
        gl.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        gl.flip();
        return gl;
    }


}
