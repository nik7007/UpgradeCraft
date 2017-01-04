package com.nik7.upgcraft.inventory;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerUpgC extends Container {

    protected IInventory inventory;
    protected int[] lastInformation;

    public ContainerUpgC(IInventory inventory) {
        this.inventory = inventory;
        this.lastInformation = new int[inventory.getFieldCount()];
    }

    protected void addPlayerSlots(InventoryPlayer playerInventory, int x, int y) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(playerInventory, i, x + i * 18, y + 58));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        int field;
        for (IContainerListener listener : this.listeners) {
            for (int i = 0; i < this.inventory.getFieldCount(); i++) {
                if (this.lastInformation[i] != (field = this.inventory.getField(i)))
                    listener.sendProgressBarUpdate(this, i, field);
            }

        }

        for (int i = 0; i < inventory.getFieldCount(); i++) {
            lastInformation[i] = inventory.getField(i);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return inventory.isUsableByPlayer(playerIn);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.inventory.setField(id, data);
    }
}
