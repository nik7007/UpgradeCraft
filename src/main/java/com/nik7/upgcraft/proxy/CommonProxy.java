package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.handler.ConfigurationHandler;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.init.Recipes;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentity.TileEntityFunnel;
import com.nik7.upgcraft.tileentity.TileEntityWoodenFluidTank;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {


    @Override
    public void preInit(FMLPreInitializationEvent event) {

        //read config
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        //init blocks
        ModBlocks.init();
        //init Items
        //register

    }

    @Override
    public void init(FMLInitializationEvent event) {

        //recipes
        Recipes.init();
        //tileEntities
        GameRegistry.registerTileEntity(TileEntityWoodenFluidTank.class, Reference.RESOURCE_PREFIX + "TileEntityWoodenFluidTank");
        GameRegistry.registerTileEntity(TileEntityFunnel.class, Reference.RESOURCE_PREFIX + "TileEntityFunnel");
        //interModComm

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
