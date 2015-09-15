package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.handler.BucketHandler;
import com.nik7.upgcraft.handler.PlayerEventHandler;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.*;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {

    public void registerTileEntities() {

        GameRegistry.registerTileEntity(UpgCtileentityWoodenTankSmall.class, Names.TileEntity.UPGC_TILE_ENTITY_TANK + "Small");
        GameRegistry.registerTileEntity(UpgCtilientityBasicFluidHopper.class, Names.TileEntity.UPGC_TILE_ENTITY_FLUID_HOPPER + "Basic");
        GameRegistry.registerTileEntity(UpgCtilientityEnderHopper.class, Names.TileEntity.UPGC_TILE_ENTITY_ENDER_HOPPER);
        GameRegistry.registerTileEntity(UpgCtileentityFluidFurnace.class, Names.TileEntity.UPGC_TILE_ENTITY_FLUID_FURNACE + "Basic");
        GameRegistry.registerTileEntity(UpgCtileentityFluidInfuser.class, Names.TileEntity.UPG_TILE_ENTITY_FLUID_INFUSER + "Basic");
        GameRegistry.registerTileEntity(UpgCtileentityTankClay.class, Names.TileEntity.UPGC_TILE_ENTITY_TANK + "Clay");
        GameRegistry.registerTileEntity(UpgCtileentityEnderTank.class, Names.TileEntity.UPGC_TILE_ENTITY_TANK + "Ender");
        GameRegistry.registerTileEntity(UpgCtileentityTankIron.class, Names.TileEntity.UPGC_TILE_ENTITY_TANK + "Iron");
        GameRegistry.registerTileEntity(UpgCtileentityTermoFluidFurnace.class, Names.TileEntity.UPGC_TILE_ENTITY_FLUID_FURNACE + "Termo");
        GameRegistry.registerTileEntity(UpgCtileentityActiveMaker.class, Names.TileEntity.UPG_TILE_ENTITY_ACTIVE_MAKER);
        GameRegistry.registerTileEntity(UpgCtilientityFluidHopper.class, Names.TileEntity.UPGC_TILE_ENTITY_FLUID_HOPPER);
    }

    public void registerEventHandlers() {

        PlayerEventHandler.init();
        BucketHandler.init();

    }
}
