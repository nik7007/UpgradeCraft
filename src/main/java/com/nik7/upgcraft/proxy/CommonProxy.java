package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.init.Recipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class CommonProxy implements IProxy{


    @Override
    public void preInit(FMLPreInitializationEvent event) {

        //read config
        //init blocks
        ModBlocks.init();
        //init Items
        //register

    }

    @Override
    public void init(FMLInitializationEvent event) {

        //recipes
        Recipes.init();
        //tileEntity
        //interModComm

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
