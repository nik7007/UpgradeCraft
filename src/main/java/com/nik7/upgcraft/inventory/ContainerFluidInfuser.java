package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.registry.FluidInfuserRegister;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFluidInfuser extends ContainerUpgC {

    private static final int MELT = UpgCtileentityFluidInfuser.MELT;
    private static final int INFUSE = UpgCtileentityFluidInfuser.INFUSE;
    private static final int OUTPUT = UpgCtileentityFluidInfuser.OUTPUT;
    private final UpgCtileentityFluidInfuser fluidInfuser;
    private int lastMeltingTime;
    private int lastMeltingTicks;
    private int lastInfusingTime;
    private int lastInfusingTicks;

    public ContainerFluidInfuser(InventoryPlayer playerInventory, UpgCtileentityFluidInfuser fluidInfuser) {

        this.fluidInfuser = fluidInfuser;

        int dX = 20;

        this.addSlotToContainer(new Slot(fluidInfuser, MELT, 26 + dX, 17));
        this.addSlotToContainer(new Slot(fluidInfuser, INFUSE, 56 + dX, 17));
        this.addSlotToContainer(new SlotInfuser(playerInventory.player, fluidInfuser, OUTPUT, 116 + dX, 35));

        addPlayerSlots(playerInventory, 8, 84);
    }

    public void addCraftingToCrafters(ICrafting iCrafting) {
        super.addCraftingToCrafters(iCrafting);

        iCrafting.sendProgressBarUpdate(this, 0, this.fluidInfuser.tickMelting);
        iCrafting.sendProgressBarUpdate(this, 1, this.fluidInfuser.properties[UpgCtileentityFluidInfuser.TICK_FOR_MELT]);

        iCrafting.sendProgressBarUpdate(this, 2, this.fluidInfuser.tickInfusing);
        iCrafting.sendProgressBarUpdate(this, 3, this.fluidInfuser.properties[UpgCtileentityFluidInfuser.TICK_FOR_INFUSE]);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (this.lastMeltingTime != this.fluidInfuser.tickMelting) {
                icrafting.sendProgressBarUpdate(this, 0, this.fluidInfuser.tickMelting);
            }

            if (this.lastMeltingTicks != this.fluidInfuser.properties[UpgCtileentityFluidInfuser.TICK_FOR_MELT]) {
                icrafting.sendProgressBarUpdate(this, 1, this.fluidInfuser.properties[UpgCtileentityFluidInfuser.TICK_FOR_MELT]);
            }

            if (this.lastInfusingTime != this.fluidInfuser.tickInfusing) {
                icrafting.sendProgressBarUpdate(this, 2, this.fluidInfuser.tickInfusing);
            }

            if (this.lastInfusingTicks != this.fluidInfuser.properties[UpgCtileentityFluidInfuser.TICK_FOR_INFUSE]) {
                icrafting.sendProgressBarUpdate(this, 3, this.fluidInfuser.properties[UpgCtileentityFluidInfuser.TICK_FOR_INFUSE]);
            }
        }

        this.lastMeltingTime = this.fluidInfuser.tickMelting;
        this.lastMeltingTicks = this.fluidInfuser.properties[UpgCtileentityFluidInfuser.TICK_FOR_MELT];
        this.lastInfusingTime = this.fluidInfuser.tickInfusing;
        this.lastInfusingTicks = this.fluidInfuser.properties[UpgCtileentityFluidInfuser.TICK_FOR_INFUSE];
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        switch (index) {
            case 0:
                this.fluidInfuser.tickMelting = value;
                break;
            case 1:
                this.fluidInfuser.properties[UpgCtileentityFluidInfuser.TICK_FOR_MELT] = value;
                break;
            case 2:
                this.fluidInfuser.tickInfusing = value;
                break;
            case 3:
                this.fluidInfuser.properties[UpgCtileentityFluidInfuser.TICK_FOR_INFUSE] = value;
                break;
        }

    }


    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return fluidInfuser.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex == OUTPUT) {
                if (!this.mergeItemStack(itemstack1,3, 39, true)) {
                    return null;
                }

            } else if (slotIndex != INFUSE && slotIndex != MELT) {
                if (FluidInfuserRegister.canBeMelted(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (FluidInfuserRegister.canBeInfused(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                } else if (slotIndex >= 3 && slotIndex < 30) {

                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return null;
                    }
                } else if (slotIndex >= 30 && slotIndex < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return null;
                }

            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
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
