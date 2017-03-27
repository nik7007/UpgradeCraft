package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.UpgradeCraft;
import com.nik7.upgcraft.handler.ConfigurationHandler;
import com.nik7.upgcraft.handler.GuiHandler;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.init.ModItems;
import com.nik7.upgcraft.init.Recipes;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentity.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {


    @Override
    public void preInit(FMLPreInitializationEvent event) {

        //read config
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        //init blocks
        ModBlocks.init();
        //init Items
        ModItems.init();
        //register

    }

    @Override
    public void init(FMLInitializationEvent event) {

        //recipes
        Recipes.init();
        //tileEntities
        GameRegistry.registerTileEntity(TileEntityWoodenFluidTank.class, Reference.RESOURCE_PREFIX + "TileEntityWoodenFluidTank");
        GameRegistry.registerTileEntity(TileEntityFunnel.class, Reference.RESOURCE_PREFIX + "TileEntityFunnel");
        GameRegistry.registerTileEntity(TileEntityBasicFunnel.class, Reference.RESOURCE_PREFIX + "TileEntityBasicFunnel");
        GameRegistry.registerTileEntity(TileEntityFluidFurnace.class, Reference.RESOURCE_PREFIX + "TileEntityFluidFurnace");
        GameRegistry.registerTileEntity(TileEntityFluidInfuser.class, Reference.RESOURCE_PREFIX + "TileEntityFluidInfuser");
        GameRegistry.registerTileEntity(TileEntityClayFluidTank.class, Reference.RESOURCE_PREFIX + "TileEntityClayFluidTank");
        GameRegistry.registerTileEntity(TileEntityHardenedClayFluidTank.class, Reference.RESOURCE_PREFIX + "TileEntityHardenedClayFluidTank");
        //Network
        NetworkRegistry.INSTANCE.registerGuiHandler(UpgradeCraft.instance, new GuiHandler());
        //interModComm

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
