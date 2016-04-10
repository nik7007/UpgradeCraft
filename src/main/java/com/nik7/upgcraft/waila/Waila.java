package com.nik7.upgcraft.waila;


import com.nik7.upgcraft.block.BlockUpgCBasicFluidHopper;
import com.nik7.upgcraft.block.BlockUpgCContainerOrientable;
import com.nik7.upgcraft.block.BlockUpgCFluidTank;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila {
    public static void callbackRegister(IWailaRegistrar registrar) {

        registrar.registerBodyProvider(new WailaFluidTankHandler(), BlockUpgCFluidTank.class);
        registrar.registerBodyProvider(new WailaFluidTankHandler(), BlockUpgCBasicFluidHopper.class);
        // registrar.registerNBTProvider(new WailaFluidTankHandler(), BlockUpgCTank.class);
        registrar.registerBodyProvider(new WailaInventoryFluidHandler(), BlockUpgCContainerOrientable.class);
        registrar.registerNBTProvider(new WailaInventoryFluidHandler(), BlockUpgCContainerOrientable.class);
    }
}
