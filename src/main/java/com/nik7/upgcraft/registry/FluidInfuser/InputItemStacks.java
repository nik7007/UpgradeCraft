package com.nik7.upgcraft.registry.FluidInfuser;

import com.nik7.upgcraft.registry.ItemOD;
import net.minecraft.item.ItemStack;

public class InputItemStacks {

    private ItemOD toMelt;
    private ItemOD toInfuse;


    public InputItemStacks(ItemStack toMelt, ItemStack toInfuse) {
        this.toMelt = new ItemOD(toMelt);
        this.toInfuse = new ItemOD(toInfuse);


    }

    public ItemStack getToMelt() {
        return toMelt.itemStack;
    }

    public ItemStack getToInfuse() {
        return toInfuse.itemStack;
    }

    public String getToMeltS() {
        return toMelt.nameOD;
    }

    public String getToInfuseS() {
        return toInfuse.nameOD;
    }

    public boolean equals(Object o) {
        if (o instanceof InputItemStacks) {

            InputItemStacks iS = (InputItemStacks) o;

            if (this.toMelt != null && iS.toMelt != null && this.toInfuse != null && iS.toInfuse != null) {
                return this.toMelt.equals(iS.toMelt) && this.toInfuse.equals(iS.toInfuse);
            } else if (this.toMelt == null || iS.toMelt == null) {
                return this.toInfuse.equals(iS.toInfuse);
            } else
                return this.toMelt.equals(iS.toMelt);

        }

        return false;
    }
}