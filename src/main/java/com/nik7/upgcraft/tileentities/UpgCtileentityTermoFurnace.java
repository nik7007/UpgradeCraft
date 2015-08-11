package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tank.UpgCActiveTank;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class UpgCtileentityTermoFurnace extends UpgCtileentityInventoryFluidHandler {

    //tank[0]: fuel - tank[1]: waste
    //itemStacks[0]: input - itemStacks[1]: output - itemStacks[2]: waste

    private static final int diamondThermalConductivity = 1600;
    private static final int diamondSpecificHeat = 502;
    private float internalHeat = 0;
    private static final int internalVolume = 1;

    public UpgCtileentityTermoFurnace() {
        this.tank = new UpgCActiveTank[]{new UpgCActiveTank(Capacity.INTERNAL_FLUID_TANK_TR1, this), new UpgCActiveTank(Capacity.INTERNAL_FLUID_TANK_TR1, this)};
        this.itemStacks = new ItemStack[3];

    }

    @Override
    public void updateEntity() {

    }


    private float heatLost() {
        int t = tank[0] == null ? 0 : tank[0].getFluid().getFluid().getTemperature(tank[0].getFluid());
        return (float) (Math.abs((t - (double) internalHeat)) / diamondThermalConductivity);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return super.fill(from, resource, 0, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return super.drain(from, resource, 1, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return super.drain(from, maxDrain, 1, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return super.getTankInfo(from, 0);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return slot == 0;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int size) {
        return slot == 1 || slot == 2;
    }

    @Override
    public String getInventoryName() {
        if (hasCustomInventoryName())
            return customName;
        else
            return Names.Inventory.UPGC_TERMO_FURNACE;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return slot == 0;
    }
}
