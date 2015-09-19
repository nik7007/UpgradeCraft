package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.registry.TermoSmeltingRegister;
import com.nik7.upgcraft.tileentities.UpgCtileentityTermoFluidFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ContainerTermoFluidFurnace extends ContainerUpgC {

    private final UpgCtileentityTermoFluidFurnace termoFluidFurnace;

    private int lastSmeltingTicks;
    private int lastTotalSmeltingTicks;
    private int lastInternalTemp;

    public ContainerTermoFluidFurnace(InventoryPlayer playerInventory, UpgCtileentityTermoFluidFurnace termoFluidFurnace) {
        this.termoFluidFurnace = termoFluidFurnace;

        this.addSlotToContainer(new Slot(termoFluidFurnace, 0, 55, 20));
        this.addSlotToContainer(new SlotTermoFurnace(playerInventory.player, termoFluidFurnace, 1, 115, 28));
        this.addSlotToContainer(new SlotOutPut(termoFluidFurnace, 2, 80, 62));
        this.addSlotToContainer(new SlotOutPut(termoFluidFurnace, 3, 98, 62));
        this.addSlotToContainer(new Slot(termoFluidFurnace, 4, 148, 62));


        addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public void addCraftingToCrafters(ICrafting iCrafting) {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.termoFluidFurnace.smeltingTicks);
        iCrafting.sendProgressBarUpdate(this, 1, this.termoFluidFurnace.totalSmeltingTicks);
        iCrafting.sendProgressBarUpdate(this, 2, (int) this.termoFluidFurnace.internalTemp);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (this.lastSmeltingTicks != this.termoFluidFurnace.smeltingTicks) {
                icrafting.sendProgressBarUpdate(this, 0, this.termoFluidFurnace.smeltingTicks);

            }
            if (this.lastTotalSmeltingTicks != this.termoFluidFurnace.totalSmeltingTicks) {
                icrafting.sendProgressBarUpdate(this, 1, this.termoFluidFurnace.totalSmeltingTicks);
            }

            if (this.lastInternalTemp != (int) this.termoFluidFurnace.internalTemp) {
                icrafting.sendProgressBarUpdate(this, 2, (int) this.termoFluidFurnace.internalTemp);
            }
        }

        this.lastSmeltingTicks = this.termoFluidFurnace.smeltingTicks;
        this.lastTotalSmeltingTicks = this.termoFluidFurnace.totalSmeltingTicks;
        this.lastInternalTemp = (int) this.termoFluidFurnace.internalTemp;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        switch (index) {
            case 0:
                this.termoFluidFurnace.smeltingTicks = value;
                break;
            case 1:
                this.termoFluidFurnace.totalSmeltingTicks = value;
                break;
            case 2:
                this.termoFluidFurnace.internalTemp = value;
                break;
        }

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return termoFluidFurnace.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex == 1 || slotIndex == 2 || slotIndex == 3 || slotIndex == 4) {
                if (!this.mergeItemStack(itemstack1, 5, 41, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotIndex != 0) {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null || TermoSmeltingRegister.getRecipeFromInput(itemstack1) != null) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if ((itemstack1.getItem() == Items.bucket)) {
                    if (!this.mergeItemStack(itemstack1, 4, 5, false)) {
                        return null;
                    }

                } else if (itemstack1.getItem() instanceof IFluidContainerItem) {

                    IFluidContainerItem item = (IFluidContainerItem) itemstack1.getItem();
                    if (item.getCapacity(itemstack1) > 0) {
                        if (!this.mergeItemStack(itemstack1, 4, 5, false)) {
                            return null;
                        }
                    }

                } else if (slotIndex >= 5 && slotIndex < 32) {
                    if (!this.mergeItemStack(itemstack1, 32, 41, false)) {
                        return null;
                    }
                } else if (slotIndex >= 32 && slotIndex < 41 && !this.mergeItemStack(itemstack1, 5, 32, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 5, 41, false)) {
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
