package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.UpgCraftFluidHopper;
import com.nik7.upgcraft.tileentities.UpgCtileentityTankSmall;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {

    public void registerTileEntities() {

        GameRegistry.registerTileEntity(UpgCtileentityTankSmall.class, Names.TileEntity.UPGC_TILE_ENTITY_TANK + "Small");
        GameRegistry.registerTileEntity(UpgCraftFluidHopper.class, Names.TileEntity.UPGC_TILE_ENTITY_FLUID_HOPPER);
    }
}
