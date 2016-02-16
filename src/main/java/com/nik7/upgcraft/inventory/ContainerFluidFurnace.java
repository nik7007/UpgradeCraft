package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerFluidFurnace extends ContainerUpgC {

    private static final int INPUT = UpgCtileentityFluidFurnace.INPUT;
    private static final int OUTPUT = UpgCtileentityFluidFurnace.OUTPUT;

    public ContainerFluidFurnace(InventoryPlayer playerInventory, UpgCtileentityFluidFurnace fluidFurnace) {

        super(fluidFurnace);

        this.addSlotToContainer(new Slot(fluidFurnace, INPUT, 56, 17));
        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, fluidFurnace, OUTPUT, 116, 35));

        addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex == OUTPUT) {
                if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotIndex != INPUT) {
                if (FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (slotIndex >= 2 && slotIndex < 29) {
                    if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
                        return null;
                    }
                } else if (slotIndex >= 29 && slotIndex < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
}
