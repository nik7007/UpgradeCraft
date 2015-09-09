package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;

public class UpgCtileentityTankClay extends UpgCtileentityTank {

    private static final int TOTAL_PROGRESS = 200;
    private static final int AMOUNT_LOST = 5;
    public boolean isCooking = false;
    private int progress = TOTAL_PROGRESS;

    public UpgCtileentityTankClay() {
        super(Capacity.SMALL_TANK, true);

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        progress = tag.getInteger("progress");
        isCooking = tag.getBoolean("isCooking");

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("progress", progress);
        tag.setBoolean("isCooking", isCooking);
    }

    public void updateEntity() {

        super.updateEntity();

        if (!worldObj.isRemote) {
            if (getBlockMetadata() < 2) {

                if (progress <= 0) {
                    int newMeta;
                    if (getBlockMetadata() == 0) {
                        newMeta = 2;


                    } else newMeta = 3;

                    this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 3);
                }

                cook();

                if (getFluid() != null) {
                    this.drain(ForgeDirection.UNKNOWN, AMOUNT_LOST, true);
                    if (this.getTank().isToHot()) {

                        progress--;
                        this.drain(ForgeDirection.UNKNOWN, 2 * AMOUNT_LOST, true);

                    } else if (progress < TOTAL_PROGRESS)
                        progress++;
                    else if (progress > TOTAL_PROGRESS)
                        progress = TOTAL_PROGRESS;

                }

            }
        }
    }

    private void cook() {
        boolean result;
        result = isBlockFluidHot(xCoord + 1, yCoord, zCoord);
        result |= isBlockFluidHot(xCoord - 1, yCoord, zCoord);
        result |= isBlockFluidHot(xCoord, yCoord + 1, zCoord);
        result |= isBlockFluidHot(xCoord, yCoord - 1, zCoord);
        result |= isBlockFluidHot(xCoord, yCoord, zCoord + 1);
        result |= isBlockFluidHot(xCoord, yCoord, zCoord - 1);

        isCooking = result;
        if (isCooking) {
            progress--;
        }
        if (getFluid() != null) {
            isCooking = this.getTank().isToHot();
        }

    }

    private boolean isBlockFluidHot(int x, int y, int z) {

        Block block = worldObj.getBlock(x, y, z);
        String name = block.getUnlocalizedName();

        if (block instanceof IFluidBlock) {
            Fluid fluid = ((IFluidBlock) block).getFluid();

            return fluid.getTemperature() > (300 + 273);

        } else if (name.equals("tile.lava")) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canMerge(TileEntity tileEntity) {
        int myMeta = this.getBlockMetadata();
        int otherMeta = tileEntity.getBlockMetadata();

        boolean result = (myMeta < 2 && otherMeta < 2) || (myMeta >= 2 && otherMeta >= 2);

        return tileEntity instanceof UpgCtileentityTankClay && result;
    }
}
