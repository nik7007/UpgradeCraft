package com.nik7.upgcraft.fluid;


import com.nik7.upgcraft.reference.Names;
import net.minecraft.item.EnumRarity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class ActiveLava extends UpgFluid {

    public static final String ACTIVE_VALUE = "activeValue";
    public static final int MAX_ACTIVE_VALUE = 5800;

    public ActiveLava() {
        super(Names.Fluid.ACTIVE_LAVE);
        setLuminosity(8);
        setDensity(5000);
        setViscosity(8000);
        setTemperature(500 + 273);
        setRarity(EnumRarity.uncommon);
    }

    private float getScalarAcV(FluidStack stack) {

        if (stack.tag != null) {
            if (stack.tag.hasKey(ACTIVE_VALUE))
                return (float) stack.tag.getInteger(ACTIVE_VALUE) / MAX_ACTIVE_VALUE;
            else {
                return 0;
            }

        } else {
            stack.tag = new NBTTagCompound();
            stack.tag.setInteger(ACTIVE_VALUE, 0);
            return 0;
        }
    }

    public void increaseActiveValue(FluidStack stack) {
        if (stack.tag == null) {
            stack.tag = new NBTTagCompound();
            stack.tag.setInteger(ACTIVE_VALUE, 1);
        } else if (stack.tag.hasKey(ACTIVE_VALUE)) {

            int activeValue = stack.tag.getInteger(ACTIVE_VALUE) + 1;
            if (activeValue > MAX_ACTIVE_VALUE)
                activeValue = MAX_ACTIVE_VALUE;
            stack.tag.removeTag(ACTIVE_VALUE);
            stack.tag.setInteger(ACTIVE_VALUE, activeValue);

        }

    }

    public void decreaseActiveValue(FluidStack stack) {
        if (stack.tag == null) {
            stack.tag = new NBTTagCompound();
            stack.tag.setInteger(ACTIVE_VALUE, 0);
        } else if (stack.tag.hasKey(ACTIVE_VALUE)) {

            int activeValue = stack.tag.getInteger(ACTIVE_VALUE) - 1;
            if (activeValue < 0)
                activeValue = 0;
            stack.tag.removeTag(ACTIVE_VALUE);
            stack.tag.setInteger(ACTIVE_VALUE, activeValue);

        }

    }

    public int getLuminosity(FluidStack stack) {
        return (int) (getLuminosity() + (15 - getLuminosity()) * getScalarAcV(stack));
    }

    public int getDensity(FluidStack stack) {
        return (int) (getDensity() - 2650 * getScalarAcV(stack));
    }

    public int getTemperature(FluidStack stack) {
        return (int) (getTemperature() + (MAX_ACTIVE_VALUE - getTemperature()) * getScalarAcV(stack));
    }

    public int getViscosity(FluidStack stack) {
        return (int) (getViscosity() - 4200 * getScalarAcV(stack));
    }

}
