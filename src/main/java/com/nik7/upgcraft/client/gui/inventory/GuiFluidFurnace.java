package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentity.TileEntityFluidFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiFluidFurnace extends GuiWithFluid {

    private final static ResourceLocation fluidFurnaceGuiTextures = new ResourceLocation(Texture.GUI.FLUID_FURNACE);
    private final TileEntityFluidFurnace fluidFurnace;

    public GuiFluidFurnace(InventoryPlayer inventoryPlayer, TileEntityFluidFurnace fluidFurnace) {
        super(fluidFurnace, new ContainerFluidFurnace(inventoryPlayer, fluidFurnace));
        this.fluidFurnace = fluidFurnace;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(fluidFurnaceGuiTextures);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);

        int inc = this.fluidFurnace.getBurnTimeRemainingScaled(14);
        this.drawTexturedModalRect(xOffset + 56, yOffset + 36 + 13 - inc, 176, 13 - inc, 14, inc);

        inc = this.fluidFurnace.getCookProgressScaled(24);
        this.drawTexturedModalRect(xOffset + 79, yOffset + 34, 176, 14, inc + 1, 16);

    }


}
