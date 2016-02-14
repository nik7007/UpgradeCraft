package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.registry.FluidInfuserRegister;
import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    public void onCraftGuiOpened(ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.sendAllWindowProperties(this, this.fluidInfuser);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (ICrafting icrafting : this.crafters) {
            if (lastMeltingTime != fluidInfuser.getField(0))
                icrafting.sendProgressBarUpdate(this, 0, fluidInfuser.getField(0));
            if (lastMeltingTicks != fluidInfuser.getField(1))
                icrafting.sendProgressBarUpdate(this, 1, fluidInfuser.getField(1));
            if (lastInfusingTime != fluidInfuser.getField(2))
                icrafting.sendProgressBarUpdate(this, 2, fluidInfuser.getField(2));
            if (lastInfusingTicks != fluidInfuser.getField(3))
                icrafting.sendProgressBarUpdate(this, 3, fluidInfuser.getField(3));


        }

        lastMeltingTime = fluidInfuser.getField(0);
        lastMeltingTicks = fluidInfuser.getField(1);
        lastInfusingTime = fluidInfuser.getField(2);
        lastInfusingTicks = fluidInfuser.getField(3);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return fluidInfuser.isUseableByPlayer(playerIn);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.fluidInfuser.setField(id, data);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
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
