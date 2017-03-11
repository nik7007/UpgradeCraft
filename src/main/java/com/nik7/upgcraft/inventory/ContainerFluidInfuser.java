package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.registry.FluidInfuserRegister;
import com.nik7.upgcraft.tileentity.TileEntityFluidInfuser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

import static com.nik7.upgcraft.tileentity.TileEntityFluidInfuser.*;


public class ContainerFluidInfuser extends ContainerUpgC {

    private final TileEntityFluidInfuser fluidInfuser;

    public ContainerFluidInfuser(InventoryPlayer playerInventory, TileEntityFluidInfuser inventory) {
        super(inventory);

        this.addSlotToContainer(new Slot(inventory, MELT, 52, 20));
        this.addSlotToContainer(new Slot(inventory, INFUSE, 82, 20));

        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, inventory, OUTPUT, 142, 38));

        addPlayerSlots(playerInventory, 8, 84);

        this.fluidInfuser = inventory;
    }


    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {


        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);


        if (slot != null && slot.getHasStack()) {

            ItemStack slotStack = slot.getStack();

            itemstack = slotStack.copy();


            if (slotIndex == OUTPUT) {

                if (!this.mergeItemStack(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(slotStack, itemstack);

            } else if (slotIndex != INFUSE && slotIndex != MELT) {

                final ItemStack toMelt = this.fluidInfuser.getStackInSlot(MELT).copy();
                final ItemStack toInfuse = this.fluidInfuser.getStackInSlot(INFUSE).copy();
                final FluidStack fluid = this.fluidInfuser.getFluid();

                if (FluidInfuserRegister.isInputCorrect(fluid, slotStack, toInfuse)) {

                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }

                } else if (FluidInfuserRegister.isInputCorrect(fluid, toMelt, slotStack)) {

                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }

                } else if (slotIndex >= 3 && slotIndex < 30) {

                    if (!this.mergeItemStack(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }

                } else if (slotIndex >= 30 && slotIndex < 39 && !this.mergeItemStack(slotStack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }


            } else if (!this.mergeItemStack(slotStack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);

        }

        return itemstack;

    }
}
