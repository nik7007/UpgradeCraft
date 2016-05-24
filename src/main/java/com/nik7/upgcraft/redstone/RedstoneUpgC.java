package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.INBTTagProvider;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public final class RedstoneUpgC implements INBTTagProvider<Integer> {
    public static final int INVALID_ID = Integer.MIN_VALUE;
    static int globalID = 0;
    private static final Map<Integer, IRedstoneLogicElement> REDSTONE_ELEMENT_MAP = new HashMap<>();

    private final static RedstoneUpgC INSTANCE = new RedstoneUpgC();


    private RedstoneUpgC() {

    }

    public static RedstoneUpgC getInstance() {
        return INSTANCE;
    }


    static boolean addRedstoneElemet(IRedstoneLogicElement element) {
        if (!REDSTONE_ELEMENT_MAP.containsKey(element.getID())) {
            REDSTONE_ELEMENT_MAP.put(element.getID(), element);
            return true;
        } else
            return false;
    }

    static IRedstoneLogicElement getRedstoneElemet(int ID) {
        return REDSTONE_ELEMENT_MAP.get(ID);
    }

    static boolean evaluateBooleanExpression(boolean input1, boolean input2, boolean input3, ExpressionType expressionType) {

        switch (expressionType) {
            case AND:
                return input1 && input2 && input3;

            case OR:
                return input1 || input2 || input3;

            case NOT:
                return !input1;

            default:
                throw new RuntimeException("Invalid expression type: '" + expressionType + "'!");
        }

    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("globalID", globalID);

    }

    @Override
    public Integer readFomNBT(NBTTagCompound tag) {
        globalID = tag.getInteger("globalID");
        return globalID;
    }
}
