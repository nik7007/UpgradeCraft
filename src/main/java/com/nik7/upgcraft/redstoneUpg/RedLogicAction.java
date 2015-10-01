package com.nik7.upgcraft.redstoneUpg;


import net.minecraft.nbt.NBTTagCompound;

public enum RedLogicAction {

    AND,
    OR,
    NOT,
    DELAY,
    CABLE,
    CUSTOM;

    public void WriteToNBT(NBTTagCompound tag) {

        NBTTagCompound action = new NBTTagCompound();
        int ordinal = this.ordinal();

        action.setInteger("action", ordinal);
        tag.setTag("LogicAction", action);

    }

    public static RedLogicAction ReadFromNBT(NBTTagCompound tag) {

        NBTTagCompound action = tag.getCompoundTag("LogicAction");
        int ordinal = action.getInteger("action");

        for (RedLogicAction redLogicAction : RedLogicAction.values()) {
            if (redLogicAction.ordinal() == ordinal)
                return redLogicAction;
        }

        return CUSTOM;

    }


}
