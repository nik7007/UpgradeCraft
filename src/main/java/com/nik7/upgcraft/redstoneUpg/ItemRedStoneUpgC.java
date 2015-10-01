package com.nik7.upgcraft.redstoneUpg;


import com.nik7.upgcraft.item.ItemUpgC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class ItemRedStoneUpgC extends ItemUpgC implements RedStoneUpg {

    protected RedLogicAction action;
    protected IORedSignal[] ioRedSignals;
    protected boolean[] status = new boolean[4];


    public ItemRedStoneUpgC(RedLogicAction action, IORedSignal[] ioRedSignals) {
        super();
        this.action = action;
        this.ioRedSignals = ioRedSignals;
    }

    @Override
    public boolean[] acting(boolean[] inputs) {
        return inputs;
    }

    @Override
    public boolean[] acting(boolean[] inputs, int metadata) {

        return acting(inputs);
    }

    private void WriteIONBT(IORedSignal[] ioRedSignals, NBTTagCompound tag) {
        NBTTagList ios = new NBTTagList();

        int length = ioRedSignals.length;

        for (int i = 0; i < length; i++) {
            NBTTagCompound io = new NBTTagCompound();
            io.setInteger("index", i);
            ioRedSignals[i].WriteToNBT(io);
            ios.appendTag(io);
        }

        tag.setTag("ioRedSignals", ios);
        tag.setInteger("ioRedSignalsL", length);

    }

    private IORedSignal[] ReadIONBT(NBTTagCompound tag) {

        int length = tag.getInteger("ioRedSignalsL");
        NBTTagList ios = tag.getTagList("ioRedSignals", length);
        IORedSignal[] ioRedSignal = new IORedSignal[length];

        for (int i = 0; i < ios.tagCount(); i++) {
            NBTTagCompound io = ios.getCompoundTagAt(i);
            int index = io.getInteger("index");
            if (index >= 0 && index < length) {
                ioRedSignal[index] = IORedSignal.ReadFromNBT(io);
            }
        }

        return ioRedSignal;
    }

    private void WriteStatusNBT(boolean[] status, NBTTagCompound tag) {

        NBTTagList statusL = new NBTTagList();


        for (int i = 0; i < status.length; i++) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger("index", i);
            tagCompound.setBoolean("status", status[i]);
            statusL.appendTag(tagCompound);
        }

        tag.setInteger("StatusL", status.length);
        tag.setTag("Status", statusL);

    }

    private boolean[] ReadStatusNBT(NBTTagCompound tag) {

        int length = tag.getInteger("StatusL");
        NBTTagList statusL = tag.getTagList("Status", length);
        boolean[] status = new boolean[length];

        for (int i = 0; i < statusL.tagCount(); i++) {

            NBTTagCompound stat = statusL.getCompoundTagAt(i);
            int index = stat.getInteger("index");
            if (index >= 0 && index < length) {
                status[index] = stat.getBoolean("status");

            }

        }

        return status;
    }

    @Override
    public void WriteNBT(NBTTagCompound tag) {

        action.WriteToNBT(tag);
        WriteIONBT(ioRedSignals, tag);
        WriteStatusNBT(status, tag);
    }

    @Override
    public void ReadNBT(NBTTagCompound tag) {

        action = RedLogicAction.ReadFromNBT(tag);
        ioRedSignals = ReadIONBT(tag);
        status = ReadStatusNBT(tag);

    }

    @Override
    public final RedLogicAction getLogicAction() {
        return action;
    }

    @Override
    public final boolean isCustomAction() {
        return action.equals(RedLogicAction.CUSTOM);
    }


    @Override
    public IORedSignal[] getIOConfiguration(int metadata) {
        IORedSignal[] result = new IORedSignal[4];

        for (int i = 0; i < 4; i++) {
            result[i] = IORedSignal.getIOIoRedSignal((short) ((ioRedSignals[i].getStatusID() + metadata) % 4));
        }

        return result;
    }

    @Override
    public RedStoneUpg getInnerLogicFunction() {
        return null;
    }
}
