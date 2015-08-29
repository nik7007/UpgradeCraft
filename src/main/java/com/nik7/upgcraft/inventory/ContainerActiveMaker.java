package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.tileentities.UpgCtileentityActiveMaker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerActiveMaker extends ContainerUpgC {

    private final UpgCtileentityActiveMaker upgCtileentityActiveMaker;

    public ContainerActiveMaker(InventoryPlayer playerInventory, UpgCtileentityActiveMaker upgCtileentityActiveMaker) {
        this.upgCtileentityActiveMaker = upgCtileentityActiveMaker;

        this.addSlotToContainer(new Slot(upgCtileentityActiveMaker, 0, 47, 24));
        this.addSlotToContainer(new Slot(upgCtileentityActiveMaker, 1, 80, 32));
        this.addSlotToContainer(new Slot(upgCtileentityActiveMaker, 2, 114, 40));

        addPlayerSlots(playerInventory, 8, 84);
    }


    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return upgCtileentityActiveMaker.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        //todo
        return null;
    }
}
