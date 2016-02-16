package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.tileentities.UpgCtileentityInventoryFluidHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ContainerUpgC extends Container {

    protected final UpgCtileentityInventoryFluidHandler inventoryFluidHandler;
    protected int[] lastInformation;

    public ContainerUpgC(UpgCtileentityInventoryFluidHandler inventoryFluidHandler) {
        this.inventoryFluidHandler = inventoryFluidHandler;
        lastInformation = new int[inventoryFluidHandler.getFieldCount()];

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

    public void onCraftGuiOpened(ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.sendAllWindowProperties(this, this.inventoryFluidHandler);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (ICrafting icrafting : this.crafters) {

            for (int i = 0; i < inventoryFluidHandler.getFieldCount(); i++) {
                if (lastInformation[i] != inventoryFluidHandler.getField(i))
                    icrafting.sendProgressBarUpdate(this, i, inventoryFluidHandler.getField(i));
            }

        }

        for (int i = 0; i < inventoryFluidHandler.getFieldCount(); i++) {
            lastInformation[i] = inventoryFluidHandler.getField(i);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return inventoryFluidHandler.isUseableByPlayer(playerIn);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.inventoryFluidHandler.setField(id, data);
    }

}
