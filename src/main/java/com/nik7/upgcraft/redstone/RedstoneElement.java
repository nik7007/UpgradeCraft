package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

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
        this.ID = -1;
        RedstoneUpgC.addRedstoneElemet(this);
    }

    // TODO: 11/05/2016  
    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    // TODO: 11/05/2016  
    @Override
    public IRedstoneElement getFomNBT(NBTTagCompound nbt) {
        return null;
    }

    @Override
    public void initID(){
        this.ID = RedstoneUpgC.globalID;
        RedstoneUpgC.globalID++;
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
                    rE.exec();
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
