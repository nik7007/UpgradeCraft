package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.WoodenLiquidTankEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

//toDo: tileEntity
public class BlockWoodenLiquidTank extends BlockUpgC implements IFluidTank, ITileEntityProvider {

    protected FluidStack fluid;
    protected int capacity;
    protected WoodenLiquidTankEntity tile;

    public BlockWoodenLiquidTank() {
        super(Material.wood);
        this.setBlockName(Names.Blocks.WOODENLIQUIDTANK);
        this.setHardness(2.5f);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f); //chest block bounds
        this.setStepSound(soundTypeWood);

        this.setCapacity(Capacity.SMALL_WOODEN_TANK);

    }

    public BlockWoodenLiquidTank readFromNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("Empty")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);

            if (fluid != null) {
                setFluid(fluid);
            }
        }
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (fluid != null) {
            fluid.writeToNBT(nbt);
        } else {
            nbt.setString("Empty", "");
        }
        return nbt;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {

        if (world.getBlock(x, y - 1, z) == this)
            if (world.getBlock(x, y - 2, z) == this)
                return false;
        if (world.getBlock(x, y + 1, z) == this)
            if (world.getBlock(x, y + 2, z) == this)
                return false;
        if (world.getBlock(x, y + 1, z) == this && world.getBlock(x, y - 1, z) == this)
            return false;

        return true;
    }

    @Override
    public FluidStack getFluid() {
        return fluid;
    }

    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
    }

    @Override
    public int getFluidAmount() {
        if (fluid == null)
            return 0;
        return fluid.amount;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override //toDo: tileEntity
    public int fill(FluidStack resource, boolean doFill) {

        int canFill = Math.min(resource.amount, this.capacity);

        if (resource == null)
            return 0;

        if (!fluid.isFluidEqual(resource))
            return 0;

        if (!doFill) {
            if (fluid == null)
                return canFill;

            return Math.min(this.capacity - this.fluid.amount, resource.amount);

        }

        if (fluid == null) {
            fluid = new FluidStack(resource, canFill);
            if (tile != null) {
                FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, this, fluid.amount));
            }
            return fluid.amount;

        }

        int filled = Math.min(this.capacity - this.fluid.amount, resource.amount);

        fluid.amount += filled;

        if (tile != null) {
            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, this, filled));
        }

        return filled;


    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {

        if (fluid == null)
            return null;

        if (maxDrain == 0)
            return null;

        int drained = maxDrain;
        if (fluid.amount < drained) {
            drained = fluid.amount;
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain) {
            fluid.amount -= drained;
            if (fluid.amount <= 0) {
                fluid = null;
            }

            if (tile != null) {
                FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, this, drained));
            }

        }
        return stack;

    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {

        tile = new WoodenLiquidTankEntity();

        return tile;
    }
}


