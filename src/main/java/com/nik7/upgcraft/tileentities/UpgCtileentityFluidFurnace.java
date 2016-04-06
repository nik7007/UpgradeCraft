package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.inventory.ContainerFluidFurnace;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Reference;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class UpgCtileentityFluidFurnace extends UpgCtileentityInventoryFluidHandler implements ITickable, IInteractionObject {

    private static final Map<String, FluidFuelSpecific> FUEL_CACHE = new HashMap<>();
    private static final FluidFuelSpecific normalSpec = new FluidFuelSpecific(20, 200);
    private final static String LAVA = "lava";
    private static int lava_temp = -1;
    private static int lava_burning_time = -1;

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    private static final int[] input = {INPUT};
    private static final int[] output = {OUTPUT};

    private int burningTime = 0;
    private int progress = 0;

    private boolean isActive = false;

    public UpgCtileentityFluidFurnace() {
        super(new ItemStack[2], new UpgCFluidTank[]{new UpgCFluidTank(Capacity.INTERNAL_FLUID_TANK_TR1)}, "FluidFurnace");
        this.tanks[0].setTileEntity(this);

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


    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        this.burningTime = tag.getShort("burningTime");
        this.progress = tag.getShort("progress");
        this.isActive = tag.getBoolean("active");

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setShort("burningTime", (short) this.burningTime);
        tag.setShort("progress", (short) this.progress);
        tag.setBoolean("active", isActive);
    }

    @Override
    public void writeToPacket(PacketBuffer buf) {
        buf.writeBoolean(isActive);
        writeFluidToByteBuf(this.tanks[0], buf);
    }

    @Override
    public void readFromPacket(PacketBuffer buf) {

        this.isActive = buf.readBoolean();
        readFluidToByteBuf(this.tanks[0], buf);
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public int getTankToShow() {
        return 0;
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
        switch (id) {
            case 0:
                return this.burningTime;
            case 1:
                return this.progress;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.burningTime = value;
                break;
            case 1:
                this.progress = value;
                break;
        }

    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public void update() {

        if (!worldObj.isRemote) {
            boolean oldIsActive = isActive;

            boolean toUpdate = false;

            if (canSmelt()) {
                this.isActive = true;
                this.burning();

                progress++;

                if (progress == FUEL_CACHE.get(this.tanks[0].getFluid().getFluid().getName()).speed) {
                    progress = 0;
                    smeltItem();
                    toUpdate = true;
                }

                if (toUpdate) {
                    updateModBlock();
                    this.markDirty();
                }


            } else {
                this.isActive = false;
            }
            if (inventory[INPUT] == null) {
                progress = 0;
                this.isActive = false;
            }

            if (oldIsActive != isActive)
                updateModBlock();

        }


    }

    private boolean canSmelt() {

        if (tanks[0] != null && tanks[0].getFluid() != null && this.inventory[INPUT] != null) {
            if (tanks[0].getFluid().amount >= 1) {

                ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.inventory[INPUT]);

                if (itemstack == null) return false;
                if (this.inventory[OUTPUT] == null) return true;
                if (!this.inventory[OUTPUT].isItemEqual(itemstack)) return false;
                int result = inventory[OUTPUT].stackSize + itemstack.stackSize;
                return result <= getInventoryStackLimit() && result <= this.inventory[OUTPUT].getMaxStackSize();

            }

        }
        return false;
    }

    private void burning() {
        if (burningTime <= 0 && this.tanks[0].getFluid() != null) {
            Fluid fluid = this.tanks[0].drain(1, true).getFluid();
            burningTime = FUEL_CACHE.get(fluid.getName()).duration;
            updateModBlock();
        } else burningTime--;
    }

    private void smeltItem() {

        ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.inventory[INPUT]);

        if (this.inventory[OUTPUT] == null) {
            this.inventory[OUTPUT] = itemstack.copy();
        } else if (this.inventory[OUTPUT].isItemEqual(itemstack)) {
            this.inventory[OUTPUT].stackSize += itemstack.stackSize;
        } else return;
        decrStackSize(INPUT, 1);

    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerFluidFurnace(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return Reference.MOD_ID + ":" + ModBlocks.blockUpgCFluidFurnace.getName();
    }

    public int getCapacity() {
        return getCapacity(getTankToShow());
    }

    @SideOnly(Side.CLIENT)
    public float getFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) (this.tanks[0].getFluid() == null ? 0 : tanks[0].getFluid().amount) / (float) getCapacity();
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scaleFactor) {
        if (tanks[0].getFluidAmount() == 0)
            return 0;

        return this.burningTime * scaleFactor / (getFluidFuelSpecific(tanks[0].getFluid())).duration;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scaleFactor) {
        if (tanks[0].getFluidAmount() == 0)
            return 0;
        return this.progress * scaleFactor / (getFluidFuelSpecific(tanks[0].getFluid())).speed;
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
