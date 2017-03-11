package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.tileentity.TileEntityFluidInfuser;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

import static com.nik7.upgcraft.tileentity.TileEntityFluidInfuser.*;


public class ContainerFluidInfuser extends ContainerUpgC {


    public ContainerFluidInfuser(InventoryPlayer playerInventory, TileEntityFluidInfuser inventory) {
        super(inventory);

        this.addSlotToContainer(new Slot(inventory, MELT, 52, 20));
        this.addSlotToContainer(new Slot(inventory, INFUSE, 82, 20));

        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, inventory, OUTPUT, 142, 38));

        addPlayerSlots(playerInventory, 8, 84);
    }
}
