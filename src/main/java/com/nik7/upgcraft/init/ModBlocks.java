package com.nik7.upgcraft.init;


import com.nik7.upgcraft.block.*;
import com.nik7.upgcraft.item.*;
import net.minecraft.block.Block;
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
    public static BlockUpgC blockUpgCEnderFluidTank;
    public static BlockUpgC blockUpgCActiveLavaMaker;
    public static BlockUpgC blockUpgCThermoFluidFurnace;
    public static BlockUpgC blockUpgCIronFluidTank;
    public static BlockUpgC blockUpgCFluidTankMold;
    public static BlockUpgC blockUpgCClayBrick;
    public static BlockUpgCStairs blockUpgCStairsClayBrick;
    public static BlockUpgCStairs blockUpgCStairsCookedClayBrick;
    public static BlockUpgC testBlock;

    public static void init() {
        ModBlocks.blockUpgCSlimyLog = new BlockUpgCSlimyLog();
        ModBlocks.blockUpgCWoodenFluidTank = new BlockUpgCWoodenFluidTank();
        ModBlocks.blockUpgCBasicFluidHopper = new BlockUpgCBasicFluidHopper();
        ModBlocks.blockUpgCFluidFurnace = new BlockUpgCFluidFurnace();
        ModBlocks.blockUpgCFluidInfuser = new BlockUpgCFluidInfuser();
        ModBlocks.blockUpgCClayFluidTank = new BlockUpgCClayFluidTank();
        ModBlocks.blockUpgCFluidHopper = new BlockUpgCFluidHopper();
        ModBlocks.blockUpgCSlimyObsidian = new BlockUpgCSlimyObsidian();
        ModBlocks.blockUpgCEnderFluidTank = new BlockUpgCEnderFluidTank();
        ModBlocks.blockUpgCActiveLavaMaker = new BlockUpgCActiveLavaMaker();
        ModBlocks.blockUpgCThermoFluidFurnace = new BlockUpgCThermoFluidFurnace();
        ModBlocks.blockUpgCIronFluidTank = new BlockUpgCIronFluidTank();
        ModBlocks.blockUpgCFluidTankMold = new BlockUpgCFluidTankMold();
        ModBlocks.blockUpgCClayBrick = new BlockUpgCClayBrick();
        ModBlocks.blockUpgCStairsClayBrick = new BlockUpgCStairs(blockUpgCClayBrick.getDefaultState());
        ModBlocks.blockUpgCStairsCookedClayBrick = new BlockUpgCStairs(blockUpgCClayBrick.getDefaultState(), "StairsCooked" + blockUpgCClayBrick.getName());
        testBlock = new TestBlock();

        ModBlocks.registerBlock(ModBlocks.blockUpgCSlimyLog);
        ModBlocks.registerBlock(ModBlocks.blockUpgCWoodenFluidTank, ItemBlockWoodenFluidTank.class);
        ModBlocks.registerBlock(ModBlocks.blockUpgCBasicFluidHopper, ItemBlockUpgCBasicFluidHopper.class);
        ModBlocks.registerBlock(ModBlocks.blockUpgCFluidFurnace);
        ModBlocks.registerBlock(ModBlocks.blockUpgCFluidInfuser);
        ModBlocks.registerBlock(ModBlocks.blockUpgCClayFluidTank, ItemBlockClayFluidTank.class);
        ModBlocks.registerBlock(ModBlocks.blockUpgCFluidHopper);
        ModBlocks.registerBlock(ModBlocks.blockUpgCSlimyObsidian);
        ModBlocks.registerBlock(ModBlocks.blockUpgCEnderFluidTank);
        ModBlocks.registerBlock(ModBlocks.blockUpgCActiveLavaMaker);
        ModBlocks.registerBlock(ModBlocks.blockUpgCThermoFluidFurnace);
        ModBlocks.registerBlock(ModBlocks.blockUpgCIronFluidTank, ItemBlockIronFluidTank.class);
        ModBlocks.registerBlock(ModBlocks.blockUpgCFluidTankMold, ItemBlockFluidTankMold.class);
        ModBlocks.registerBlock(ModBlocks.blockUpgCClayBrick, ItemBlockClayBrick.class);
        ModBlocks.registerBlock(ModBlocks.blockUpgCStairsClayBrick);
        ModBlocks.registerBlock(ModBlocks.blockUpgCStairsCookedClayBrick);
        registerBlock(testBlock);
    }


    private static void registerBlock(IBlockUpgC block) {
        ModBlocks.registerBlock(block, ItemBlock.class);
    }

    private static void registerBlock(IBlockUpgC block, Class<? extends ItemBlock> itemBlock) {

        String name = block.getName();

        GameRegistry.registerBlock((Block) block, itemBlock, name);

    }

}
