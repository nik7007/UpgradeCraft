package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.inventory.ContainerEnderHopper;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtilientityEnderHopper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiEnderHopper extends GuiContainer {

    private final UpgCtilientityEnderHopper enderHopper;
    private final static ResourceLocation enderHopperGuiTextures = new ResourceLocation(Texture.GUI.ENDER_HOPPER);

    public GuiEnderHopper(InventoryPlayer playerInventory, UpgCtilientityEnderHopper enderHopper) {
        super(new ContainerEnderHopper(playerInventory, enderHopper));
        this.enderHopper = enderHopper;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.enderHopper.hasCustomInventoryName() ? this.enderHopper.getInventoryName() : I18n.format(this.enderHopper.getInventoryName());
        this.fontRendererObj.drawString(I18n.format(s), this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(enderHopperGuiTextures);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);

    }
}
