package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

public class RedstoneComplexLogicElement extends RedstoneLogicElement {

    private final RedstoneLogicExecutor logicExecutor;

    public RedstoneComplexLogicElement(RedstoneLogicExecutor logicExecutor) {
        super(ExpressionType.CUSTOM, logicExecutor.getTickToComplete(), logicExecutor.getInputsPort(), logicExecutor.getOutputPort());
        this.logicExecutor = logicExecutor;
    }

    public RedstoneComplexLogicElement(int tickToComplete, short[] inputsPort, short outputPort, short row_dimension, short column_dimension) {
        super(ExpressionType.CUSTOM, tickToComplete, inputsPort, outputPort);
        logicExecutor = new RedstoneLogicExecutor(row_dimension, column_dimension, inputsPort, outputPort);

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagCompound lEtag = new NBTTagCompound();
        this.logicExecutor.writeToNBT(lEtag);
        tag.setTag("logicExecutor", lEtag);
    }

    @Override
    public IRedstoneLogicElement readFomNBT(NBTTagCompound tag) {
        super.readFomNBT(tag);
        this.logicExecutor.readFomNBT(tag.getCompoundTag("logicExecutor"));
        return this;
    }

    public short getRowDimension() {
        return this.logicExecutor.getRowDimension();
    }

    public short getColumnDimension() {
        return this.logicExecutor.getColumnDimension();
    }

    @Override
    protected void myExec() {

        if (getCurrentTik() == 0) {

            short ports[] = getInputsPort();
            RedstoneIOConnectionElement[] ioConnections = this.logicExecutor.getConnections();

            for (short p : ports) {
                boolean vale = connections[p].getOutputValue(this, p);
                ioConnections[p].setInput(vale, p);
            }

            this.logicExecutor.exec();

            output = ioConnections[getOutputPort()].getOutput();

        }
    }
}
