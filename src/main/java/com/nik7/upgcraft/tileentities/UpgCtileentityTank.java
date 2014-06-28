package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.UpgCTank;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.TileFluidHandler;

public class UpgCtileentityTank extends TileFluidHandler {


    public UpgCtileentityTank(int capacity) {
        super();
        setTank(new UpgCTank(capacity));
    }

    public UpgCTank getTank() {
        return (UpgCTank) tank;
    }

    public void setTank(FluidTank tank) {
        this.tank = tank;
    }


}
