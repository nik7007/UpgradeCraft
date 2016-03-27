package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.inventory.ContainerActiveLavaMaker;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityActiveLavaMaker;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class GuiActiveLavaMaker extends GuiWithFluid {

    private final static ResourceLocation activeLavaMakerGuiTexture = new ResourceLocation(Texture.GUI.ACTIVE_LAVA_MAKER);
    private final UpgCtileentityActiveLavaMaker activeLavaMaker;

    private final static Random RANDOM = new Random();
    int inc = -1;

    public GuiActiveLavaMaker(InventoryPlayer inventoryPlayer, UpgCtileentityActiveLavaMaker activeLavaMaker) {
        super(new ContainerActiveLavaMaker(inventoryPlayer, activeLavaMaker));
        this.activeLavaMaker = activeLavaMaker;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = this.activeLavaMaker.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        int fuelLevel = this.activeLavaMaker.getFluid(0) == null ? 0 : this.activeLavaMaker.getFluid(0).amount;

        if (fuelLevel > 0) {
            int level = (int) this.activeLavaMaker.getFluidLevelScaled(32);
            super.renderFluidWithToolTipAndTemp(activeLavaMaker.getFluid(0), Capacity.INTERNAL_FLUID_TANK_TR1, 16, 56, 16, 32, mouseX, mouseY, level);
        }

        int activeLevel = this.activeLavaMaker.getFluid(1) == null ? 0 : this.activeLavaMaker.getFluid(1).amount;

        if (activeLevel > 0) {
            int level = (int) this.activeLavaMaker.getActiveFluidLevelScaled(32);
            super.renderFluidToolTipAndTemp(activeLavaMaker.getFluid(1), Capacity.INTERNAL_FLUID_TANK_TR1, 145, 56, 16, 32, mouseX, mouseY, level);
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(activeLavaMakerGuiTexture);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);

        if (this.activeLavaMaker.isOperating()) {
            if (inc == -1 || Math.random() < 0.49)
                inc = (int) (1 + 13 * RANDOM.nextFloat());
            this.drawTexturedModalRect(xOffset + 82, yOffset + 38 + 31 - inc, 176, 14 - inc, 14, inc);
        }

        FluidStack fluidStack = activeLavaMaker.getFluid(1);

        if (fluidStack != null && fluidStack.amount > 0) {
            int level = (int) this.activeLavaMaker.getActiveFluidLevelScaled(32);
            super.renderFluid(fluidStack.getFluid(), xOffset + 145, yOffset + 56, 16, level);
        }

    }

}
