package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentities.UpgCTileEntityTank;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {

    public void registerTileEntities() {

        GameRegistry.registerTileEntity(UpgCTileEntityTank.class, Names.TileEntity.UPGC_TILE_ENTITY_TANK);
    }
}
