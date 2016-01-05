package com.nik7.upgcraft.tank;


import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

public class UpgCFluidTank extends FluidTank {

    public UpgCFluidTank(int capacity, TileEntity tile) {
        super(capacity);
        this.tile = tile;
    }

}
