package com.nik7.upgcraft.client.render.tileentity;

import com.nik7.upgcraft.client.render.model.ModelDoubleTank;
import com.nik7.upgcraft.client.render.model.ModelTank;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityRendererWoodenTank extends TileEntitySpecialRenderer {

    private final ModelBase smallModelTank = new ModelTank();
    private final ModelBase doubleModelTank = new ModelDoubleTank();

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {

        ModelBase modelTank;

        boolean doubleTank = false;
        ResourceLocation textures;
        int dY = 0;

        if (te instanceof UpgCtileentityTank) {
            doubleTank = ((UpgCtileentityTank) te).hasAdjacentTank();
            if (((UpgCtileentityTank) te).adjacentTankYNeg != null) {
                dY = 1;
            }
        }

        if (!doubleTank) {
            modelTank = smallModelTank;
            textures = (new ResourceLocation(Texture.Blocks.MODEL_SMALL_WOODEN_TANK));
        } else {
            modelTank = doubleModelTank;
            textures = (new ResourceLocation(Texture.Blocks.MODEL_DOUBLE_WOODEN_TANK));
        }


        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F - dY, (float) z + 0.5F);
        this.bindTexture(textures);

        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        modelTank.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        GL11.glPopMatrix();


    }


}
