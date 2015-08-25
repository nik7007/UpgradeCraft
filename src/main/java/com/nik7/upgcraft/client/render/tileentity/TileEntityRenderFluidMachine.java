package com.nik7.upgcraft.client.render.tileentity;


import com.nik7.upgcraft.client.render.model.ModelFluidMachine;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.*;
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
public class TileEntityRenderFluidMachine extends TileEntitySpecialRenderer {

    private static final ModelBase fluidMachine = new ModelFluidMachine();
    private static final ResourceLocation textureFurnace = new ResourceLocation(Texture.Blocks.MODEL_FLUID_FURNACE);
    private static final ResourceLocation textureInfuser = new ResourceLocation(Texture.Blocks.MODEL_FLUID_INFUSER);
    private static final ResourceLocation textureTermoFluidFurnace = new ResourceLocation(Texture.Blocks.MODEL_TERMO_FLUID_FURNACE);
    private static final ResourceLocation textureActionMaker = new ResourceLocation(Texture.Blocks.MODEL_ACTION_MAKER);

    //Fluid render
    private final static float xMin = 0.063f;
    private final static float yMin = 0.0625f;
    private final static float zMin = 0.063f;

    private final static float xMaz = 0.85f;
    private final static float yMaz = 0.5f;
    private final static float zMaz = 0.987f;


    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {

        if (tileEntity instanceof UpgCtileentityInventoryFluidHandler) {

            UpgCtileentityInventoryFluidHandler inventoryFluidHandler = (UpgCtileentityInventoryFluidHandler) tileEntity;

            ResourceLocation texture;

            if (inventoryFluidHandler instanceof UpgCtileentityFluidFurnace) {
                texture = textureFurnace;

            } else if (inventoryFluidHandler instanceof UpgCtileentityFluidInfuser) {
                texture = textureInfuser;


            } else if (inventoryFluidHandler instanceof UpgCtileentityTermoFluidFurnace) {
                texture = textureTermoFluidFurnace;
            } else if (inventoryFluidHandler instanceof UpgCtileentityActiveMaker) {
                texture = textureActionMaker;
            } else {
                LogHelper.error("This entity has not a skin! " + tileEntity.getClass());
                return;
            }

            int meta = tileEntity.getBlockMetadata();
            short angle = 0;

            switch (meta) {
                case 2:
                    angle = 0;
                    break;
                case 3:
                    angle = 180;
                    break;
                case 4:
                    angle = -90;
                    break;
                case 5:
                    angle = 90;
                    break;
            }

            GL11.glPushMatrix();

            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
            this.bindTexture(texture);

            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef((float) angle, 0.0F, 1.0F, 0.0F);

            fluidMachine.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            float dA = 0;

            switch (angle) {
                case 90:
                    dA = -180;
                    break;
                case -90:
                    dA = 0;
                    break;
                case 180:
                    dA = 90;
                    break;
                case 0:
                    dA = -90;
                    break;
            }


            GL11.glRotatef(angle + dA, 0.0F, 1.0F, 0.0F);

            FluidStack fluid = inventoryFluidHandler.getFluid(0);


            if (fluid != null) {

                float level = (float) fluid.amount / (float) inventoryFluidHandler.capacity;
                RenderHelper.fluidRender(level, fluid.getFluid(), xMin, yMin, zMin, xMaz, yMaz, zMaz, false, false);

            }
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        } else {
            LogHelper.error("Try to Render something that isn't a UpgCtileentityInventoryFluidHandler: " + tileEntity.getClass());
        }


    }
}
