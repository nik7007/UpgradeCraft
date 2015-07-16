package com.nik7.upgcraft.client.render.item;


import com.nik7.upgcraft.client.render.model.ModelFluidMachine;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public abstract class ItemRenderMachine implements IItemRenderer {

    protected final ModelFluidMachine model = new ModelFluidMachine();

    protected void renderModel(ItemRenderType type) {

        GL11.glPushMatrix();

        switch (type) {
            case ENTITY:
            case INVENTORY:
                GL11.glTranslated(0, 0.99, 0);
                break;
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(0.28f, 1.53f, 0.28f);
                break;
            default:
                break;

        }

        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(-90, 0, 1, 0);

        this.model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}
