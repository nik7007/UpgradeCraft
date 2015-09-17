package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.fluid.ActiveLava;
import com.nik7.upgcraft.init.ModBlocks;
import com.nik7.upgcraft.init.ModFluids;
import com.nik7.upgcraft.init.ModItems;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tank.UpgCActiveTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class UpgCtileentityActiveMaker extends UpgCtileentityInventoryFluidHandler {

    //tank[0]: Lava - tank[1]: ActiveLava
    //itemStacks[0]: Lava bucket - itemStacks[1]: blaze stuff - itemStacks[2]: ActiveLava output

    private static final int BLAZE_ROD = 1;
    private static final int BLAZE_POWDER = 2;

    private int workingTick = 0;
    private byte searchingTick = 0;
    private int coolDown = 0;
    private int[] coordinates;

    public byte operate;


    public UpgCtileentityActiveMaker() {
        this.tank = new UpgCActiveTank[]{new UpgCActiveTank(Capacity.INTERNAL_FLUID_TANK_TR1, this), new UpgCActiveTank(Capacity.INTERNAL_FLUID_TANK_TR1, this)};
        this.itemStacks = new ItemStack[3];
        coordinates = new int[3];
        coordinates[0] = this.xCoord;
        coordinates[1] = this.yCoord;
        coordinates[2] = this.zCoord;
        capacity = Capacity.INTERNAL_FLUID_TANK_TR1;
    }

    @Override
    public void writeToPacket(ByteBuf buf) {
        writeFluidToByteBuf(this.tank[0], buf);
        writeFluidToByteBuf(this.tank[1], buf);
        buf.writeByte(operate);
    }

    @Override
    public void readFromPacket(ByteBuf buf) {
        readFluidToByteBuf(this.tank[0], buf);
        readFluidToByteBuf(this.tank[1], buf);
        operate = buf.readByte();
    }


    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        this.workingTick = tag.getInteger("workingTick");
        this.coordinates = tag.getIntArray("coordinates");
        this.searchingTick = tag.getByte("searchingTick");
        this.coolDown = tag.getInteger("coolDown");

        if (coordinates.length < 3) {
            coordinates = new int[3];
            coordinates[0] = this.xCoord;
            coordinates[1] = this.yCoord;
            coordinates[2] = this.zCoord;
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setInteger("workingTick", this.workingTick);
        tag.setIntArray("termoOperation", this.coordinates);
        tag.setByte("searchingTick", this.searchingTick);
        tag.setInteger("coolDown", this.coolDown);
    }

    @SideOnly(Side.CLIENT)
    public float getFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) (this.tank[0].getFluidAmount()) / capacity;
    }

    @SideOnly(Side.CLIENT)
    public float getActiveFluidLevelScaled(int scaleFactor) {
        return scaleFactor * (float) (this.tank[1].getFluidAmount()) / capacity;
    }

    @Override
    public void updateEntity() {

        if (!worldObj.isRemote) {

            int lastAmount = tank[0].getFluidAmount();
            int lastActive = tank[1].getFluidAmount();

            byte type = operationType(itemStacks[1]);
            if (type != 0 && (workingTick % 10) == 0) {
                int activeValue = tank[1].getFluid() == null ? 0 : ((ActiveLava) tank[1].getFluid().getFluid()).getActiveValue(tank[1].getFluid());
                if (activeValue < ActiveLava.MAX_ACTIVE_VALUE || tank[1].getFluidAmount() < tank[1].getCapacity()) {
                    if (tank[0].getFluid() != null || tank[1].getFluid() != null) {
                        this.operate = 1;
                        if (tank[1].getFluid() == null) {
                            initOperation(type);
                        } else if (tank[0].getFluid() != null)
                            normalOperation(type);
                        else {

                            if (activeValue < ActiveLava.MAX_ACTIVE_VALUE)
                                heatOperation(type);
                            else
                                this.operate = 0;

                        }

                        if (tank[1].getCapacity() == tank[1].getFluidAmount() && (workingTick % 80) == 5)
                            if (activeValue < ActiveLava.MAX_ACTIVE_VALUE)
                                heatOperation(type);
                            else
                                this.operate = 0;

                    } else
                        this.operate = 0;
                } else
                    this.operate = 0;
                updateModBlock();
            } else if (type == 0)
                this.operate = 0;

            if ((workingTick % 20) == 0 && !worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
                if (coolDown > 0) {
                    coolDown--;
                } else if (tank[1].getFluid() != null) {
                    termoOperation();
                }
            }

            outputActiveLava();
            inputLava();


            workingTick++;
            workingTick %= 2000;


            if (lastAmount != tank[0].getFluidAmount() || lastActive != tank[1].getFluidAmount())
                updateModBlock();
        }

    }

    private void inputLava() {

        if (itemStacks[0] != null) {
            Item item = itemStacks[0].getItem();

            if (item != null) {
                if (item == Items.lava_bucket || item == ModItems.itemActiveLavaBucket) {

                    if (tank[0].getCapacity() - tank[0].getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME) {
                        FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStacks[0]);

                        int result = tank[0].fill(fluidStack, false);

                        if (result == FluidContainerRegistry.BUCKET_VOLUME) {
                            itemStacks[0] = new ItemStack(Items.bucket, 1);
                            tank[0].fill(fluidStack, true);
                        }
                    }


                } else if (item instanceof IFluidContainerItem && (workingTick % 20) == 5) {
                    IFluidContainerItem fluidContainerItem = (IFluidContainerItem) item;
                    FluidStack fluidStack = fluidContainerItem.getFluid(itemStacks[0]);

                    if (fluidStack != null) {
                        fluidStack = fluidStack.copy();

                        if (fluidStack.amount > FluidContainerRegistry.BUCKET_VOLUME)
                            fluidStack.amount = FluidContainerRegistry.BUCKET_VOLUME;

                        int result = tank[0].fill(fluidStack, true);
                        if (result > 0) {
                            fluidContainerItem.drain(itemStacks[0], result, true);
                        }
                    }
                }

            }


        }

    }

    private void outputActiveLava() {
        FluidStack fluid = tank[1].getFluid();
        if (fluid != null) {
            if (itemStacks[2] != null && itemStacks[2].stackSize == 1) {
                ItemStack itemStack = itemStacks[2];
                if (FluidContainerRegistry.isEmptyContainer(itemStack)) {
                    if (fluid.amount >= FluidContainerRegistry.BUCKET_VOLUME) {
                        FluidStack oneBucketOfFluid = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
                        ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(oneBucketOfFluid, FluidContainerRegistry.EMPTY_BUCKET);
                        itemStacks[2] = filledBucket;
                        fluid.amount -= FluidContainerRegistry.BUCKET_VOLUME;
                    }
                } else if (itemStack.getItem() instanceof IFluidContainerItem && (workingTick % 20) == 10) {
                    if (fluid.amount >= FluidContainerRegistry.BUCKET_VOLUME) {

                        IFluidContainerItem fluidContainerItem = (IFluidContainerItem) itemStack.getItem();
                        FluidStack fluidStack = fluid.copy();

                        if (fluidStack.amount > FluidContainerRegistry.BUCKET_VOLUME)
                            fluidStack.amount = FluidContainerRegistry.BUCKET_VOLUME;

                        int amount = fluidContainerItem.fill(itemStack, fluidStack, true);
                        if (amount > 0) {
                            fluid.amount -= amount;
                        }
                        if (fluid.amount == 0)
                            tank[1].setFluid(null);
                    }

                }

            }
        }
    }

    private void termoOperation() {

        int x = coordinates[0];
        int y = coordinates[1];
        int z = coordinates[2];

        TileEntity tileEntity = worldObj.getTileEntity(x, y, z);

        if (tileEntity == null || !(tileEntity instanceof UpgCtileentityTermoFluidFurnace)) {
            if (searchingTick == 6) {
                coolDown = 5;
                searchingTick = 0;
                return;
            }

            switch (searchingTick) {
                case 0:
                    coordinates[0] = this.xCoord + 1;
                    coordinates[1] = this.yCoord;
                    coordinates[2] = this.zCoord;
                    break;
                case 1:
                    coordinates[0] = this.xCoord - 1;
                    coordinates[1] = this.yCoord;
                    coordinates[2] = this.zCoord;
                    break;
                case 2:
                    coordinates[0] = this.xCoord;
                    coordinates[1] = this.yCoord + 1;
                    coordinates[2] = this.zCoord;
                    break;
                case 3:
                    coordinates[0] = this.xCoord;
                    coordinates[1] = this.yCoord - 1;
                    coordinates[2] = this.zCoord;
                    break;
                case 4:
                    coordinates[0] = this.xCoord;
                    coordinates[1] = this.yCoord;
                    coordinates[2] = this.zCoord + 1;
                    break;
                case 5:
                    coordinates[0] = this.xCoord;
                    coordinates[1] = this.yCoord;
                    coordinates[2] = this.zCoord - 1;
                    break;
                default:
                    coordinates[0] = this.xCoord + 1;
                    coordinates[1] = this.yCoord;
                    coordinates[2] = this.zCoord;
            }

            searchingTick++;
            searchingTick %= 7;

        } else {

            UpgCtileentityTermoFluidFurnace termoFluidFurnace = (UpgCtileentityTermoFluidFurnace) tileEntity;

            FluidTank inputTank = termoFluidFurnace.tank[0];

            int amount = inputTank.getCapacity() - inputTank.getFluidAmount();
            if (amount > 0) {

                if (amount > FluidContainerRegistry.BUCKET_VOLUME)
                    amount = FluidContainerRegistry.BUCKET_VOLUME;

                FluidStack fluidStack = this.tank[1].drain(amount, true);
                if (fluidStack != null)
                    termoFluidFurnace.fill(ForgeDirection.UNKNOWN, fluidStack, true);

                amount = this.tank[1].getCapacity() - this.tank[1].getFluidAmount();
                if (amount > 0) {

                    if (amount > FluidContainerRegistry.BUCKET_VOLUME)
                        amount = FluidContainerRegistry.BUCKET_VOLUME;

                    fluidStack = termoFluidFurnace.drain(ForgeDirection.UNKNOWN, amount, true);
                    if (fluidStack != null)
                        this.tank[1].fill(fluidStack, true);
                }
            }


        }

    }

    private void heatOperation(byte type) {

        ActiveLava fluid = (ActiveLava) tank[1].getFluid().getFluid();
        int activeValue = fluid.getActiveValue(tank[1].getFluid());
        if (activeValue < ActiveLava.MAX_ACTIVE_VALUE) {
            activeValue = Math.abs(activeValue);
            activeValue += type * 25;
            fluid.setActiveValue(tank[1].getFluid(), activeValue);
            if (Math.random() < 0.22)
                decrStackSize(1, 1);
        }

    }

    private void initOperation(byte type) {

        if (tank[0].getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME) {

            FluidStack fluidStacks = tank[0].drain(FluidContainerRegistry.BUCKET_VOLUME, true);
            FluidStack activeLava;
            int activeValue = 0;
            if (fluidStacks.getFluid() == ModFluids.ActiveLava) {
                activeLava = new FluidStack(ModFluids.ActiveLava, FluidContainerRegistry.BUCKET_VOLUME / 5);
                activeValue = ((ActiveLava) fluidStacks.getFluid()).getActiveValue(fluidStacks);
            } else
                activeLava = new FluidStack(ModFluids.ActiveLava, FluidContainerRegistry.BUCKET_VOLUME / 10);

            activeValue += type * 3;
            ((ActiveLava) activeLava.getFluid()).setActiveValue(activeLava, activeValue);

            tank[1].fill(activeLava, true);

            decrStackSize(1, 1);
        }


    }

    private void normalOperation(byte type) {

        int amount = tank[1].getCapacity() - tank[1].getFluidAmount();

        if (amount > FluidContainerRegistry.BUCKET_VOLUME / 10)
            amount = FluidContainerRegistry.BUCKET_VOLUME / 10;
        if (amount > 0) {
            FluidStack fluidStack;
            FluidStack activeLava;
            int activeValue;

            if (tank[0].getFluid().getFluid() == ModFluids.ActiveLava) {

                fluidStack = tank[0].drain(amount, true);
                activeLava = new FluidStack(ModFluids.ActiveLava, fluidStack.amount);
                activeValue = ((ActiveLava) activeLava.getFluid()).getActiveValue(fluidStack);

            } else {

                fluidStack = tank[0].drain(amount * 2, true);
                activeLava = new FluidStack(ModFluids.ActiveLava, fluidStack.amount / 2);
                activeValue = tank[0].getFluid().getFluid().getTemperature();
            }

            ActiveLava fluid = (ActiveLava) tank[1].getFluid().getFluid();

            //activeValue += fluid.getActiveValue(tank[1].getFluid());

            activeValue += type * 6;

            activeValue = Math.abs(activeValue);

            ((ActiveLava) activeLava.getFluid()).setActiveValue(activeLava, activeValue);

            tank[1].fill(activeLava, true);

            if (Math.random() < 0.42) {
                fluid.increaseActiveValue(tank[1].getFluid());
                if (Math.random() < 0.16)
                    decrStackSize(1, 1);
            }

        } else {

            ActiveLava fluid = (ActiveLava) tank[1].getFluid().getFluid();
            int activeValue = fluid.getActiveValue(tank[1].getFluid());
            activeValue += type * 2;
            fluid.setActiveValue(tank[1].getFluid(), activeValue);

            if (Math.random() < 0.22)
                decrStackSize(1, 1);

            if (Math.random() < 0.27)
                heatOperation(type);

        }


    }

    private byte operationType(ItemStack itemStack) {

        if (itemStack == null)
            return 0;
        if (itemStack.stackSize <= 0)
            return 0;
        Item item = itemStack.getItem();
        if (item == Items.blaze_rod)
            return BLAZE_ROD;
        else if (item == Items.blaze_powder)
            return BLAZE_POWDER;
        return 0;
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

        if ((fluid == null)) {
            return true;
        } else if (fluid.getBlock() == Blocks.lava || fluid == ModFluids.ActiveLava) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid == null || fluid.getBlock() == ModBlocks.blockActiveLava;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        if (from == ForgeDirection.DOWN)
            return super.getTankInfo(from, 1);
        return super.getTankInfo(from, 0);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return isItemValidForSlot(slot, item);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int size) {
        return false;
    }

    @Override
    public String getInventoryName() {
        return Names.Inventory.UPGC_ACTIVE_MAKER;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return slot == 1 && operationType(itemStack) != 0;
    }
}
