package com.nik7.upgcraft.redstone;


import net.minecraft.nbt.NBTTagCompound;

public class RedstoneSimpleElement extends RedstoneElement {


    public RedstoneSimpleElement(ExpressionType type, Connection[] connections, int tickToComplete, boolean defaultOutput) {
        this(type, connections, tickToComplete);
        this.output = defaultOutput;
    }

    public RedstoneSimpleElement(ExpressionType type, Connection[] connections, int tickToComplete) {
        super(type, connections, tickToComplete);
    }

    @Override
    protected void myExec() {

        if (getCurrentTik() == 0 && type.isBooleanExpression()) {
            boolean[] inputs = new boolean[3];
            int i = 0;
            for (Connection c : connections) {
                if (c.getConnectionType() == Connection.ConnectionType.INPUT) {
                    inputs[i] = c.getValue();
                    i++;
                }
            }
            output = RedstoneUpgC.evaluateBooleanExpression(inputs[0], inputs[1], inputs[2], type);
        }

    }

}
