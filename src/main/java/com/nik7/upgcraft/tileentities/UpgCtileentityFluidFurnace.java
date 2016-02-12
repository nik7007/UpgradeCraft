package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.*;

import java.util.HashMap;
import java.util.Map;

public class UpgCtileentityFluidFurnace extends UpgCtileentityInventoryFluidHandler implements ITickable {

    private static final Map<String, FluidFuelSpecific> FUEL_CACHE = new HashMap<String, FluidFuelSpecific>();
    private static final FluidFuelSpecific normalSpec = new FluidFuelSpecific(20, 200);
    private final static String LAVA = "lava";
    private static int lava_temp = -1;
    private static int lava_burning_time = -1;

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    private static final int[] input = {INPUT};
    private static final int[] output = {OUTPUT};

    public UpgCtileentityFluidFurnace() {
        super(new ItemStack[2], new FluidTank[]{new UpgCFluidTank(Capacity.INTERNAL_FLUID_TANK_TR1)}, "FluidFurnace");
        ((UpgCFluidTank) this.tanks[0]).setTileEntity(this);

        if (lava_temp == -1 || lava_burning_time == -1) {
            Fluid lava = FluidRegistry.getFluid(LAVA);
            if (lava_temp == -1) {

                lava_temp = lava.getTemperature();
            }
            if (lava_burning_time == -1) {
                lava_burning_time = getBurningTime(new FluidStack(lava, 1));
            }
        }

        if (!FUEL_CACHE.containsKey(LAVA)) {
            FUEL_CACHE.put(LAVA, normalSpec);
        }

    }

    private FluidFuelSpecific getFluidFuelSpecific(FluidStack fluidStack) {

        if (fluidStack == null || fluidStack.getFluid() == null || fluidStack.getFluid().getName() == null)
            return null;
        FluidFuelSpecific result;
        String fluidName = fluidStack.getFluid().getName();
        if (FUEL_CACHE.containsKey(fluidName)) {
            FluidFuelSpecific specific = FUEL_CACHE.get(fluidName);
            if (specific.duration == -1 || specific.speed == -1)
                return null;
            return specific;
        } else {

            int burningTime = getBurningTime(fluidStack);
            if (burningTime == 0) {
                FUEL_CACHE.put(fluidName, new FluidFuelSpecific(-1, -1));
                return null;
            } else {
                int temp = fluidStack.getFluid().getTemperature(fluidStack);
                int duration = (burningTime / lava_burning_time) * normalSpec.duration;
                int speed = (temp / lava_temp) * normalSpec.speed;
                result = new FluidFuelSpecific(duration, speed);
            }
        }

        return result;
    }

    private int getBurningTime(FluidStack fluidStack) {
        if (fluidStack != null)
            return getBurningTime(fluidStack.getFluid());
        return 0;
    }

    private int getBurningTime(Fluid fluid) {
        if (fluid == null) return 0;
        ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME), FluidContainerRegistry.EMPTY_BUCKET);
        return TileEntityFurnace.getItemBurnTime(filledBucket);
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (getFluidFuelSpecific(resource) != null)
            return super.fill(0, from, resource, doFill);
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (resource != null && canDrain(from, resource.getFluid()))
            return super.drain(0, from, resource, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        if (canDrain(from, null))
            return super.drain(0, from, maxDrain, doDrain);
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return fluid != null && (FUEL_CACHE.containsKey(fluid.getName()) || getBurningTime(fluid) > 0);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return from == EnumFacing.DOWN;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return super.getTankInfo(0, from);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN)
            return output;
        return input;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == INPUT && direction != EnumFacing.DOWN;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == OUTPUT && direction == EnumFacing.DOWN;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index != OUTPUT && index == INPUT && FurnaceRecipes.instance().getSmeltingResult(stack) != null;
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
    public void update() {

    }

    private static class FluidFuelSpecific {
        private int duration;
        private int speed;

        FluidFuelSpecific(int duration, int speed) {
            this.duration = duration;
            this.speed = speed;
        }


    }

}
