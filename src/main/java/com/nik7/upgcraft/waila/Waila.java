package com.nik7.upgcraft.waila;


import com.nik7.upgcraft.block.BlockUpgCTank;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila {

    public static void callbackRegister(IWailaRegistrar registrar) {

        registrar.registerBodyProvider(new WailaFluidTankHandler(), BlockUpgCTank.class);
        registrar.registerNBTProvider(new WailaFluidTankHandler(), BlockUpgCTank.class);
    }
}
