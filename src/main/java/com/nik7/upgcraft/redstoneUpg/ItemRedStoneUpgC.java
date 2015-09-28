package com.nik7.upgcraft.redstoneUpg;


import com.nik7.upgcraft.item.ItemUpgC;
import net.minecraft.nbt.NBTTagCompound;

public class ItemRedStoneUpgC extends ItemUpgC implements RedStoneUpg {

    protected String actionName;
    private Short[] side = new Short[4];
    private Short[] status = new Short[4];

    @Override
    public Short[] acting(Short[] inputs, int tick) {
        return new Short[0];
    }

    @Override
    public int getDelay() {
        return 0;
    }

    @Override
    public void WriteNBT(NBTTagCompound tag) {

    }

    @Override
    public void ReadNBT(NBTTagCompound tag) {

    }
}
