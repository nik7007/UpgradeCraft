package com.nik7.upgcraft.redstoneUpg;


public enum RedLogicAction {

    AND     ((short) 0),
    OR      ((short) 1),
    DELAY   ((short) 2),
    CUSTOM  ((short) 3);

    private short actionID;

    RedLogicAction(short actionID) {
        this.actionID = actionID;
    }

    short getActionID() {
        return actionID;
    }
}
