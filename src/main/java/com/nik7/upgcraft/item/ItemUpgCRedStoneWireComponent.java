package com.nik7.upgcraft.item;

import com.nik7.upgcraft.redstoneUpg.IORedSignal;
import com.nik7.upgcraft.redstoneUpg.ItemRedStoneUpgC;
import com.nik7.upgcraft.redstoneUpg.RedLogicAction;

import java.util.LinkedList;
import java.util.List;

public class ItemUpgCRedStoneWireComponent extends ItemRedStoneUpgC {

    public ItemUpgCRedStoneWireComponent() {
        super(RedLogicAction.CABLE, new IORedSignal[]{IORedSignal.IO, IORedSignal.IO, IORedSignal.IO, IORedSignal.IO});
    }

    @Override
    public boolean[] acting(boolean[] inputs, int metadata) {

        IORedSignal[] ioRedSignal = getIOConfiguration(metadata);
        boolean[] status = new boolean[4];
        List<Short> index = new LinkedList<Short>();

        for (short i = 0; i < 4; i++) {
            if (ioRedSignal[i].equals(IORedSignal.IO))
                index.add(i);
        }

        for (boolean b : inputs) {
            if (b) {
                for (short i : index) {
                    status[i] = true;
                }
                break;
            }
        }

        return status;
    }

    @Override
    public IORedSignal[] getIOConfiguration(int metadata) {

        IORedSignal[] ioRedSignals = {IORedSignal.CLOSE, IORedSignal.CLOSE, IORedSignal.CLOSE, IORedSignal.CLOSE};

        if ((metadata & 1) == 1)
            ioRedSignals[0] = IORedSignal.IO;
        if ((metadata & 2) == 1)
            ioRedSignals[1] = IORedSignal.IO;
        if ((metadata & 4) == 1)
            ioRedSignals[2] = IORedSignal.IO;
        if ((metadata & 8) == 1)
            ioRedSignals[3] = IORedSignal.IO;

        return ioRedSignals;
    }

    @Override
    public int getDelay() {
        return 0;
    }
}
