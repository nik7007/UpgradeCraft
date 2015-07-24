package com.nik7.upgcraft.client.render.tileentity;


import com.nik7.upgcraft.block.BlockUpgCBasicFluidHopper;
import com.nik7.upgcraft.client.render.model.ModelHopper;
import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtilientityEnderHopper;
import com.nik7.upgcraft.util.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class TileEntityRenderEnderHopper extends TileEntitySpecialRenderer {

    private final ModelHopper enderHopper = new ModelHopper();


    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {

        ForgeDirection direction = ForgeDirection.getOrientation(BlockUpgCBasicFluidHopper.getDirectionFromMetadata(te.getBlockMetadata()));

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        this.bindTexture(new ResourceLocation(Texture.Blocks.MODEL_ENDER_TANK));
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);


        float angle = (float) Math.PI / 2;

        switch (direction) {

            case EAST:
                enderHopper.end.setRotationPoint(-4f, 16f, -2f);
                enderHopper.end.rotateAngleZ = angle;
                break;

            case WEST:
                enderHopper.end.setRotationPoint(4f, 20, -2f);
                enderHopper.end.rotateAngleZ = -angle;
                break;

            case NORTH:
                enderHopper.end.setRotationPoint(-2f, 20, -8f);
                enderHopper.end.rotateAngleZ = -angle;
                break;

            case SOUTH:
                enderHopper.end.setRotationPoint(-2f, 20, 4f);
                enderHopper.end.rotateAngleZ = -angle;
                break;

            case DOWN:
                enderHopper.end.setRotationPoint(-2F, 20F, -2F);
                enderHopper.end.rotateAngleZ = 0;
                break;
        }


        enderHopper.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        if (te instanceof UpgCtilientityEnderHopper)
            if (((UpgCtilientityEnderHopper) te).needsEnderPortalRender())
                RenderHelper.renderEndPortal(Render.TankInternalDimension.xMin - 0.5, Render.TankInternalDimension.xMaz - 0.5, 0.68, 0, Render.TankInternalDimension.zMin - 0.5, Render.TankInternalDimension.zMaz - 0.5, (float) this.field_147501_a.field_147560_j, (float) this.field_147501_a.field_147561_k, (float) this.field_147501_a.field_147558_l);

        GL11.glPopMatrix();

    }


}
