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

public class ContainerFluidFurnace extends ContainerUpgC {

    private final UpgCtileentityFluidFurnace fluidFurnace;

    private int lastBurningTime;
    private int lastProgress;
    private int lastFluidLevel;


    public ContainerFluidFurnace(InventoryPlayer playerInventory, UpgCtileentityFluidFurnace fluidFurnace) {
        this.fluidFurnace = fluidFurnace;

        this.addSlotToContainer(new Slot(fluidFurnace, UpgCtileentityFluidFurnace.INPUT, 56, 17));
        this.addSlotToContainer(new SlotFurnace(playerInventory.player, fluidFurnace, UpgCtileentityFluidFurnace.OUTPUT, 116, 35));

        addPlayerSlots(playerInventory, 8, 84);
    }

    public void addCraftingToCrafters(ICrafting iCrafting) {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.fluidFurnace.burningTime);
        iCrafting.sendProgressBarUpdate(this, 1, this.fluidFurnace.progress);
        iCrafting.sendProgressBarUpdate(this, 2, this.fluidFurnace.fluidLevel);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.lastBurningTime != this.fluidFurnace.burningTime) {
                icrafting.sendProgressBarUpdate(this, 0, this.fluidFurnace.burningTime);

            }  if (this.lastProgress != this.fluidFurnace.progress) {
                icrafting.sendProgressBarUpdate(this, 1, this.fluidFurnace.progress);
            }

            if(this.lastFluidLevel !=this.fluidFurnace.fluidLevel)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.fluidFurnace.fluidLevel);
            }

        }

        this.lastBurningTime = this.fluidFurnace.burningTime;
        this.lastProgress = this.fluidFurnace.progress;
        this.lastFluidLevel = this.fluidFurnace.fluidLevel;

    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        if (index == 0) {
            this.fluidFurnace.burningTime = value;
        }
        if (index == 1) {
            this.fluidFurnace.progress = value;
        }

        if(index == 2)
        {
            this.fluidFurnace.fluidLevel = value;
        }

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return fluidFurnace.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        return null;
    }
}
