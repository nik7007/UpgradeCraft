package com.nik7.upgcraft.util;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

public class ItemHelper {

    public static void dropItems(World world, int x, int y, int z, ItemStack itemStack, Random rand) {

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

    public static ItemStack generateItemStack(ItemStack itemStack, int size) {

        return generateItemStack(itemStack, size, itemStack.getItemDamage());

    }

    public static ItemStack generateItemStack(ItemStack itemStack, int size, int damage) {

        if (size <= 0)
            return null;

        ItemStack result = new ItemStack(itemStack.getItem(), size, damage);

        if (itemStack.hasTagCompound())
            result.setTagCompound(itemStack.getTagCompound());

        return result;

    }


}


