package com.nik7.upgcraft.item;


import com.nik7.upgcraft.redstoneUpg.IORedSignal;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.reference.Texture;

public class ItemUpgCRedStoneORLogicComponent extends ItemUpgCRedStoneANDLogicComponent {

    public ItemUpgCRedStoneORLogicComponent() {
        super();
        this.setUnlocalizedName(Names.Items.OR_COMPONENT);
        this.setTextureName(Texture.Items.OR_COMPONENT);
    }

    @Override
    public boolean[] acting(boolean[] inputs, int metadata) {
        boolean[] status = inputs.clone();
        boolean outPut = false;
        IORedSignal[] ioRedSignal = this.getIOConfiguration(metadata);
        int i;
        for (i = 0; i < 4; i++) {
            if (ioRedSignal[i].equals(IORedSignal.OUTPUT))
                break;
        }

        for (int c = 1; c <= 3; c++) {
            int index = (c + i) % 4;
            if ((outPut = inputs[index]))
                break;
        }

        status[i] = outPut;


        return status;
    }
}
