package com.nik7.upgcraft.client.gui.inventory;


import com.nik7.upgcraft.inventory.ContainerFluidInfuser;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiFluidInfuser extends GuiWithFluid {

    private static final ResourceLocation fluidInfuserGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
    private final UpgCtileentityFluidInfuser fluidInfuser;

    public GuiFluidInfuser(InventoryPlayer playerInventory, UpgCtileentityFluidInfuser fluidInfuser) {
        super(new ContainerFluidInfuser(playerInventory, fluidInfuser));
        this.fluidInfuser = fluidInfuser;

    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.fluidInfuser.hasCustomInventoryName() ? this.fluidInfuser.getInventoryName() : I18n.format(this.fluidInfuser.getInventoryName());
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(fluidInfuserGuiTextures);
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, this.xSize, this.ySize);


    }
}
