package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentities.WoodenLiquidTankEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public class InitBlocks {

    public static final BlockUpgC blockWoodenLiquidTank = new BlockWoodenLiquidTank();

    public static void init() {

        GameRegistry.registerBlock(blockWoodenLiquidTank, "blockWoodenLiquidTank");

    }
}
