package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.INBTTagProvider;
import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedList;
import java.util.List;

public class Connection implements INBTTagProvider<Connection> {

    private final ConnectionType connectionType;
    private boolean value;
    private List<IRedstoneElement> connectedToMe = new LinkedList<>();

    public Connection(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setBoolean("value", value);
        int[] IDs = new int[connectedToMe.size()];
        for (int i = 0; i < connectedToMe.size(); i++) {
            IDs[i] = connectedToMe.get(i).getID();
        }
        tag.setIntArray("connectedToMe", IDs);

    }

    @Override
    public Connection readFomNBT(NBTTagCompound tag) {
        this.value = tag.getBoolean("value");
        int[] IDs = tag.getIntArray("connectedToMe");
        for (int id : IDs) {
            addConnection(RedstoneUpgC.getRedstoneElemet(id));
        }
        return this;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }


    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean addConnection(IRedstoneElement redstoneElement) {
        return this.connectedToMe.add(redstoneElement);
    }

    public boolean removeConnection(IRedstoneElement redstoneElement) {
        return this.connectedToMe.remove(redstoneElement);
    }

    public List<IRedstoneElement> getConnection() {
        return this.connectedToMe;
    }


    public enum ConnectionType {

        INPUT,

        OUTPUT,

        CLOSE,

        IO

    }
}
