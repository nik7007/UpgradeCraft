package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.block.BlockFluidTank;
import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTankWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityFluidTank extends TileEntityFluidHandler implements ITickable {

    protected final EnumCapacity capacity;
    private final boolean canBeDouble;
    private TileEntityFluidTank adjFluidTank;
    private boolean isTop;
    private boolean isFirst = true;


    public TileEntityFluidTank(EnumCapacity capacity) {
        this(capacity, null, true);
    }

    public TileEntityFluidTank(EnumCapacity capacity, Class<? extends UpgCFluidTank> TankClass, boolean canBeDouble) {
        super(capacity, TankClass);
        this.canBeDouble = canBeDouble;
        this.capacity = capacity;

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
                this.separateTank();
        }

        this.readFromNBT(nbt);
        this.updateLight();
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

            if (getWorld().getTileEntity(this.adjFluidTank.pos) != this.adjFluidTank) {
                this.separateTank();
            }

        }

        if (this.adjFluidTank == null) {
            TileEntity te = getWorld().getTileEntity(pos.down());
            if (this.canMerge(te) && fluidAreCompatible(te)) {
                this.adjFluidTank = (TileEntityFluidTank) te;
                this.isTop = true;
                this.merge(this.adjFluidTank);
            } else {
                te = getWorld().getTileEntity(pos.up());
                if (this.canMerge(te) && fluidAreCompatible(te))
                    this.adjFluidTank = (TileEntityFluidTank) te;
            }

            if (this.adjFluidTank != null)
                this.adjFluidTank.findAdjFluidTank();
            getWorld().updateComparatorOutputLevel(getPos(), getWorld().getBlockState(getPos()).getBlock());
        }
    }

    private void merge(TileEntityFluidTank fluidTank) {
        if (fluidTank != null) {
            EnumCapacity doubleCapacity = EnumCapacity.getDoubleCapacity(this.capacity);
            int newCapacity = EnumCapacity.getCapacity(doubleCapacity);
            UpgCFluidTankWrapper otherTank = fluidTank.fluidTank;

            int thisCapacity = this.fluidTank.getCapacity();
            int otherCApacity = otherTank.getCapacity();

            if (thisCapacity == newCapacity)
                fluidTank.fluidTank = this.fluidTank;
            else if (otherCApacity == newCapacity)
                this.fluidTank = fluidTank.fluidTank;
            else {
                FluidStack fluidStack1 = this.getFluid();
                FluidStack fluidStack2 = fluidTank.getFluid();
                UpgCFluidTankWrapper newTank = new UpgCFluidTankWrapper(doubleCapacity, super.tankClass, this, fluidTank);
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

            if (!getWorld().isRemote)
                this.syncTileEntity();
        }

    }

    public void separateTank() {
        if (this.adjFluidTank != null) {
            TileEntityFluidTank otherFluidTank = this.adjFluidTank;
            FluidStack fluidStack = this.getSingleTankFluid();
            this.adjFluidTank = null;
            this.isTop = false;

            this.fluidTank = new UpgCFluidTankWrapper(capacity, super.tankClass, this);
            this.fluidTank.fill(fluidStack, true);

            if (!getWorld().isRemote)
                this.syncTileEntity();

            otherFluidTank.separateTank();
        }

    }

    protected void updateBlock() {
        markDirty();

        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        this.updateLight();

    }

    @Override
    public void syncTileEntity() {
        updateBlock();
    }

    @Override
    protected void updateLight() {
        IBlockState state = this.getWorld().getBlockState(this.pos);
        boolean glassed = state.getBlock() instanceof BlockFluidTank ? state.getValue(BlockFluidTank.GLASSED) : true;
        if (glassed)
            super.updateLight();

    }

    @SideOnly(Side.CLIENT)
    public boolean renderInsideFluid() {
        boolean render = this.getWorld().getBlockState(this.pos).getValue(BlockFluidTank.GLASSED);
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

}
