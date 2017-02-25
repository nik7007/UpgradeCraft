package com.nik7.upgcraft.registry.recipes;


import com.nik7.upgcraft.registry.ItemOD;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;


public class FluidInfuserRecipe {

    private final FluidStack fluidStack;

    private final int tickToMelt;
    private final ItemOD toMelt;

    private final int tickToInfuse;
    private final ItemOD toInfuse;

    private final ItemStack result;

    public FluidInfuserRecipe(FluidStack fluidStack, int tickToMelt, ItemStack toMelt, int tickToInfuse, ItemStack toInfuse, ItemStack result) {
        this.fluidStack = fluidStack;
        this.tickToMelt = tickToMelt;
        this.toMelt = new ItemOD(toMelt);
        this.tickToInfuse = tickToInfuse;
        this.toInfuse = new ItemOD(toInfuse);
        this.result = result;
    }

    public FluidStack getFluidStack() {
        return this.fluidStack.copy();
    }

    public int getTickToMelt() {
        return this.tickToMelt;
    }

    public int getTickToInfuse() {
        return this.tickToInfuse;
    }

    public ItemStack getResult() {
        return this.result.copy();
    }

    public ItemStack getToMelt() {
        return this.toMelt.getItemStack();
    }

    public ItemStack getToInfuse() {
        return this.toInfuse.getItemStack();
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof FluidInfuserRecipe) {
            FluidInfuserRecipe other = (FluidInfuserRecipe) o;
            if (this.fluidStack.isFluidStackIdentical(other.fluidStack))
                if (this.toInfuse.equals(other.toInfuse))
                    if (this.toMelt.equals(other.toMelt))
                        return true;

        }

        return false;
    }

    @Override
    public String toString() {

        String result;

        result = "Melt: " + toMelt + " ticks " + tickToMelt;
        result += " -- Infuse: " + toInfuse + " ticks " + tickToInfuse;
        result += " -- Fluid: " + fluidStack.getLocalizedName() + " amount: " + fluidStack.amount;

        return result;

    }
}
