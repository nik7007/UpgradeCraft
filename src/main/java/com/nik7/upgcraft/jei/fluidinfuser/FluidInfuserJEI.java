package com.nik7.upgcraft.jei.fluidinfuser;


import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class FluidInfuserJEI extends BlankRecipeWrapper {

    protected final List<ItemStack> melt;
    protected final List<ItemStack> infuse;
    protected final List<List<ItemStack>> inputs;
    private final ItemStack output;
    private final FluidStack fluid;

    public FluidInfuserJEI(List<ItemStack> melt, List<ItemStack> infuse, ItemStack output, FluidStack fluid) {
        this.melt = melt;
        this.infuse = infuse;
        this.output = output;
        this.fluid = fluid;

        this.inputs = new ArrayList<>();
        this.inputs.add(melt);
        this.inputs.add(infuse);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {

        ingredients.setInputLists(ItemStack.class, this.inputs);
        ingredients.setInput(FluidStack.class, this.fluid);
        ingredients.setOutput(ItemStack.class, this.output);

    }
}
