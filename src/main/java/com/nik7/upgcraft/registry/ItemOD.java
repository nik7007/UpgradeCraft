package com.nik7.upgcraft.registry;


import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemOD {

    private final ItemStack itemStack;
    private final String nameOD;


    public ItemOD(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.nameOD = itemStackToNameOD(itemStack);
    }

    public static String itemStackToNameOD(ItemStack itemStack) {
        int[] iDs = OreDictionary.getOreIDs(itemStack);

        if (iDs.length > 0)
            return OreDictionary.getOreName(iDs[0]);
        else return "Unknown";
    }

    public ItemStack getItemStack() {
        if (nameOD.equals("Unknown"))
            return itemStack;
        ItemStack oreDictionaryItem = OreDictionary.getOres(nameOD).get(0);
        return new ItemStack(oreDictionaryItem.getItem(), itemStack.stackSize, oreDictionaryItem.getItemDamage());
    }

    public String getOreDictionaryName() {
        return nameOD;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ItemOD) {
            ItemOD other = (ItemOD) o;
            if (this.nameOD.equals("Unknown") && other.nameOD.equals("Unknown")) {
                return this.itemStack.isItemEqual(other.itemStack);
            } else return this.nameOD.equals(other.nameOD);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (!nameOD.equals("Unknown")) {
            return nameOD.hashCode();
        } else {
            ItemStack stack = new ItemStack(this.itemStack.getItem(), 1, this.itemStack.getItemDamage());
            String name = stack.toString();
            return name.hashCode();
        }
    }

}
