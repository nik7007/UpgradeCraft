package com.nik7.upgcraft.entity.player;


import com.nik7.upgcraft.tank.UpgCEnderFluidTank;

public class CapabilitiesPlayerUpgC implements ICapabilitiesPlayerUpgC {

    private final UpgCEnderFluidTank upgCEnderFluidTank;

    public CapabilitiesPlayerUpgC() {
        this.upgCEnderFluidTank = new UpgCEnderFluidTank();
    }

    @Override
    public UpgCEnderFluidTank getEnderFluidTank() {
        return upgCEnderFluidTank;
    }
}
