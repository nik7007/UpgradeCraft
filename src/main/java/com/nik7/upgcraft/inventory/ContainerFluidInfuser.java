package com.nik7.upgcraft.inventory;


import com.nik7.upgcraft.tileentities.UpgCtileentityFluidInfuser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerFluidInfuser extends ContainerUpgC {

    private static final int MELT = UpgCtileentityFluidInfuser.MELT;
    private static final int INFUSE = UpgCtileentityFluidInfuser.INFUSE;
    private static final int OUTPUT = UpgCtileentityFluidInfuser.OUTPUT;

    private final UpgCtileentityFluidInfuser fluidInfuser;

    public ContainerFluidInfuser(InventoryPlayer playerInventory, UpgCtileentityFluidInfuser fluidInfuser) {

        this.fluidInfuser = fluidInfuser;

        int dX = 20;

        this.addSlotToContainer(new Slot(fluidInfuser, MELT, 26 + dX, 17));
        this.addSlotToContainer(new Slot(fluidInfuser, INFUSE, 56 + dX, 17));
        this.addSlotToContainer(new SlotFurnace(playerInventory.player, fluidInfuser, OUTPUT, 116 + dX, 35));

        addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return fluidInfuser.isUseableByPlayer(player);
    }

    @Override
    //TODO
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        return null;
    }
}
