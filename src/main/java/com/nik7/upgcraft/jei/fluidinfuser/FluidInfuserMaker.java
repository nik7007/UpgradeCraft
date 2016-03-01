package com.nik7.upgcraft.jei.fluidinfuser;


import com.nik7.upgcraft.registry.FluidInfuser.CustomCraftingExperience;
import com.nik7.upgcraft.registry.FluidInfuser.FluidInfuserRecipe;
import com.nik7.upgcraft.registry.FluidInfuser.InputItemStacks;
import com.nik7.upgcraft.registry.FluidInfuserRegister;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FluidInfuserMaker {

    @Nonnull
    public static List<FluidInfuserJEI> getRecipes() {

        ArrayList<FluidInfuserJEI> recipes = new ArrayList<FluidInfuserJEI>();

        for (HashSet<FluidInfuserRecipe> fluidInfuserRecipes : FluidInfuserRegister.getRecipes()) {
            for (FluidInfuserRecipe fluidInfuserRecipe : fluidInfuserRecipes) {
                InputItemStacks inputs = fluidInfuserRecipe.getInputs();

                ItemStack output = fluidInfuserRecipe.getResult();
                float experience = 0;

                if (output.getItem() instanceof CustomCraftingExperience) {
                    experience = ((CustomCraftingExperience) output.getItem()).getCustomCraftingExperience(output);
                } else if (output.getItem() instanceof ItemBlock) {
                    Block block = ((ItemBlock) output.getItem()).block;
                    if (block instanceof CustomCraftingExperience)
                        experience = ((CustomCraftingExperience) block).getCustomCraftingExperience(output);
                }

                experience *= output.stackSize;

                FluidInfuserJEI recipe = new FluidInfuserJEI(inputs.getToInfuse(), inputs.getToMelt(), output, fluidInfuserRecipe.getFluidStack(), experience);
                recipes.add(recipe);
            }

        }


        return recipes;
    }
}
