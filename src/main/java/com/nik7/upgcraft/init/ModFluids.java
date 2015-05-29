package com.nik7.upgcraft.init;

import com.nik7.upgcraft.fluid.ActiveLava;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

    public static final Fluid ActiveLava = new ActiveLava();

    public static void init() {

        FluidRegistry.registerFluid(ActiveLava);

    }

}
