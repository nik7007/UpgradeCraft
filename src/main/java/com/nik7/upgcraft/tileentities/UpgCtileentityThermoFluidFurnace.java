package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.fluid.ISecondaryFluidTankShow;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.registry.ThermoSmeltingRegister;
import com.nik7.upgcraft.tank.UpgCEPFluidTank;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import com.nik7.upgcraft.util.PhysicsHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fluids.*;

public class UpgCtileentityThermoFluidFurnace extends UpgCtileentityInventoryFluidHandler implements ITickable, IInteractionObject, ISecondaryFluidTankShow {

    public static final int INTERNAL_CAPACITY_WORKING_TANK = 10 * FluidContainerRegistry.BUCKET_VOLUME;

    public static final int INPUT_TANK = 0;
    public static final int WORKING_TANK = 1;
    public static final int WASTE_TANK = 2;

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int WASTE_SLOT_C = 2;
    public static final int WASTE_SLOT_O = 3;
    public static final int OUTPUT_TANK_SLOT = 4;

    public static final int MAX_TEMPERATURE = 5700;

    private static final int NORMAL_TEMP = 273;

    private static final int DIAMOND_THERMAL_CONDUCTIVITY = 1600;
    private static final float INTERNAL_MASS = 1 * 0.004f;

    private float internalTemp = NORMAL_TEMP;
    private int increaseTemperatureSpeed = 1;

    private float workingTemp = 0;
    private float oldWTemp = 1;
    private boolean balanced;
    private int balanceTick = 0;

    private int tick = 0;


    protected UpgCtileentityThermoFluidFurnace() {
        super(new ItemStack[5], new UpgCFluidTank[]{new UpgCEPFluidTank(Capacity.INTERNAL_FLUID_TANK_TR1), new UpgCEPFluidTank(INTERNAL_CAPACITY_WORKING_TANK), new UpgCEPFluidTank(INTERNAL_CAPACITY_WORKING_TANK)}, "ThermoFluidFurnace");
        this.tanks[INPUT_TANK].setTileEntity(this);
        this.tanks[WORKING_TANK].setTileEntity(this);
        this.tanks[WASTE_TANK].setTileEntity(this);
    }

    @Override
    public void update() {
        if (!worldObj.isRemote) {

            if (!balanced && internalTemp < MAX_TEMPERATURE && tanks[WORKING_TANK].getFluid() != null && Math.abs(workingTemp) <= Math.abs(oldWTemp) / 50)
                tempOperation();
            else if (balanced) {
                if (++balanceTick > 120) {
                    balanced = false;
                    balanceTick = 0;
                }
            }
            tick++;
            if ((tick % 5) == 0) {
                if (Math.abs(workingTemp) >= increaseTemperatureSpeed) {
                    if (workingTemp > 0) {
                        internalTemp += increaseTemperatureSpeed;
                        workingTemp -= increaseTemperatureSpeed;
                    } else {
                        internalTemp -= increaseTemperatureSpeed;
                        workingTemp += increaseTemperatureSpeed;
                        workingTemp = Math.round(workingTemp);
                    }
                }

                if (workingTemp > NORMAL_TEMP)
                    workingTemp--;
                else if (workingTemp < NORMAL_TEMP)
                    workingTemp += 10;
            }



            tick %= 20;
        }
    }

    private void tempOperation() {
        this.workingTemp += PhysicsHelper.getFinalTemp(tanks[WORKING_TANK].getFluid(), internalTemp, INTERNAL_MASS, 20, heatLost());

        if (((int) Math.abs(workingTemp)) <= 12) {
            balanced = true;
            increaseTemperatureSpeed = 1;
        } else {
            increaseTemperatureSpeed = Math.max(1, (int) (Math.abs(workingTemp) / 100));
            oldWTemp = workingTemp;
        }

    }

    private float heatLost() {
        int t = tanks[INPUT_TANK].getFluid() == null ? 0 : tanks[INPUT_TANK].getFluid().getFluid().getTemperature(tanks[INPUT_TANK].getFluid());
        return (float) (Math.abs((t - (double) internalTemp)) / DIAMOND_THERMAL_CONDUCTIVITY);
    }

    @Override
    public int getSecondaryFluidTankToShow() {
        return WASTE_TANK;
    }

    @Override
    public int getTankToShow() {
        return INPUT_TANK;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{INPUT_SLOT, OUTPUT_SLOT, WASTE_SLOT_C, WASTE_SLOT_O};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {

        return index == INPUT_SLOT && ThermoSmeltingRegister.isRegisterContainsInput(itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index > 0 && index < 4;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {

        if (stack == null || stack.getItem() == null)
            return false;
        else {
            switch (index) {
                case INPUT_SLOT:
                    return ThermoSmeltingRegister.isRegisterContainsInput(stack);
                case OUTPUT_SLOT:
                case WASTE_SLOT_C:
                case WASTE_SLOT_O:
                    return false;
                case OUTPUT_TANK_SLOT:
                    return stack.getItem() instanceof IFluidContainerItem || stack.getItem() == Items.bucket;
                default:
                    return false;
            }
        }
    }

    @Override
    // TODO: 31/03/2016
    public int getField(int id) {
        return 0;
    }

    @Override
    // TODO: 31/03/2016
    public void setField(int id, int value) {

    }

    @Override
    // TODO: 31/03/2016
    public int getFieldCount() {
        return 0;
    }

    @Override
    // TODO: 31/03/2016
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    // TODO: 31/03/2016
    public String getGuiID() {
        return null;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return super.fill(INPUT_TANK, from, resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return super.drain(WASTE_TANK, from, resource, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return super.drain(WASTE_TANK, from, maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        if (from == EnumFacing.DOWN)
            return super.getTankInfo(WASTE_TANK, from);
        return super.getTankInfo(INPUT_TANK, from);
    }
}
