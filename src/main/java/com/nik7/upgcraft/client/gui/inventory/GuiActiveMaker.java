package com.nik7.upgcraft.client.gui.inventory;

import com.nik7.upgcraft.inventory.ContainerActiveMaker;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityActiveMaker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiActiveMaker extends GuiWithFluid {

    private final static ResourceLocation guiTextures = new ResourceLocation(Texture.GUI.ACTIVE_MAKER);
    private final UpgCtileentityActiveMaker upgCtileentityActiveMaker;

    public GuiActiveMaker(InventoryPlayer inventoryPlayer, UpgCtileentityActiveMaker upgCtileentityActiveMaker) {
        super(new ContainerActiveMaker(inventoryPlayer, upgCtileentityActiveMaker));
        this.upgCtileentityActiveMaker = upgCtileentityActiveMaker;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.upgCtileentityActiveMaker.hasCustomInventoryName() ? this.upgCtileentityActiveMaker.getInventoryName() : I18n.format(this.upgCtileentityActiveMaker.getInventoryName());
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        int fuelLevel = this.upgCtileentityActiveMaker.getFluid(0) == null ? 0 : this.upgCtileentityActiveMaker.getFluid(0).amount;

        if (fuelLevel > 0) {
            int level = (int) this.upgCtileentityActiveMaker.getFluidLevelScaled(32);
            super.renderFluidWithToolTipAndTemp(upgCtileentityActiveMaker.getFluid(0), upgCtileentityActiveMaker.capacity, 16, 56, 16, 32, mouseX, mouseY, level);
        }

        int activeLevel = this.upgCtileentityActiveMaker.getFluid(1) == null ? 0 : this.upgCtileentityActiveMaker.getFluid(1).amount;

        if (activeLevel > 0) {
            int level = (int) this.upgCtileentityActiveMaker.getActiveFluidLevelScaled(32);
            super.renderFluidToolTipAndTemp(upgCtileentityActiveMaker.getFluid(1), upgCtileentityActiveMaker.capacity, 145, 56, 16, 32, mouseX, mouseY, level);
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTextures);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);

        FluidStack fluidStack = upgCtileentityActiveMaker.getFluid(1);

        if (fluidStack != null && fluidStack.amount > 0) {
            int level = (int) this.upgCtileentityActiveMaker.getActiveFluidLevelScaled(32);
            super.renderFluid(fluidStack.getFluid(), xOffset + 145, yOffset + 56, 16, level);
        }

    }
}
