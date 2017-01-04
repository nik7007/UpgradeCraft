package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentity.TileEntityFluidFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiFluidFurnace extends GuiContainer {

    private final static ResourceLocation fluidFurnaceGuiTextures = new ResourceLocation(Texture.GUI.FLUID_FURNACE);
    private final TileEntityFluidFurnace fluidFurnace;

    public GuiFluidFurnace(InventoryPlayer inventoryPlayer, TileEntityFluidFurnace fluidFurnace) {
        super(new ContainerFluidFurnace(inventoryPlayer, fluidFurnace));
        this.fluidFurnace = fluidFurnace;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        ITextComponent name = this.fluidFurnace.getDisplayName();
        if (name != null) {
            String s = name.getUnformattedText();
            this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        }
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

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

        FluidStack fluidStack = this.fluidFurnace.getFluid();

        if (fluidStack != null) {
            renderFluid(fluidStack.getFluid(), xOffset + 15, yOffset + 58, (int) (this.fluidFurnace.getFillPercentage() * 32));
        }

    }


    protected void drawToolTip(List<String> text, int x, int y, int mazX, int maxY, int mouseX, int mouseY) {
        int posX = mouseX - this.guiLeft;
        int posY = mouseY - this.guiTop;


        if ((posX > x && posX < (mazX + x)) && (posY < y && posY > (y - maxY - 2))) {

            drawHoveringText(text, posX, posY, mc.fontRendererObj);

        }
    }


    protected void renderFluid(Fluid fluid, int x, int y, int mazX, int level) {

        TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());

        if (fluidStillSprite == null)
            fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();

        int color = fluid.getColor();

        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GL11.glColor4f(r, g, b, 1.0F);

        drawTexturedModalRect(x, y - level, fluidStillSprite, mazX, level);
    }


    protected void renderFluid(Fluid fluid, int x, int y, int level) {
        renderFluid(fluid, x, y, 16, level);
    }


}
