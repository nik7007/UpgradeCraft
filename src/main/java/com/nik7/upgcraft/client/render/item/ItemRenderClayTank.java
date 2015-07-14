package com.nik7.upgcraft.client.render.item;


import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.util.LogHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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

        renderModel();

    }

}
