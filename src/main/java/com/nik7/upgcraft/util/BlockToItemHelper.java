package com.nik7.upgcraft.util;


import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockToItemHelper {

    private final static BlockToItemHelper ourInstance = new BlockToItemHelper();
    private Map<String, ArrayList<ItemStack>> drops = new HashMap<String, ArrayList<ItemStack>>();

    public static BlockToItemHelper getInstance() {
        return ourInstance;
    }

    private BlockToItemHelper() {
    }

    public static void addDrops(int x, int y, int z, int dim, ArrayList<ItemStack> itemStacks) {

        String key = coordToKey(x, y, z, dim);
        if (ourInstance.drops.containsKey(key)) {
            ourInstance.drops.remove(key);
        }
        ourInstance.drops.put(key, itemStacks);

    }

    public static ArrayList<ItemStack> getDrops(int x, int y, int z, int dim) {

        String key = coordToKey(x, y, z, dim);

        if (ourInstance.drops.containsKey(key)) {
            ArrayList<ItemStack> result = ourInstance.drops.get(key);
            ourInstance.drops.remove(key);

            return result;
        }
        return null;
    }


    private static String coordToKey(int x, int y, int z, int dim) {
        return "x:" + x + "y:" + y + "z:" + z + "dim:" + dim;
    }

}
