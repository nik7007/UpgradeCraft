package com.nik7.upgcraft.redstone;


import com.nik7.upgcraft.util.INBTTagProvider;
import net.minecraft.nbt.NBTTagCompound;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

public final class RedstoneUpgC implements INBTTagProvider<Integer> {
    public static final int INVALID_ID = Integer.MIN_VALUE;
    static int globalID = 0;
    private static final ScriptEngineManager sem = new ScriptEngineManager();
    private static final ScriptEngine se = sem.getEngineByName("JavaScript");
    private static final Map<Integer, IRedstoneElement> REDSTONE_ELEMENT_MAP = new HashMap<>();

    private final static RedstoneUpgC INSTANCE = new RedstoneUpgC();


    private RedstoneUpgC() {

    }

    public static RedstoneUpgC getInstance() {
        return INSTANCE;
    }


    static boolean addRedstoneElemet(IRedstoneElement element) {
        if (!REDSTONE_ELEMENT_MAP.containsKey(element.getID())) {
            REDSTONE_ELEMENT_MAP.put(element.getID(), element);
            return true;
        } else
            return false;
    }

    static IRedstoneElement getRedstoneElemet(int ID) {
        return REDSTONE_ELEMENT_MAP.get(ID);
    }

    static boolean evaluateBooleanExpression(boolean input1, boolean input2, boolean input3, ExpressionType expressionType) {
        Boolean result = false;
        String myExpression;
        if (expressionType != ExpressionType.NOT)
            myExpression = String.format("%b" + expressionType + "%b" + expressionType + "%b", input1, input2, input3);
        else myExpression = String.format(expressionType + "%b", input1);

        try {
            result = (Boolean) se.eval(myExpression);
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return result;
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
