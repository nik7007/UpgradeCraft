package com.nik7.upgcraft.tank;


import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

public class UpgCFluidTank extends FluidTank {

    public UpgCFluidTank(int capacity, TileEntity tile) {
        super(capacity);
        this.tile = tile;
    }

    public UpgCFluidTank(int capacity) {
        super(capacity);
    }

    public boolean isFluidHot() {
        return fluid != null && fluid.getFluid().getTemperature(fluid) > 300 + 273;
    }

    public boolean isFull() {
        return fluid != null && fluid.amount == capacity;
    }

    public void setTileEntity(TileEntity tileEntity) {
        if (tileEntity != null)
            this.tile = tileEntity;
    }

}
