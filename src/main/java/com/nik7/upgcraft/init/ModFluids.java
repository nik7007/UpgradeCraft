package com.nik7.upgcraft.init;

import com.nik7.upgcraft.block.BlockFluidUpgC;
import com.nik7.upgcraft.block.BlockFluidUpgCActiveLava;
import com.nik7.upgcraft.fluid.FluidUpgC;
import com.nik7.upgcraft.fluid.FluidUpgCActiveLava;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

    public static FluidUpgC fluidUpgCActiveLava;
    //
    public static BlockFluidUpgC blockFluidUpgCActiveLava;

    public static void init() {
        fluidUpgCActiveLava = new FluidUpgCActiveLava();
        FluidRegistry.registerFluid(fluidUpgCActiveLava);
        //
        blockFluidUpgCActiveLava = new BlockFluidUpgCActiveLava(fluidUpgCActiveLava);
        registerBlock(blockFluidUpgCActiveLava);
        FluidRegistry.addBucketForFluid(fluidUpgCActiveLava);

    }

    private static void registerBlock(BlockFluidUpgC block) {
        ModBlocks.registerBlock(block, "fluid." + block.getFluid().getName());
    }


}
