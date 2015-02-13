package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiFluidFurnace extends GuiContainer {

    private final ResourceLocation fluidFurnaceGuiTextures = new ResourceLocation(Texture.GUI.FLUID_FURNACE);
    private UpgCtileentityFluidFurnace fluidFurnace;

    public GuiFluidFurnace(InventoryPlayer inventoryPlayer, UpgCtileentityFluidFurnace fluidFurnace) {
        super(new ContainerFluidFurnace(inventoryPlayer, fluidFurnace));
        this.fluidFurnace = fluidFurnace;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.fluidFurnace.hasCustomInventoryName() ? this.fluidFurnace.getInventoryName() : I18n.format(this.fluidFurnace.getInventoryName());
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(fluidFurnaceGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int i1 = this.fluidFurnace.getBurnTimeRemainingScaled(13);

        if (this.fluidFurnace.isBurning()) {

            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
            i1 = this.fluidFurnace.getCookProgressScaled(24);
            this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
        } else {
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
        }

        if (this.fluidFurnace.fluidLevel > 0) {

            int level = this.fluidFurnace.getFluidLevelScaled(16);

            //String name = this.fluidFurnace.getFluid().getLocalizedName();

            IIcon texture = FluidRegistry.getFluid("lava").getStillIcon();

            this.drawTexturedModelRectFromIcon(k + 56, l+36 + 12 - level, texture, 16, level);
        }

    }
}
