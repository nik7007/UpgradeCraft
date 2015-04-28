package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.BlockUpgCTank;
import com.nik7.upgcraft.tank.UpgCTank;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.TileFluidHandler;

public abstract class UpgCtileentityTank extends TileFluidHandler {

    private final int TANK_CAPACITY;
    public UpgCtileentityTank adjacentTankYPos = null;
    public UpgCtileentityTank adjacentTankYNeg = null;
    private boolean canBeDouble = false;
    private boolean isDouble = false;
    private boolean isTop = false;

    protected UpgCtileentityTank(int tankCapacity, boolean canBeDouble) {

        this(tankCapacity);
        setCanBeDouble(canBeDouble);

    }

    protected UpgCtileentityTank(int tankCapacity) {
        super();
        this.TANK_CAPACITY = tankCapacity;
        this.setTank(new UpgCTank(tankCapacity));
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        isDouble = tag.getBoolean("isDouble");
        isTop = tag.getBoolean("isTop");

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("isDouble", isDouble);
        tag.setBoolean("isTop", isTop);
    }

    public boolean isCanBeDouble() {
        return canBeDouble;
    }

    public void setCanBeDouble(boolean canBeDouble) {
        this.canBeDouble = canBeDouble;
    }

    public UpgCTank getTank() {
        return (UpgCTank) tank;
    }

    void setTank(FluidTank tank) {
        this.tank = tank;
    }

    private void updateModBlock() {
        //worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        Block block = this.worldObj.getBlock(xCoord, yCoord, zCoord);
        this.worldObj.notifyBlockChange(xCoord, yCoord, zCoord, block);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {

        if (!worldObj.isRemote) {
            int result;

            result = super.fill(from, resource, doFill);

            if (result > 0) {
                updateModBlock();
                markDirty();
            }

            return result;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (!worldObj.isRemote) {
            FluidStack origin = getTank().getFluid();
            FluidStack result;

            if (canBeDouble && isDouble && isTop) {

                int capacity = ((BlockUpgCTank) worldObj.getBlock(xCoord, yCoord, zCoord)).getCapacity();

                int canDrain = getFluid().amount - capacity - resource.amount;

                if (canDrain < 0) {
                    return null;
                }


            }
            result = super.drain(from, resource, doDrain);

            if (origin != result) {
                updateModBlock();
                markDirty();
            }

            return result;
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {

        if (!worldObj.isRemote) {

            FluidStack origin = getTank().getFluid();

            if (canBeDouble && isDouble && isTop) {

                int capacity = ((BlockUpgCTank) worldObj.getBlock(xCoord, yCoord, zCoord)).getCapacity();

                int canDrain = getFluid().amount - capacity - maxDrain;

                if (canDrain < 0) {
                    return null;
                }

            }

            FluidStack result = super.drain(from, maxDrain, doDrain);

            if (origin != result) {
                updateModBlock();
                markDirty();
            }

            return result;
        }
        return null;
    }

    public void updateEntity() {

        if (canBeDouble) {
            findAdjacentTank();
            markDirty();

        }

    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    protected abstract boolean canMerge(TileEntity tileEntity);


    private void merge() {

        if (!(getTank().getCapacity() == 2 * this.TANK_CAPACITY)) {

            UpgCtileentityTank tank;
            FluidStack fluidStack = this.getFluid();

            if (isTop)
                tank = adjacentTankYNeg;
            else tank = adjacentTankYPos;


            UpgCTank cTank = tank.getTank();

            if (cTank.getCapacity() == this.TANK_CAPACITY) {

                cTank = new UpgCTank(2 * TANK_CAPACITY);
            } else if (cTank.getCapacity() != 2 * this.TANK_CAPACITY)
                LogHelper.error("Unknow tank capacity: " + cTank.getCapacity());

            this.setTank(cTank);

            this.tank.fill(fluidStack, true);

        }

    }

    private void separate() {

        if (!(getTank().getCapacity() == this.TANK_CAPACITY)) {

            int amount = 0;
            FluidStack fluidStack = null;
            UpgCTank tank = new UpgCTank(this.TANK_CAPACITY);

            if (this.tank.getFluid() != null)
                fluidStack = new FluidStack(this.tank.getFluid(), amount);

            if (fluidStack != null) {


                if (isTop) {
                    amount = this.tank.getFluidAmount() - this.TANK_CAPACITY;
                    if (amount < 0) amount = 0;

                } else {

                    if (this.tank.getFluidAmount() > this.TANK_CAPACITY)
                        amount = this.TANK_CAPACITY;
                    else {

                        amount = this.tank.getFluidAmount();

                    }
                }

                if (amount == 0)
                    fluidStack = null;
                else
                    fluidStack.amount = amount;

            }


            tank.fill(fluidStack, true);

            this.setTank(tank);


        }

    }

    private void findAdjacentTank() {

        boolean shouldBeDouble = true;


        this.adjacentTankYNeg = null;
        this.adjacentTankYPos = null;


        if (isCanBeDouble()) {

            World world = this.getWorldObj();

            TileEntity tileEntity = world.getTileEntity(xCoord, yCoord + 1, zCoord);

            if (tileEntity != null && canMerge(tileEntity)) {

                adjacentTankYPos = (UpgCtileentityTank) tileEntity;
                if (adjacentTankYPos.getFluid() != null) {
                    if (this.getTank() != null)
                        if (this.getTank().getFluid() != null) {
                            shouldBeDouble = adjacentTankYPos.getFluid().equals(this.getFluid());
                        }
                }
                if (shouldBeDouble) {
                    isTop = false;
                    merge();
                }

                adjacentTankYPos.updateModBlock();

            } else {
                tileEntity = world.getTileEntity(xCoord, yCoord - 1, zCoord);
                if (tileEntity != null && canMerge(tileEntity)) {

                    adjacentTankYNeg = (UpgCtileentityTank) tileEntity;

                    if (adjacentTankYNeg.getFluid() != null) {
                        if (this.getTank() != null)
                            if (this.getTank().getFluid() != null) {
                                shouldBeDouble = adjacentTankYNeg.getFluid().equals(this.getFluid());
                            }
                    }
                    isTop = true;
                    if (shouldBeDouble) {
                        merge();
                    }

                    adjacentTankYNeg.updateModBlock();
                } else {

                    separate();
                    isTop = false;
                    this.updateModBlock();
                }
            }


        }
        isDouble = (!(adjacentTankYNeg == null && adjacentTankYPos == null)) && shouldBeDouble;

    }


    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();

        if (canBeDouble)
            isDouble = false;

    }

    public boolean hasAdjacentTank() {
        return isDouble;
    }

    public float getCapacity() {
        return this.tank.getCapacity();
    }

    public FluidStack getFluid() {
        if (getTank() != null)
            return this.getTank().getFluid();
        else return null;
    }

    public boolean isEmpty() {
        return getFluid() == null;
    }

    public boolean isFull() {
        return !(getTank() == null || getFluid() == null) && getTank().getCapacity() == getFluid().amount;
    }

    public float getFillPercentage() {

        int amount;

        if (!this.isEmpty()) {
            amount = this.getFluid().amount;
        } else amount = 0;

        return (amount / this.getCapacity());


    }

    public int getFluidLightLevel() {
        FluidStack stack = tank.getFluid();
        if (stack != null) {
            Fluid fluid = stack.getFluid();
            if (fluid != null) return fluid.getLuminosity();
        }

        return 0;
    }

}
