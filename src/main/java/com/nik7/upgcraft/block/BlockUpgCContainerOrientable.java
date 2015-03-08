package com.nik7.upgcraft.block;


import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockUpgCContainerOrientable extends BlockUpgC implements ITileEntityProvider {

    public BlockUpgCContainerOrientable(Material material) {
        super(material);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item) {
        byte meta = 0;
        int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch (l) {
            case 0:
                meta = 2;
                break;
            case 1:
                meta = 5;
                break;
            case 2:
                meta = 3;
                break;
            case 3:
                meta = 4;
                break;
        }

        world.setBlockMetadataWithNotify(x, y, z, meta, 3);

    }

    protected void dropItems(World world, int x, int y, int z, ItemStack itemStack, Random rand) {

        if (itemStack != null) {
            float dX = rand.nextFloat() * 0.8F + 0.1F;
            float dY = rand.nextFloat() * 0.8F + 0.1F;
            float dZ = rand.nextFloat() * 0.8F + 0.1F;

            while (itemStack.stackSize > 0) {
                int j1 = rand.nextInt(21) + 10;

                if (j1 > itemStack.stackSize) {
                    j1 = itemStack.stackSize;
                }

                itemStack.stackSize -= j1;
                EntityItem entityitem = new EntityItem(world, (double) ((float) x + dX), (double) ((float) y + dY), (double) ((float) z + dZ), new ItemStack(itemStack.getItem(), j1, itemStack.getItemDamage()));

                if (itemStack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float f3 = 0.05F;
                entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
                entityitem.motionY = (double) ((float) rand.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);
                world.spawnEntityInWorld(entityitem);
            }
        }

    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
    }
}
