package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

import static com.nik7.upgcraft.redstone.RedstoneUpgC.INVALID_ID;

public abstract class RedstoneElement implements IRedstoneElement {

    private int ID;
    protected final ExpressionType type;
    protected IConnection[] connections;
    private final int tickTocomplete;
    private int tick = 0;
    protected boolean output;

    private final short[] inputsPort;
    private final short outputPort;

    protected RedstoneElement(ExpressionType type, int tickTocomplete) {
        short[] inputsPort;
        short outputPort;

        if (type == ExpressionType.NOT) {
            inputsPort = new short[]{0};
            outputPort = 1;
        } else {
            inputsPort = new short[]{0, 1, 2};
            outputPort = 3;
        }
        this.type = type;
        this.inputsPort = inputsPort;
        this.outputPort = outputPort;
        this.connections = new Connection[inputsPort.length + 1];
        this.tickTocomplete = tickTocomplete + 1;
        this.ID = INVALID_ID;
    }

    protected RedstoneElement(ExpressionType type, int tickTocomplete, short[] inputsPort, short outputPort) {
        this.type = type;
        this.inputsPort = inputsPort;
        this.outputPort = outputPort;
        this.connections = new Connection[inputsPort.length + 1];
        this.tickTocomplete = tickTocomplete + 1;
        this.ID = INVALID_ID;
    }


    public RedstoneElement(ExpressionType type, int tickTocomplete, short[] inputsPort, short outputPort, IConnection[] connections) {
        this(type, tickTocomplete, inputsPort, outputPort);
        System.arraycopy(connections, 0, this.connections, 0, 4);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        if (this.ID == INVALID_ID)
            initID();
        tag.setInteger("ID", this.ID);

        for (int i = 0; i < connections.length; i++) {
            NBTTagCompound cNBT = new NBTTagCompound();
            connections[i].writeToNBT(cNBT);
            tag.setTag("connection" + i, cNBT);
        }

        tag.setInteger("tick", this.tick);
        tag.setBoolean("output", this.output);

    }


    @Override
    public IRedstoneElement readFomNBT(NBTTagCompound tag) {
        this.ID = tag.getInteger("ID");
        RedstoneUpgC.addRedstoneElemet(this);

        for (int i = 0; i < connections.length; i++) {
            NBTTagCompound cNBT = tag.getCompoundTag("connection" + i);
            connections[i].readFomNBT(cNBT);
        }

        this.tick = tag.getInteger("tick");
        this.output = tag.getBoolean("output");

        return this;
    }

    @Override
    public void initID() {
        if (this.ID == INVALID_ID) {
            this.ID = RedstoneUpgC.globalID;
            RedstoneUpgC.globalID++;
            RedstoneUpgC.addRedstoneElemet(this);
        }
    }

    protected final void increaseTick() {
        tick++;
        tick %= tickTocomplete;
    }

    protected int getCurrentTik() {
        return this.tick;
    }

    @Override
    public int getID() {
        return this.ID;
    }


    public ExpressionType getBooleanType() {
        return this.type;
    }


    @Override
    public boolean getOutput() {
        return this.output;
    }

    @Override
    public void setConnection(IConnection connection, short port) {
        if (port >= 0 && port < inputsPort.length + 1)
            this.connections[port] = connection;
    }

    @Override
    public IConnection[] getConnections() {
        return connections;
    }


    @Override
    public short[] getInputsPort() {

        return inputsPort;
    }

    @Override
    public short getOutputPort() {
        return outputPort;
    }

    protected abstract void myExec();


    @Override
    public void exec() {
        if (getCurrentTik() == 0)
            setOutputToConnection();
        myExec();
        //this.connections[getOutputPort()].setInputValue(this, getOutputPort(), getOutput());
        increaseTick();

    }

    protected void setOutputToConnection() {
        this.connections[getOutputPort()].setInputValue(this, getOutputPort(), getOutput());

    }
}
