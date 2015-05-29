package com.nik7.upgcraft.fluid;


import com.nik7.upgcraft.reference.Names;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.FluidStack;

public class ActiveLava extends UpgFluid {

    public static final String ACTIVE_VALUE = "activeValue";
    public static final int MAX_ACTIVE_VALUE = 5800;

    public ActiveLava() {
        super(Names.Fluid.ACTIVE_LAVE);
        setLuminosity(2);
        setDensity(5000);
        setViscosity(8000);
        setTemperature(500);
        setRarity(EnumRarity.uncommon);
    }

    private int getScalarAcV(FluidStack stack) {
        if (stack.tag.hasKey(ACTIVE_VALUE))
            return stack.tag.getInteger(ACTIVE_VALUE) / MAX_ACTIVE_VALUE;
        else return 0;
    }

    public int getLuminosity(FluidStack stack) {
        return getLuminosity() + (15 - getLuminosity()) * getScalarAcV(stack);
    }

    public int getDensity(FluidStack stack) {
        return getDensity() - 2650 * getScalarAcV(stack);
    }

    public int getTemperature(FluidStack stack) {
        return getTemperature() + (MAX_ACTIVE_VALUE - getTemperature()) * getScalarAcV(stack);
    }

    public int getViscosity(FluidStack stack) {
        return getViscosity() - 4200 * getScalarAcV(stack);
    }

}
