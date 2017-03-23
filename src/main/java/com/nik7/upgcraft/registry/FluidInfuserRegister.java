package com.nik7.upgcraft.registry;


import com.nik7.upgcraft.registry.recipes.FluidInfuserRecipe;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;


public final class FluidInfuserRegister {

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

    public static FluidInfuserRecipe getRecipe(FluidStack fluidStack, ItemStack toMelt, ItemStack toInfuse) {

        List<FluidInfuserRecipe> toMeltList;
        List<FluidInfuserRecipe> toInfuseList;
        List<FluidInfuserRecipe> fluidList;

        if (fluidStack != null && fluidStack.getFluid() != null && toMelt != null && !toMelt.isEmpty() && toInfuse != null && !toInfuse.isEmpty()) {

            fluidList = INSTANCE.fluidToRecipe.get(createKey(fluidStack));
            toMeltList = INSTANCE.toMeltToRecipe.get(createKey(toMelt));
            toInfuseList = INSTANCE.toInfuseToRecipe.get(createKey(toInfuse));

            if (fluidList.size() == 0)
                fluidList = null;
            if (toMeltList.size() == 0)
                toMeltList = null;
            if (toInfuseList.size() == 0)
                toInfuseList = null;

            List<FluidInfuserRecipe> result = getCommonRecipes(getCommonRecipes(fluidList, toInfuseList), toMeltList);

            if (!result.isEmpty()) {
                if (result.size() != 1) {
                    LogHelper.error("[FluidInfuserRegister] More than one result!! -> FluidStack: " + fluidStack + " melt: " + toMelt + " infuse: " + toInfuse);
                }
                return result.get(0);
            }
        }
        return null;
    }

    public static boolean isInputCorrect(FluidStack fluidStack, ItemStack toMelt, ItemStack toInfuse) {


        List<FluidInfuserRecipe> toMeltList = null;
        List<FluidInfuserRecipe> toInfuseList = null;
        List<FluidInfuserRecipe> fluidList = null;


        if (fluidStack != null && fluidStack.getFluid() != null) {
            fluidList = INSTANCE.fluidToRecipe.get(createKey(fluidStack));
            if (fluidList == null)
                return false;
        }
        if (toMelt != null && !toMelt.isEmpty()) {
            toMeltList = INSTANCE.toMeltToRecipe.get(createKey(toMelt));
            if (toMeltList == null)
                return false;
        }
        if (toInfuse != null && !toInfuse.isEmpty()) {
            toInfuseList = INSTANCE.toInfuseToRecipe.get(createKey(toInfuse));
            if (toInfuseList == null)
                return false;
        }

        List toCheck = getCommonRecipes(getCommonRecipes(fluidList, toInfuseList), toMeltList);


        return !toCheck.isEmpty();
    }

    private static List<FluidInfuserRecipe> getCommonRecipes(List<FluidInfuserRecipe> list1, List<FluidInfuserRecipe> list2) {
        List<FluidInfuserRecipe> result = new LinkedList<>();

        if (list1 == null)
            return list2 != null ? list2 : result;

        if (list2 == null)
            return list1;

        for (FluidInfuserRecipe r1 : list1) {
            for (FluidInfuserRecipe r2 : list2) {
                if (r1.equals(r2)) {
                    result.add(r1);
                }
            }
        }

        return result;
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
