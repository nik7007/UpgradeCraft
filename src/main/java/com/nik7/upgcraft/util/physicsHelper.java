package com.nik7.upgcraft.util;


import net.minecraftforge.fluids.FluidStack;

public class PhysicsHelper {

    public static float getFluidMass(FluidStack fluidStack) {
        if (fluidStack == null)
            return 0;
        float volume = getFluidVolume(fluidStack);
        return getFluidMass(fluidStack, volume);

    }

    public static float getFluidVolume(FluidStack fluidStack) {
        if (fluidStack == null)
            return 0;
        return fluidStack.amount / (float) (1000 * 1000); // "m3"
    }

    public static float getFluidMass(FluidStack fluidStack, float volume) {
        if (fluidStack == null)
            return 0;
        int density = fluidStack.getFluid().getDensity(fluidStack);
        return density * volume;
    }

    //*********** "Thermodynamics" ***********//

    public static float getFinalTemp(FluidStack fluidStack, float otherTemp, float otherMass, int dTime, float heatLose) {

        if (fluidStack == null)
            return 0;

        final double heatTransferCoefficient = 10000 * 1.75;

        float fluidHeat = getHeat(fluidStack, otherTemp, otherMass) - heatLose;

        return (float) ((fluidHeat / (0.25 * heatTransferCoefficient * dTime)));

    }

    public static float getBalanceTemperature(float temp1, float temp2, float mass1, float mass2) {

        if (mass1 == 0 || mass2 == 0)
            return 0;

        return (temp1 * mass1 + temp2 * mass2) / (mass1 + mass2);
    }

    public static float getHeat(FluidStack fluidStack, float otherTemp, float otherMass) {
        if (fluidStack == null)
            return 0;

        float temp = fluidStack.getFluid().getTemperature(fluidStack);
        float mass = getFluidMass(fluidStack);
        float finalTemp = getBalanceTemperature(temp, otherTemp, mass, otherMass);

        return mass * getSpecificHeatFromFluid(fluidStack) * (finalTemp - otherTemp); //Q = m c (Tfin - Tiniz)

    }

    public static float getSpecificHeatFromFluid(FluidStack fluidStack) {

        if (fluidStack == null)
            return 0;

        float volume = 1;
        float mass = getFluidMass(fluidStack, volume);//density * volume;

        int t1 = 100 + 273;//fluidStack.getFluid().getTemperature(fluidStack);
        int t2 = 25 + 273;

        float tf = getBalanceTemperature(t1, t2, mass, volume);// volume*1

        return (((mass + 1) * (tf - t2) / (mass * (t1 - tf))) / 4);
    }

    //*********** **************** ***********//

}
