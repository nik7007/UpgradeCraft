package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.UpgCTank;
import com.nik7.upgcraft.reference.Capacity;

public class UpgCtileentityTankSmall extends UpgCtileentityTank {

    public UpgCtileentityTankSmall() {

        super();
        setTank(new UpgCTank(Capacity.SMALL_WOODEN_TANK));


    }

}