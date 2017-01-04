package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.tileentity.TileEntityFluidFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import javax.annotation.Nonnull;

import static com.nik7.upgcraft.tileentity.TileEntityFluidFurnace.INPUT;
import static com.nik7.upgcraft.tileentity.TileEntityFluidFurnace.OUTPUT;


public class ContainerFluidFurnace extends ContainerUpgC {

    public ContainerFluidFurnace(InventoryPlayer playerInventory, TileEntityFluidFurnace inventory) {
        super(inventory);
        this.addSlotToContainer(new Slot(inventory, INPUT, 56, 17));
        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, inventory, OUTPUT, 116, 35));

        addPlayerSlots(playerInventory, 8, 84);

    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (slotIndex == OUTPUT) {
                if (!this.mergeItemStack(slotStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(slotStack, itemStack);
            } else if (slotIndex != INPUT) {
                if (FurnaceRecipes.instance().getSmeltingResult(slotStack) != ItemStack.EMPTY) {
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 2 && slotIndex < 29) {
                    if (!this.mergeItemStack(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 29 && slotIndex < 38 && !this.mergeItemStack(slotStack, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 2, 38, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemStack;
    }
}
