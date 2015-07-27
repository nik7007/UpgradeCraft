package com.nik7.upgcraft.client.render.item;


import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.util.LogHelper;
import com.nik7.upgcraft.util.RenderHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ItemRenderClayTank extends ItemRenderTank {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        int meta = item.getItemDamage();

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Texture.Blocks.MODEL_SMALL_CLAY_TANK));

        switch (meta) {
            case 0:
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Texture.Blocks.MODEL_SMALL_CLAY_TANK));
                break;
            case 1:
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Texture.Blocks.MODEL_SMALL_HOLLOW_CLAY_TANK));
                break;
            case 2:
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Texture.Blocks.MODEL_SMALL_HARDENED_CLAY_TANK));
                break;
            case 3:
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Texture.Blocks.MODEL_SMALL_HOLLOW_HARDENED_CLAY_TANK));
                break;
            default:
                LogHelper.fatal("Something really wrong appends in the item render for the wooden tank!!");
                return;
        }

        renderModel(type, meta, item);

    }


    protected void renderModel(ItemRenderType type, int meta, ItemStack item) {

        GL11.glPushMatrix(); //start

        switch (type) {
            case ENTITY:
            case INVENTORY:
                GL11.glTranslated(0, 0.99, 0);
                break;
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(0.42, 1.53, 0.42);
                break;
            default:
                break;

        }

        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(90, 0, 1, 0);

        this.modelTank.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        /***/
        if (meta == 3) {
            FluidStack fluidStack = ((IFluidContainerItem) item.getItem()).getFluid(item);

            int capacity = ((IFluidContainerItem) item.getItem()).getCapacity(item);
            float percentage = 0;
            if (fluidStack != null && capacity != 0) {
                percentage = (float)fluidStack.amount /(float) capacity;
            }
            if (fluidStack != null)
                RenderHelper.fluidRender(percentage, fluidStack.getFluid(), Render.TankInternalDimension.xMin, Render.TankInternalDimension.yMin, Render.TankInternalDimension.zMin, Render.TankInternalDimension.xMaz, Render.TankInternalDimension.yMaz, Render.TankInternalDimension.zMaz, false, false);
        }
        /***/

        GL11.glPopMatrix();

    }

}
