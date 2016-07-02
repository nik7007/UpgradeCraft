package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

public final class RedstoneIOConnectionElement extends RedstoneConnectionElement implements IIOConnection {


    private boolean input;


    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("input", this.input);

    }

    @Override
    public IRedstoneConnectionElement readFomNBT(NBTTagCompound tag) {
        this.input = tag.getBoolean("input");
        return super.readFomNBT(tag);
    }

    @Override
    public void setInput(boolean input) {
        this.input = input;
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

        boolean value = this.input;

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
}
