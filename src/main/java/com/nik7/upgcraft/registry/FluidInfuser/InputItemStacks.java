package com.nik7.upgcraft.registry.FluidInfuser;

import com.nik7.upgcraft.registry.ItemOD;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class InputItemStacks {

    private ItemOD toMelt;
    private ItemOD toInfuse;
    private FluidStack fluid;


    public InputItemStacks(ItemStack toMelt, ItemStack toInfuse, FluidStack fluid) {
        this.toMelt = new ItemOD(toMelt);
        this.toInfuse = new ItemOD(toInfuse);
        this.fluid = fluid;

    }

    public ItemStack getToMelt() {
        return toMelt.getItemStack();
    }

    public ItemStack getToInfuse() {
        return toInfuse.getItemStack();
    }

    public String getToMeltS() {
        return toMelt.getOreDictionaryName();
    }

    public String getToInfuseS() {
        return toInfuse.getOreDictionaryName();
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public boolean equals(Object o) {
        if (o instanceof InputItemStacks) {

            InputItemStacks iS = (InputItemStacks) o;

            if (this.toMelt != null && iS.toMelt != null && this.toInfuse != null && iS.toInfuse != null && this.fluid != null && iS.fluid != null) {

                return this.toMelt.equals(iS.toMelt) && this.toInfuse.equals(iS.toInfuse) && this.fluid.isFluidStackIdentical(iS.fluid);

            } else if (((this.fluid != null) || (iS.fluid != null)) && (this.toMelt != null) && (iS.toMelt != null) && (this.toInfuse != null) && (iS.toInfuse != null)) {

                return this.toMelt.equals(iS.toMelt) && this.toInfuse.equals(iS.toInfuse);

            } else if ((this.toMelt == null) || ((iS.toMelt == null) && ((this.fluid == null) || (iS.fluid == null)))) {
                return this.toInfuse.equals(iS.toInfuse);
            } else if (((this.fluid == null) || (iS.fluid == null))) {
                return this.toMelt.equals(iS.toMelt);
            } else if (((iS.toMelt == null))) {

                return this.toInfuse.equals(iS.toInfuse) && this.fluid.isFluidStackIdentical(iS.fluid);

            } else {

                return this.toMelt.equals(iS.toMelt) && this.fluid.isFluidStackIdentical(iS.fluid);
            }

        }

        return false;
    }
}