package com.nik7.upgcraft.init;

import com.nik7.upgcraft.block.BlockUpgC;
import com.nik7.upgcraft.block.FluidTank;
import com.nik7.upgcraft.block.SlimyLog;
import com.nik7.upgcraft.block.WoodenFluidTank;
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


    public static BlockUpgC slimyLog;
    public static BlockUpgC woodenFluidTank;

    public static void init() {
        slimyLog = new SlimyLog();
        woodenFluidTank = new WoodenFluidTank();

        register();
    }

    private static void register() {
        ModBlocks.registerBlock(slimyLog);
        ModBlocks.registerBlock(woodenFluidTank, ItemBlockFluidTank.class);
    }

    private static void registerBlock(BlockUpgC block) {
        ModBlocks.registerBlock(block, ItemBlock.class);
    }

    private static void registerBlock(BlockUpgC block, Class<? extends ItemBlock> itemBlock) {

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
