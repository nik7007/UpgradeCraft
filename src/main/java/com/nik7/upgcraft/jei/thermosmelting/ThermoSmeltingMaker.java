package com.nik7.upgcraft.jei.thermosmelting;


import com.nik7.upgcraft.registry.CustomCraftingExperience;
import com.nik7.upgcraft.registry.ThermoSmelting.ThermoSmeltingRecipe;
import com.nik7.upgcraft.registry.ThermoSmeltingRegister;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.nik7.upgcraft.tileentities.UpgCtileentityThermoFluidFurnace.MIN_OPERATION_TEMP;

public class ThermoSmeltingMaker {

    public static List<ThermoSmeltingJEI> getRecipes() {

        FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
        Map<ItemStack, ItemStack> smeltingMap = furnaceRecipes.getSmeltingList();
        Collection<ThermoSmeltingRecipe> thermoSmeltingRecipes = ThermoSmeltingRegister.getRecipes();

        List<ThermoSmeltingJEI> recipes = new ArrayList<>();


        for (Map.Entry<ItemStack, ItemStack> itemStackItemStackEntry : smeltingMap.entrySet()) {
            ItemStack input = itemStackItemStackEntry.getKey();
            ItemStack output = itemStackItemStackEntry.getValue();

            float experience = furnaceRecipes.getSmeltingExperience(output);
            recipes.add(new ThermoSmeltingJEI(input, output, experience, MIN_OPERATION_TEMP));
        }

        for (ThermoSmeltingRecipe thermoSmeltingRecipe : thermoSmeltingRecipes) {

            ItemStack input = thermoSmeltingRecipe.getInput();
            ItemStack output = thermoSmeltingRecipe.getOutput();
            float temperature = thermoSmeltingRecipe.getTemperature();

            float experience = 0;

            if (output.getItem() instanceof CustomCraftingExperience) {
                experience = ((CustomCraftingExperience) output.getItem()).getCustomCraftingExperience(output);
            } else if (output.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) output.getItem()).block;
                if (block instanceof CustomCraftingExperience)
                    experience = ((CustomCraftingExperience) block).getCustomCraftingExperience(output);
            }

            experience *= output.stackSize;
            recipes.add(new ThermoSmeltingJEI(input, output, experience, (int) temperature));

        }

        return recipes;
    }
}
