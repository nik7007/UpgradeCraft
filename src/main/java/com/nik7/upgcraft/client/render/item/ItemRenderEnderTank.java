package com.nik7.upgcraft.client.render.item;


import com.nik7.upgcraft.client.render.model.ModelTank;
import com.nik7.upgcraft.reference.Render;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.util.RenderHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ItemRenderEnderTank implements IItemRenderer {

    private final ModelTank modelTank = new ModelTank();

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

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Texture.Blocks.MODEL_SMALL_ENDER_TANK));

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

        if (data.length > 1) {

            EntityClientPlayerMP player = null;

            for (Object aData : data) {
                if (aData instanceof EntityClientPlayerMP) {
                    player = (EntityClientPlayerMP) aData;
                    break;

                }

            }

            if (player != null) {

                RenderHelper.renderEndPortal(Render.TankInternalDimension.xMin - 0.5, Render.TankInternalDimension.xMaz - 0.5, Render.TankInternalDimension.yMin + 0.5, Render.TankInternalDimension.yMaz + 0.5, Render.TankInternalDimension.zMin - 0.5, Render.TankInternalDimension.zMaz - 0.5, (float) player.field_71091_bM, (float) player.field_71096_bN, (float) player.field_71097_bO);

            } else
                RenderHelper.renderEndPortal(Render.TankInternalDimension.xMin - 0.5, Render.TankInternalDimension.xMaz - 0.5, Render.TankInternalDimension.yMin + 0.5, Render.TankInternalDimension.yMaz + 0.5, Render.TankInternalDimension.zMin - 0.5, Render.TankInternalDimension.zMaz - 0.5, 1, 1, 1);


        }


        GL11.glPopMatrix();

    }
}
