package com.nik7.upgcraft.redstoneUpg;

import net.minecraft.nbt.NBTTagCompound;

public interface RedStoneUpg {
    /**
     * @param inputs vector, each cell is a direction input: 0 -> 0 side; 1 -> 1 side; 2 -> 2 side; 3 -> 3 side
     *               <br/>
     * @return vector: the output at given input and current tick
     */
    boolean[] acting(boolean[] inputs);

    /**
     * @param inputs   vector,, each cell is a direction input: 0 -> 0 side; 1 -> 1 side; 2 -> 2 side; 3 -> 3 side
     *                 <br/>
     * @param metadata the object metadata
     * @return vector: the output at given inputs and current tick
     */
    boolean[] acting(boolean[] inputs, int metadata);

    /**
     * @return get number of tick for the competition of the operation (acting method)
     */
    int getDelay();

    /**
     * Save data
     *
     * @param tag the TagCompound where to save;
     */
    void WriteNBT(NBTTagCompound tag, boolean[] status);

    /**
     * Loading data from
     *
     * @param tag the TagCompound where loading data from
     */
    boolean[] ReadNBT(NBTTagCompound tag);

    /**
     * @return the logic function
     */
    RedLogicAction getLogicAction();

    /**
     * @return true if it is a custom logic function
     */
    boolean isCustomAction();

    /**
     * @param metadata object metadata, where it is stored the rotation information
     *                 <br/>
     * @return a new IORedSignal base on its new direction
     */
    IORedSignal[] getIOConfiguration(int metadata);

    /**
     * @return return inner logic function, if the object has any
     */
    RedStoneUpg getInnerLogicFunction();


}
