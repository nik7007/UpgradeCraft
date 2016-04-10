package com.nik7.upgcraft.jei.fluidfurnace;


import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FluidFurnaceMaker {

    @Nonnull
    public static List<FluidFurnaceJEI> getRecipes() {

        FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
        Map<ItemStack, ItemStack> smeltingMap = furnaceRecipes.getSmeltingList();

        List<FluidFurnaceJEI> recipes = new ArrayList<>();

        for (Map.Entry<ItemStack, ItemStack> itemStackItemStackEntry : smeltingMap.entrySet()) {
            ItemStack input = itemStackItemStackEntry.getKey();
            ItemStack output = itemStackItemStackEntry.getValue();

            float experience = furnaceRecipes.getSmeltingExperience(output);

            FluidFurnaceJEI furnaceJEI = new FluidFurnaceJEI(input, output, experience);

            recipes.add(furnaceJEI);

        }

        return recipes;

    }
}
