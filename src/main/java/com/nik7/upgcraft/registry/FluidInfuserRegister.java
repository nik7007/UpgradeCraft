package com.nik7.upgcraft.registry;


import com.nik7.upgcraft.registry.recipes.FluidInfuserRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;


public class FluidInfuserRegister {

    private final static FluidInfuserRegister INSTANCE = new FluidInfuserRegister();

    private final Set<FluidInfuserRecipe> recipes = new HashSet<>();

    private final Map<ItemOD, List<FluidInfuserRecipe>> toMeltToRecipe = new HashMap<>();
    private final Map<ItemOD, List<FluidInfuserRecipe>> toInfuseToRecipe = new HashMap<>();

    private final Map<FluidStack, List<FluidInfuserRecipe>> fluidToRecipe = new HashMap<>();

    private FluidInfuserRegister() {

    }

    private static void registerFluidInfuserRecipe(FluidInfuserRecipe recipe) {
        if (!INSTANCE.recipes.contains(recipe)) {
            INSTANCE.recipes.add(recipe);

            ItemOD toMeltKey = createKey(recipe.getToMelt());
            ItemOD toInfuseKey = createKey(recipe.getToInfuse());
            FluidStack fluidStackKey = createKey(recipe.getFluidStack());

            boolean checkMelt = saveOnMapItem(INSTANCE.toMeltToRecipe, toMeltKey, recipe);
            boolean checkInfuse = saveOnMapItem(INSTANCE.toInfuseToRecipe, toInfuseKey, recipe);
            boolean checkFluid = saveOnMapFluid(INSTANCE.fluidToRecipe, fluidStackKey, recipe);

            if (!(checkFluid || checkInfuse || checkMelt)) {
                throw new RuntimeException("Illegal Fluid Infuser Recipe: " + recipe);
            }
        } else {
            throw new RuntimeException("Illegal Fluid Infuser Recipe: " + recipe);
        }
    }

    private static boolean saveOnMapItem(Map<ItemOD, List<FluidInfuserRecipe>> map, ItemOD key, FluidInfuserRecipe recipe) {

        return saveOnMap((Map) map, key, recipe);
    }

    private static boolean saveOnMapFluid(Map<FluidStack, List<FluidInfuserRecipe>> map, FluidStack key, FluidInfuserRecipe recipe) {

        return saveOnMap((Map) map, key, recipe);

    }

    private static boolean saveOnMap(Map<Object, List<FluidInfuserRecipe>> map, Object key, FluidInfuserRecipe recipe) {

        if (!map.containsKey(key)) {
            List<FluidInfuserRecipe> recipeList = new LinkedList<>();
            recipeList.add(recipe);
            map.put(key, recipeList);
            return true;

        } else if (!map.get(key).contains(recipe)) {
            map.get(key).add(recipe);
            return true;
        } else
            return false;
    }


    private static ItemOD createKey(ItemStack original) {
        original = original.copy();
        original.setCount(1);
        return new ItemOD(original);
    }

    private static FluidStack createKey(FluidStack fluidStack) {
        fluidStack = fluidStack.copy();
        fluidStack.amount = 1;

        return fluidStack;
    }


}
