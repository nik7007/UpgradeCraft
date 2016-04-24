package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.handler.AchievementHandler;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.init.ModFluids;
import com.nik7.upgcraft.tileentities.UpgCtileentityActiveLavaMaker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;

import static com.nik7.upgcraft.handler.AchievementHandler.AchievementList.ACTIVE_LAVA;
import static com.nik7.upgcraft.tileentities.UpgCtileentityActiveLavaMaker.*;

public class ContainerActiveLavaMaker extends ContainerUpgC {

    private final UpgCtileentityActiveLavaMaker activeLavaMaker;

    public ContainerActiveLavaMaker(InventoryPlayer playerInventory, UpgCtileentityActiveLavaMaker activeLavaMaker) {
        super(activeLavaMaker);
        this.addSlotToContainer(new Slot(activeLavaMaker, INPUT_TANK_SLOT, 47, 24));
        this.addSlotToContainer(new Slot(activeLavaMaker, WORKING_SLOT, 80, 32));
        this.addSlotToContainer(new Slot(activeLavaMaker, OUTPUT_TANK_SLOT, 114, 40));
        addPlayerSlots(playerInventory, 8, 84);
        this.activeLavaMaker = activeLavaMaker;

        if (activeLavaMaker != null && !playerInventory.player.hasAchievement(ACTIVE_LAVA)) {
            if (activeLavaMaker.getFluid(WORKING_TANK) != null && activeLavaMaker.getFluid(WORKING_TANK).getFluid() == ModFluids.fluidUpgCActiveLava)
                AchievementHandler.craftAchievement(playerInventory.player, new ItemStack(ModBlocks.blockUpgCActiveLavaMaker));
        }

    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < this.activeLavaMaker.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.activeLavaMaker.getSizeInventory(), this.inventorySlots.size(), true)) {
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
                if (!this.mergeItemStack(itemstack1, 3, this.activeLavaMaker.getSizeInventory(), false)) {
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
