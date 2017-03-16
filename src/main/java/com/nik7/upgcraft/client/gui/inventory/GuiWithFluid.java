package com.nik7.upgcraft.client.gui.inventory;

import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentity.TileEntityInventoryAndFluidHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuiWithFluid extends GuiContainer {

    protected final TileEntityInventoryAndFluidHandler inventoryAndFluidHandler;

    public GuiWithFluid(TileEntityInventoryAndFluidHandler inventoryAndFluidHandler, Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.inventoryAndFluidHandler = inventoryAndFluidHandler;
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        ITextComponent name = this.inventoryAndFluidHandler.getDisplayName();
        if (name != null) {
            String s = name.getUnformattedText();
            this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        }
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        drawTankContent(this.inventoryAndFluidHandler.getFluid(), this.inventoryAndFluidHandler.getCapacity(), mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }


    protected void drawTankContent(FluidStack fluidStack, int capacity, int mouseX, int mouseY) {
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;

        String fluidName = net.minecraft.util.text.translation.I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":tank.empty");
        List<String> toolTip = new ArrayList<>();

        if (fluidStack != null) {
            renderFluid(fluidStack.getFluid(), 15, 58, (int) (this.inventoryAndFluidHandler.getFillPercentage() * 32));
            fluidName = fluidStack.getLocalizedName();
            String amount = fluidStack.amount + "/" + capacity + "mB";

            toolTip.add(fluidName);
            toolTip.add(TextFormatting.GRAY + amount);

            String modMane = getModName(fluidStack.getFluid());
            if (modMane != null) {
                toolTip.add(TextFormatting.BLUE + TextFormatting.ITALIC.toString() + modMane);
            }


        } else toolTip.add(fluidName);

        drawToolTip(toolTip, xOffset + 15, yOffset + 58, 16, 32, mouseX, mouseY);

    }

    private String getModName(Fluid fluid) {

        if (fluid != null) {
            String defaultFluidName = FluidRegistry.getDefaultFluidName(fluid);
            if (defaultFluidName != null) {
                ResourceLocation fluidResourceName = new ResourceLocation(defaultFluidName);
                Map<String, ModContainer> modMap = Loader.instance().getIndexedModList();
                return modMap.get(fluidResourceName.getResourceDomain()).getName();
            }
        }
        return null;
    }


    protected void drawToolTip(List<String> text, int x, int y, int mazX, int maxY, int mouseX, int mouseY) {

        int posX = mouseX - this.guiLeft;
        int posY = mouseY - this.guiTop;

        if ((mouseX > x && mouseX < (mazX + x)) && (mouseY < y && mouseY > (y - maxY - 2))) {

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
