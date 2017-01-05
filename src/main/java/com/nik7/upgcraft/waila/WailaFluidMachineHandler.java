package com.nik7.upgcraft.waila;


import com.nik7.upgcraft.fluids.tank.IUpgCFluidTank;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.List;

public class WailaFluidMachineHandler extends WailaFluidTankHandler {

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        TileEntity fluidTank = accessor.getTileEntity();
        IUpgCFluidTank tank = null;


        if (fluidTank.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            tank = (IUpgCFluidTank) fluidTank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH);
        }
        printTankInformation(tank, currenttip);

        return currenttip;
    }

}
