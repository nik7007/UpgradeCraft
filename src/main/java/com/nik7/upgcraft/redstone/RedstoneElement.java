package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

import static com.nik7.upgcraft.redstone.RedstoneUpgC.INVALID_ID;

public abstract class RedstoneElement implements IRedstoneElement {

    private int ID;
    protected final ExpressionType type;
    protected final Connection[] connections;
    private final int tickTocomplete;
    private int tick = 0;
    protected short position;
    protected boolean output;

    public RedstoneElement(ExpressionType type, Connection[] connections, int tickTocomplete) {
        this.type = type;
        this.connections = connections;
        this.tickTocomplete = tickTocomplete;
        this.ID = INVALID_ID;
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
        tag.setShort("position", this.position);
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
        this.position = tag.getShort("position");
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

    @Override
    public short getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(short position) {
        this.position = position;
    }

    public ExpressionType getBooleanType() {
        return this.type;
    }


    @Override
    public boolean getOutput() {
        return this.output;
    }

    @Override
    public Connection[] getConnections() {
        return connections;
    }

    protected abstract void myExec();

    @Override
    public void setInput(int ID, boolean value) {
        for (Connection c : connections) {
            if (c.getConnectionType() == Connection.ConnectionType.INPUT) {
                for (IRedstoneElement re : c.getConnection()) {
                    if (re.getID() == ID)
                        c.setValue(value);
                }
            }
        }

    }

    @Override
    public void exec() {
        if (getCurrentTik() == 0)
            setOutputToConnection();
        myExec();
        for (Connection c : connections) {
            if (c.getConnectionType() == Connection.ConnectionType.OUTPUT)
                for (IRedstoneElement rE : c.getConnection()) {
                    if (getCurrentTik() == 0) {
                        rE.setInput(ID, getOutput());
                    }
                    //rE.exec();
                }
        }
        increaseTick();

    }

    protected void setOutputToConnection() {
        for (Connection c : connections) {
            if (c.getConnectionType() == Connection.ConnectionType.OUTPUT) {
                c.setValue(output);
                break;
            }
        }

    }
}
