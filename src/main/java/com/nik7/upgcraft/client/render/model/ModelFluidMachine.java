package com.nik7.upgcraft.client.render.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelFluidMachine extends ModelBase {
    //fields
    ModelRenderer top;
    ModelRenderer d;
    ModelRenderer l1;
    ModelRenderer l4;
    ModelRenderer l3;
    ModelRenderer l2;
    ModelRenderer fa;
    ModelRenderer fb;
    ModelRenderer fc;
    ModelRenderer fd;
    ModelRenderer fT;

    public ModelFluidMachine() {
        textureWidth = 128;
        textureHeight = 64;

        top = new ModelRenderer(this, 0, 0);
        top.addBox(0F, 0F, 0F, 16, 8, 16);
        top.setRotationPoint(-8F, 8F, -8F);
        top.setTextureSize(128, 64);
        top.mirror = true;
        setRotation(top, 0F, 0F, 0F);
        d = new ModelRenderer(this, 0, 24);
        d.addBox(0F, 0F, 0F, 16, 1, 16);
        d.setRotationPoint(-8F, 23F, -8F);
        d.setTextureSize(128, 64);
        d.mirror = true;
        setRotation(d, 0F, 0F, 0F);
        l1 = new ModelRenderer(this, 0, 0);
        l1.addBox(0F, 0F, 0F, 2, 7, 2);
        l1.setRotationPoint(-8F, 16F, -8F);
        l1.setTextureSize(128, 64);
        l1.mirror = true;
        setRotation(l1, 0F, 0F, 0F);
        l4 = new ModelRenderer(this, 0, 0);
        l4.addBox(0F, 0F, 0F, 2, 7, 2);
        l4.setRotationPoint(6F, 16F, 6F);
        l4.setTextureSize(128, 64);
        l4.mirror = true;
        setRotation(l4, 0F, 0F, 0F);
        l3 = new ModelRenderer(this, 0, 0);
        l3.addBox(0F, 0F, 0F, 2, 7, 2);
        l3.setRotationPoint(-8F, 16F, 6F);
        l3.setTextureSize(128, 64);
        l3.mirror = true;
        setRotation(l3, 0F, 0F, 0F);
        l2 = new ModelRenderer(this, 0, 0);
        l2.addBox(0F, 0F, 0F, 2, 7, 2);
        l2.setRotationPoint(6F, 16F, -8F);
        l2.setTextureSize(128, 64);
        l2.mirror = true;
        setRotation(l2, 0F, 0F, 0F);
        fa = new ModelRenderer(this, 48, 8);
        fa.addBox(0F, 0F, 0F, 12, 7, 0);
        fa.setRotationPoint(-6F, 16F, -8F);
        fa.setTextureSize(128, 64);
        fa.mirror = true;
        setRotation(fa, 0F, 0F, 0F);
        fb = new ModelRenderer(this, 48, 0);
        fb.addBox(0F, 0F, 0F, 12, 7, 1);
        fb.setRotationPoint(-6F, 16F, 7F);
        fb.setTextureSize(128, 64);
        fb.mirror = true;
        setRotation(fb, 0F, 0F, 0F);
        fc = new ModelRenderer(this, 74, 0);
        fc.addBox(0F, 0F, 0F, 1, 7, 12);
        fc.setRotationPoint(7F, 16F, -6F);
        fc.setTextureSize(128, 64);
        fc.mirror = true;
        setRotation(fc, 0F, 0F, 0F);
        fd = new ModelRenderer(this, 74, 0);
        fd.addBox(0F, 0F, 0F, 1, 7, 12);
        fd.setRotationPoint(-8F, 16F, -6F);
        fd.setTextureSize(128, 64);
        fd.mirror = true;
        setRotation(fd, 0F, 0F, 0F);
        fT = new ModelRenderer(this, 48, 24);
        fT.addBox(0F, 0F, 0F, 12, 7, 0);
        fT.setRotationPoint(-6F, 16F, -6F);
        fT.setTextureSize(128, 64);
        fT.mirror = true;
        setRotation(fT, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        top.render(f5);
        d.render(f5);
        l1.render(f5);
        l4.render(f5);
        l3.render(f5);
        l2.render(f5);
        fa.render(f5);
        fb.render(f5);
        fc.render(f5);
        fd.render(f5);
        fT.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
