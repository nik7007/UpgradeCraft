package com.nik7.upgcraft.init;


import com.nik7.upgcraft.block.*;
import com.nik7.upgcraft.item.ItemBlockClayFluidTank;
import com.nik7.upgcraft.item.ItemBlockUpgCBasicFluidHopper;
import com.nik7.upgcraft.item.ItemBlockWoodenFluidTank;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static BlockUpgC blockUpgCSlimyLog;
    public static BlockUpgC blockUpgCWoodenFluidTank;
    public static BlockUpgC blockUpgCBasicFluidHopper;
    public static BlockUpgC blockUpgCFluidFurnace;
    public static BlockUpgC blockUpgCFluidInfuser;
    public static BlockUpgC blockUpgCClayFluidTank;
    public static BlockUpgC blockUpgCFluidHopper;
    public static BlockUpgC blockUpgCSlimyObsidian;

    public static void init() {
        ModBlocks.blockUpgCSlimyLog = new BlockUpgCSlimyLog();
        ModBlocks.blockUpgCWoodenFluidTank = new BlockUpgCWoodenFluidTank();
        ModBlocks.blockUpgCBasicFluidHopper = new BlockUpgCBasicFluidHopper();
        ModBlocks.blockUpgCFluidFurnace = new BlockUpgCFluidFurnace();
        ModBlocks.blockUpgCFluidInfuser = new BlockUpgCFluidInfuser();
        ModBlocks.blockUpgCClayFluidTank = new BlockUpgCClayFluidTank();
        ModBlocks.blockUpgCFluidHopper = new BlockUpgCFluidHopper();
        blockUpgCSlimyObsidian = new BlockUpgCSlimyObsidian();

        ModBlocks.registerBlock(ModBlocks.blockUpgCSlimyLog);
        ModBlocks.registerBlock(ModBlocks.blockUpgCWoodenFluidTank, ItemBlockWoodenFluidTank.class);
        ModBlocks.registerBlock(ModBlocks.blockUpgCBasicFluidHopper, ItemBlockUpgCBasicFluidHopper.class);
        ModBlocks.registerBlock(ModBlocks.blockUpgCFluidFurnace);
        ModBlocks.registerBlock(ModBlocks.blockUpgCFluidInfuser);
        ModBlocks.registerBlock(ModBlocks.blockUpgCClayFluidTank, ItemBlockClayFluidTank.class);
        ModBlocks.registerBlock(ModBlocks.blockUpgCFluidHopper);
        ModBlocks.registerBlock(blockUpgCSlimyObsidian);
    }


    private static void registerBlock(BlockUpgC block) {
        ModBlocks.registerBlock(block, null);
    }

    private static void registerBlock(BlockUpgC block, Class<? extends ItemBlock> itemBlock) {

        String name = block.getName();

        if (itemBlock == null)
            GameRegistry.registerBlock(block, name);
        else
            GameRegistry.registerBlock(block, itemBlock, name);

    }

}
