package com.nik7.upgcraft.registry.ThermoSmelting;


import com.nik7.upgcraft.registry.ItemOD;
import net.minecraft.item.ItemStack;

public class ThermoSmeltingRecipe {

    private final ItemOD input;
    private final ItemStack output;

    private final float temperature;
    private final int ticks;

    public ThermoSmeltingRecipe(ItemStack input, ItemStack output, float temperature, int ticks) {
        this.input = new ItemOD(input);
        this.output = output;
        this.temperature = temperature;
        this.ticks = ticks;
    }

    public ItemStack getInput() {
        return input.getItemStack();
    }

    public ItemStack getOutput() {
        return output;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getTicks() {
        return ticks;
    }
}
