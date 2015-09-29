package com.nik7.upgcraft.redstoneUpg;

import net.minecraft.nbt.NBTTagCompound;

public interface RedStoneUpg {
    /**
     * @param inputs 4x4 matrix, each cell is a direction input: 0 0 -> 0 side; 0 1 -> 1 side; 1 0 -> 2 side; 1 1 3 side
     *               <br/>
     * @return 4x4 matrix: the output at given input and current tick
     */
    Short[] acting(Short[] inputs);

    /**
     * @return get number of tick for the competition of the operation (acting method)
     */
    int getDelay();

    /**
     * Save data
     *
     * @param tag the TagCompound where to save;
     */
    void WriteNBT(NBTTagCompound tag);

    /**
     * Loading data from
     *
     * @param tag the TagCompound where loading data from
     */
    void ReadNBT(NBTTagCompound tag);

    RedLogicAction getLogicAction();

    boolean isCustomAction();



}
