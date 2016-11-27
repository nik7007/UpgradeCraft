package com.nik7.upgcraft.util;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelper {

    private static Tessellator tessellator = Tessellator.getInstance();
    private static VertexBuffer vertexBuffer = tessellator.getBuffer();

    public static void renderFluid(float fluidLevel, float sizeX, float sizeZ, float maxHeight, float minHeight, FluidStack fluid, boolean renderTop, boolean isTop, boolean renderDown, boolean isDouble) {
        renderFluid(fluidLevel, sizeX, -sizeX, sizeZ, maxHeight, minHeight, fluid, renderTop, isTop, renderDown, isDouble);
    }

    public static void renderFluid(float fluidLevel, float xMax, float xMin, float sizeZ, float maxHeight, float minHeight, FluidStack fluid, boolean renderTop, boolean isTop, boolean renderDown, boolean isDouble) {

        if (fluid == null || minHeight >= maxHeight) return;

        if (isDouble && !isTop)
            maxHeight = 1;

        GlStateManager.pushMatrix();


        FluidStack fluidStack = new FluidStack(fluid, 1000);

        GlStateManager.translate(0.5, 0, 0.5); // minHeight + (fluidLevel) * (maxHeight - minHeight)


        TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill().toString());

        if (fluidStillSprite == null)
            fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();

        int fluidColor = fluid.getFluid().getColor(fluidStack);

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        setGLColorFromInt(fluidColor);

        if (fluidLevel > 1) fluidLevel = 1.05f;

        float height = renderTop ? minHeight + (fluidLevel) * (maxHeight - minHeight) : 1;

        vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);

        float zMax, zMin, yMin = minHeight;
        zMax = sizeZ;
        zMin = -sizeZ;

        if (isTop) yMin = 0;

        renderCuboid(vertexBuffer, xMax, xMin, yMin, height, zMin, zMax, fluidStillSprite, renderTop, renderDown);


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

    private static void renderCuboid(VertexBuffer vertexBuffer, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite textureAtlasSprite, boolean renderTop, boolean renderDown) {

        if (textureAtlasSprite == null)
            return;

        double uMin = (double) textureAtlasSprite.getMinU();
        double uMax = (double) textureAtlasSprite.getMaxU();
        double vMin = (double) textureAtlasSprite.getMinV();
        double vMax = (double) textureAtlasSprite.getMaxV();

        final double vHeight = vMax - vMin;

        //top
        if (renderTop) {
            addVertexWithUV(vertexBuffer, xMax, height, zMax, uMax, vMin);
            addVertexWithUV(vertexBuffer, xMax, height, zMin, uMin, vMin);
            addVertexWithUV(vertexBuffer, xMin, height, zMin, uMin, vMax);
            addVertexWithUV(vertexBuffer, xMin, height, zMax, uMax, vMax);
        }

        //north
        addVertexWithUV(vertexBuffer, xMax, yMin, zMin, uMax, vMin);
        addVertexWithUV(vertexBuffer, xMin, yMin, zMin, uMin, vMin);
        addVertexWithUV(vertexBuffer, xMin, height, zMin, uMin, vMin + (vHeight * height));
        addVertexWithUV(vertexBuffer, xMax, height, zMin, uMax, vMin + (vHeight * height));

        //south
        addVertexWithUV(vertexBuffer, xMax, yMin, zMax, uMin, vMin);
        addVertexWithUV(vertexBuffer, xMax, height, zMax, uMin, vMin + (vHeight * height));
        addVertexWithUV(vertexBuffer, xMin, height, zMax, uMax, vMin + (vHeight * height));
        addVertexWithUV(vertexBuffer, xMin, yMin, zMax, uMax, vMin);

        //east
        addVertexWithUV(vertexBuffer, xMax, yMin, zMin, uMin, vMin);
        addVertexWithUV(vertexBuffer, xMax, height, zMin, uMin, vMin + (vHeight * height));
        addVertexWithUV(vertexBuffer, xMax, height, zMax, uMax, vMin + (vHeight * height));
        addVertexWithUV(vertexBuffer, xMax, yMin, zMax, uMax, vMin);

        //west
        addVertexWithUV(vertexBuffer, xMin, yMin, zMax, uMin, vMin);
        addVertexWithUV(vertexBuffer, xMin, height, zMax, uMin, vMin + (vHeight * height));
        addVertexWithUV(vertexBuffer, xMin, height, zMin, uMax, vMin + (vHeight * height));
        addVertexWithUV(vertexBuffer, xMin, yMin, zMin, uMax, vMin);

        if (renderDown) {
            addVertexWithUV(vertexBuffer, xMax, yMin, zMin, uMax, vMin);
            addVertexWithUV(vertexBuffer, xMax, yMin, zMax, uMin, vMin);
            addVertexWithUV(vertexBuffer, xMin, yMin, zMax, uMin, vMax);
            addVertexWithUV(vertexBuffer, xMin, yMin, zMin, uMax, vMax);
        }

    }


    private static void addVertexWithUV(VertexBuffer vertexBuffer, float x, float y, float z, double u, double v) {

        vertexBuffer.pos(x / 2f, y, z / 2f).tex(u, v).endVertex();

    }

    private static void addVertexWithColor(VertexBuffer vertexBuffer, float x, float y, float z, float red, float green, float blue, float alpha) {

        vertexBuffer.pos(x / 2f, y, z / 2f).color(red, green, blue, alpha).endVertex();

    }
}
