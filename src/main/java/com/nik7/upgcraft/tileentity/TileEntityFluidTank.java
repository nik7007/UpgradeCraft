package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.block.FluidTank;
import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public abstract class TileEntityFluidTank extends TileEntity implements IFluidHandler, ITickable {

    protected UpgCFluidTank fluidTank;
    private int oldLight;
    private TileEntityFluidTank adjFluidTank;
    private boolean isTop;
    private final EnumCapacity capacity;
    private boolean isFirst = true;
    private final boolean canBeDouble;
    private final Class<? extends UpgCFluidTank> TankClass;


    public TileEntityFluidTank(EnumCapacity capacity) {
        this(capacity, null, true);
    }

    public TileEntityFluidTank(EnumCapacity capacity, Class<? extends UpgCFluidTank> TankClass, boolean canBeDouble) {
        this.capacity = capacity;
        this.TankClass = TankClass;
        this.canBeDouble = canBeDouble;
        this.fluidTank = createTank(EnumCapacity.BASIC_CAPACITY, this);
    }

    private UpgCFluidTank createTank(EnumCapacity capacity, TileEntityFluidTank... tileEntities) {
        if (TankClass != null) {
            UpgCFluidTank result = null;
            try {
                result = TankClass.asSubclass(UpgCFluidTank.class).getConstructor(EnumCapacity.class, TileEntityFluidTank[].class).newInstance(capacity, tileEntities);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return result;

        } else
            return new UpgCFluidTank(capacity, tileEntities);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.fluidTank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        this.fluidTank.writeToNBT(tag);
        return tag;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {

        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        nbtTag.setBoolean("isDoubleTank", this.canBeDouble && this.isDouble());
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        NBTTagCompound nbt = packet.getNbtCompound();
        if (nbt.hasKey("isDoubleTank")) {
            if (!nbt.getBoolean("isDoubleTank"))
                this.separeteTank();
        }

        this.readFromNBT(nbt);
        this.updateLight();
    }

    private void updateLight() {
        int light = this.getFluidLight();
        if (worldObj != null) {
            boolean glassed = this.worldObj.getBlockState(this.pos).getBlock() instanceof FluidTank ? this.worldObj.getBlockState(this.pos).getValue(FluidTank.GLASSED) : true;

            if (this.oldLight != light && glassed) {
                worldObj.checkLightFor(EnumSkyBlock.BLOCK, getPos());
                this.oldLight = light;
            }
        }
    }

   /* @Override
    public void onLoad() {
        this.findAdjFluidTank();
    }*/

    @Override
    public void update() {
        if (isFirst && this.canBeDouble) {
            isFirst = false;
            this.findAdjFluidTank();
        }
    }

    protected abstract boolean canMerge(TileEntity te);

    private boolean fluidAreCompatible(TileEntity te) {

        FluidStack otherFluid = ((TileEntityFluidTank) te).getFluid();

        return otherFluid == null || this.fluidTank.getFluid() == null || this.fluidTank.getFluid().isFluidEqual(otherFluid);
    }

    public void findAdjFluidTank() {

        if (this.adjFluidTank != null) {

            if (worldObj.getTileEntity(this.adjFluidTank.pos) != this.adjFluidTank) {
                this.separeteTank();
            }

        }

        if (this.adjFluidTank == null) {
            TileEntity te = worldObj.getTileEntity(pos.down());
            if (this.canMerge(te) && fluidAreCompatible(te)) {
                this.adjFluidTank = (TileEntityFluidTank) te;
                this.isTop = true;
                this.merge(this.adjFluidTank);
            } else {
                te = worldObj.getTileEntity(pos.up());
                if (this.canMerge(te) && fluidAreCompatible(te))
                    this.adjFluidTank = (TileEntityFluidTank) te;
            }

            if (this.adjFluidTank != null)
                this.adjFluidTank.findAdjFluidTank();
            worldObj.updateComparatorOutputLevel(getPos(), worldObj.getBlockState(getPos()).getBlock());
        }
    }

    private void merge(TileEntityFluidTank fluidTank) {
        if (fluidTank != null) {
            EnumCapacity doubleCapacity = EnumCapacity.getDoubleCapacity(this.capacity);
            int newCapacity = EnumCapacity.getCapacity(doubleCapacity);
            UpgCFluidTank otherTank = fluidTank.fluidTank;

            int thisCapacity = this.fluidTank.getCapacity();
            int otherCApacity = otherTank.getCapacity();

            if (thisCapacity == newCapacity)
                fluidTank.fluidTank = this.fluidTank;
            else if (otherCApacity == newCapacity)
                this.fluidTank = fluidTank.fluidTank;
            else {
                FluidStack fluidStack1 = this.getFluid();
                FluidStack fluidStack2 = fluidTank.getFluid();
                UpgCFluidTank newTank = createTank(doubleCapacity, this, fluidTank);
                FluidStack result = null;
                if (fluidStack1 != null) {
                    result = fluidStack1;
                    result.amount += fluidTank.fluidTank.getFluidAmount();
                } else if (fluidStack2 != null) {
                    result = fluidStack2;
                }

                if (result != null)
                    newTank.fill(result, true);
                this.fluidTank = newTank;
                fluidTank.fluidTank = newTank;

            }

            if (!worldObj.isRemote)
                this.syncTileEntity();
        }

    }

    public void separeteTank() {
        if (this.adjFluidTank != null) {
            TileEntityFluidTank otherFluidTank = this.adjFluidTank;
            FluidStack fluidStack = this.getSingleTankFluid();
            this.adjFluidTank = null;
            this.isTop = false;

            this.fluidTank = createTank(capacity, this);
            this.fluidTank.fill(fluidStack, true);

            if (!worldObj.isRemote)
                this.syncTileEntity();

            otherFluidTank.separeteTank();
        }

    }

    protected void updateBlock() {
        markDirty();

        if (worldObj != null) {
            IBlockState state = worldObj.getBlockState(getPos());
            worldObj.notifyBlockUpdate(getPos(), state, state, 3);
            worldObj.updateComparatorOutputLevel(getPos(), state.getBlock());
            this.updateLight();
        }

    }

    public void syncTileEntity() {
        updateBlock();
    }

    @SideOnly(Side.CLIENT)
    public boolean renderInsideFluid() {
        boolean render = this.worldObj.getBlockState(this.pos).getValue(FluidTank.GLASSED);
        if (this.isDouble()) {
            render |= this.adjFluidTank.getBlockMetadata() % 2 == 1;
        }
        return render;
    }

    public boolean isDouble() {
        return this.adjFluidTank != null;
    }

    public boolean isTop() {
        return this.isTop && this.isDouble();
    }

    @SideOnly(Side.CLIENT)
    public float getFillPercentage() {
        return (float) this.fluidTank.getFluidAmount() / (float) this.fluidTank.getCapacity();
    }

    public int getFluidLight() {

        FluidStack fluidStack = this.fluidTank.getFluid();

        if (fluidStack == null)
            return 0;
        return fluidStack.getFluid().getLuminosity(this.fluidTank.getFluid());
    }

    public FluidStack getFluid() {
        return this.fluidTank.getFluid() == null ? null : this.fluidTank.getFluid().copy();
    }

    public FluidStack getSingleTankFluid() {

        if (this.isDouble()) {

            if (this.getFluid() != null) {
                int fluidAmount = this.fluidTank.getFluidAmount();
                if (this.isTop()) {
                    fluidAmount -= EnumCapacity.getCapacity(capacity);
                    if (fluidAmount > 0)
                        return new FluidStack(this.getFluid(), fluidAmount);
                    else return null;
                } else {
                    if (fluidAmount > EnumCapacity.getCapacity(capacity))
                        return new FluidStack(this.getFluid(), EnumCapacity.getCapacity(capacity));
                    else return this.getFluid();
                }
            } else return null;

        } else return this.getFluid();
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return this.fluidTank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return this.fluidTank.fill(resource, doFill);
    }


    public boolean canDrain(int amount) {

        if (this.isTop()) {
            FluidStack fluidStack = this.getSingleTankFluid();
            return fluidStack != null && fluidStack.amount >= amount;
        }
        return true;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return this.fluidTank.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return this.fluidTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) this.fluidTank;
        return super.getCapability(capability, facing);
    }

}
