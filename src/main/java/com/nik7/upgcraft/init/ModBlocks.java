package com.nik7.upgcraft.init;


import com.nik7.upgcraft.block.BlockBasicFluidHopper;
import com.nik7.upgcraft.block.BlockSlimyLog;
import com.nik7.upgcraft.block.BlockUpgC;
import com.nik7.upgcraft.block.BlockWoodenLiquidTank;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {

    public static final BlockUpgC blockWoodenLiquidTank = new BlockWoodenLiquidTank();
    public static final BlockUpgC blockSlimyLog = new BlockSlimyLog();
    public static final BlockUpgC blockFluidBasicHopper = new BlockBasicFluidHopper();

    public static void init() {

        GameRegistry.registerBlock(blockWoodenLiquidTank, Reference.MOD_ID + "Block" + Names.Blocks.WOODEN_LIQUID_TANK);
        GameRegistry.registerBlock(blockSlimyLog, Reference.MOD_ID + "Block" + Names.Blocks.SLIMY_LOG);
        GameRegistry.registerBlock(blockFluidBasicHopper, Reference.MOD_ID + "Block" + Names.Blocks.FLUID_HOPPER);

    }
}
