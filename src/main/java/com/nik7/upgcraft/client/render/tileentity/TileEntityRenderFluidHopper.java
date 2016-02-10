package com.nik7.upgcraft.client.render.tileentity;


import com.nik7.upgcraft.block.BlockUpgCBasicFluidHopper;
import com.nik7.upgcraft.client.render.model.ModelFluidHopper;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtilientityBasicFluidHopper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityRenderFluidHopper extends TileEntitySpecialRenderer<UpgCtilientityBasicFluidHopper> {

    private static final ModelFluidHopper fluidHopper = new ModelFluidHopper();

    @Override
    public void renderTileEntityAt(UpgCtilientityBasicFluidHopper te, double x, double y, double z, float partialTicks, int destroyStage) {

        int meta = te.getBlockMetadata();
        Block hopperBlock = te.getBlockType();
        IBlockState blockState = hopperBlock.getStateFromMeta(meta);

        EnumFacing direction = blockState.getValue(BlockUpgCBasicFluidHopper.FACING);
        boolean burned = blockState.getValue(BlockUpgCBasicFluidHopper.BURNED);

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);

        if (!burned)
            this.bindTexture(new ResourceLocation(Texture.MODEL_BASIC_FLUID_HOPPER));
        else this.bindTexture(new ResourceLocation(Texture.MODEL_BASIC_FLUID_HOPPER_BURNED));

        GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);

        float angle = (float) Math.PI / 2;

        switch (direction) {

            case EAST:
                fluidHopper.end.setRotationPoint(-4f, 16f, -2f);
                fluidHopper.end.rotateAngleZ = angle;
                break;

            case WEST:
                fluidHopper.end.setRotationPoint(4f, 20, -2f);
                fluidHopper.end.rotateAngleZ = -angle;
                break;

            case NORTH:
                fluidHopper.end.setRotationPoint(-2f, 20, -8f);
                fluidHopper.end.rotateAngleZ = -angle;
                break;

            case SOUTH:
                fluidHopper.end.setRotationPoint(-2f, 20, 4f);
                fluidHopper.end.rotateAngleZ = -angle;
                break;

            case DOWN:
                fluidHopper.end.setRotationPoint(-2F, 20F, -2F);
                fluidHopper.end.rotateAngleZ = 0;
                break;
        }


        fluidHopper.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        GlStateManager.popMatrix();

    }
}
