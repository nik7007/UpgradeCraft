package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.block.BlockUpgCBasicFluidHopper;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class UpgCtilientityFluidHopper extends UpgCtileentityTank {

    protected int speed = Capacity.Speed.BASIC_FLUID_HOPPER_SPEED;

    public UpgCtilientityFluidHopper() {
        super(Capacity.FLUID_HOPPER_TANK);
    }

    @Override
    public Packet getDescriptionPacket() {

        FluidStack fluidStack = getFluid();
        NBTTagCompound tag = new NBTTagCompound();
        if (fluidStack != null) {
            fluidStack.writeToNBT(tag);
        }
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(packet.func_148857_g());
        if (fluidStack != null && fluidStack.amount <= 0)
            fluidStack = null;
        this.getTank().setFluid(fluidStack);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void updateEntity() {
        int meta = this.getBlockMetadata();

        if (worldObj != null && !this.worldObj.isRemote && BlockUpgCBasicFluidHopper.isNotPowered(meta)) {
            int lastFluidAmount = tank.getFluidAmount();
            fillFromUp();
            int dir = BlockUpgCBasicFluidHopper.getDirectionFromMetadata(meta);
            ForgeDirection forgeDirection = ForgeDirection.getOrientation(dir);

            if (lastFluidAmount > 0)
                autoDrain(forgeDirection);


        }

    }

    private void fillFromUp() {

        if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IFluidHandler) {
            IFluidHandler fluidHandler = (IFluidHandler) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
            FluidStack fluidStack;
            if ((fluidStack = fluidHandler.drain(ForgeDirection.DOWN, 1, false)) != null) {

                fluidStack = new FluidStack(fluidStack, speed);

                int cacity = getTank().fill(fluidStack, false);
                if (cacity > 0) {
                    fluidStack = fluidHandler.drain(ForgeDirection.DOWN, cacity, true);
                    super.fill(ForgeDirection.UP, fluidStack, true);

                }
            }


        }

    }

    private void autoDrain(ForgeDirection direction) {

        if (!this.isEmpty()) {
            if (worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ) instanceof IFluidHandler) {
                IFluidHandler fluidHandler = (IFluidHandler) worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

                FluidStack fluidStack = new FluidStack(this.getFluid(), speed);
                int amount;
                if ((amount = fluidHandler.fill(direction.getOpposite(), fluidStack, false)) > 0) {
                    fluidStack = this.drain(direction, amount, true);
                    if (amount != fluidHandler.fill(direction.getOpposite(), fluidStack, true)) {
                        LogHelper.error("Something wrong appends: Fluid is transferring wrong!!");
                    }
                }

            }
        }

    }


    @Override
    protected boolean canMerge(TileEntity tileEntity) {
        return false;
    }
}
