package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.registry.ThermoSmeltingRegister;
import com.nik7.upgcraft.tileentities.UpgCtileentityThermoFluidFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.IFluidContainerItem;

import static com.nik7.upgcraft.tileentities.UpgCtileentityThermoFluidFurnace.*;

public class ContainerThermoFluidFurnace extends ContainerUpgC {
    public ContainerThermoFluidFurnace(InventoryPlayer playerInventory, UpgCtileentityThermoFluidFurnace thermoFluidFurnace) {

        super(thermoFluidFurnace);

        this.addSlotToContainer(new Slot(thermoFluidFurnace, INPUT_SLOT, 55, 20));
        this.addSlotToContainer(new SlotThermoFurnace(playerInventory.player, thermoFluidFurnace, OUTPUT_SLOT, 115, 28));
        this.addSlotToContainer(new SlotOutPut(thermoFluidFurnace, WASTE_SLOT_C, 80, 62));
        this.addSlotToContainer(new SlotOutPut(thermoFluidFurnace, WASTE_SLOT_O, 98, 62));
        this.addSlotToContainer(new Slot(thermoFluidFurnace, OUTPUT_TANK_SLOT, 148, 62));

        addPlayerSlots(playerInventory, 8, 84);


    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();

            if (slotIndex == OUTPUT_SLOT || slotIndex == WASTE_SLOT_C || slotIndex == WASTE_SLOT_O || slotIndex == OUTPUT_TANK_SLOT) {
                if (!this.mergeItemStack(itemStack1, 5, 41, true)) {
                    return null;
                }
                slot.onSlotChange(itemStack1, itemStack);
            } else if (slotIndex != INPUT_SLOT) {
                if (FurnaceRecipes.instance().getSmeltingResult(itemStack1) != null || ThermoSmeltingRegister.getRecipeFromInput(itemStack1) != null) {
                    if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
                        return null;
                    }
                } else if ((itemStack1.getItem() == Items.bucket)) {
                    if (!this.mergeItemStack(itemStack1, 4, 5, false)) {
                        return null;
                    }

                } else if (itemStack1.getItem() instanceof IFluidContainerItem) {

                    IFluidContainerItem item = (IFluidContainerItem) itemStack1.getItem();
                    if (item.getCapacity(itemStack1) > 0) {
                        if (!this.mergeItemStack(itemStack1, 4, 5, false)) {
                            return null;
                        }
                    }

                } else if (slotIndex >= 5 && slotIndex < 32) {
                    if (!this.mergeItemStack(itemStack1, 32, 41, false)) {
                        return null;
                    }
                } else if (slotIndex >= 32 && slotIndex < 41 && !this.mergeItemStack(itemStack1, 5, 32, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemStack1, 5, 41, false)) {
                return null;
            }

            if (itemStack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemStack1.stackSize == itemStack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemStack1);
        }

        return itemStack;
    }
}
