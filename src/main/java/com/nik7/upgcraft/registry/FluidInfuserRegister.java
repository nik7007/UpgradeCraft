package com.nik7.upgcraft.registry;


import com.nik7.upgcraft.registry.recipes.FluidInfuserRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FluidInfuserRegister {

    private final static FluidInfuserRegister INSTANCE = new FluidInfuserRegister();

    private final Set<FluidInfuserRecipe> recipes = new HashSet<>();

    private final Map<ItemOD, Map<ItemOD, Map<FluidStack, FluidInfuserRecipe>>> toMeltToRecipe = new HashMap<>();
    private final Map<ItemOD, Map<ItemOD, Map<FluidStack, FluidInfuserRecipe>>> toInfuseToRecipe = new HashMap<>();

    private final Map<FluidStack, Set<ItemOD>> fluidToMelt = new HashMap<>();
    private final Map<FluidStack, Set<ItemOD>> fluidToInfuse = new HashMap<>();

    private final Map<ItemOD, Set<FluidStack>> toMeltFluid = new HashMap<>();
    private final Map<ItemOD, Set<FluidStack>> toInfuseFluid = new HashMap<>();

    private FluidInfuserRegister() {

    }

    private static void registerFluidInfuserRecipe(FluidInfuserRecipe recipe) {
        if (!INSTANCE.recipes.contains(recipe)) {
            INSTANCE.recipes.add(recipe);

            ItemOD toMeltKey = INSTANCE.createKey(recipe.getToMelt());
            ItemOD toInfuseKey = INSTANCE.createKey(recipe.getToInfuse());
            FluidStack fluidStackKey = INSTANCE.createKey(recipe.getFluidStack());


        }
    }

    private ItemOD createKey(ItemStack original) {
        original.setCount(1);
        return new ItemOD(original);
    }

    private FluidStack createKey(FluidStack fluidStack) {
        fluidStack.amount = 1;

        return fluidStack;
    }


}
