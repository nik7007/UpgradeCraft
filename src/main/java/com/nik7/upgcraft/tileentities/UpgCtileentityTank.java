package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.tank.UpgCFluidTank;
import com.nik7.upgcraft.util.LogHelper;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.TileFluidHandler;

import java.lang.reflect.InvocationTargetException;

public abstract class UpgCtileentityTank extends TileFluidHandler implements ITickable {

    private UpgCtileentityTank otherTank;
    private boolean isTop;
    private boolean isDouble;
    private boolean canBeDouble;
    private int capacity;
    private final int originalCapacity;
    private Class<? extends UpgCFluidTank> TankClass = null;
    private boolean isFirst = true;

    public UpgCtileentityTank(int capacity, boolean canBeDouble) {
        super();
        this.capacity = this.originalCapacity = capacity;
        this.tank = createTank(capacity);
        this.isTop = false;
        this.isDouble = false;
        this.canBeDouble = canBeDouble;
    }

    public UpgCtileentityTank(int capacity, boolean canBeDouble, Class<? extends UpgCFluidTank> TankClass) {

        this(capacity, canBeDouble);
        this.TankClass = TankClass;

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("isDouble", this.isDouble);
        tag.setBoolean("isTop", this.isTop);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.isDouble = tag.getBoolean("isDouble");
        this.isTop = tag.getBoolean("isTop");

        if (isDouble) {
            capacity = 2 * originalCapacity;
        }
        this.tank.setCapacity(capacity);

    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new S35PacketUpdateTileEntity(pos, -1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {

        readFromNBT(packet.getNbtCompound());
        worldObj.markBlockForUpdate(pos);

    }

    @Override
    public void update() {

        if (isFirst) {
            isFirst = false;
            if (isDouble) {

                UpgCtileentityTank tank;
                if (isTop) {
                    tank = (UpgCtileentityTank) worldObj.getTileEntity(pos.down());
                } else tank = (UpgCtileentityTank) worldObj.getTileEntity(pos.up());

                merge(tank);
            }
        }

    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        FluidStack result;
        if (isDouble && isTop) {

            int realContent = this.tank.getFluidAmount() - originalCapacity;

            if (realContent < resource.amount)
                return null;

            else result = super.drain(from, resource, doDrain);


        } else
            result = super.drain(from, resource, doDrain);

        if (result != null)
            updateModBlock();

        return result;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        FluidStack result;
        if (isDouble && isTop) {

            int realContent = this.tank.getFluidAmount() - originalCapacity;
            if (realContent < maxDrain)
                return null;
            else result = super.drain(from, maxDrain, doDrain);

        } else
            result = super.drain(from, maxDrain, doDrain);

        if (result != null)
            updateModBlock();

        return result;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        int result = super.fill(from, resource, doFill);

        if (result > 0)
            updateModBlock();

        return result;

    }


    private UpgCFluidTank createTank(int capacity) {
        if (TankClass != null) {
            UpgCFluidTank result = null;
            try {
                result = TankClass.asSubclass(UpgCFluidTank.class).getConstructor(int.class, TileEntity.class).newInstance(capacity, this);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return result;

        } else
            return new UpgCFluidTank(capacity, this);
    }

    public void separateTanks() {

        if (otherTank != null)
            otherTank.separateTank();
        this.separateTank();

    }


    private void separateTank() {

        if (isDouble) {
            isDouble = false;
            otherTank = null;

            if (capacity == 2 * originalCapacity) {

                capacity = originalCapacity;
                UpgCFluidTank newTank = createTank(capacity);

                FluidStack oldFluid = this.tank.getFluid();
                int newFluidAmount;
                if (oldFluid != null) {
                    int oldFluidAmount = oldFluid.amount;

                    if (isTop) {
                        newFluidAmount = oldFluidAmount - capacity;
                    } else {
                        newFluidAmount = oldFluidAmount > capacity ? capacity : oldFluidAmount;
                    }

                    if (newFluidAmount > 0) {
                        FluidStack newFluid = new FluidStack(oldFluid, newFluidAmount);
                        newTank.fill(newFluid, true);
                    }

                }


                this.tank = newTank;
            }
            updateModBlock();

        }

        isTop = isTop && isDouble;

    }

    public void findAdjTank() {

        if (!isDouble) {

            UpgCtileentityTank otherTank = null;
            Block myBlock = this.getBlockType();

            if (WorldHelper.getBlock(worldObj, pos.up()) == myBlock) {
                otherTank = (UpgCtileentityTank) worldObj.getTileEntity(pos.up());
            } else if (WorldHelper.getBlock(worldObj, pos.down()) == myBlock)
                otherTank = (UpgCtileentityTank) worldObj.getTileEntity(pos.down());

            if (otherTank != null) {
                otherTank.merge(this);
                this.merge(otherTank);
            }
        }

    }

    private boolean fluidAreCompatible(FluidStack otherFluid) {
        return otherFluid == null || this.tank.getFluid() == null || this.tank.getFluid().isFluidEqual(otherFluid);
    }

    private void merge(UpgCtileentityTank otherTank) {

        if (canBeDouble && canMerge(otherTank) && fluidAreCompatible(otherTank.tank.getFluid())) {
            this.otherTank = otherTank;
            isTop = this.pos.getY() > otherTank.pos.getY();

            if (capacity == originalCapacity) {

                capacity = 2 * originalCapacity;

                if (!isTop) {

                    UpgCFluidTank doubleTank = createTank(capacity);
                    FluidStack myFluid = this.tank.getFluid();
                    FluidStack otherFluid = otherTank.tank.getFluid();

                    doubleTank.fill(myFluid, true);
                    doubleTank.fill(otherFluid, true);

                    this.tank = doubleTank;
                    otherTank.tank = doubleTank;

                }
                isDouble = true;

            } else if (capacity == 2 * originalCapacity) {

                if (!isTop) {
                    otherTank.tank = this.tank;
                }

            } else {
                LogHelper.error("Impossible to merge! capacity = " + capacity);
            }

            isTop = isTop && isDouble;
            updateModBlock();

        }

    }

    protected abstract boolean canMerge(TileEntity tileEntity);

    public boolean isTop() {
        return isTop;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public int getFluidLight() {
        if (tank.getFluid() == null)
            return 0;
        return tank.getFluid().getFluid().getLuminosity(tank.getFluid());
    }

    public int getAdjMetadata() {
        if (otherTank != null)
            return otherTank.getBlockMetadata();
        return -1;
    }

    private void updateModBlock() {
        //worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
        worldObj.markBlockForUpdate(pos);
        this.worldObj.notifyBlockOfStateChange(pos, getBlockType());
    }

    public FluidStack getFluid() {
        if (tank.getFluid() != null)
            return new FluidStack(tank.getFluid(), tank.getFluidAmount());
        else return null;
    }

    public float getFillPercentage() {

        FluidStack fluidStack = tank.getFluid();
        if (fluidStack == null)
            return 0;

        return (float) capacity / (float) tank.getFluidAmount();
    }
}
