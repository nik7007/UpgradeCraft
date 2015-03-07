package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiFluidFurnace extends GuiWithFluid {

    private final static ResourceLocation fluidFurnaceGuiTextures = new ResourceLocation(Texture.GUI.FLUID_FURNACE);
    private final UpgCtileentityFluidFurnace fluidFurnace;

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
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);

        int inc = this.fluidFurnace.getBurnTimeRemainingScaled(14);
        this.drawTexturedModalRect(xOffset + 56, yOffset + 36 + 13 - inc, 176, 13 - inc, 14, inc);

        inc = this.fluidFurnace.getCookProgressScaled(24);
        this.drawTexturedModalRect(xOffset + 79, yOffset + 34, 176, 14, inc + 1, 16);


        if (this.fluidFurnace.fluidLevel > 0) {

            int level = (int) this.fluidFurnace.getFluidLevelScaled(32);

            Fluid fluid = FluidRegistry.getFluid("lava");

            super.renderFluid(fluid, xOffset + 26, yOffset + 60, level);

        }

    }


}
