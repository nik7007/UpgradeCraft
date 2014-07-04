package com.nik7.upgcraft.tileentities;

import com.nik7.upgcraft.block.InitBlocks;
import com.nik7.upgcraft.block.UpgCTank;
import com.nik7.upgcraft.reference.Capacity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class UpgCtileentityTankSmall extends UpgCtileentityTank {

    public boolean adjacentTank = false;
    public UpgCtileentityTankSmall adjacentTankYPos;
    public UpgCtileentityTankSmall adjacentTankYNeg;

    public UpgCtileentityTankSmall() {

        super();
        setTank(new UpgCTank(Capacity.SMALL_WOODEN_TANK));
        this.capacity = Capacity.SMALL_WOODEN_TANK;


    }

    public void checkForAdjacent() {

        this.adjacentTankYNeg = null;
        this.adjacentTankYPos = null;

        if (isTankEntity(yCoord - 1)) {

            this.adjacentTankYNeg = (UpgCtileentityTankSmall) this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
            this.setCapacity(Capacity.BIG_WOODEN_TANK);
            this.adjacentTank = true;

            return;
        }

        if (isTankEntity(yCoord + 1)) {

            this.adjacentTankYPos = (UpgCtileentityTankSmall) this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
            this.setCapacity(Capacity.BIG_WOODEN_TANK);
            this.adjacentTank = true;

            return;
        }

        this.adjacentTank = false;


    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {

        if (adjacentTank && adjacentTankYNeg != null) {
            FluidStack fluiD = adjacentTankYNeg.getTank().getFluid();

            if (fluiD != null) {
                if (!fluiD.isFluidEqual(resource)) {
                    return 0;
                }
            }

            int resourceFilled = 0;
            int result = adjacentTankYNeg.fill(from, resource, doFill);
            int result2 = 0;

            if (resource != null)
                resourceFilled = resource.amount;

            if (resourceFilled > result) {
                FluidStack fluid = new FluidStack(resource, (resourceFilled - result));
                result2 = super.fill(from, fluid, doFill);
            }
            return result + result2;
        }

        return super.fill(from, resource, doFill);
    }

    private boolean isTankEntity(int y) {
        return this.worldObj.getBlock(xCoord, y, zCoord) == InitBlocks.blockWoodenLiquidTank;
    }


    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {

        if (this.getTank().getFluid() == null || resource == null) {

            return null;
        }


        int amountResult = 0;

        if (adjacentTank && adjacentTankYPos != null && adjacentTankYPos.getTank().getFluid() != null) {
            int addToResult = 0;
            FluidStack fluid = adjacentTankYPos.drain(from, resource, doDrain);

            if (fluid != null)
                addToResult = fluid.amount;

            amountResult += addToResult;
        }

        FluidStack result = super.drain(from, (resource.amount - amountResult), doDrain);
        result.amount += amountResult;

        return result;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {

        if (this.getTank().getFluid() == null)
            return null;
        FluidStack resource = new FluidStack(this.getTank().getFluid(), maxDrain);

        return this.drain(from, resource, doDrain);

    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        this.checkForAdjacent();
        updateModBlock();
    }

}
