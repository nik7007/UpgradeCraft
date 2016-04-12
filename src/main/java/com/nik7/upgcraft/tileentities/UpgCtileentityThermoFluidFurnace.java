package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.fluid.FluidUpgCActiveLava;
import com.nik7.upgcraft.fluid.ISecondaryFluidTankShow;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.init.ModFluids;
import com.nik7.upgcraft.inventory.ContainerThermoFluidFurnace;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.registry.ThermoSmelting.ThermoSmeltingRecipe;
import com.nik7.upgcraft.registry.ThermoSmeltingRegister;
import com.nik7.upgcraft.tank.UpgCEPFluidTank;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import com.nik7.upgcraft.util.PhysicsHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class UpgCtileentityThermoFluidFurnace extends UpgCtileentityInventoryFluidHandler implements ITickable, IInteractionObject, ISecondaryFluidTankShow {

    public static final int INTERNAL_CAPACITY_WORKING_TANK = 10 * FluidContainerRegistry.BUCKET_VOLUME;

    private final Random rnd = new Random();

    public static final int INPUT_TANK = 0;
    public static final int WORKING_TANK = 1;
    public static final int WASTE_TANK = 2;

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int WASTE_SLOT_C = 2;
    public static final int WASTE_SLOT_O = 3;
    public static final int OUTPUT_TANK_SLOT = 4;

    public static final int MAX_TEMPERATURE = 5700;
    public static final int MIN_OPERATION_TEMP = 270 + 273;

    private static final int STD_BURN_TIME = 200;

    private static final int NORMAL_TEMP = 273;

    private static final int DIAMOND_THERMAL_CONDUCTIVITY = 1600;
    private static final float INTERNAL_MASS = 1 * 0.004f;

    private float internalTemp = NORMAL_TEMP;
    private float increaseTemperatureSpeed = 1;

    private float workingTemp = 0;
    private float oldWTemp = 1;

    private boolean balanced;
    private int balanceTick = 0;

    private int smeltTick = 0;
    private int totalSmeltingTicks = 0;

    private ThermoSmeltingRecipe thermoSmeltingRecipe = null;

    private int tick = 0;
    private boolean isActive = false;

    private boolean canOperate = true;

    private static final int MAX_FUEL_TTL = 10;
    private int fuelTTL = MAX_FUEL_TTL;
    private float oldTemp = 0;

    private static final int MAX_FUEL_DRAIN = 1000;


    public UpgCtileentityThermoFluidFurnace() {
        super(new ItemStack[5], new UpgCFluidTank[]{new UpgCEPFluidTank(Capacity.INTERNAL_FLUID_TANK_TR1), new UpgCEPFluidTank(INTERNAL_CAPACITY_WORKING_TANK), new UpgCEPFluidTank(INTERNAL_CAPACITY_WORKING_TANK)}, "ThermoFluidFurnace");
        this.tanks[INPUT_TANK].setTileEntity(this);
        this.tanks[WORKING_TANK].setTileEntity(this);
        this.tanks[WASTE_TANK].setTileEntity(this);
    }


    @Override
    public void writeToPacket(PacketBuffer buf) {
        buf.writeBoolean(isActive);
        writeFluidToByteBuf(this.tanks[INPUT_TANK], buf);
        writeFluidToByteBuf(this.tanks[WASTE_TANK], buf);
    }

    @Override
    public void readFromPacket(PacketBuffer buf) {
        this.isActive = buf.readBoolean();
        readFluidToByteBuf(this.tanks[INPUT_TANK], buf);
        readFluidToByteBuf(this.tanks[WASTE_TANK], buf);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        this.internalTemp = tag.getFloat("internalTemp");
        this.workingTemp = tag.getFloat("workingTemp");
        this.smeltTick = tag.getInteger("smeltTick");
        this.totalSmeltingTicks = tag.getInteger("totalSmeltingTicks");
        this.fuelTTL = tag.getInteger("fuelTTL");


    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setFloat("internalTemp", this.internalTemp);
        tag.setFloat("workingTemp", this.workingTemp);
        tag.setInteger("smeltTick", this.smeltTick);
        tag.setInteger("totalSmeltingTicks", this.totalSmeltingTicks);
        tag.setInteger("fuelTTL", this.fuelTTL);


    }

    @SideOnly(Side.CLIENT)
    public float getInternalTemp() {
        return internalTemp;
    }

    @SideOnly(Side.CLIENT)
    public float getFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) (this.tanks[INPUT_TANK].getFluidAmount()) / tanks[INPUT_TANK].getCapacity();
    }

    @SideOnly(Side.CLIENT)
    public float getWasteFluidLevelScaled(int scaleFactor) {

        return scaleFactor * (float) (this.tanks[WASTE_TANK].getFluidAmount()) / tanks[WASTE_TANK].getCapacity();
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scaleFactor) {

        if (totalSmeltingTicks <= 0)
            return 0;

        return (int) ((float) this.smeltTick * scaleFactor) / this.totalSmeltingTicks;
    }

    @SideOnly(Side.CLIENT)
    public int geTemperatureScaled(int scaleFactor) {

        if (internalTemp <= 273)
            return 0;

        int result = (int) (internalTemp - 273) * scaleFactor / (MAX_TEMPERATURE - 273);

        if (result == 0 && internalTemp > 273 + 100)
            return 1;

        return result;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            tick++;
            if (canOperate) {

                if (!balanced && internalTemp < MAX_TEMPERATURE && tanks[WORKING_TANK].getFluid() != null && Math.floor(Math.abs(workingTemp)) <= Math.abs(oldWTemp) / 50)
                    tempOperation();
                else if (balanced)
                    if (++balanceTick > 120) {
                        balanced = false;
                        balanceTick = 0;
                    }
                if ((tick % 5) == 0) {
                    if (Math.abs(workingTemp) > 0) {

                        increaseTemperatureSpeed = (int) Math.min(Math.abs(increaseTemperatureSpeed), Math.abs(workingTemp));

                        if (internalTemp < MAX_TEMPERATURE && increaseTemperatureSpeed > 0) {
                            if (workingTemp > 0) {
                                internalTemp += increaseTemperatureSpeed;
                                workingTemp -= increaseTemperatureSpeed;
                                internalTemp = Math.min(MAX_TEMPERATURE, internalTemp);
                            } else if (internalTemp > 0) {
                                internalTemp -= increaseTemperatureSpeed;
                                workingTemp += increaseTemperatureSpeed;
                                workingTemp = Math.round(workingTemp);
                                if (internalTemp < 0)
                                    internalTemp = 0;
                            }
                        } else if (workingTemp > 0)
                            workingTemp--;
                        else if (workingTemp < 0)
                            workingTemp++;
                    }

                }

                if (canSmelt()) {
                    smelt();
                } else {
                    smeltTick = 0;
                    isActive = false;
                }

                int oldAmount = this.tanks[INPUT_TANK].getFluidAmount();

                if ((tick) % 2 == 0) {
                    internalFill();
                    wasteOperation();
                }

                if (oldAmount != this.tanks[INPUT_TANK].getFluidAmount())
                    updateModBlock();

            } else {
                if ((tick % 5) == 0) {

                    FluidStack workFluid = tanks[WORKING_TANK].getFluid();

                    if (workFluid != null) {

                        if (workFluid.getFluid() == ModFluids.fluidUpgCActiveLava) {
                            canOperate = !tanks[WASTE_TANK].isFull();
                        } else {
                            boolean slotC, slotO;

                            slotC = inventory[WASTE_SLOT_C] == null || inventory[WASTE_SLOT_C].stackSize < getInventoryStackLimit();
                            slotO = inventory[WASTE_SLOT_O] == null || inventory[WASTE_SLOT_O].stackSize < getInventoryStackLimit();

                            canOperate = slotC && slotO;

                        }

                    }
                }
            }

            if ((tick % 5) == 0) {

                //int wasteTemp = Math.abs(increaseTemperatureSpeed) == 1 ? 2 : 1;

                if (internalTemp > NORMAL_TEMP)
                    internalTemp--;//= wasteTemp;
                else if (internalTemp < NORMAL_TEMP)
                    internalTemp += 10;

            }

            if (oldTemp != internalTemp) {
                updateModBlock();
                oldTemp = internalTemp;
            }

            outputActiveLava();

            tick %= 200;
        }
    }

    private void outputActiveLava() {
        FluidStack fluid = tanks[WASTE_TANK].getFluid();
        if (fluid != null) {
            if (inventory[OUTPUT_TANK_SLOT] != null && inventory[OUTPUT_TANK_SLOT].stackSize == 1) {
                ItemStack itemStack = inventory[OUTPUT_TANK_SLOT];
                if (FluidContainerRegistry.isEmptyContainer(itemStack)) {
                    if (fluid.amount >= FluidContainerRegistry.BUCKET_VOLUME) {
                        FluidStack oneBucketOfFluid = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
                        ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(oneBucketOfFluid, FluidContainerRegistry.EMPTY_BUCKET);
                        if (filledBucket != null) {
                            inventory[OUTPUT_TANK_SLOT] = filledBucket;
                            fluid.amount -= FluidContainerRegistry.BUCKET_VOLUME;
                            if (fluid.amount == 0)
                                tanks[WASTE_TANK].setFluid(null);
                            updateModBlock();
                        }
                    }
                } else if (itemStack.getItem() instanceof IFluidContainerItem && (tick % 20) == 10) {
                    if (fluid.amount >= FluidContainerRegistry.BUCKET_VOLUME) {

                        IFluidContainerItem fluidContainerItem = (IFluidContainerItem) itemStack.getItem();
                        FluidStack fluidStack = fluid.copy();

                        if (fluidStack.amount > FluidContainerRegistry.BUCKET_VOLUME)
                            fluidStack.amount = FluidContainerRegistry.BUCKET_VOLUME;

                        int amount = fluidContainerItem.fill(itemStack, fluidStack, true);
                        if (amount > 0) {
                            fluid.amount -= amount;
                            updateModBlock();
                        }
                        if (fluid.amount == 0) {
                            tanks[WASTE_TANK].setFluid(null);
                            updateModBlock();
                        }
                    }
                }
            }
        }
    }


    private void internalFill() {
        if (tanks[INPUT_TANK].getFluid() != null && !tanks[WORKING_TANK].isFull()) {

            int dAmount = 250;

            if (tanks[INPUT_TANK].getFluid().amount > dAmount || (tick % 20) == 0) {

                FluidStack fluidStack = tanks[INPUT_TANK].drain(dAmount, false);
                int amount = 0;
                if (fluidStack != null) {
                    amount = tanks[WORKING_TANK].fill(fluidStack, true);
                }
                if (amount > 0) {
                    tanks[INPUT_TANK].drain(amount, true);
                }
            }

        }

    }

    private void wasteOperation() {

        if (tanks[WORKING_TANK].getFluid() != null) {

            if (tanks[WORKING_TANK].getFluid().getFluid() == ModFluids.fluidUpgCActiveLava) {
                FluidStack activeLava = tanks[WORKING_TANK].getFluid();
                if (tanks[INPUT_TANK].getFluid() != null) {

                    if (internalTemp < MAX_TEMPERATURE && internalTemp > 0) {

                        int active = Math.max(1, Math.min(50, (int) (workingTemp / 10)));
                        FluidUpgCActiveLava.decreaseActiveValue(activeLava, active);

                        int tempIn = tanks[INPUT_TANK].getFluid().getFluid().getTemperature(tanks[INPUT_TANK].getFluid());
                        int tempW = activeLava.getFluid().getTemperature(activeLava);

                        if (tempW < (float) tempIn && fuelTTL <= 3) {
                            FluidStack waste = tanks[WORKING_TANK].drain(MAX_FUEL_DRAIN, true);
                            if (waste != null) {
                                if (canOperate)
                                    canOperate = tanks[WASTE_TANK].fill(waste, true) == waste.amount;
                                else {
                                    if (!tanks[WASTE_TANK].isFull()) {
                                        canOperate = tanks[WASTE_TANK].fill(waste, true) == waste.amount;
                                    } else tanks[WORKING_TANK].fill(waste, true);
                                }
                            }
                            fuelTTL = MAX_FUEL_TTL / 2;
                        }
                    }
                } else {
                    if (fuelTTL <= 3) {

                        FluidStack waste = tanks[WORKING_TANK].drain(MAX_FUEL_DRAIN, true);
                        if (waste != null) {
                            if (canOperate)
                                canOperate = tanks[WASTE_TANK].fill(waste, true) == waste.amount;
                            else {
                                if (!tanks[WASTE_TANK].isFull()) {
                                    canOperate = tanks[WASTE_TANK].fill(waste, true) == waste.amount;
                                } else tanks[WORKING_TANK].fill(waste, true);
                            }
                        }
                        fuelTTL = MAX_FUEL_TTL / 2;

                    }

                }

            } else {
                if (tanks[WORKING_TANK].getFluid().getFluid().getTemperature(tanks[WORKING_TANK].getFluid()) <= NORMAL_TEMP + 100) {
                    tanks[WORKING_TANK].setFluid(null);
                } else {
                    if (fuelTTL <= 0) {
                        tanks[WORKING_TANK].drain(300, true);

                        if (rnd.nextDouble() <= 0.32) {
                            addObsidian();
                        } else if (rnd.nextDouble() <= 0.81) {
                            addCobblestone();
                        }
                        fuelTTL = MAX_FUEL_TTL;
                    }


                }
            }


        }
    }

    private void addObsidian() {
        addWaste(WASTE_SLOT_O);
    }

    private void addCobblestone() {
        addWaste(WASTE_SLOT_C);
    }


    private void addWaste(int index) {
        if (index == WASTE_SLOT_O || index == WASTE_SLOT_C) {

            if (this.inventory[index] == null) {
                this.inventory[index] = index == WASTE_SLOT_O ? new ItemStack(Blocks.obsidian) : new ItemStack(Blocks.cobblestone);
            } else if (this.inventory[index].stackSize < this.getSizeInventory() && this.inventory[index].stackSize < this.inventory[index].getItem().getItemStackLimit(inventory[index])) {
                this.inventory[index].stackSize++;
            } else {
                canOperate = false;
            }
        }
    }

    private boolean isThermoSmelting() {
        return thermoSmeltingRecipe != null;
    }

    private int smeltingTime() {

        if (isThermoSmelting()) {
            if (internalTemp < thermoSmeltingRecipe.getTemperature())
                return -1;
            return (int) ((thermoSmeltingRecipe.getTemperature() / internalTemp) * thermoSmeltingRecipe.getTicks());
        } else {

            if (internalTemp < MIN_OPERATION_TEMP)
                return -1;
            return (int) ((MIN_OPERATION_TEMP / internalTemp) * STD_BURN_TIME);
        }
    }

    private void smelt() {
        this.totalSmeltingTicks = smeltingTime();

        if (totalSmeltingTicks != -1) {

            if (smeltTick >= totalSmeltingTicks) {
                smeltItem();
                smeltTick = 0;

            } else {
                if (isThermoSmelting()) {
                    internalTemp -= (int) (3f * Math.random());
                } else internalTemp--;

                isActive = true;
                smeltTick++;
            }


        }


    }

    private void smeltItem() {

        ItemStack result;
        if (isThermoSmelting()) {
            result = thermoSmeltingRecipe.getOutput();

        } else {
            result = FurnaceRecipes.instance().getSmeltingResult(inventory[INPUT_SLOT]);
        }

        if (inventory[OUTPUT_SLOT] != null && inventory[OUTPUT_SLOT].isItemEqual(result)) {
            inventory[OUTPUT_SLOT].stackSize += result.stackSize;
            decrStackSize(INPUT_SLOT, 1);
        } else if (inventory[OUTPUT_SLOT] == null) {
            inventory[OUTPUT_SLOT] = result.copy();
            decrStackSize(INPUT_SLOT, 1);
        }
    }

    private boolean canSmelt() {

        if (inventory[INPUT_SLOT] != null && inventory[INPUT_SLOT].stackSize > 0 && (inventory[OUTPUT_SLOT] == null || (inventory[OUTPUT_SLOT].stackSize < getInventoryStackLimit() && inventory[OUTPUT_SLOT].stackSize < inventory[OUTPUT_SLOT].getMaxStackSize()))) {

            if (isThermoSmelting()) {
                if (thermoSmeltingRecipe.getInput().isItemEqual(inventory[INPUT_SLOT]))
                    return true;
                else thermoSmeltingRecipe = null;
            }

            ItemStack outputItem = FurnaceRecipes.instance().getSmeltingResult(inventory[INPUT_SLOT]);
            float temp;

            if (outputItem != null) {
                temp = MIN_OPERATION_TEMP;
            } else {
                thermoSmeltingRecipe = ThermoSmeltingRegister.getRecipeFromInput(inventory[INPUT_SLOT]);
                if (isThermoSmelting()) {
                    temp = thermoSmeltingRecipe.getTemperature();
                    outputItem = thermoSmeltingRecipe.getOutput();
                } else
                    return false;
            }

            if (internalTemp >= temp) {

                if (this.inventory[OUTPUT_SLOT] == null)
                    return true;
                else if (!this.inventory[OUTPUT_SLOT].isItemEqual(outputItem))
                    return false;
                else {
                    int result = inventory[OUTPUT_SLOT].stackSize + outputItem.stackSize;
                    return result <= getInventoryStackLimit() && result <= this.inventory[OUTPUT_SLOT].getMaxStackSize();
                }


            } else return false;

        } else
            return false;
    }

    private void tempOperation() {
        this.workingTemp += PhysicsHelper.getFinalTemp(tanks[WORKING_TANK].getFluid(), internalTemp, INTERNAL_MASS, 20, heatLost());

        if (((int) Math.abs(workingTemp)) <= 15 || (internalTemp > NORMAL_TEMP && internalTemp < 0)) {
            balanced = true;
            increaseTemperatureSpeed = (workingTemp / 16) * 0.95f;
            if (tanks[WORKING_TANK].getFluid().getFluid() == ModFluids.fluidUpgCActiveLava)
                fuelTTL--;
        } else {
            increaseTemperatureSpeed = Math.max(1, (int) (Math.abs(workingTemp) / 10));
            oldWTemp = workingTemp;
            fuelTTL--;
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
    public int getField(int id) {

        switch (id) {
            case 0:
                return (int) internalTemp;
            case 1:
                return smeltTick;
            case 2:
                return totalSmeltingTicks;
        }

        return -1;
    }

    @Override
    public void setField(int id, int value) {

        switch (id) {
            case 0:
                internalTemp = value;
                break;
            case 1:
                smeltTick = value;
                break;
            case 2:
                totalSmeltingTicks = value;
                break;
        }

    }

    @Override
    public int getFieldCount() {
        return 3;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerThermoFluidFurnace(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return Reference.MOD_ID + ":" + ModBlocks.blockUpgCThermoFluidFurnace.getName();
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
