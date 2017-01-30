package com.nik7.upgcraft.registry;


import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemOD {

    private final static String UNKNOWN = "Unknown";

    private final ItemStack itemStack;
    private final String nameOD;
    private int hashCode = 0;


    public ItemOD(ItemStack itemStack) {

        this.nameOD = itemStackToNameOD(itemStack);
        this.itemStack = getItemStack(this.nameOD, itemStack);

    }

    private static int createHashCode(String nameOD, ItemStack itemStack) {
        if (!nameOD.equals(UNKNOWN)) {
            return nameOD.hashCode();
        } else {
            ItemStack stack = new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage());
            String name = stack.toString() + "-" + itemStack.getItem().getRegistryName();
            return name.hashCode();
        }
    }

    public static String itemStackToNameOD(ItemStack itemStack) {
        int[] iDs = OreDictionary.getOreIDs(itemStack);

        if (iDs.length > 0)
            return OreDictionary.getOreName(iDs[0]);
        else return UNKNOWN;
    }

    private static ItemStack getItemStack(String nameOD, ItemStack itemStack) {
        if (nameOD.equals(UNKNOWN))
            return itemStack;
        ItemStack oreDictionaryItem = OreDictionary.getOres(nameOD).get(0);
        return new ItemStack(oreDictionaryItem.getItem(), itemStack.getCount(), oreDictionaryItem.getItemDamage());
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public String getOreDictionaryName() {
        return nameOD;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ItemOD) {
            ItemOD other = (ItemOD) o;
            if (this.nameOD.equals(UNKNOWN) && other.nameOD.equals(UNKNOWN)) {
                return this.itemStack.isItemEqual(other.itemStack);
            } else return this.nameOD.equals(other.nameOD);
        }
        return false;
    }

    @Override
    public int hashCode() {

        if (this.hashCode == 0)
            this.hashCode = createHashCode(this.nameOD, this.itemStack);

        return this.hashCode;
    }

}
