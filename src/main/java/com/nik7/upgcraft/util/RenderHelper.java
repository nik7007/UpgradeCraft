package com.nik7.upgcraft.util;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.FloatBuffer;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class RenderHelper {

    private static Tessellator tessellator = Tessellator.getInstance();
    private static WorldRenderer worldRenderer = tessellator.getWorldRenderer();

    public static void renderFluid(float fluidLevel, float sizeX, float sizeZ, float maxHeight, float minHeight, FluidStack fluid, boolean renderTop, boolean isTop, boolean renderDown, boolean isDouble) {

        if (fluid == null || minHeight >= maxHeight) return;

        if (isDouble && !isTop)
            maxHeight = 1;

        GlStateManager.pushMatrix();


        FluidStack fluidStack = new FluidStack(fluid, 1000);

        GlStateManager.translate(0.5, 0, 0.5); // minHeight + (fluidLevel) * (maxHeight - minHeight)


        TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill().toString());

        int fluidColor = fluid.getFluid().getColor(fluidStack);

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        setGLColorFromInt(fluidColor);

        if (fluidLevel > 1) fluidLevel = 1.05f;

        float height = renderTop ? minHeight + (fluidLevel) * (maxHeight - minHeight) : 1;

        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        float xMax, zMax, xMin, zMin, yMin = minHeight;
        xMax = sizeX;
        zMax = sizeZ;
        xMin = -sizeX;
        zMin = -sizeZ;

        if (isTop) yMin = 0;

        renderCuboid(worldRenderer, xMax, xMin, yMin, height, zMin, zMax, fluidStillSprite, renderTop, renderDown);


        tessellator.draw();

        GlStateManager.popMatrix();

    }

    public static void renderFluid(float fluidLevel, float size, float maxHeight, float minHeight, FluidStack fluid, boolean renderTop, boolean isTop, boolean renderDown, boolean isDouble) {
        renderFluid(fluidLevel, size, size, maxHeight, minHeight, fluid, renderTop, isTop, renderDown, isDouble);
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        GlStateManager.color(red, green, blue, 1.0F);
    }

    private static void renderCuboid(WorldRenderer worldRenderer, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite textureAtlasSprite, boolean renderTop, boolean renderDown) {

        double uMin = (double) textureAtlasSprite.getMinU();
        double uMax = (double) textureAtlasSprite.getMaxU();
        double vMin = (double) textureAtlasSprite.getMinV();
        double vMax = (double) textureAtlasSprite.getMaxV();

        final double vHeight = vMax - vMin;

        //top
        if (renderTop) {
            addVertexWithUV(worldRenderer, xMax, height, zMax, uMax, vMin);
            addVertexWithUV(worldRenderer, xMax, height, zMin, uMin, vMin);
            addVertexWithUV(worldRenderer, xMin, height, zMin, uMin, vMax);
            addVertexWithUV(worldRenderer, xMin, height, zMax, uMax, vMax);
        }

        //north
        addVertexWithUV(worldRenderer, xMax, yMin, zMin, uMax, vMin);
        addVertexWithUV(worldRenderer, xMin, yMin, zMin, uMin, vMin);
        addVertexWithUV(worldRenderer, xMin, height, zMin, uMin, vMin + (vHeight * height));
        addVertexWithUV(worldRenderer, xMax, height, zMin, uMax, vMin + (vHeight * height));

        //south
        addVertexWithUV(worldRenderer, xMax, yMin, zMax, uMin, vMin);
        addVertexWithUV(worldRenderer, xMax, height, zMax, uMin, vMin + (vHeight * height));
        addVertexWithUV(worldRenderer, xMin, height, zMax, uMax, vMin + (vHeight * height));
        addVertexWithUV(worldRenderer, xMin, yMin, zMax, uMax, vMin);

        //east
        addVertexWithUV(worldRenderer, xMax, yMin, zMin, uMin, vMin);
        addVertexWithUV(worldRenderer, xMax, height, zMin, uMin, vMin + (vHeight * height));
        addVertexWithUV(worldRenderer, xMax, height, zMax, uMax, vMin + (vHeight * height));
        addVertexWithUV(worldRenderer, xMax, yMin, zMax, uMax, vMin);

        //west
        addVertexWithUV(worldRenderer, xMin, yMin, zMax, uMin, vMin);
        addVertexWithUV(worldRenderer, xMin, height, zMax, uMin, vMin + (vHeight * height));
        addVertexWithUV(worldRenderer, xMin, height, zMin, uMax, vMin + (vHeight * height));
        addVertexWithUV(worldRenderer, xMin, yMin, zMin, uMax, vMin);

        if (renderDown) {
            addVertexWithUV(worldRenderer, xMax, yMin, zMin, uMax, vMin);
            addVertexWithUV(worldRenderer, xMax, yMin, zMax, uMin, vMin);
            addVertexWithUV(worldRenderer, xMin, yMin, zMax, uMin, vMax);
            addVertexWithUV(worldRenderer, xMin, yMin, zMin, uMax, vMax);
        }

    }

    private static void addVertexWithUV(WorldRenderer worldRenderer, float x, float y, float z, double u, double v) {

        worldRenderer.pos(x / 2f, y, z / 2f).tex(u, v).endVertex();

    }

    private static void addVertexWithColor(WorldRenderer worldRenderer, float x, float y, float z, float red, float green, float blue, float alpha) {

        worldRenderer.pos(x / 2f, y, z / 2f).color(red, green, blue, alpha).endVertex();

    }

    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);

    public static void renderEndPortal(float size, float yMin, float yMax, float oldPx, float oldPy, float oldPz) {
        renderEndPortal(-size, size, yMin, yMax, -size, size, oldPx, oldPy, oldPz);
    }

    public static void renderEndPortal(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, float oldPx, float oldPy, float oldPz) {
        GlStateManager.disableLighting();
        RANDOM.setSeed(31100L);
        float f3 = 0.75F;

        for (int i = 0; i < 5; ++i) {
            GlStateManager.pushMatrix();
            float f4 = (float) (7.4 - i);
            float f5 = 1F;// 0.0625F;
            float f6 = 1.0F / (f4 + 1.0F - 0.4F);

            if (i == 0) {
                Minecraft.getMinecraft().renderEngine.bindTexture(END_SKY_TEXTURE);
                f6 = 0.1F;
                f4 = 65.0F;
                f5 = 0.125F;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
            }

            if (i >= 1) {
                Minecraft.getMinecraft().renderEngine.bindTexture(END_PORTAL_TEXTURE);
            }

            if (i == 1) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(1, 1);
                f5 = 0.5F;
            }

            float f7 = (float) (-(yMin + (double) f3));
            float f8 = f7 + (float) ActiveRenderInfo.getPosition().yCoord;
            float f9 = f7 + f4 + (float) ActiveRenderInfo.getPosition().yCoord;
            float f10 = f8 / f9;
            f10 = (float) (yMin + (double) f3) + f10;
            GlStateManager.translate(oldPx, f10, oldPz);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9473, fillFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.T, 9473, fillFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.R, 9473, fillFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GlStateManager.texGen(GlStateManager.TexGen.Q, 9474, fillFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, (float) (Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GlStateManager.scale(f5, f5, f5);
            GlStateManager.translate(0.5F, 0.5F, 0.0F);
            GlStateManager.rotate((float) (i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-0.5F, -0.5F, 0.0F);
            GlStateManager.translate(-oldPx, -oldPz, -oldPy);
            f8 = f7 + (float) ActiveRenderInfo.getPosition().yCoord;
            GlStateManager.translate((float) ActiveRenderInfo.getPosition().xCoord * f4 / f8, (float) ActiveRenderInfo.getPosition().zCoord * f4 / f8, -oldPy);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            float f11 = (RANDOM.nextFloat() * 0.5F + 0.1F) * f6;
            float f12 = (RANDOM.nextFloat() * 0.5F + 0.4F) * f6;
            float f13 = (RANDOM.nextFloat() * 0.5F + 0.5F) * f6;

            if (i == 0) {
                f11 = f12 = f13 = 1.0F * f6;
            }

            /*worldrenderer.pos(x, y + (double)f3, z).color(f11, f12, f13, 1.0F).endVertex();
            worldrenderer.pos(x, y + (double)f3, z + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
            worldrenderer.pos(x + 1.0D, y + (double)f3, z + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
            worldrenderer.pos(x + 1.0D, y + (double)f3, z).color(f11, f12, f13, 1.0F).endVertex();*/

            //north
            addVertexWithColor(worldRenderer, xMax, yMin, zMin, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMin, yMin, zMin, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMin, yMax, zMin, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMax, yMax, zMin, f11, f12, f13, 1.0F);

            //south
            addVertexWithColor(worldRenderer, xMax, yMin, zMax, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMax, yMax, zMax, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMin, yMax, zMax, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMin, yMin, zMax, f11, f12, f13, 1.0F);

            //east
            addVertexWithColor(worldRenderer, xMax, yMin, zMin, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMax, yMax, zMin, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMax, yMax, zMax, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMax, yMin, zMax, f11, f12, f13, 1.0F);

            //west
            addVertexWithColor(worldRenderer, xMin, yMin, zMax, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMin, yMax, zMax, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMin, yMax, zMin, f11, f12, f13, 1.0F);
            addVertexWithColor(worldRenderer, xMin, yMin, zMin, f11, f12, f13, 1.0F);


            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            Minecraft.getMinecraft().renderEngine.bindTexture(END_SKY_TEXTURE);
        }

        GlStateManager.disableBlend();
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
        GlStateManager.enableLighting();
    }

    private static FloatBuffer fillFloatBuffer(float f1, float f2, float f3, float f4) {
        floatBuffer.clear();
        floatBuffer.put(f1).put(f2).put(f3).put(f4);
        floatBuffer.flip();
        return floatBuffer;
    }


}
