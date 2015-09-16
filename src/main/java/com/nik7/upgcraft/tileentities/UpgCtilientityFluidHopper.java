package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.block.BlockUpgCBasicFluidHopper;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tank.UpgCActiveTank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class UpgCtilientityFluidHopper extends UpgCtilientityBasicFluidHopper {

    int tick = 0;

    public UpgCtilientityFluidHopper() {
        super(Capacity.FLUID_HOPPER_TANK, UpgCActiveTank.class);
        this.speed = Capacity.Speed.FLUID_HOPPER_SPEED;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.tick = tag.getInteger("tick");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("tick", this.tick);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (worldObj != null && !worldObj.isRemote) {
            int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            if ((tick %= 20) == 0 && BlockUpgCBasicFluidHopper.isNotPowered(meta)) {

                Block block = worldObj.getBlock(xCoord, yCoord + 1, zCoord);
                if (block != null)
                    worldBlockFluidHandler(block, xCoord, yCoord + 1, zCoord);
            }
            tick++;
        }

    }

    private void worldBlockFluidHandler(Block block, int x, int y, int z) {

        FluidStack fluidStack = null;

        if (block instanceof IFluidBlock) {
            IFluidBlock fluidBlock = (IFluidBlock) block;

            if (fluidBlock.canDrain(worldObj, x, y, z)) {
                fluidStack = new FluidStack(fluidBlock.getFluid(), FluidContainerRegistry.BUCKET_VOLUME);

            }

        } else if (block instanceof BlockLiquid) {
            int meta = worldObj.getBlockMetadata(x, y, z);
            if (meta == 0) {
                String name = block.getUnlocalizedName();
                name = name.replaceAll("tile.", "");
                Fluid fluid = FluidRegistry.getFluid(name);
                if (fluid != null) {
                    fluidStack = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
                }
            }
        }

        if (fluidStack != null) {
            int amount = this.fill(ForgeDirection.UP, fluidStack, false);

            if (amount == FluidContainerRegistry.BUCKET_VOLUME) {
                this.fill(ForgeDirection.UP, fluidStack, true);
                worldObj.setBlockToAir(x, y, z);
            }
        }
    }
}

