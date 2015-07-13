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
    ModelRenderer cornerFL;
    ModelRenderer cornerFR;
    ModelRenderer cornerBR;
    ModelRenderer cornerBL;
    ModelRenderer barFU;
    ModelRenderer barFD;
    ModelRenderer barBU;
    ModelRenderer barBD;
    ModelRenderer barRU;
    ModelRenderer barRD;
    ModelRenderer barLD;
    ModelRenderer barLU;
    ModelRenderer up;
    ModelRenderer down;
    ModelRenderer front;
    ModelRenderer back;
    ModelRenderer right;
    ModelRenderer left;
  
  public ModelTank()
  {
    textureWidth = 64;
    textureHeight = 64;
    
      cornerFL = new ModelRenderer(this, 0, 22);
      cornerFL.addBox(0F, 0F, 0F, 2, 16, 2);
      cornerFL.setRotationPoint(-7F, 8F, -7F);
      cornerFL.setTextureSize(64, 64);
      cornerFL.mirror = true;
      setRotation(cornerFL, 0F, 0F, 0F);
      cornerFR = new ModelRenderer(this, 0, 22);
      cornerFR.addBox(0F, 0F, 0F, 2, 16, 2);
      cornerFR.setRotationPoint(5F, 8F, -7F);
      cornerFR.setTextureSize(64, 64);
      cornerFR.mirror = true;
      setRotation(cornerFR, 0F, 0F, 0F);
      cornerBR = new ModelRenderer(this, 0, 22);
      cornerBR.addBox(0F, 0F, 0F, 2, 16, 2);
      cornerBR.setRotationPoint(-7F, 8F, 5F);
      cornerBR.setTextureSize(64, 64);
      cornerBR.mirror = true;
      setRotation(cornerBR, 0F, 0F, 0F);
      cornerBL = new ModelRenderer(this, 0, 22);
      cornerBL.addBox(0F, 0F, 0F, 2, 16, 2);
      cornerBL.setRotationPoint(5F, 8F, 5F);
      cornerBL.setTextureSize(64, 64);
      cornerBL.mirror = true;
      setRotation(cornerBL, 0F, 0F, 0F);
      barFU = new ModelRenderer(this, 8, 22);
      barFU.addBox(0F, 0F, 0F, 10, 2, 2);
      barFU.setRotationPoint(-5F, 8F, -7F);
      barFU.setTextureSize(64, 64);
      barFU.mirror = true;
      setRotation(barFU, 0F, 0F, 0F);
      barFD = new ModelRenderer(this, 8, 22);
      barFD.addBox(0F, 0F, 0F, 10, 2, 2);
      barFD.setRotationPoint(-5F, 22F, -7F);
      barFD.setTextureSize(64, 64);
      barFD.mirror = true;
      setRotation(barFD, 0F, 0F, 0F);
      barBU = new ModelRenderer(this, 8, 22);
      barBU.addBox(0F, 0F, 0F, 10, 2, 2);
      barBU.setRotationPoint(-5F, 8F, 5F);
      barBU.setTextureSize(64, 64);
      barBU.mirror = true;
      setRotation(barBU, 0F, 0F, 0F);
      barBD = new ModelRenderer(this, 8, 22);
      barBD.addBox(0F, 0F, 0F, 10, 2, 2);
      barBD.setRotationPoint(-5F, 22F, 5F);
      barBD.setTextureSize(64, 64);
      barBD.mirror = true;
      setRotation(barBD, 0F, 0F, 0F);
      barRU = new ModelRenderer(this, 8, 26);
      barRU.addBox(0F, 0F, 0F, 2, 2, 10);
      barRU.setRotationPoint(-7F, 8F, -5F);
      barRU.setTextureSize(64, 64);
      barRU.mirror = true;
      setRotation(barRU, 0F, 0F, 0F);
      barRD = new ModelRenderer(this, 8, 26);
      barRD.addBox(0F, 0F, 0F, 2, 2, 10);
      barRD.setRotationPoint(-7F, 22F, -5F);
      barRD.setTextureSize(64, 64);
      barRD.mirror = true;
      setRotation(barRD, 0F, 0F, 0F);
      barLD = new ModelRenderer(this, 8, 26);
      barLD.addBox(0F, 0F, 0F, 2, 2, 10);
      barLD.setRotationPoint(5F, 22F, -5F);
      barLD.setTextureSize(64, 64);
      barLD.mirror = true;
      setRotation(barLD, 0F, 0F, 0F);
      barLU = new ModelRenderer(this, 8, 26);
      barLU.addBox(0F, 0F, 0F, 2, 2, 10);
      barLU.setRotationPoint(5F, 8F, -5F);
      barLU.setTextureSize(64, 64);
      barLU.mirror = true;
      setRotation(barLU, 0F, 0F, 0F);
      up = new ModelRenderer(this, -10, 0);
      up.addBox(0F, 0.5F, 0F, 10, 0, 10);
      up.setRotationPoint(-5F, 8F, -5F);
      up.setTextureSize(64, 64);
      up.mirror = true;
      setRotation(up, 0F, 0F, 0F);
      down = new ModelRenderer(this, -10, 0);
      down.addBox(0F, 1.5F, 0F, 10, 0, 10);
      down.setRotationPoint(-5F, 22F, -5F);
      down.setTextureSize(64, 64);
      down.mirror = true;
      setRotation(down, 0F, 0F, 0F);
      front = new ModelRenderer(this, 0, 10);
      front.addBox(0F, 0F, 0.5F, 10, 12, 0);
      front.setRotationPoint(-5F, 10F, -7F);
      front.setTextureSize(64, 64);
      front.mirror = true;
      setRotation(front, 0F, 0F, 0F);
      back = new ModelRenderer(this, 0, 10);
      back.addBox(0F, 0F, -0.5F, 10, 12, 0);
      back.setRotationPoint(-5F, 10F, 7F);
      back.setTextureSize(64, 64);
      back.mirror = true;
      setRotation(back, 0F, 0F, 0F);
      right = new ModelRenderer(this, 0, 0);
      right.addBox(0.5F, 0F, 0F, 0, 12, 10);
      right.setRotationPoint(-7F, 10F, -5F);
      right.setTextureSize(64, 64);
      right.mirror = true;
      setRotation(right, 0F, 0F, 0F);
      left = new ModelRenderer(this, 0, 0);
      left.addBox(-0.5F, 0F, 0F, 0, 12, 10);
      left.setRotationPoint(7F, 10F, -5F);
      left.setTextureSize(64, 64);
      left.mirror = true;
      setRotation(left, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    cornerFL.render(f5);
    cornerFR.render(f5);
    cornerBR.render(f5);
    cornerBL.render(f5);
    barFU.render(f5);
    barFD.render(f5);
    barBU.render(f5);
    barBD.render(f5);
    barRU.render(f5);
    barRD.render(f5);
    barLD.render(f5);
    barLU.render(f5);
    up.render(f5);
    down.render(f5);
    front.render(f5);
    back.render(f5);
    right.render(f5);
    left.render(f5);
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
