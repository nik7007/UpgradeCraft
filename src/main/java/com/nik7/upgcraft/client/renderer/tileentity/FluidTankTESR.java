package com.nik7.upgcraft.client.renderer.tileentity;


import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.tileentity.TileEntityFluidTank;
import com.nik7.upgcraft.util.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class FluidTankTESR extends TileEntitySpecialRenderer<TileEntityFluidTank> {

    @Override
    public void renderTileEntityAt(TileEntityFluidTank te, double x, double y, double z, float partialTicks, int destroyStage) {

        float fluidPercentage = te.getFillPercentage();
        if (te.renderInsideFluid() && fluidPercentage > 0) {

            FluidStack fluidStack = te.getFluid();

            if (fluidStack != null) {
                GlStateManager.disableLighting();

                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, z);

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

                this.renderFluid(fluidPercentage, te.getFluid(), true, false, false);

                GlStateManager.disableBlend();

                GlStateManager.popMatrix();

                GlStateManager.enableLighting();
            }

        }

    }


    private void renderFluid(float fluidLevel, FluidStack fluid, boolean renderTop, boolean isTop, boolean isDouble) {
        float minHeight = Render.TankInternalDimension.yMin;
        float maxHeight = Render.TankInternalDimension.yMax;
        float size = Render.TankInternalDimension.xMaz;
        RenderHelper.renderFluid(fluidLevel, size, maxHeight, minHeight, fluid, renderTop, isTop, false, isDouble);

    }

}
