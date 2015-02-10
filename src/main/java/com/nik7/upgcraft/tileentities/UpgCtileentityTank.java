package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.BlockUpgCTank;
import com.nik7.upgcraft.inventory.UpgCTank;
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

    public UpgCtileentityTank adjacentTankYPos = null;
    public UpgCtileentityTank adjacentTankYNeg = null;
    private boolean canBeDouble = false;
    private boolean isDouble = false;
    private boolean isTop = false;

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
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.func_148857_g());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    protected abstract boolean tileEntityInstanceOf(TileEntity tileEntity);

    private void findAdjacentTank() {

        boolean shouldBeDouble = true;
        this.adjacentTankYNeg = null;
        this.adjacentTankYPos = null;

        if (isCanBeDouble()) {

            World world = this.getWorldObj();

            TileEntity tileEntity = world.getTileEntity(xCoord, yCoord + 1, zCoord);

            if (tileEntity != null && tileEntityInstanceOf(tileEntity)) {

                adjacentTankYPos = (UpgCtileentityTank) tileEntity;
                if (adjacentTankYPos.getFluid() != null) {
                    if (this.getTank() != null)
                        if (this.getTank().getFluid() != null) {
                            shouldBeDouble = adjacentTankYPos.getFluid().equals(this.getFluid());
                        }
                }
                if (shouldBeDouble) {
                    mergeTank(adjacentTankYPos);
                }

                adjacentTankYPos.updateModBlock();

            } else {
                tileEntity = world.getTileEntity(xCoord, yCoord - 1, zCoord);
                if (tileEntity != null && tileEntityInstanceOf(tileEntity)) {

                    adjacentTankYNeg = (UpgCtileentityTank) tileEntity;

                    if (adjacentTankYNeg.getFluid() != null) {
                        if (this.getTank() != null)
                            if (this.getTank().getFluid() != null) {
                                shouldBeDouble = adjacentTankYNeg.getFluid().equals(this.getFluid());
                            }
                    }

                    if (shouldBeDouble) {
                        mergeTank(adjacentTankYNeg);
                    }
                    isTop = true;
                    adjacentTankYNeg.updateModBlock();
                } else {

                    separateTank();
                    isTop = false;
                    this.updateModBlock();
                }
            }


        }
        isDouble = (!(adjacentTankYNeg == null && adjacentTankYPos == null)) && shouldBeDouble;

    }

    private void mergeTank(UpgCtileentityTank other) {

        int capacity = ((BlockUpgCTank) getWorldObj().getBlock(xCoord, yCoord, zCoord)).getCapacity();

        UpgCTank newUpgCTank;

        if (this.getTank() != null && other.getTank() != null) {

            if (this.getTank() == other.getTank()) {

                if (this.getTank().getCapacity() != (capacity * 2)) {

                    LogHelper.debug("Merge Tank: This case should be not possible!!");

                }
            } else if (this.getTank().equals(other.getTank()) && this.getTank().getCapacity() == capacity * 2) {

                other.setTank(this.getTank());

            } else if (this.getTank().getCapacity() == other.getTank().getCapacity() && this.getTank().getCapacity() == capacity) {

                newUpgCTank = new UpgCTank(capacity * 2);

                newUpgCTank.fill(this.getFluid(), true);
                newUpgCTank.fill(other.getFluid(), true);

                this.setTank(newUpgCTank);
                other.setTank(newUpgCTank);

            } else {
                LogHelper.fatal("This two tank can't be merged:at least one of two has already a double tank container!!");
            }
        } else {

            if (this.getTank() == null && other.getTank() != null) {

                if (other.getTank().getCapacity() == capacity * 2) {
                    this.setTank(other.getTank());
                } else {

                    newUpgCTank = new UpgCTank(capacity * 2);

                    newUpgCTank.fill(other.getFluid(), true);

                    this.setTank(newUpgCTank);
                    other.setTank(newUpgCTank);

                }


            } else if (this.getTank() != null) {

                if (this.getTank().getCapacity() == capacity * 2) {
                    other.setTank(this.getTank());
                } else {
                    newUpgCTank = new UpgCTank(capacity * 2);

                    newUpgCTank.fill(this.getFluid(), true);

                    this.setTank(newUpgCTank);
                    other.setTank(newUpgCTank);
                }

            } else {

                newUpgCTank = new UpgCTank(capacity * 2);

                this.setTank(newUpgCTank);
                other.setTank(newUpgCTank);


            }
        }

    }

    private void separateTank() {
        int capacity = ((BlockUpgCTank) getWorldObj().getBlock(xCoord, yCoord, zCoord)).getCapacity();

        if (this.getTank().getCapacity() == capacity * 2) {
            UpgCTank upgCTank = new UpgCTank(capacity);
            if (!isTop) {
                upgCTank.fill(this.getFluid(), true);
            } else {

                int amount = 0;

                if (this.getFluid() != null) {
                    amount = this.getFluid().amount;
                }

                int fluidAmount = amount - capacity;
                if (fluidAmount > 0 && this.getFluid() != null) {
                    FluidStack fluidStack = new FluidStack(this.getFluid(), fluidAmount);
                    upgCTank.fill(fluidStack, true);
                }

            }
            this.setTank(upgCTank);

        } else if (this.getTank().getCapacity() != capacity) {
            LogHelper.error("Unable to separate tank: this has a strange capacity!!");
        }
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
