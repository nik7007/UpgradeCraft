package com.nik7.upgcraft.proxy;

import com.nik7.upgcraft.handler.PlayerEventHandler;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tileentities.*;
import net.minecraftforge.fml.common.registry.GameRegistry;


public abstract class CommonProxy implements IProxy {

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(UpgCtileentityWoodenFluidTank.class, Reference.RESOURCE_PREFIX + "UpgCtileentityWoodenFluidTank");
        GameRegistry.registerTileEntity(UpgCtilientityBasicFluidHopper.class, Reference.RESOURCE_PREFIX + "UpgCtilientityBasicFluidHopper");
        GameRegistry.registerTileEntity(UpgCtileentityFluidFurnace.class, Reference.RESOURCE_PREFIX + "UpgCtileentityFluidFurnace");
        GameRegistry.registerTileEntity(UpgCtileentityFluidInfuser.class, Reference.RESOURCE_PREFIX + "UpgCtileentityFluidInfuser");
        GameRegistry.registerTileEntity(UpgCtileentityClayFluidTank.class, Reference.RESOURCE_PREFIX + "UpgCtileentityClayFluidTank");
    }

    public void registerEventHandlers() {

        PlayerEventHandler.init();

    }
}
