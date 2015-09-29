package com.nik7.upgcraft.redstoneUpg;


public enum IORedSignal {

    INPUT   ((short) 1),
    OUTPUT  ((short) 0),
    CLOSE   ((short) 2);

    private short statusID;

    IORedSignal(short statusID) {
        this.statusID = statusID;
    }

    short getStatusID() {
        return this.statusID;
    }

}
