package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.BlockUpgCTank;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import com.nik7.upgcraft.util.LogHelper;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.TileFluidHandler;

import java.lang.reflect.InvocationTargetException;

public abstract class UpgCtileentityTank extends TileFluidHandler {

    private UpgCtileentityTank otherTank;
    private boolean isTop;
    private boolean isDouble;
    private boolean canBeDouble;
    private int capacity;
    private final int originalCapacity;
    private Class<? extends UpgCFluidTank> TankClass = null;

    public UpgCtileentityTank(boolean canBeDouble) {
        super();
        this.capacity = this.originalCapacity = ((BlockUpgCTank) (WorldHelper.getBlock(worldObj, pos))).getCapacity();
        this.tank = createTank(capacity);
        this.isTop = false;
        this.isDouble = false;
        this.canBeDouble = canBeDouble;

    }

    public UpgCtileentityTank(boolean canBeDouble, Class<? extends UpgCFluidTank> TankClass) {

        this(canBeDouble);
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
            this.tank.setCapacity(capacity);

            if (isTop) {
                otherTank = (UpgCtileentityTank) worldObj.getTileEntity(pos.up());
            } else otherTank = (UpgCtileentityTank) worldObj.getTileEntity(pos.down());

            merge(otherTank);

        } else this.tank.setCapacity(capacity);

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

    private void merge(UpgCtileentityTank otherTank) {

        if (canBeDouble && canMerge(otherTank)) {
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

        }

    }

    protected abstract boolean canMerge(TileEntity tileEntity);
}
