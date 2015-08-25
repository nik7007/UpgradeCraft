package com.nik7.upgcraft.client.gui.inventory;

import com.nik7.upgcraft.inventory.ContainerActiveMaker;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityActiveMaker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiActiveMaker extends GuiWithFluid {

    private final static ResourceLocation guiTextures = new ResourceLocation(Texture.GUI.ACTIVE_MAKER);
    private final UpgCtileentityActiveMaker upgCtileentityActiveMaker;

    public GuiActiveMaker(InventoryPlayer inventoryPlayer, UpgCtileentityActiveMaker upgCtileentityActiveMaker) {
        super(new ContainerActiveMaker(inventoryPlayer, upgCtileentityActiveMaker));
        this.upgCtileentityActiveMaker = upgCtileentityActiveMaker;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTextures);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);

    }
}
