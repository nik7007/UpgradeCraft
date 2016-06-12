package com.nik7.upgcraft.util;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTTagHelper {

    public static void writeInventoryToNBT(ItemStack[] inventory, NBTTagCompound tag){

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < inventory.length; ++i) {
            if (inventory[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(nbtTagCompound);
                nbttaglist.appendTag(nbtTagCompound);
            }
        }

        tag.setTag("Items", nbttaglist);
    }

    public static void readInventoryFromNBT(ItemStack[] inventory, NBTTagCompound tag){

        NBTTagList nbttaglist = tag.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbtTagCompound.getByte("Slot");

            if (b0 >= 0 && b0 < inventory.length) {
                inventory[b0] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
            }
        }

    }

    public static void setShortArray(NBTTagCompound tag, short[] array) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setShort("length", (short) array.length);
        for (int i = 0; i < array.length; i++) {
            tagCompound.setShort("v" + i, array[i]);
        }
        tag.setTag("shortArray", tagCompound);
    }

    public static short[] getShortArray(NBTTagCompound tag) {
        NBTTagCompound tagCompound = tag.getCompoundTag("shortArray");
        short l = tagCompound.getShort("length");
        short[] array = new short[l];
        for (short i = 0; i < l; i++) {
            array[i] = tagCompound.getShort("v" + i);
        }
        return array;
    }
}
