package com.nik7.upgcraft.client.render.item;

import com.nik7.upgcraft.client.render.model.ModelTank;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.util.LogHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;


//todo:separate item render from item block
@SideOnly(Side.CLIENT)
public class ItemRendererWoodenTank implements IItemRenderer {

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

        int meta = item.getItemDamage();

        switch (meta) {
            case 0:
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Texture.Blocks.MODEL_SMALL_WOODEN_TANK));
                break;
            case 1:
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Texture.Blocks.MODEL_SMALL_HOLLOW_WOODEN_TANK));
                break;
            default:
                LogHelper.fatal("Something really wrong appends in the item render for the wooden tank!!");
                break;
        }

        GL11.glPushMatrix(); //start

        GL11.glTranslated(0f, 1f, 0f);

        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(-90, 0, 1, 0);

        this.modelTank.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();

    }
}
