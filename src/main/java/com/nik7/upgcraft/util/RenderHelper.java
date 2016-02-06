package com.nik7.upgcraft.util;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelper {

    public static void renderFluid(float fluidLevel, float size, float maxHeight, float minHeight, FluidStack fluid, boolean renderTop, boolean isTop, boolean renderDown) {
        if (fluid == null) return;
        GlStateManager.pushMatrix();


        FluidStack fluidStack = new FluidStack(fluid, 1000);

        GlStateManager.translate(0.5, 0, 0.5); // minHeight + (fluidLevel) * (maxHeight - minHeight)

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();


        TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill().toString());

        int fluidColor = fluid.getFluid().getColor(fluidStack);

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        setGLColorFromInt(fluidColor);

        if (fluidLevel > 1) fluidLevel = 1.05f;

        float height = renderTop ? minHeight + (fluidLevel) * (maxHeight - minHeight) : 1;

        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        float xMax, zMax, xMin, zMin, yMin = minHeight;
        xMax = zMax = size;
        xMin = zMin = -size;

        if (isTop) yMin = 0;

        renderCuboid(worldRenderer, xMax, xMin, yMin, height, zMin, zMax, fluidStillSprite, renderTop, renderDown);


        tessellator.draw();

        GlStateManager.popMatrix();
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


    /*public static void fluidRender(float fillPercentage, Fluid fluid, float xMin, float yMin, float zMin, float xMax, float maxY, float zMax, boolean top, boolean renderDown, boolean renderTop) {


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

            TextureAtlasSprite texture = fluid.getFlowing();

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

            tessellator.startDrawingQuads();
            tessellator.setColorOpaque_F(r, g, b);


            //north
            tessellator.addVertexWithUV(xMax, yMin, zMin, uMax, vMin);
            tessellator.addVertexWithUV(xMin, yMin, zMin, uMin, vMin);
            tessellator.addVertexWithUV(xMin, height, zMin, uMin, vMin + (vHeight * height));
            tessellator.addVertexWithUV(xMax, height, zMin, uMax, vMin + (vHeight * height));

            //south
            tessellator.addVertexWithUV(xMax, yMin, zMax, uMin, vMin);
            tessellator.addVertexWithUV(xMax, height, zMax, uMin, vMin + (vHeight * height));
            tessellator.addVertexWithUV(xMin, height, zMax, uMax, vMin + (vHeight * height));
            tessellator.addVertexWithUV(xMin, yMin, zMax, uMax, vMin);

            //east
            tessellator.addVertexWithUV(xMax, yMin, zMin, uMin, vMin);
            tessellator.addVertexWithUV(xMax, height, zMin, uMin, vMin + (vHeight * height));
            tessellator.addVertexWithUV(xMax, height, zMax, uMax, vMin + (vHeight * height));
            tessellator.addVertexWithUV(xMax, yMin, zMax, uMax, vMin);

            //west
            tessellator.addVertexWithUV(xMin, yMin, zMax, uMin, vMin);
            tessellator.addVertexWithUV(xMin, height, zMax, uMin, vMin + (vHeight * height));
            tessellator.addVertexWithUV(xMin, height, zMin, uMax, vMin + (vHeight * height));
            tessellator.addVertexWithUV(xMin, yMin, zMin, uMax, vMin);

            //up
            if (renderTop && height <= maxY) {
                tessellator.addVertexWithUV(xMax, height, zMax, uMax, vMin);
                tessellator.addVertexWithUV(xMax, height, zMin, uMin, vMin);
                tessellator.addVertexWithUV(xMin, height, zMin, uMin, vMax);
                tessellator.addVertexWithUV(xMin, height, zMax, uMax, vMax);
            }


            //down
            if (renderDown) {
                tessellator.addVertexWithUV(xMax, yMin, zMin, uMax, vMin);
                tessellator.addVertexWithUV(xMax, yMin, zMax, uMin, vMin);
                tessellator.addVertexWithUV(xMin, yMin, zMax, uMin, vMax);
                tessellator.addVertexWithUV(xMin, yMin, zMin, uMax, vMax);
            }

            tessellator.draw();
            GL11.glEnable(GL11.GL_LIGHTING);

        }

    }

    private static final ResourceLocation endSky = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation endPortal = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random random = new Random(31100L);
    private static final FloatBuffer gl = GLAllocation.createDirectFloatBuffer(5);

    public static void renderEndPortal(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax, float oldPx, float oldPy, float oldPz) {
        GL11.glDisable(GL11.GL_LIGHTING);
        random.setSeed(31100L);
        float f4 = 0.75F;

        for (int i = 0; i < 5; ++i) {
            GL11.glPushMatrix();
            float f5 = (float) (7.4 - i);
            float f6 = 1f;//0.0625F + 0.3f;
            float f7 = 1F / (f5 + 1.0F - 0.4f);

            if (i == 0) {
                Minecraft.getMinecraft().renderEngine.bindTexture(endSky);
                f7 = 0.086F;
                f5 = 8.0F;
                f6 = 0.97F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if (i == 1) {
                Minecraft.getMinecraft().renderEngine.bindTexture(endPortal);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                f6 = 0.26F;
                f7 = 0.148F;
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

            GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, fillFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, fillFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, fillFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, fillFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));

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

            if (yMax > yMin) {
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
            } else {

                //up
                // tessellator.addVertex(xMax, yMax, zMax);
                // tessellator.addVertex(xMax, yMax, zMin);
                // tessellator.addVertex(xMin, yMax, zMin);
                // tessellator.addVertex(xMin, yMax, zMax);


                //down
                tessellator.addVertex(xMax, yMin, zMin);
                tessellator.addVertex(xMax, yMin, zMax);
                tessellator.addVertex(xMin, yMin, zMax);
                tessellator.addVertex(xMin, yMin, zMin);
            }

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

    private static FloatBuffer fillFloatBuffer(float f1, float f2, float f3, float f4) {
        gl.clear();
        gl.put(f1).put(f2).put(f3).put(f4);
        gl.flip();
        return gl;
    }*/


}
