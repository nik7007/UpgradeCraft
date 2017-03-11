package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.tileentity.TileEntityFluidInfuser;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

import static com.nik7.upgcraft.tileentity.TileEntityFluidInfuser.INFUSE;
import static com.nik7.upgcraft.tileentity.TileEntityFluidInfuser.MELT;
import static com.nik7.upgcraft.tileentity.TileEntityFluidInfuser.OUTPUT;


public class ContainerFluidInfuser extends ContainerUpgC {


    public ContainerFluidInfuser(InventoryPlayer playerInventory, TileEntityFluidInfuser inventory) {
        super(inventory);

        this.addSlotToContainer(new Slot(inventory, MELT, 56, 17));
        this.addSlotToContainer(new Slot(inventory, INFUSE, 76, 17));

        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, inventory, OUTPUT, 116, 35));

        addPlayerSlots(playerInventory, 8, 84);
    }
}
