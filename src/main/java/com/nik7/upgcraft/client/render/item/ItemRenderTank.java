package com.nik7.upgcraft.client.render.item;

import com.nik7.upgcraft.client.render.model.ModelTank;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public abstract class ItemRenderTank implements IItemRenderer {

    protected final ModelTank modelTank = new ModelTank();

    protected void renderModel(){

        GL11.glPushMatrix(); //start

        GL11.glTranslated(0, 0.99, 0);
        GL11.glRotatef(180, 1.0f, 0, 0);
        GL11.glRotatef(-90, 0, 1, 0);

        this.modelTank.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();

    }

}


