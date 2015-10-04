package com.nik7.upgcraft.redstoneUpg;


import net.minecraft.nbt.NBTTagCompound;

public enum IORedSignal {

    INPUT((short) 1),
    OUTPUT((short) 0),
    IO((short) 2), //only for wire
    CLOSE((short) 3);

    private short statusID;

    IORedSignal(short statusID) {
        this.statusID = statusID;
    }

    public short getStatusID() {
        return this.statusID;
    }

    public static IORedSignal getIOIoRedSignal(short statusID) {
        switch (statusID) {
            case 1:
                return INPUT;
            case 0:
                return OUTPUT;
            case 2:
                return IO;
            default:
                return CLOSE;
        }
    }

    public void WriteToNBT(NBTTagCompound tag) {

        NBTTagCompound io = new NBTTagCompound();

        io.setShort("ID", this.getStatusID());
        tag.setTag("IORedSignal", io);
    }

    public static IORedSignal ReadFromNBT(NBTTagCompound tag) {

        NBTTagCompound io = tag.getCompoundTag("IORedSignal");

        return getIOIoRedSignal(io.getShort("ID"));

    }

}
