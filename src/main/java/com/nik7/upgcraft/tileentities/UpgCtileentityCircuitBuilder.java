package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.redstone.RedStoneLogicBuilder;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tank.UpgCEPFluidTank;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class UpgCtileentityCircuitBuilder extends UpgCtileentityInventoryFluidHandler implements IInteractionObject {

    private RedStoneLogicBuilder logicBuilder;
    private int phase;
    //private int

    protected UpgCtileentityCircuitBuilder() {
        super(new ItemStack[9 * 9 + 2], new UpgCFluidTank[]{new UpgCEPFluidTank(Capacity.INTERNAL_FLUID_TANK_TR1)}, "CircuitBuilder");
    }


    @Override
    public void update() {
        super.update();

        if(logicBuilder!=null){




        }

    }

    @Override
    public int getTankToShow() {
        return 0;
    }

    @Override
    public int getFluidLight() {
        return 0;
    }

    @Override
    public FluidStack getFluid() {
        return null;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public String getGuiID() {
        return null;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[0];
    }
}
