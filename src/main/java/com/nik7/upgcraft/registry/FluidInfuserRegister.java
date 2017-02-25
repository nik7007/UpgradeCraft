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

    public static Collection<FluidInfuserRecipe> getRecipes() {

        Set<FluidInfuserRecipe> recipesCopy = new HashSet<>();

        for (FluidInfuserRecipe recipe : INSTANCE.recipes) {
            recipesCopy.add(recipe);
        }

        return recipesCopy;
    }

    public static void addRecipe(FluidStack fluidStack, ItemStack result, ItemStack toMelt, int ticksToMelt, ItemStack toInfuse, int ticksToInfuse) {

        if (checkConstrains(fluidStack, result, toMelt, ticksToMelt, toInfuse, ticksToInfuse)) {
            registerFluidInfuserRecipe(new FluidInfuserRecipe(fluidStack, ticksToMelt, toMelt, ticksToInfuse, toInfuse, result));
        } else {
            throw new RuntimeException("Illegal Fluid Infuser Recipe! result: " + result);
        }
    }

    private static boolean checkConstrains(FluidStack fluidStack, ItemStack result, ItemStack toMelt, int ticksToMelt, ItemStack toInfuse, int ticksToInfuse) {
        if (fluidStack != null && result != null && toMelt != null && toInfuse != null && ticksToMelt > 0 && ticksToInfuse > 0) {
            if (fluidStack.getFluid() != null && fluidStack.amount > 0) {
                if (!result.isEmpty() && !toMelt.isEmpty() && !toInfuse.isEmpty())
                    return true;
            }
        }
        return false;
    }

    private static void registerFluidInfuserRecipe(FluidInfuserRecipe recipe) {
        if (!INSTANCE.recipes.contains(recipe)) {
            INSTANCE.recipes.add(recipe);

            ItemOD toMeltKey = createKey(recipe.getToMelt());
            ItemOD toInfuseKey = createKey(recipe.getToInfuse());
            FluidStack fluidStackKey = createKey(recipe.getFluidStack());

            boolean checkMelt = saveOnMap(INSTANCE.toMeltToRecipe, toMeltKey, recipe);
            boolean checkInfuse = saveOnMap(INSTANCE.toInfuseToRecipe, toInfuseKey, recipe);
            boolean checkFluid = saveOnMap(INSTANCE.fluidToRecipe, fluidStackKey, recipe);

            if (!checkFluid && !checkInfuse && !checkMelt) {
                throw new RuntimeException("Illegal Fluid Infuser Recipe: " + recipe);
            }
        } else {
            throw new RuntimeException("Illegal Fluid Infuser Recipe: " + recipe);
        }
    }

    private static <k> boolean saveOnMap(Map<k, List<FluidInfuserRecipe>> map, k key, FluidInfuserRecipe recipe) {

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
