package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public final class RedstoneIOConnectionElement extends RedstoneConnectionElement implements IIOConnection {


    private boolean inputs[] = new boolean[3];


    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < inputs.length; i++) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger("index", i);
            tagCompound.setBoolean("input", this.inputs[i]);
            tagList.appendTag(tagCompound);
        }
        tag.setTag("inputs", tagList);

    }

    @Override
    public IRedstoneConnectionElement readFomNBT(NBTTagCompound tag) {
        NBTTagList tagList = tag.getTagList("inputs", 10);

        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound elementTag = tagList.getCompoundTagAt(i);
            this.inputs[elementTag.getInteger("index")] = elementTag.getBoolean("input");
        }
        return super.readFomNBT(tag);
    }

    @Override
    public void setInput(boolean input, int port) {
        this.inputs[port] = input;
        for (ConnectionElement e : super.inputs) {
            e.setValue(input);

        }
    }

    @Override
    public boolean getOutput() {
        boolean output;

        for (ConnectionElement e : super.outputs) {
            output = e.getValue();
            if (output)
                return true;
        }
        return false;
    }

    @Override
    public void exec() {

        boolean value = this.inputs[0] | this.inputs[1] | this.inputs[2];

        if (super.outputs.isEmpty())
            return;

        if (!super.inputs.isEmpty()) {
            //OR behavior
            for (ConnectionElement e : super.inputs) {
                if (value) {
                    break;
                }
                value = e.getValue();
            }
        }

        for (ConnectionElement cE : super.outputs) {
            cE.setValue(value);
        }
    }

    @Override
    public void reSet() {
        super.reSet();
        for (int i = 0; i < this.inputs.length; i++)
            this.inputs[i] = false;
    }

}
