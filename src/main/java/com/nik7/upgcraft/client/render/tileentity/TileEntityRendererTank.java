package com.nik7.upgcraft.client.render.tileentity;


import com.nik7.upgcraft.block.BlockUpgCTank;
import com.nik7.upgcraft.client.render.model.ModelDoubleTank;
import com.nik7.upgcraft.client.render.model.ModelTank;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityWoodenFluidTank;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityRendererTank extends TileEntitySpecialRenderer<UpgCtileentityTank> {

    private final static ModelBase smallModelTank = new ModelTank();
    private final static ModelBase doubleModelTank = new ModelDoubleTank();


    @Override
    public void renderTileEntityAt(UpgCtileentityTank te, double x, double y, double z, float partialTicks, int destroyStage) {

        Block blockTank = te.getBlockType();
        ModelBase modelTank;

        int meta = te.getBlockMetadata();

        boolean isGlasses = blockTank.getStateFromMeta(meta).getValue(BlockUpgCTank.TYPE).equals(BlockUpgCTank.TankType.GLASSES);

        boolean isTop = false;

        boolean isDouble = te.isDouble();
        if (isDouble) {
            isTop = te.isTop();
            modelTank = doubleModelTank;
        } else
            modelTank = smallModelTank;

        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);

        if (!isDouble) {

            if (destroyStage >= 0) {
                this.bindTexture(DESTROY_STAGES[destroyStage]);
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 1.0F);
                GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                GlStateManager.matrixMode(5888);

            } else if (isGlasses) {

                if (te instanceof UpgCtileentityWoodenFluidTank)
                    this.bindTexture(new ResourceLocation(Texture.MODEL_SMALL_HOLLOW_WOODEN_TANK));


            } else {

                if (te instanceof UpgCtileentityWoodenFluidTank)
                    this.bindTexture(new ResourceLocation(Texture.MODEL_SMALL_WOODEN_TANK));

            }

        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }

        int dY = 0;

        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F - dY, (float) z + 0.5F);

        GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);
        modelTank.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);


        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }


    }
}
