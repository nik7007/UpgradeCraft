package com.nik7.upgcraft.registry;

import com.nik7.upgcraft.registry.TermoSmelting.TermoSmeltingRecipe;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TermoSmeltingRegister {

    private final static TermoSmeltingRegister INSTANCE = new TermoSmeltingRegister();

    private Map<ItemOD, TermoSmeltingRecipe> inputToAll;

    private TermoSmeltingRegister() {
        inputToAll = new HashMap<ItemOD, TermoSmeltingRecipe>();
    }

    public static void addRecipe(ItemStack input, ItemStack output, float temperatureKelvin, int tick) {
        if (input != null && output != null && temperatureKelvin > 273 && tick > 0) {

            if (INSTANCE.inputToAll.get(new ItemOD(input)) == null) {

                TermoSmeltingRecipe recipe = new TermoSmeltingRecipe(input, output, temperatureKelvin, tick);
                INSTANCE.inputToAll.put(new ItemOD(input), recipe);

            } else LogHelper.error("TermoSmeltingRegister: Recipe rejected - Duplicated inputs are not allowed!!");
        } else
            LogHelper.error("TermoSmeltingRegister: Impossible to insert this recipe - Violate integrity constraints!");
    }

    public static Collection<TermoSmeltingRecipe> getRecipes() {
        return INSTANCE.inputToAll.values();
    }

    public static TermoSmeltingRecipe getRecipeFromInput(ItemStack input) {
        return INSTANCE.inputToAll.get(new ItemOD(input));
    }

}
