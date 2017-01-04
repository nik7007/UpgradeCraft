package com.nik7.upgcraft.client.renderer.tileentity;

import com.nik7.upgcraft.tileentity.TileEntityFluidFurnace;
import com.nik7.upgcraft.util.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import static com.nik7.upgcraft.reference.Render.TankInternalDimension.*;

@SideOnly(Side.CLIENT)
public class FluidMachineTESR extends TileEntitySpecialRenderer<TileEntityFluidFurnace> {

    @Override
    public void renderTileEntityAt(TileEntityFluidFurnace te, double x, double y, double z, float partialTicks, int destroyStage) {
        float fluidPercentage = te.getFillPercentage();

        if (fluidPercentage > 0) {

            FluidStack fluidStack = te.getFluid();
            if (fluidStack != null) {
                GlStateManager.disableLighting();

                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, z);

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

                RenderHelper.renderFluid(fluidPercentage, FM_SIZE, FM_Y_MAX, FM_Y_MIN, fluidStack);

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                GlStateManager.enableLighting();
            }

        }

    }
}
