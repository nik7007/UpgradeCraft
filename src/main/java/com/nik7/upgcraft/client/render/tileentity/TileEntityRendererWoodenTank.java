package com.nik7.upgcraft.client.render.tileentity;

import com.nik7.upgcraft.client.render.model.ModelDoubleTank;
import com.nik7.upgcraft.client.render.model.ModelTank;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.util.LogHelper;
import com.nik7.upgcraft.util.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityRendererWoodenTank extends TileEntitySpecialRenderer {

    private final ModelBase smallModelTank = new ModelTank();
    private final ModelBase doubleModelTank = new ModelDoubleTank();

    private final float xMin = 0.063f;
    private final float yMin = 0.06f;
    private final float zMin = 0.063f;

    private final float xMaz = 0.937f;
    private final float yMaz = 0.9f;
    private final float zMaz = 0.937f;


    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {

        ModelBase modelTank;


        ResourceLocation textures;
        int dY = 0;

        if (te instanceof UpgCtileentityTank) {

            boolean doubleTank;
            int meta = te.getBlockMetadata();
            int metaAdj = 0;
            doubleTank = ((UpgCtileentityTank) te).hasAdjacentTank();

            if (((UpgCtileentityTank) te).adjacentTankYNeg != null) {
                dY = 1;
                metaAdj = ((UpgCtileentityTank) te).adjacentTankYNeg.blockMetadata;
            } else if (((UpgCtileentityTank) te).adjacentTankYPos != null) {
                metaAdj = ((UpgCtileentityTank) te).adjacentTankYPos.blockMetadata;
            }


            if (!doubleTank) {
                modelTank = smallModelTank;
                if (meta == 0) {
                    textures = new ResourceLocation(Texture.Blocks.MODEL_SMALL_WOODEN_TANK);
                } else if (meta == 1) {
                    textures = new ResourceLocation(Texture.Blocks.MODEL_SMALL_HOLLOW_WOODEN_TANK);
                } else {
                    LogHelper.fatal("Impossible to render wooden tank, 'cause metadata!!");
                    return;
                }
            } else {
                modelTank = doubleModelTank;
                if (meta == 1 && metaAdj == 1)
                    textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_WOODEN_TANK);

                else if (meta == 1 || metaAdj == 1) {
                    if (meta == 0 && ((UpgCtileentityTank) te).adjacentTankYNeg != null) {
                        textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_DOWN_WOODEN_TANK);
                    } else if (meta == 1 && ((UpgCtileentityTank) te).adjacentTankYNeg == null) {
                        textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_DOWN_WOODEN_TANK);
                    } else textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_TOP_WOODEN_TANK);
                } else
                    textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_WOODEN_TANK);
            }


            GL11.glPushMatrix();

            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F - dY, (float) z + 0.5F);
            this.bindTexture(textures);

            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);

            modelTank.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);


            FluidStack fluidStack;

            if ((fluidStack = ((UpgCtileentityTank) te).getFluid()) != null) {
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

                if (meta == 1 && !doubleTank) {

                    RenderHelper.fluidRender(((UpgCtileentityTank) te).getFillPercentage(), fluidStack.getFluid(), xMin, yMin, zMin, xMaz, yMaz, zMaz, false, false);


                } else if (meta == 1 || metaAdj == 1) {

                    float percentage = ((UpgCtileentityTank) te).getFillPercentage();
                    if (dY == 0) {

                        RenderHelper.fluidRender(percentage * 2, fluidStack.getFluid(), xMin, yMin, zMin, xMaz, 1, zMaz, false, false);


                    } else if (percentage >= 0.5f) {
                        RenderHelper.fluidRender((percentage - 0.5f) * 2, fluidStack.getFluid(), xMin, 0, zMin, xMaz, yMaz, zMaz, true, false);
                    }

                }

                GL11.glDisable(GL11.GL_BLEND);
            }

            GL11.glPopMatrix();

        }
    }


}
