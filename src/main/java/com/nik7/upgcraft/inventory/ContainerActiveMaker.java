package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.tileentities.UpgCtileentityActiveMaker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;

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

        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < this.upgCtileentityActiveMaker.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.upgCtileentityActiveMaker.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (FluidContainerRegistry.isBucket(itemstack1)) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                        return null;
                    }
                }

            } else if (itemstack1.getItem() instanceof IFluidContainerItem) {

                IFluidContainerItem item = (IFluidContainerItem) itemstack1.getItem();
                if (item.getCapacity(itemstack1) > 0) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                        if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                            return null;
                        }
                } else return null;

            } else if (itemstack1.getItem() == Items.blaze_powder || itemstack1.getItem() == Items.blaze_rod) {
                if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    return null;
                }

            } else {
                if (!this.mergeItemStack(itemstack1, 3, this.upgCtileentityActiveMaker.getSizeInventory(), false)) {
                    return null;
                }
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
