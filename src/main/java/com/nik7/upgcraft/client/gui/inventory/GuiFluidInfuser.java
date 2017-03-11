package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.inventory.ContainerFluidInfuser;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentity.TileEntityFluidInfuser;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiFluidInfuser extends GuiWithFluid {

    private static final ResourceLocation fluidInfuserGuiTextures = new ResourceLocation(Texture.GUI.FLUID_INFUSER);
    private final TileEntityFluidInfuser fluidInfuser;

    public GuiFluidInfuser(InventoryPlayer inventoryPlayer, TileEntityFluidInfuser fluidInfuser) {
        super(fluidInfuser, new ContainerFluidInfuser(inventoryPlayer, fluidInfuser));
        this.fluidInfuser = fluidInfuser;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(fluidInfuserGuiTextures);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);

    }
}
