package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import net.minecraft.util.ITickable;

public class TileEntityFunnel extends TileEntityFluidHandler implements ITickable {


    public TileEntityFunnel() {
        super(EnumCapacity.FUNNEL_CAPACITY);
    }

    @Override
    public void update() {

    }


    @Override
    public void syncTileEntity() {
    }

}
