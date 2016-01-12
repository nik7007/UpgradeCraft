package com.nik7.upgcraft.client.render.item;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.tileentities.UpgCtileentityWoodenFluidTank;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemStackRender extends TileEntityItemStackRenderer {

    private UpgCtileentityWoodenFluidTank tank = new UpgCtileentityWoodenFluidTank();
    private TileEntityItemStackRenderer instance;

    public ItemStackRender(TileEntityItemStackRenderer instance) {
        this.instance = instance;
    }

    public void renderByItem(ItemStack itemStack) {
        Block block = Block.getBlockFromItem(itemStack.getItem());

        if (block == ModBlocks.blockUpgCWoodenFluidTank)
            TileEntityRendererDispatcher.instance.renderTileEntityAt(tank, 0.0d, 0.0d, 0.0d, 0.0f);

        else
            this.instance.renderByItem(itemStack);
    }
}
