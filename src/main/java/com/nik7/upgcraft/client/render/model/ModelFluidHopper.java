package com.nik7.upgcraft.client.render.model;


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFluidHopper extends ModelBase {
    //fields
    private ModelRenderer middle;
    public ModelRenderer end;
    private ModelRenderer top_s1;
    private ModelRenderer top_s2;
    private ModelRenderer top_s3;
    private ModelRenderer top_s4;
    private ModelRenderer top_b;

    public ModelFluidHopper() {
        textureWidth = 128;
        textureHeight = 128;

        middle = new ModelRenderer(this, 0, 0);
        middle.addBox(0F, 0F, 0F, 8, 6, 8);
        middle.setRotationPoint(-4F, 14F, -4F);
        middle.setTextureSize(128, 128);
        middle.mirror = true;
        setRotation(middle, 0F, 0F, 0F);
        end = new ModelRenderer(this, 0, 0);
        end.addBox(0F, 0F, 0F, 4, 4, 4);
        end.setRotationPoint(-2F, 20F, -2F);
        end.setTextureSize(128, 128);
        end.mirror = true;
        setRotation(end, 0F, 0F, 0F);
        top_s1 = new ModelRenderer(this, 0, 18);
        top_s1.addBox(0F, 0F, 0F, 16, 6, 2);
        top_s1.setRotationPoint(-8F, 8F, -8F);
        top_s1.setTextureSize(128, 128);
        top_s1.mirror = true;
        setRotation(top_s1, 0F, 0F, 0F);
        top_s2 = new ModelRenderer(this, 0, 18);
        top_s2.addBox(0F, 0F, 0F, 16, 6, 2);
        top_s2.setRotationPoint(-8F, 8F, 6F);
        top_s2.setTextureSize(128, 128);
        top_s2.mirror = true;
        setRotation(top_s2, 0F, 0F, 0F);
        top_s3 = new ModelRenderer(this, 32, 0);
        top_s3.addBox(0F, 0F, 0F, 2, 6, 12);
        top_s3.setRotationPoint(-8F, 8F, -6F);
        top_s3.setTextureSize(128, 128);
        top_s3.mirror = true;
        setRotation(top_s3, 0F, 0F, 0F);
        top_s4 = new ModelRenderer(this, 32, 0);
        top_s4.addBox(0F, 0F, 0F, 2, 6, 12);
        top_s4.setRotationPoint(6F, 8F, -6F);
        top_s4.setTextureSize(128, 128);
        top_s4.mirror = true;
        setRotation(top_s4, 0F, 0F, 0F);
        top_b = new ModelRenderer(this, 36, 18);
        top_b.addBox(0F, 0.2F, 0F, 14, 5, 14);
        top_b.setRotationPoint(-7F, 9F, -7F);
        top_b.setTextureSize(128, 128);
        top_b.mirror = true;
        setRotation(top_b, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        middle.render(f5);
        end.render(f5);
        top_s1.render(f5);
        top_s2.render(f5);
        top_s3.render(f5);
        top_s4.render(f5);
        top_b.render(f5);
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