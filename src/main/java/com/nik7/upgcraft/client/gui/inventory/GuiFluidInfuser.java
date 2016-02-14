package com.nik7.upgcraft.client.gui.inventory;

import com.nik7.upgcraft.inventory.ContainerFluidInfuser;
import com.nik7.upgcraft.reference.Texture;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class GuiFluidInfuser extends GuiWithFluid {

    private static final ResourceLocation fluidInfuserGuiTextures = new ResourceLocation(Texture.GUI.FLUID_INFUSER);
    private final UpgCtileentityFluidInfuser fluidInfuser;

    public GuiFluidInfuser(InventoryPlayer playerInventory, UpgCtileentityFluidInfuser fluidInfuser) {
        super(new ContainerFluidInfuser(playerInventory, fluidInfuser));
        this.fluidInfuser = fluidInfuser;

    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.fluidInfuser.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        if (this.fluidInfuser.getFluid(0) != null && this.fluidInfuser.getFluid(0).amount > 0) {

            FluidStack fluid = this.fluidInfuser.getFluid(0);
            int level = (int) this.fluidInfuser.getFluidLevelScaled(32);

            super.renderFluidWithToolTip(fluid, fluidInfuser.getCapacity(), 16, 56, 16, 32, mouseX, mouseY, level);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(fluidInfuserGuiTextures);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);

        int inc = fluidInfuser.getMeltingTimeRemainingScaled(13);
        this.drawTexturedModalRect(xOffset + 46, yOffset + 36 + 13 - inc, 176, 13 - inc, 15, inc);

        inc = fluidInfuser.getInfusingTimeRemainingScaled(24);
        this.drawTexturedModalRect(xOffset + 99, yOffset + 34, 176, 14, inc + 1, 16);

    }
}
