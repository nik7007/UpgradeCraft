package com.nik7.upgcraft.init;


import com.nik7.upgcraft.block.*;
import com.nik7.upgcraft.item.ItemBlockCSlimyLog;
import com.nik7.upgcraft.item.ItemBlockClayTank;
import com.nik7.upgcraft.item.ItemBlockWoodenTank;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {

    public static final Block blockWoodenLiquidTank = new BlockWoodenLiquidTank();
    public static final Block blockClayLiquidTank = new BlockClayFluidTank();
    public static final Block blockUpgCEnderTank = new BlockUpgCEnderTank();
    public static final Block blockSlimyLog = new BlockUpgCSlimyLog();
    public static final Block blockFluidBasicHopper = new BlockUpgCBasicFluidHopper();
    public static final Block blockFluidFurnace = new BlockUpgCFluidFurnace();
    public static final Block blockFluidInfuse = new BlockUpgCFluidInfuser();

    public static void init() {

        GameRegistry.registerBlock(blockWoodenLiquidTank, ItemBlockWoodenTank.class, Reference.MOD_ID + "Block" + Names.Blocks.WOODEN_LIQUID_TANK);
        GameRegistry.registerBlock(blockClayLiquidTank, ItemBlockClayTank.class, Reference.MOD_ID + "Block" + Names.Blocks.CLAY_LIQUID_TANK);
        GameRegistry.registerBlock(blockUpgCEnderTank ,/* ItemBlockClayTank.class,*/ Reference.MOD_ID + "Block" + Names.Blocks.ENDER_TANK);
        GameRegistry.registerBlock(blockSlimyLog, ItemBlockCSlimyLog.class, Reference.MOD_ID + "Block" + Names.Blocks.SLIMY_LOG);
        GameRegistry.registerBlock(blockFluidBasicHopper, Reference.MOD_ID + "Block" + Names.Blocks.FLUID_HOPPER);
        GameRegistry.registerBlock(blockFluidFurnace, Reference.MOD_ID + "Block" + Names.Blocks.FLUID_FURNACE);
        GameRegistry.registerBlock(blockFluidInfuse, Reference.MOD_ID + "Block" + Names.Blocks.FLUID_INFUSE);

    }
}
