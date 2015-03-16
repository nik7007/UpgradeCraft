package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.*;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {

    public void registerTileEntities() {

        GameRegistry.registerTileEntity(UpgCtileentityWoodenTankSmall.class, Names.TileEntity.UPGC_TILE_ENTITY_TANK + "Small");
        GameRegistry.registerTileEntity(UpgCtilientityFluidHopper.class, "Basic"+Names.TileEntity.UPGC_TILE_ENTITY_FLUID_HOPPER);
        GameRegistry.registerTileEntity(UpgCtileentityFluidFurnace.class, "Basic"+Names.TileEntity.UPGC_TILE_ENTITY_FLUID_FURNACE);
        GameRegistry.registerTileEntity(UpgCtileentityFluidInfuser.class, "Basic"+Names.TileEntity.UPG_TILE_ENTITY_FLUID_INFUSER);
        GameRegistry.registerTileEntity(UpgCtileentityTankClay.class, Names.TileEntity.UPGC_TILE_ENTITY_TANK + "Clay");
    }
}
