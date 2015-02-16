package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.tileentities.UpgCtileentityFluidFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerFluidFurnace extends ContainerUpgC {

    private static final int INPUT = UpgCtileentityFluidFurnace.INPUT;
    private static final int OUTPUT = UpgCtileentityFluidFurnace.OUTPUT;
    private final UpgCtileentityFluidFurnace fluidFurnace;
    private int lastBurningTime;
    private int lastProgress;

    public ContainerFluidFurnace(InventoryPlayer playerInventory, UpgCtileentityFluidFurnace fluidFurnace) {
        this.fluidFurnace = fluidFurnace;

        this.addSlotToContainer(new Slot(fluidFurnace, INPUT, 56, 17));
        this.addSlotToContainer(new SlotFurnace(playerInventory.player, fluidFurnace, OUTPUT, 116, 35));

        addPlayerSlots(playerInventory, 8, 84);
    }

    public void addCraftingToCrafters(ICrafting iCrafting) {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.fluidFurnace.burningTime);
        iCrafting.sendProgressBarUpdate(this, 1, this.fluidFurnace.progress);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (this.lastBurningTime != this.fluidFurnace.burningTime) {
                icrafting.sendProgressBarUpdate(this, 0, this.fluidFurnace.burningTime);

            }
            if (this.lastProgress != this.fluidFurnace.progress) {
                icrafting.sendProgressBarUpdate(this, 1, this.fluidFurnace.progress);
            }

        }

        this.lastBurningTime = this.fluidFurnace.burningTime;
        this.lastProgress = this.fluidFurnace.progress;

    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        if (index == 0) {
            this.fluidFurnace.burningTime = value;
        }
        if (index == 1) {
            this.fluidFurnace.progress = value;
        }

        if (index == 2) {
            this.fluidFurnace.fluidLevel = value;
        }

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return fluidFurnace.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex == OUTPUT) {
                if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotIndex != INPUT) {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
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
