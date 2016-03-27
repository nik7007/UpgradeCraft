package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.fluid.FluidUpgCActiveLava;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.inventory.ContainerActiveLavaMaker;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tank.UpgCEPFluidTank;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static com.nik7.upgcraft.init.ModFluids.fluidUpgCActiveLava;

public class UpgCtileentityActiveLavaMaker extends UpgCtileentityInventoryFluidHandler implements ITickable, IInteractionObject {

    //tank[0]: input tank - tank[1]: working tank
    //itemStacks[0]: input bucket/tank - itemStacks[1]: blaze stuff - itemStacks[2]: output bucket/tank

    private Random rnd = new Random();

    public static final int INPUT_TANK = 0;
    public static final int WORKING_TANK = 1;

    public static final int INPUT_TANK_SLOT = 0;
    public static final int WORKING_SLOT = 1;
    public static final int OUTPUT_TANK_SLOT = 2;

    private int activeOperation = 0;
    private boolean isOperating = false;

    private int tick = 0;

    private static final int BLAZE_ROD = 1;
    private static final int BLAZE_POWDER = 2;

    public UpgCtileentityActiveLavaMaker() {
        super(new ItemStack[3], new UpgCFluidTank[]{new UpgCEPFluidTank(Capacity.INTERNAL_FLUID_TANK_TR1), new UpgCEPFluidTank(Capacity.INTERNAL_FLUID_TANK_TR1)}, "ActiveLavaMaker");
        this.tanks[INPUT_TANK].setTileEntity(this);
        this.tanks[WORKING_TANK].setTileEntity(this);
    }

    @Override
    // TODO: 25/03/2016
    public int getTankToShow() {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public float getFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) (this.tanks[INPUT_TANK].getFluid() == null ? 0 : tanks[INPUT_TANK].getFluid().amount) / (float) tanks[INPUT_TANK].getCapacity();
    }

    @SideOnly(Side.CLIENT)
    public float getActiveFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) (this.tanks[WORKING_TANK].getFluid() == null ? 0 : tanks[WORKING_TANK].getFluid().amount) / (float) tanks[WORKING_TANK].getCapacity();
    }

    @Override
    public void writeToPacket(PacketBuffer buf) {
        buf.writeBoolean(isOperating);
        writeFluidToByteBuf(this.tanks[INPUT_TANK], buf);
        writeFluidToByteBuf(this.tanks[WORKING_TANK], buf);
    }

    @Override
    public void readFromPacket(PacketBuffer buf) {
        this.isOperating = buf.readBoolean();
        readFluidToByteBuf(this.tanks[INPUT_TANK], buf);
        readFluidToByteBuf(this.tanks[WORKING_TANK], buf);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.activeOperation = tag.getInteger("activeOperation");

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("activeOperation", this.activeOperation);
    }

    public boolean isOperating() {
        return isOperating;
    }

    @Override
    public void update() {

        if (!worldObj.isRemote) {
            if (canOperate()) {
                consumeItem();
                isOperating = true;

                if ((tick % 5) == 0)
                    transferOperation();

                if ((tick % 10) == 0)
                    heatOperation();

                if (rnd.nextFloat() > 0.55 && tanks[INPUT_TANK].getFluid() == null) {
                    heatOperation();
                }


            } else {
                boolean oldOP = isOperating;
                isOperating = false;
                if (oldOP != isOperating)
                    updateModBlock();
            }

            tick++;
            tick %= 20;
        }

    }

    private boolean canOperate() {

        if (inventory[WORKING_SLOT] != null && inventory[WORKING_SLOT].stackSize > 0) {
            Item item = inventory[WORKING_SLOT].getItem();

            if (item == Items.blaze_powder || item == Items.blaze_rod) {
                if (tanks[INPUT_TANK].getFluid() != null && !tanks[WORKING_TANK].isFull())
                    return true;
                else if (tanks[WORKING_TANK].getFluid() != null) {
                    return !FluidUpgCActiveLava.hasMaximumTemperature(tanks[WORKING_TANK].getFluid());
                }
            }
        }

        return false;

    }

    private void consumeItem() {
        if (activeOperation <= 0) {
            ItemStack itemStack = decrStackSize(WORKING_SLOT, 1);
            activeOperation = 100;

            if (itemStack.getItem() == Items.blaze_powder)
                activeOperation *= BLAZE_POWDER;
            if (itemStack.getItem() == Items.blaze_rod)
                activeOperation *= BLAZE_ROD;
            updateModBlock();

        }

    }


    private void transferOperation() {
        if (tanks[INPUT_TANK].getFluid() != null && !tanks[WORKING_TANK].isFull()) {
            int maxTransfer = 100;
            FluidStack fluidStack = new FluidStack(fluidUpgCActiveLava, maxTransfer);
            FluidStack inputFluidStack;
            maxTransfer = tanks[WORKING_SLOT].fill(fluidStack, false);

            inputFluidStack = tanks[INPUT_TANK].drain(maxTransfer, true);

            if (inputFluidStack.getFluid() != fluidUpgCActiveLava) {
                maxTransfer = inputFluidStack.amount;
                maxTransfer /= 2;
                if (inputFluidStack.amount > 0 && maxTransfer == 0)
                    maxTransfer = 1;
                int temp = inputFluidStack.getFluid().getTemperature(inputFluidStack);
                inputFluidStack = new FluidStack(fluidUpgCActiveLava, maxTransfer);
                FluidUpgCActiveLava.increaseActiveValue(inputFluidStack, temp);
                activeOperation--;
            }

            if (rnd.nextFloat() > 0.6) {
                if (FluidUpgCActiveLava.increaseActiveValue(inputFluidStack, 10) > 0)
                    activeOperation -= 2;
            }

            tanks[WORKING_SLOT].fill(inputFluidStack, true);

            if (maxTransfer > 0)
                updateModBlock();

        }
    }

    private void heatOperation() {
        FluidStack activeLava;
        if ((activeLava = tanks[WORKING_TANK].getFluid()) != null) {

            if (!FluidUpgCActiveLava.hasMaximumTemperature(activeLava)) {
                if (rnd.nextFloat() > 0.7) {
                    if (FluidUpgCActiveLava.increaseActiveValue(activeLava, 100) > 0) {
                        activeOperation -= 3;
                        updateModBlock();
                    }
                } else if (rnd.nextFloat() > 0.3) {
                    if (FluidUpgCActiveLava.increaseActiveValue(activeLava, 25) > 0) {
                        activeOperation--;
                        updateModBlock();
                    }
                }
            }
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{WORKING_SLOT};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (index == WORKING_SLOT) {
            Item item = inventory[WORKING_SLOT].getItem();
            if (item == Items.blaze_powder || item == Items.blaze_rod)
                return true;
        }
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

        if (index == WORKING_SLOT) {
            Item item = inventory[WORKING_SLOT].getItem();
            if (item == Items.blaze_powder || item == Items.blaze_rod)
                return true;
        }

        return false;
    }

    @Override
    public int getField(int id) {
        if (id == 0)
            return this.activeOperation;
        return 0;
    }

    @Override
    public void setField(int id, int value) {

        if (id == 0)
            this.activeOperation = value;

    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerActiveLavaMaker(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return Reference.MOD_ID + ":" + ModBlocks.blockUpgCActiveLavaMaker.getName();
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return super.fill(INPUT_TANK, from, resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return super.drain(WORKING_TANK, from, resource, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return super.drain(WORKING_TANK, from, maxDrain, doDrain);
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
            return super.getTankInfo(WORKING_TANK, from);
        return super.getTankInfo(INPUT_TANK, from);
    }
}
