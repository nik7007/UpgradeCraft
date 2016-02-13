package com.nik7.upgcraft.client.render.tileentity;


import com.nik7.upgcraft.block.BlockUpgCContainerOrientable;
import com.nik7.upgcraft.client.render.model.ModelFluidMachine;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
import com.nik7.upgcraft.util.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityRenderFluidMachine extends TileEntitySpecialRenderer<UpgCtileentityInventoryFluidHandler> {

    private static final ModelBase fluidMachine = new ModelFluidMachine();

    private final static float yMin = 0.0625f;
    private final static float yMaz = 0.5f;
    private final static float xMaz = 0.85f;
    private final static float zMaz = 0.745f;

    @Override
    public void renderTileEntityAt(UpgCtileentityInventoryFluidHandler te, double x, double y, double z, float partialTicks, int destroyStage) {

        int meta;

        try {
            meta = te.getBlockMetadata();
        } catch (NullPointerException e) {
            meta = te.getBlockMetadataClient();
        }

        Block block = te.getBlockType();
        IBlockState blockState = block.getStateFromMeta(meta);
        EnumFacing enumfacing = blockState.getValue(BlockUpgCContainerOrientable.FACING);
        short angle = 0;

        ResourceLocation texture = null;
        if (te instanceof UpgCtileentityFluidFurnace) {
            texture = new ResourceLocation(Texture.MODEL_FLUID_FURNACE);
        }

        switch (enumfacing) {
            case NORTH:
                angle = 0;
                break;
            case SOUTH:
                angle = 180;
                break;
            case WEST:
                angle = -90;
                break;
            case EAST:
                angle = 90;
                break;
        }

        GlStateManager.pushMatrix();

        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        this.bindTexture(texture);

        GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate((float) angle, 0.0F, 1.0F, 0.0F);

        fluidMachine.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();

        FluidStack fluidStack = te.getFluid(te.getTankToShow());

        if (fluidStack != null) {

            float level = te.getFillPercentage(te.getTankToShow());

            GlStateManager.disableLighting();

            GlStateManager.pushMatrix();

            int dx = 0, dz = 0;

            switch (enumfacing) {

                case EAST:
                    dz = 1;
                    break;

                case WEST:
                    dx = 1;
                    break;

                case SOUTH:
                    dx = 1;
                    dz = 1;
                    break;

            }

            GlStateManager.translate(x + dx, y, z + dz);
            GlStateManager.rotate((float) angle, 0.0F, 1.0F, 0.0F);

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            this.renderFluid(level, fluidStack);

            GlStateManager.disableBlend();

            GlStateManager.popMatrix();

            GlStateManager.enableLighting();

        }


    }

    private void renderFluid(float fluidLevel, FluidStack fluid) {

        RenderHelper.renderFluid(fluidLevel, xMaz, zMaz, yMaz, yMin, fluid, true, false, false, false);

    }
}
