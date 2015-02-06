package com.nik7.upgcraft.client.render.tileentity;

import com.nik7.upgcraft.client.render.model.ModelDoubleTank;
import com.nik7.upgcraft.client.render.model.ModelTank;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.util.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityRendererWoodenTank extends TileEntitySpecialRenderer {

    private final ModelBase smallModelTank = new ModelTank();
    private final ModelBase doubleModelTank = new ModelDoubleTank();

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
                else
                    textures = new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_WOODEN_TANK);
            }


            GL11.glPushMatrix();

            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F - dY, (float) z + 0.5F);
            this.bindTexture(textures);

            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);


            if (meta == 1 && metaAdj == 0) {
                FluidStack fluidStack = ((UpgCtileentityTank) te).getFluid();

                if (fluidStack != null) ;
                    fluidRender(te, fluidStack.getFluid());
            }

            modelTank.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

            GL11.glPopMatrix();


        }
    }


    //TODO: RENDER FLUID INSIDE TANK!!
    public void fluidRender(TileEntity tileEntity, Fluid fluid) {


        if (!(fluid == null || tileEntity == null)) {

            /*Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();

            tessellator.draw();*/

        }


    }


}
