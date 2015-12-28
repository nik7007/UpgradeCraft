package com.nik7.upgcraft.item;


import com.nik7.upgcraft.redstoneUpg.IORedSignal;
import com.nik7.upgcraft.redstoneUpg.ItemRedStoneUpgC;
import com.nik7.upgcraft.redstoneUpg.RedLogicAction;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;

public class ItemUpgCRedStoneNOTComponent extends ItemRedStoneUpgC {


    public ItemUpgCRedStoneNOTComponent() {
        super(RedLogicAction.NOT, new IORedSignal[]{IORedSignal.OUTPUT, IORedSignal.CLOSE, IORedSignal.INPUT, IORedSignal.CLOSE});
        this.setUnlocalizedName(Names.Items.NOT_COMPONENT);
        this.setTextureName(Texture.Items.NOT_COMPONENT);

    }

    @Override
    public boolean[] acting(boolean[] inputs, int metadata) {

        IORedSignal[] ioRedSignals = getIOConfiguration(metadata);
        int inputIndex = -1;
        int outPutIndex = -1;

        boolean[] status = {false, false, false, false};

        for (int i = 0; i < ioRedSignals.length; i++) {
            if (ioRedSignals[i].equals(IORedSignal.INPUT))
                inputIndex = i;

            if (ioRedSignals[i].equals(IORedSignal.OUTPUT))
                outPutIndex = i;

            if (inputIndex > 0 && outPutIndex > 0)
                break;

        }

        status[inputIndex] = inputs[inputIndex];
        status[outPutIndex] = !inputs[inputIndex];

        return status;

    }

    @Override
    public IORedSignal[] getIOConfiguration(int metadata) {

        IORedSignal[] ioRedSignals = new IORedSignal[4];

        for (int i = 0; i < ioRedSignals.length; i++)
            ioRedSignals[i] = IORedSignal.CLOSE;

        int input = metadata & 15;
        int output = (metadata >> 4) & 15;

        if (input == output)
            return this.ioRedSignals;

        ioRedSignals[((int) Math.sqrt(input))] = IORedSignal.INPUT;
        ioRedSignals[(int) Math.sqrt(output)] = IORedSignal.OUTPUT;

        return ioRedSignals;


    }

    @Override
    public int getDelay() {
        return 0;
    }
}
