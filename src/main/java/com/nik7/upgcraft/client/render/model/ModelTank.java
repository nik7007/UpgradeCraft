

package com.nik7.upgcraft.client.render.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelTank extends ModelBase
{
  //fields
    ModelRenderer center;
    ModelRenderer clA;
    ModelRenderer clB;
    ModelRenderer clC;
    ModelRenderer bA;
    ModelRenderer bB;
    ModelRenderer bC;
    ModelRenderer bD;
    ModelRenderer clD;
    ModelRenderer tD;
    ModelRenderer tC;
    ModelRenderer tA;
    ModelRenderer tB;
    ModelRenderer Shape2;
    ModelRenderer Shape4;
    ModelRenderer Shape1;
    ModelRenderer Shape3;
  
  public ModelTank()
  {
    textureWidth = 64;
    textureHeight = 64;
    
      center = new ModelRenderer(this, 0, 0);
      center.addBox(0F, 0F, 0F, 14, 16, 14);
      center.setRotationPoint(-7F, 8F, -7F);
      center.setTextureSize(64, 64);
      center.mirror = true;
      setRotation(center, 0F, 0F, 0F);
      clA = new ModelRenderer(this, 8, 30);
      clA.addBox(0F, 0F, 0F, 2, 16, 2);
      clA.setRotationPoint(5.3F, 8F, 5.3F);
      clA.setTextureSize(64, 64);
      clA.mirror = true;
      setRotation(clA, 0F, 0F, 0F);
      clB = new ModelRenderer(this, 8, 30);
      clB.addBox(0F, 0F, 0F, 2, 16, 2);
      clB.setRotationPoint(-7.3F, 8F, 5.3F);
      clB.setTextureSize(64, 64);
      clB.mirror = true;
      setRotation(clB, 0F, 0F, 0F);
      clC = new ModelRenderer(this, 8, 30);
      clC.addBox(0F, 0F, 0F, 2, 16, 2);
      clC.setRotationPoint(-7.3F, 8F, -7.3F);
      clC.setTextureSize(64, 64);
      clC.mirror = true;
      setRotation(clC, 0F, 0F, 0F);
      bA = new ModelRenderer(this, 0, 30);
      bA.addBox(0F, 0F, 0F, 2, 14, 2);
      bA.setRotationPoint(-7.3F, 24F, -7F);
      bA.setTextureSize(64, 64);
      bA.mirror = true;
      setRotation(bA, 1.570796F, 0F, 0F);
      bB = new ModelRenderer(this, 0, 30);
      bB.addBox(0F, 0F, 0F, 2, 14, 2);
      bB.setRotationPoint(5.3F, 24F, -7F);
      bB.setTextureSize(64, 64);
      bB.mirror = true;
      setRotation(bB, 1.570796F, 0F, 0F);
      bC = new ModelRenderer(this, 0, 30);
      bC.addBox(0F, 0F, 0F, 2, 14, 2);
      bC.setRotationPoint(-7F, 24F, 7.3F);
      bC.setTextureSize(64, 64);
      bC.mirror = true;
      setRotation(bC, 1.570796F, 1.570796F, 0F);
      bD = new ModelRenderer(this, 0, 30);
      bD.addBox(0F, 0F, 0F, 2, 14, 2);
      bD.setRotationPoint(-7F, 24F, -5.3F);
      bD.setTextureSize(64, 64);
      bD.mirror = true;
      setRotation(bD, 1.570796F, 1.570796F, 0F);
      clD = new ModelRenderer(this, 8, 30);
      clD.addBox(0F, 0F, 0F, 2, 16, 2);
      clD.setRotationPoint(5.3F, 8F, -7.3F);
      clD.setTextureSize(64, 64);
      clD.mirror = true;
      setRotation(clD, 0F, 0F, 0F);
      tD = new ModelRenderer(this, 0, 30);
      tD.addBox(0F, 0F, 0F, 2, 14, 2);
      tD.setRotationPoint(-7F, 9.7F, -5.3F);
      tD.setTextureSize(64, 64);
      tD.mirror = true;
      setRotation(tD, 1.570796F, 1.570796F, 0F);
      tC = new ModelRenderer(this, 0, 30);
      tC.addBox(0F, 0F, 0F, 2, 14, 2);
      tC.setRotationPoint(-7F, 9.7F, 7.3F);
      tC.setTextureSize(64, 64);
      tC.mirror = true;
      setRotation(tC, 1.570796F, 1.570796F, 0F);
      tA = new ModelRenderer(this, 0, 30);
      tA.addBox(0F, 0F, 0F, 2, 14, 2);
      tA.setRotationPoint(-7.3F, 9.7F, -7F);
      tA.setTextureSize(64, 64);
      tA.mirror = true;
      setRotation(tA, 1.570796F, 0F, 0F);
      tB = new ModelRenderer(this, 0, 30);
      tB.addBox(0F, 0F, 0F, 2, 14, 2);
      tB.setRotationPoint(5.3F, 9.7F, -7F);
      tB.setTextureSize(64, 64);
      tB.mirror = true;
      setRotation(tB, 1.570796F, 0F, 0F);
      Shape2 = new ModelRenderer(this, 0, 30);
      Shape2.addBox(0F, 0F, 0F, 1, 1, 1);
      Shape2.setRotationPoint(6.3F, 7.7F, -7.3F);
      Shape2.setTextureSize(64, 64);
      Shape2.mirror = true;
      setRotation(Shape2, 0F, 0F, 0F);
      Shape4 = new ModelRenderer(this, 0, 30);
      Shape4.addBox(0F, 0F, 0F, 1, 1, 1);
      Shape4.setRotationPoint(-7.3F, 7.7F, 6.3F);
      Shape4.setTextureSize(64, 64);
      Shape4.mirror = true;
      setRotation(Shape4, 0F, 0F, 0F);
      Shape1 = new ModelRenderer(this, 0, 30);
      Shape1.addBox(0F, 0F, 0F, 1, 1, 1);
      Shape1.setRotationPoint(-7.3F, 7.7F, -7.3F);
      Shape1.setTextureSize(64, 64);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 0F, 0F);
      Shape3 = new ModelRenderer(this, 0, 30);
      Shape3.addBox(0F, 0F, 0F, 1, 1, 1);
      Shape3.setRotationPoint(6.3F, 7.7F, 6.3F);
      Shape3.setTextureSize(64, 64);
      Shape3.mirror = true;
      setRotation(Shape3, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5,entity);
    center.render(f5);
    clA.render(f5);
    clB.render(f5);
    clC.render(f5);
    bA.render(f5);
    bB.render(f5);
    bC.render(f5);
    bD.render(f5);
    clD.render(f5);
    tD.render(f5);
    tC.render(f5);
    tA.render(f5);
    tB.render(f5);
    Shape2.render(f5);
    Shape4.render(f5);
    Shape1.render(f5);
    Shape3.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}

