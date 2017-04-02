package com.nik7.upgcraft.tileentity;


import com.nik7.upgcraft.block.BlockClayFluidTank;
import com.nik7.upgcraft.fluids.EnumCapacity;
import com.nik7.upgcraft.reference.ConfigOptions;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public class TileEntityClayFluidTank extends TileEntityFluidTank {

    private final static Random RANDOM = new Random();

    private static final int AMOUNT_LOST = 5;
    private final int doWaterStuff = RANDOM.nextInt(6);
    private final int checkCooking = RANDOM.nextInt(6);
    private int tickToCook = 200;
    private int tick = 0;
    private boolean lastWaterResult = false;

    private boolean isCooking = false;
    private int oldTickToCook;

    public TileEntityClayFluidTank() {
        super(EnumCapacity.BASIC_CAPACITY);
        this.oldTickToCook = this.tickToCook;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.tickToCook = tag.getInteger("tickToCook");
        this.isCooking = tag.getBoolean("isCooking");
        this.oldTickToCook = this.tickToCook;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("tickToCook", this.tickToCook);
        tag.setBoolean("isCooking", this.isCooking);
        return tag;
    }

    private void normalBehavior() {
        this.drain(AMOUNT_LOST, true);
    }


    private boolean cookTank(FluidStack fluidStack) {

        if (fluidStack != null && fluidStack.getFluid().getTemperature(fluidStack) >= ConfigOptions.CLAY_COOK) {
            this.tickToCook--;
            this.drain(2 * AMOUNT_LOST, true);
            return true;
        }
        return false;
    }

    private boolean cookTank(BlockPos pos) {
        Block block = WorldHelper.getBlock(this.getWorld(), pos);
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
        return fluid != null && cookTank(new FluidStack(fluid, 1));
    }


    private boolean cookTank(int tick) {

        boolean result;

        FluidStack fluidStack = this.getFluid();
        result = cookTank(fluidStack);
        switch (tick) {

            case 0:
                result |= cookTank(this.getPos().north());
                break;
            case 1:
                result |= cookTank(this.getPos().south());
                break;
            case 2:
                result |= cookTank(this.getPos().east());
                break;
            case 3:
                result |= cookTank(this.getPos().west());
                break;
            case 4:
                result |= cookTank(this.getPos().up());
                break;
            case 5:
                result |= cookTank(this.getPos().down());
                break;
        }


        return result;
    }

    private boolean isBlockWater(BlockPos pos) {
        Block block = WorldHelper.getBlock(this.getWorld(), pos);
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

        return fluid == FluidRegistry.WATER;
    }

    private boolean fillWithWater(int tick) {

        if (tick != this.doWaterStuff)
            return this.lastWaterResult;

        final FluidStack fluidStack = this.getFluid();

        if (fluidStack != null && fluidStack.getFluid() != FluidRegistry.WATER)
            return false;

        int count = 0;
        BlockPos[] pos = {this.getPos().north(), this.getPos().south(), this.getPos().east(), this.getPos().west(), this.getPos().up(), this.getPos().down()};

        for (BlockPos p : pos) {
            if (isBlockWater(p))
                count++;
        }

        if (count > 0) {
            this.fill(new FluidStack(FluidRegistry.WATER, count * 50), true);
            return true;
        }


        return false;
    }

    @Override
    public void update() {
        super.update();

        if (!this.getWorld().isRemote) {
            this.tick %= 6;
            if (!(this.lastWaterResult = fillWithWater(this.tick)))
                if (!cookTank(this.tick))
                    normalBehavior();
                else {
                    if (this.tickToCook <= 0) {
                        Block block = this.getBlockType();
                        if (block instanceof BlockClayFluidTank)
                            ((BlockClayFluidTank) block).cookTank(this.getWorld().getBlockState(this.getPos()), this.getWorld(), this.getPos());
                    }
                }

            if (this.tick == this.checkCooking)
                if (oldTickToCook > this.tickToCook) {
                    if (!this.isCooking) {
                        this.isCooking = true;
                        this.syncTileEntity();
                    }
                    this.oldTickToCook = this.tickToCook;
                } else if (oldTickToCook == this.tickToCook && this.isCooking) {
                    this.isCooking = false;
                    this.syncTileEntity();
                }

            this.tick++;
        }

    }

    public boolean isCooking() {
        return this.isCooking;
    }

    @Override
    protected boolean canMerge(TileEntity te) {
        return te != null && te instanceof TileEntityClayFluidTank;
    }
}
