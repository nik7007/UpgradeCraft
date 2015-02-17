package com.nik7.upgcraft.registry.FluidInfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FluidInfuserRecipe {

    private InputItemStacks inputs;
    private FluidStack fluidStack;
    private ItemStack result;
    private int ticksToMelt;
    private int ticksToInfuse;

    public FluidInfuserRecipe(InputItemStacks inputs, FluidStack fluidStack, ItemStack result, int ticksToInfuse, int ticksToMelt) {
        this.inputs = inputs;
        this.fluidStack = fluidStack;
        this.result = result;
        this.ticksToInfuse = ticksToInfuse;
        this.ticksToMelt = ticksToMelt;
    }

    public int getNumberItemToMelt() {
        return inputs.getToMelt().stackSize;
    }

    public int getNumberItemToInfuse() {
        return inputs.getToInfuse().stackSize;
    }

    public int getFluidAmount() {
        return fluidStack.amount;
    }

    public ItemStack getResult() {
        return result;
    }

    public InputItemStacks getInputs() {
        return inputs;
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }

    public int getTicksToMelt() {
        return ticksToMelt;
    }

    public int getTicksToInfuse() {
        return ticksToInfuse;
    }
}
