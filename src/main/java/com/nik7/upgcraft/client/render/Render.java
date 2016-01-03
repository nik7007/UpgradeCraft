package com.nik7.upgcraft.client.render;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Render {
    private  static final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

    public static void render(){
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.blockUpgCSlimyLog), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + ModBlocks.blockUpgCSlimyLog.getName(), "inventory"));
    }
}
