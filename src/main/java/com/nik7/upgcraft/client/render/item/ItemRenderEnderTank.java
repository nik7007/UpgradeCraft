package com.nik7.upgcraft.client.render.item;


import com.nik7.upgcraft.client.render.model.ModelTank;
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
    private final static float xMin = 0.063f;
    private final static float yMin = 0.06f;
    private final static float zMin = 0.063f;

    private final static float xMaz = 0.937f;
    private final static float yMaz = 0.9f;
    private final static float zMaz = 0.937f;

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

        GL11.glTranslated(0.28f, 1.24f, 0.28f);

        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(-90, 0, 1, 0);

        this.modelTank.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        if (data.length > 1) {

            EntityClientPlayerMP player = null;

            for (int i = 0; i < data.length; i++) {

                if (data[i] instanceof EntityClientPlayerMP) {
                    player = (EntityClientPlayerMP) data[i];
                    break;

                }

            }

            if (player != null) {

                RenderHelper.renderEndPortal(xMin - 0.5, xMaz - 0.5, yMin + 0.5, yMaz + 0.5, zMin - 0.5, zMaz - 0.5, (float) player.field_71091_bM,(float) player.field_71096_bN,(float) player.field_71097_bO);

            }

        }


        GL11.glPopMatrix();

    }
}
