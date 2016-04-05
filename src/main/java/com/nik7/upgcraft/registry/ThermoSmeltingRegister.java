package com.nik7.upgcraft.registry;

import com.nik7.upgcraft.registry.ThermoSmelting.ThermoSmeltingRecipe;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ThermoSmeltingRegister {

    private final static ThermoSmeltingRegister INSTANCE = new ThermoSmeltingRegister();

    private Map<ItemOD, ThermoSmeltingRecipe> inputToAll;

    private ThermoSmeltingRegister() {
        inputToAll = new HashMap<>();
    }

    public static void addRecipe(ItemStack input, ItemStack output, float temperatureKelvin, int tick) {
        if (input != null && output != null && temperatureKelvin > 273 && tick > 0) {

            if (!isRegisterContainsInput(input)) {
                ThermoSmeltingRecipe recipe = new ThermoSmeltingRecipe(input, output, temperatureKelvin, tick);
                INSTANCE.inputToAll.put(new ItemOD(input), recipe);

            } else LogHelper.error("ThermoSmeltingRegister: Recipe rejected - Duplicated inputs are not allowed!!");
        } else
            LogHelper.error("ThermoSmeltingRegister: Impossible to insert this recipe - Violate integrity constraints!");
    }

    public static Collection<ThermoSmeltingRecipe> getRecipes() {
        return INSTANCE.inputToAll.values();
    }

    public static ThermoSmeltingRecipe getRecipeFromInput(ItemStack input) {
        return INSTANCE.inputToAll.get(new ItemOD(input));
    }

    public static boolean isRegisterContainsInput(ItemStack itemStack) {
        return INSTANCE.inputToAll.containsKey(new ItemOD(itemStack));
    }

}
