package com.nik7.upgcraft.client.render.tileentity;

import com.nik7.upgcraft.client.render.model.ModelDoubleTank;
import com.nik7.upgcraft.client.render.model.ModelTank;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityEnderTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityTankClay;
import com.nik7.upgcraft.tileentities.UpgCtileentityWoodenTankSmall;
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
public class TileEntityRendererTank extends TileEntitySpecialRenderer {

    private final static ModelBase smallModelTank = new ModelTank();
    private final static ModelBase doubleModelTank = new ModelDoubleTank();

    private final static float xMin = 0.063f;
    private final static float yMin = 0.06f;
    private final static float zMin = 0.063f;

    private final static float xMaz = 0.937f;
    private final static float yMaz = 0.9f;
    private final static float zMaz = 0.937f;


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

            if (doubleTank) {
                if (((UpgCtileentityTank) te).isTop()) {
                    dY = 1;
                }
                metaAdj = ((UpgCtileentityTank) te).getAdjMeta();

            }


            if (!doubleTank) {
                modelTank = smallModelTank;
                switch (meta) {
                    case 0:
                        if (te instanceof UpgCtileentityWoodenTankSmall)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_SMALL_WOODEN_TANK);
                        else if (te instanceof UpgCtileentityTankClay)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_SMALL_CLAY_TANK);
                        else if (te instanceof UpgCtileentityEnderTank)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_SMALL_ENDER_TANK);
                        else {
                            LogHelper.fatal("Impossible to render this tank!! Unknown entity!");
                            return;
                        }
                        break;
                    case 1:
                        if (te instanceof UpgCtileentityWoodenTankSmall)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_SMALL_HOLLOW_WOODEN_TANK);
                        else if (te instanceof UpgCtileentityTankClay)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_SMALL_HOLLOW_CLAY_TANK);
                        else {
                            LogHelper.fatal("Impossible to render this tank!! Unknown entity!");
                            return;
                        }
                        break;

                    case 2:

                        if (te instanceof UpgCtileentityTankClay)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_SMALL_HARDENED_CLAY_TANK);
                        else {
                            LogHelper.fatal("Impossible to render wooden tank, 'cause metadata!!");
                            return;
                        }

                        break;

                    case 3:

                        if (te instanceof UpgCtileentityTankClay)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_SMALL_HOLLOW_HARDENED_CLAY_TANK);
                        else {
                            LogHelper.fatal("Impossible to render wooden tank, 'cause metadata!!");
                            return;
                        }

                        break;

                    default:
                        LogHelper.fatal("Impossible to render wooden tank, 'cause metadata!!");
                        return;
                }
            } else {
                modelTank = doubleModelTank;

                if (meta == 1 && metaAdj == 1) {
                    if (te instanceof UpgCtileentityWoodenTankSmall)
                        textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_WOODEN_TANK);
                    else if (te instanceof UpgCtileentityTankClay)
                        textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_CLAY_TANK);
                    else {
                        LogHelper.fatal("Impossible to render this tank!! Unknown entity!");
                        return;
                    }
                } else if (meta == 1 || metaAdj == 1) {
                    if (meta == 0 && ((UpgCtileentityTank) te).isTop()) {
                        if (te instanceof UpgCtileentityWoodenTankSmall)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_DOWN_WOODEN_TANK);
                        else if (te instanceof UpgCtileentityTankClay)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_DOWN_CLAY_TANK);
                        else {
                            LogHelper.fatal("Impossible to render this tank!! Unknown entity!");
                            return;
                        }

                    } else if (meta == 1 && !((UpgCtileentityTank) te).isTop()) {
                        if (te instanceof UpgCtileentityWoodenTankSmall)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_DOWN_WOODEN_TANK);
                        else if (te instanceof UpgCtileentityTankClay)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_DOWN_CLAY_TANK);
                        else {
                            LogHelper.fatal("Impossible to render this tank!! Unknown entity!");
                            return;
                        }
                    } else {
                        if (te instanceof UpgCtileentityWoodenTankSmall)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_TOP_WOODEN_TANK);
                        else if (te instanceof UpgCtileentityTankClay)
                            textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_TOP_CLAY_TANK);
                        else {
                            LogHelper.fatal("Impossible to render this tank!! Unknown entity!");
                            return;
                        }
                    }
                } else {
                    if (te instanceof UpgCtileentityWoodenTankSmall)
                        textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_WOODEN_TANK);

                    else if (te instanceof UpgCtileentityTankClay) {
                        if (meta < 2 && metaAdj < 2) {

                            textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_CLAY_TANK);

                        } else if (meta >= 2 && metaAdj >= 2 && (meta <= 3 && metaAdj <= 3)) {

                            if (meta == 3 && metaAdj == 3) {
                                textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_HARDENED_CLAY_TANK);
                            } else if (meta == 3 || metaAdj == 3) {
                                if (meta == 2 && ((UpgCtileentityTank) te).isTop()) {
                                    textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_DOWN_HARDENED_CLAY_TANK);
                                } else if (meta == 3 && !((UpgCtileentityTank) te).isTop()) {
                                    textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_DOWN_HARDENED_CLAY_TANK);
                                } else {
                                    textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HOLLOW_TOP_HARDENED_CLAY_TANK);
                                }

                            } else {
                                textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_HARDENED_CLAY_TANK);
                            }
                        } else {
                            LogHelper.fatal("Impossible to render this tank!! Unknown meta data!");
                            return;
                        }
                    } else {
                        LogHelper.fatal("Impossible to render this tank!! Unknown entity!");
                        return;
                    }
                }

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

                if ((meta == 1 || (meta == 3 && te instanceof UpgCtileentityTankClay)) && !doubleTank) {

                    RenderHelper.fluidRender(((UpgCtileentityTank) te).getFillPercentage(), fluidStack.getFluid(), xMin, yMin, zMin, xMaz, yMaz, zMaz, false, false);

                } else if (meta == 1 || metaAdj == 1 || ((meta == 3 || metaAdj == 3) && te instanceof UpgCtileentityTankClay)) {

                    float percentage = ((UpgCtileentityTank) te).getFillPercentage();
                    if (dY == 0) {
                        boolean renderTop = true;

                        if (percentage > 0.5f) {
                            percentage = 0.5f;
                            renderTop = false;
                        }

                        RenderHelper.fluidRender(percentage * 2, fluidStack.getFluid(), xMin, yMin, zMin, xMaz, 1, zMaz, false, false, renderTop);

                    } else if (percentage >= 0.5f) {
                        RenderHelper.fluidRender((percentage - 0.5f) * 2, fluidStack.getFluid(), xMin, 0, zMin, xMaz, yMaz, zMaz, true, false);
                    }

                }

                GL11.glDisable(GL11.GL_BLEND);
            }

            if (te instanceof UpgCtileentityEnderTank) {
                RenderHelper.renderEndPortal(xMin - 0.5, xMaz - 0.5, yMin + 0.5, yMaz + 0.5, zMin - 0.5, zMaz - 0.5, (float) this.field_147501_a.field_147560_j, (float) this.field_147501_a.field_147561_k, (float) this.field_147501_a.field_147558_l);
            }

            GL11.glPopMatrix();

        }
    }


}
