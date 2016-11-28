package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.fluids.tank.UpgCFluidTank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
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

public class TileEntityFluidTank extends TileEntity implements IFluidHandler {

    protected final UpgCFluidTank fluidTank;
    private int oldLight;


    public TileEntityFluidTank() {
        this.fluidTank = new UpgCFluidTank(EnumCapacity.BASIC_CAPACITY, this);
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
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
        this.updateLight();
    }

    private void updateLight() {
        int light = this.getFluidLight();
        if (this.oldLight != light) {
            worldObj.checkLightFor(EnumSkyBlock.BLOCK, getPos());
            this.oldLight = light;
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
        return getBlockMetadata() % 2 == 1;
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
        return this.fluidTank.getFluid();
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return this.fluidTank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return this.fluidTank.fill(resource, doFill);
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
