package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.inventory.ContainerThermoFluidFurnace;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityThermoFluidFurnace;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

public class GuiThermoFluidFurnace extends GuiWithFluid {

    private final static ResourceLocation guiTextures = new ResourceLocation(Texture.GUI.THERMO_FLUID_FURNACE);
    private final UpgCtileentityThermoFluidFurnace thermoFluidFurnace;

    public GuiThermoFluidFurnace(InventoryPlayer playerInventory, UpgCtileentityThermoFluidFurnace thermoFluidFurnace) {
        super(new ContainerThermoFluidFurnace(playerInventory, thermoFluidFurnace));
        this.thermoFluidFurnace = thermoFluidFurnace;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.thermoFluidFurnace.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        //int fluidLevel = this.thermoFluidFurnace.getFluid(0) == null ? 0 : this.thermoFluidFurnace.getFluid(0).amount;

        int level;


        level = (int) this.thermoFluidFurnace.getFluidLevelScaled(32);
        super.renderFluidWithToolTip(thermoFluidFurnace.getFluid(thermoFluidFurnace.getTankToShow()), thermoFluidFurnace.getCapacity(thermoFluidFurnace.getTankToShow()), 16, 56, 16, 32, mouseX, mouseY, level);


        level = (int) thermoFluidFurnace.getWasteFluidLevelScaled(16);
        super.renderFluidWithToolTip(thermoFluidFurnace.getFluid(thermoFluidFurnace.getSecondaryFluidTankToShow()), thermoFluidFurnace.getCapacity(thermoFluidFurnace.getSecondaryFluidTankToShow()), 148, 40, 16, 16, mouseX, mouseY, level);


        List<String> text = new LinkedList<>();
        TextFormatting color;
        int temp = (int) thermoFluidFurnace.getInternalTemp();

        if (temp <= 273 + 25)
            color = TextFormatting.BLUE;
        else if (temp <= UpgCtileentityThermoFluidFurnace.MAX_TEMPERATURE / 2)
            color = TextFormatting.DARK_RED;
        else
            color = TextFormatting.GOLD;

        text.add(color + net.minecraft.util.text.translation.I18n.translateToLocal("tooltip." + Reference.MOD_ID + ":machine.temperature") + ": " + TextFormatting.RESET + temp + "K");
        super.drawToolTip(text, 53, 53, 16, 13, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTextures);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);

        int inc = this.thermoFluidFurnace.geTemperatureScaled(14);
        this.drawTexturedModalRect(xOffset + 56, yOffset + 38 + 14 - inc, 176, 14 - inc, 14, inc);

        inc = this.thermoFluidFurnace.getCookProgressScaled(24);
        this.drawTexturedModalRect(xOffset + 79, yOffset + 27, 176, 14, inc + 1, 16);


    }
}
