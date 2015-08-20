package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.tileentities.UpgCtilientityEnderHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEnderHopper extends ContainerUpgC {

    private final UpgCtilientityEnderHopper enderHopper;

    public ContainerEnderHopper(InventoryPlayer playerInventory, UpgCtilientityEnderHopper enderHopper) {
        this.enderHopper = enderHopper;


        this.addSlotToContainer(new Slot(enderHopper, 0, 44 + 2 * 18, 12));
        this.addSlotToContainer(new Slot(enderHopper, 1, 8, 35));

        for (int i = 2; i < 7; i++) {

            this.addSlotToContainer(new Slot(enderHopper, i, 44 + (i - 2) * 18, 35));

        }

        this.addSlotToContainer(new Slot(enderHopper, 7, 44 + 2 * 18, 58));

        addPlayerSlots(playerInventory, 8, 84);

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return enderHopper.isUseableByPlayer(player);
    }

    //TODO
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        return null;
    }
}
