package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.init.ModFluids;
import com.nik7.upgcraft.inventory.ContainerTermoFluidFurnace;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityTermoFluidFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;


@SideOnly(Side.CLIENT)
public class GuiTermoFluidFurnace extends GuiWithFluid {

    private final static ResourceLocation guiTextures = new ResourceLocation(Texture.GUI.TERMO_FLUID_FURNACE);
    private final UpgCtileentityTermoFluidFurnace termoFluidFurnace;

    public GuiTermoFluidFurnace(InventoryPlayer inventoryPlayer, UpgCtileentityTermoFluidFurnace termoFluidFurnace) {
        super(new ContainerTermoFluidFurnace(inventoryPlayer, termoFluidFurnace));
        this.termoFluidFurnace = termoFluidFurnace;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.termoFluidFurnace.hasCustomInventoryName() ? this.termoFluidFurnace.getInventoryName() : I18n.format(this.termoFluidFurnace.getInventoryName());
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        int fluidLevel = this.termoFluidFurnace.getFluid(0) == null ? 0 : this.termoFluidFurnace.getFluid(0).amount;

        if (fluidLevel > 0) {
            int level = (int) this.termoFluidFurnace.getFluidLevelScaled(32);
            super.renderFluidWithToolTip(termoFluidFurnace.getFluid(0), termoFluidFurnace.capacity, 16, 56, 16, 32, mouseX, mouseY, level);

        }

        List<String> text = new LinkedList<String>();
        EnumChatFormatting color;
        int temp = (int) termoFluidFurnace.internalTemp;

        if (temp <= 273 + 25)
            color = EnumChatFormatting.BLUE;
        else if (temp <= UpgCtileentityTermoFluidFurnace.MAX_TEMPERATURE / 2)
            color = EnumChatFormatting.DARK_RED;
        else
            color = EnumChatFormatting.GOLD;

        text.add(color + StatCollector.translateToLocal("tooltip." + Reference.MOD_ID + ":machine.temperature") + ": " + EnumChatFormatting.RESET + temp + "K");
        super.drawToolTip(text, 53, 53, 16, 13, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTextures);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);

        int inc = this.termoFluidFurnace.geTemperatureScaled(14);
        this.drawTexturedModalRect(xOffset + 56, yOffset + 38 + 14 - inc, 176, 14 - inc, 14, inc);

        inc = this.termoFluidFurnace.getCookProgressScaled(24);
        this.drawTexturedModalRect(xOffset + 79, yOffset + 27, 176, 14, inc + 1, 16);


        int level = (int) termoFluidFurnace.getWasteFluidLevelScaled(16);
        if (level > 0) {
            super.renderFluid(ModFluids.ActiveLava, xOffset + 148, yOffset + 40, level);
        }

    }
}
