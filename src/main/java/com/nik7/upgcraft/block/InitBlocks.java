package com.nik7.upgcraft.block;


import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

public class InitBlocks {

    public static final BlockUpgC blockWoodenLiquidTank = new BlockWoodenLiquidTank();
    public static final BlockUpgC blockSlimyLog = new BlockSlimyLog();

    public static void init() {

        GameRegistry.registerBlock(blockWoodenLiquidTank, Reference.MOD_ID + "Block" + Names.Blocks.WOODEN_LIQUID_TANK);
        GameRegistry.registerBlock(blockSlimyLog, Reference.MOD_ID + "Block" + Names.Blocks.SLIMY_LOG);

    }
}
