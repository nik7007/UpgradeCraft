package com.nik7.upgcraft.init;

import com.nik7.upgcraft.block.BlocFluidUpgC;
import com.nik7.upgcraft.block.BlocFluidUpgCActiveLava;
import com.nik7.upgcraft.fluid.FluidUpgC;
import com.nik7.upgcraft.fluid.FluidUpgCActiveLava;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModFluids {

    public static FluidUpgC fluidUpgCActiveLava;
    //
    public static BlocFluidUpgC blocFluidUpgCActiveLava;

    public static void init() {
        fluidUpgCActiveLava = new FluidUpgCActiveLava();
        FluidRegistry.registerFluid(fluidUpgCActiveLava);
        //
        blocFluidUpgCActiveLava = new BlocFluidUpgCActiveLava(fluidUpgCActiveLava);
        registerBlock(blocFluidUpgCActiveLava);

    }

    private static void registerBlock(BlocFluidUpgC block) {
        GameRegistry.registerBlock(block, block.getName());
    }

}
