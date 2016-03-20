package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.BlockUpgCFluidTank;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import com.nik7.upgcraft.util.LogHelper;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.TileFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.InvocationTargetException;

public abstract class UpgCtileentityFluidTank extends TileFluidHandler implements ITickable {

    private int meta = 0;

    private UpgCtileentityFluidTank otherTank;
    private boolean isTop;
    private boolean isDouble;
    private boolean canBeDouble;
    private int capacity;
    private int originalCapacity;
    private Class<? extends UpgCFluidTank> TankClass = null;
    private boolean isFirst = true;

    public UpgCtileentityFluidTank(int capacity, boolean canBeDouble) {
        super();
        this.capacity = this.originalCapacity = capacity;
        this.tank = createTank(capacity);
        this.isTop = false;
        this.isDouble = false;
        this.canBeDouble = canBeDouble;
    }

    @SideOnly(Side.CLIENT)
    public void setBlockType(Block blockType) {
        if (this.blockType == null)
            this.blockType = blockType;
    }

    @SideOnly(Side.CLIENT)
    public int getBlockMetadataClient() {
        return this.meta;
    }

    @SideOnly(Side.CLIENT)
    public void setMetadata(int metadata) {
        this.meta = metadata;
    }

    public UpgCtileentityFluidTank(int capacity, boolean canBeDouble, Class<? extends UpgCFluidTank> TankClass) {

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

        if (canBeDouble && isDouble) {
            capacity = 2 * originalCapacity;
        } else capacity = originalCapacity;
        this.tank.setCapacity(capacity);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new SPacketUpdateTileEntity(pos, -1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void update() {

        if (isFirst) {
            isFirst = false;
            if (isDouble && canBeDouble) {

                UpgCtileentityFluidTank tank;
                if (isTop) {
                    tank = (UpgCtileentityFluidTank) worldObj.getTileEntity(pos.down());
                } else tank = (UpgCtileentityFluidTank) worldObj.getTileEntity(pos.up());

                merge(tank);
            }
        }
        reloadOriginalCapacity();

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

    public FluidStack getFluidFormSingleTank() {

        if (tank.getFluid() == null)
            return null;
        else if (!canBeDouble || !isDouble) {
            return tank.getFluid();
        } else {

            FluidStack oldFluid = this.tank.getFluid();
            int newFluidAmount;
            int oldFluidAmount = oldFluid.amount;

            if (isTop) {
                newFluidAmount = oldFluidAmount - capacity;
            } else {
                newFluidAmount = oldFluidAmount > capacity ? capacity : oldFluidAmount;
            }

            if (newFluidAmount > 0) {
                return new FluidStack(oldFluid, newFluidAmount);
            }

        }
        return null;

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

            UpgCtileentityFluidTank otherTank = null;
            Block myBlock = this.getBlockType();

            if (WorldHelper.getBlock(worldObj, pos.up()) == myBlock) {
                otherTank = (UpgCtileentityFluidTank) worldObj.getTileEntity(pos.up());
            } else if (WorldHelper.getBlock(worldObj, pos.down()) == myBlock)
                otherTank = (UpgCtileentityFluidTank) worldObj.getTileEntity(pos.down());

            if (otherTank != null) {
                otherTank.merge(this);
                this.merge(otherTank);
            }
        }

    }

    private boolean fluidAreCompatible(FluidStack otherFluid) {
        return otherFluid == null || this.tank.getFluid() == null || this.tank.getFluid().isFluidEqual(otherFluid);
    }

    private void merge(UpgCtileentityFluidTank otherTank) {

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

    //To avoid infinite recursive calling of the "updateModBlock" method
    private boolean isNotAlreadyUpdating = true;

    private void updateModBlock() {

        if (isNotAlreadyUpdating && worldObj != null) {
            isNotAlreadyUpdating = false;
            //worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
            //worldObj.markBlockForUpdate(pos);
            //this.worldObj.notifyBlockOfStateChange(pos, getBlockType());
            IBlockState blockState = worldObj.getBlockState(pos);
            worldObj.notifyBlockUpdate(pos,blockState,blockState,3);
            this.worldObj.updateComparatorOutputLevel(this.pos, this.getBlockType());

            if (otherTank != null) {
                otherTank.updateModBlock();
            }
        }


        isNotAlreadyUpdating = true;
    }


    public void reloadOriginalCapacity() {
        this.originalCapacity = ((BlockUpgCFluidTank) WorldHelper.getBlock(worldObj, pos)).getCapacity();
        if (this.capacity != this.originalCapacity && this.capacity != this.originalCapacity * 2) {
            if (isDouble)
                this.capacity = 2 * this.originalCapacity;
            else
                this.capacity = this.originalCapacity;
            updateModBlock();
        }
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

        float result = (float) tank.getFluidAmount() / (float) capacity;
        if (result > 1)
            result = 1;

        return result;
    }

    public int getFluidAmount() {
        return tank.getFluidAmount();
    }

    public int getCapacity() {
        return capacity;
    }
}
