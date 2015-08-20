package com.nik7.upgcraft.inventory;


import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutPut extends Slot {
    public SlotOutPut(IInventory entity, int slotN, int x, int y) {
        super(entity, slotN, x, y);
    }

    public boolean isItemValid(ItemStack itemStack) {

        return false;
    }

}
