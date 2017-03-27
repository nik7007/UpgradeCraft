package com.nik7.upgcraft.init;

import com.nik7.upgcraft.block.*;
import com.nik7.upgcraft.item.ItemBlockBasicFunnel;
import com.nik7.upgcraft.item.ItemBlockClayBrick;
import com.nik7.upgcraft.item.ItemBlockFluidTank;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class ModBlocks {


    public static BlockUpgC blockSlimyLog;
    public static BlockUpgC blockWoodenFluidTank;
    public static BlockUpgC blockFluidFurnace;
    public static BlockUpgC blockFunnel;
    public static BlockUpgC blockBasicFunnel;
    public static BlockUpgC blockFluidInfuser;
    public static BlockUpgC blockClayBrick;
    public static BlockUpgCStairs blockStairsClayBrick;
    public static BlockUpgCStairs blockStairsCookedClayBrick;
    public static BlockUpgC blockClayFluidTank;
    public static BlockUpgC blockHardenedClayFluidTank;


    public static void init() {
        blockSlimyLog = new BlockSlimyLog();
        blockWoodenFluidTank = new BlockWoodenFluidTank();
        blockFluidFurnace = new BlockFluidFurnace();
        blockFunnel = new BlockFunnel();
        blockBasicFunnel = new BlockBasicFunnel();
        blockFluidInfuser = new BlockFluidInfuser();
        blockClayBrick = new BlockClayBrick();
        blockStairsClayBrick = new BlockUpgCStairs(blockClayBrick.getDefaultState());
        blockStairsCookedClayBrick = new BlockUpgCStairs(blockClayBrick.getDefaultState(), "stairscooked");
        blockClayFluidTank = new BlockClayFluidTank();
        blockHardenedClayFluidTank = new BlockHardenedClayFluidTank();

        register();
    }

    private static void register() {
        ModBlocks.registerBlock(blockSlimyLog);
        ModBlocks.registerBlock(blockFluidFurnace);
        ModBlocks.registerBlock(blockWoodenFluidTank, ItemBlockFluidTank.class);
        ModBlocks.registerBlock(blockFunnel);
        ModBlocks.registerBlock(blockBasicFunnel, ItemBlockBasicFunnel.class);
        ModBlocks.registerBlock(blockFluidInfuser);
        ModBlocks.registerBlock(blockClayBrick, ItemBlockClayBrick.class);
        ModBlocks.registerBlock(blockStairsClayBrick);
        ModBlocks.registerBlock(blockStairsCookedClayBrick);
        ModBlocks.registerBlock(blockClayFluidTank, ItemBlockFluidTank.class);
        ModBlocks.registerBlock(blockHardenedClayFluidTank, ItemBlockFluidTank.class);
    }

    private static void registerBlock(Block block) {
        ModBlocks.registerBlock(block, ItemBlock.class);
    }

    private static void registerBlock(Block block, Class<? extends ItemBlock> itemBlock) {

        registerBlock(block, itemBlock, block.getRegistryName());

    }

    private static void registerBlock(Block block, Class<? extends ItemBlock> itemBlock, ResourceLocation name) {

        if (itemBlock != null) {
            try {
                Class<?> blockClass = Block.class;
                Constructor<? extends ItemBlock> itemCtor = itemBlock.getConstructor(blockClass);
                ItemBlock i = itemCtor.newInstance(block);

                GameRegistry.register(i.setRegistryName(name));
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                LogHelper.error(e, "Caught an exception during block registration");
                throw new LoaderException(e);
            }

        }
        GameRegistry.register(block);

    }
}
