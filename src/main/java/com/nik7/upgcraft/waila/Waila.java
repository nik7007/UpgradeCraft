package com.nik7.upgcraft.waila;


import com.nik7.upgcraft.block.BlockFluidFurnace;
import com.nik7.upgcraft.block.BlockFluidTank;
import com.nik7.upgcraft.block.BlockFunnel;
import com.nik7.upgcraft.reference.Reference;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(Reference.MOD_ID)
public class Waila implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new WailaFluidTankHandler(), BlockFluidTank.class);
        registrar.registerBodyProvider(new WailaFunnelHandler(), BlockFunnel.class);
        registrar.registerBodyProvider(new WailaFluidMachineHandler(), BlockFluidFurnace.class);
    }

}
