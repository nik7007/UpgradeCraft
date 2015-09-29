package com.nik7.upgcraft.redstoneUpg;


import com.nik7.upgcraft.item.ItemUpgC;

public abstract class ItemRedStoneUpgC extends ItemUpgC implements RedStoneUpg {

    protected RedLogicAction action;
    protected IORedSignal[] ioRedSignals = new IORedSignal[4];


    public ItemRedStoneUpgC(RedLogicAction action, IORedSignal[] ioRedSignals) {
        this.action = action;
        this.ioRedSignals = ioRedSignals;
    }

    @Override
    public final RedLogicAction getLogicAction() {
        return action;
    }

    @Override
    public final boolean isCustomAction() {
        return action.equals(RedLogicAction.CUSTOM);
    }
}
