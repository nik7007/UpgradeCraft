package com.nik7.upgcraft.item;


import com.nik7.upgcraft.redstoneUpg.IORedSignal;
import com.nik7.upgcraft.redstoneUpg.ItemRedStoneUpgC;
import com.nik7.upgcraft.redstoneUpg.RedLogicAction;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;

public class ItemUpgCRedStoneANDLogicComponent extends ItemRedStoneUpgC {


    public ItemUpgCRedStoneANDLogicComponent() {
        super(RedLogicAction.AND, new IORedSignal[]{IORedSignal.OUTPUT, IORedSignal.INPUT, IORedSignal.INPUT, IORedSignal.INPUT});
        this.setUnlocalizedName(Names.Items.AND_COMPONENT);
        this.setTextureName(Texture.Items.AND_COMPONENT);
    }

    @Override
    public boolean[] acting(boolean[] inputs, int metadata) {

        this.status = inputs.clone();
        boolean outPut = false;
        IORedSignal[] ioRedSignal = this.getIOConfiguration(metadata);
        int i;
        for (i = 0; i < 4; i++) {
            if (ioRedSignal[i].equals(IORedSignal.OUTPUT))
                break;
        }

        for (int c = 1; c <= 3; c++) {
            int index = (c + i) % 4;
            if (!(outPut = inputs[index]))
                break;
        }

        status[i] = outPut;


        return status;
    }

    @Override
    public int getDelay() {
        return 1;
    }

}
