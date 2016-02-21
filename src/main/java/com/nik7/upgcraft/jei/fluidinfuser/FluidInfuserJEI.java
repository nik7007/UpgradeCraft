package com.nik7.upgcraft.jei.fluidinfuser;


import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FluidInfuserJEI extends BlankRecipeWrapper {

    @Nonnull
    private final ItemStack infuseInput;
    @Nonnull
    private final ItemStack meltInput;
    @Nonnull
    private final ItemStack output;
    @Nonnull
    private final FluidStack fluid;

    public FluidInfuserJEI(@Nonnull ItemStack infuseInput, @Nonnull ItemStack meltInput, @Nonnull ItemStack output, @Nonnull FluidStack fluid) {
        this.infuseInput = infuseInput;
        this.meltInput = meltInput;
        this.output = output;
        this.fluid = fluid;
    }

    @Override
    @Nonnull
    public List getInputs() {
        List<Object> inputs = new ArrayList<Object>();
        inputs.add(infuseInput);
        inputs.add(meltInput);
        inputs.add(fluid);
        return inputs;
    }

    @Override
    @Nonnull
    public List getOutputs() {
        return Collections.singletonList(output);
    }
}
