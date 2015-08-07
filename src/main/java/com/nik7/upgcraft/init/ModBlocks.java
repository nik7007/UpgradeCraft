package com.nik7.upgcraft.init;


import com.nik7.upgcraft.block.*;
import com.nik7.upgcraft.item.ItemBlockClayTank;
import com.nik7.upgcraft.item.ItemBlockSlimyLog;
import com.nik7.upgcraft.item.ItemBlockWoodenTank;
import com.nik7.upgcraft.item.itemBlockSlimyObsidian;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {

    public static final Block blockWoodenLiquidTank = new BlockWoodenLiquidTank();
    public static final Block blockClayLiquidTank = new BlockClayFluidTank();
    public static final Block blockUpgCEnderTank = new BlockUpgCEnderTank();
    public static final Block blockSlimyLog = new BlockUpgCSlimyLog();
    public static final Block blockFluidBasicHopper = new BlockUpgCBasicFluidHopper();
    public static final Block blockFluidFurnace = new BlockUpgCFluidFurnace();
    public static final Block blockFluidInfuse = new BlockUpgCFluidInfuser();
    public static final Block blockUpgCSlimyObsidian = new BlockUpgCSlimyObsidian();
    public static final Block blockUpgCEnderHopper = new BlockUpgCEnderHopper();
    public static final Block blockActiveLava =  new BlockUpgCActiveLava();

    public static void init() {

        GameRegistry.registerBlock(blockWoodenLiquidTank, ItemBlockWoodenTank.class, Reference.MOD_ID + "Block" + Names.Blocks.WOODEN_LIQUID_TANK);
        GameRegistry.registerBlock(blockClayLiquidTank, ItemBlockClayTank.class, Reference.MOD_ID + "Block" + Names.Blocks.CLAY_LIQUID_TANK);
        GameRegistry.registerBlock(blockUpgCEnderTank, Reference.MOD_ID + "Block" + Names.Blocks.ENDER_TANK);
        GameRegistry.registerBlock(blockSlimyLog, ItemBlockSlimyLog.class, Reference.MOD_ID + "Block" + Names.Blocks.SLIMY_LOG);
        GameRegistry.registerBlock(blockUpgCSlimyObsidian, itemBlockSlimyObsidian.class, Reference.MOD_ID + "Block" + Names.Blocks.SLIMY_OBSIDIAN);
        GameRegistry.registerBlock(blockFluidBasicHopper, Reference.MOD_ID + "Block" + Names.Blocks.FLUID_HOPPER);
        GameRegistry.registerBlock(blockFluidFurnace, Reference.MOD_ID + "Block" + Names.Blocks.FLUID_FURNACE);
        GameRegistry.registerBlock(blockFluidInfuse, Reference.MOD_ID + "Block" + Names.Blocks.FLUID_INFUSE);
        GameRegistry.registerBlock(blockUpgCEnderHopper, Reference.MOD_ID + "Block" + Names.Blocks.ENDER_HOPPER);

        GameRegistry.registerBlock(blockActiveLava, Reference.MOD_ID + "Block" + Names.Fluid.ACTIVE_LAVE);


    }
}
