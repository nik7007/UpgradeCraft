package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tank.UpgCTank;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class UpgCtileentityTankClay extends UpgCtileentityTank {

    private static int TOTAL_PROGRESS = 200;
    private int progress = TOTAL_PROGRESS;
    private int amountLost = 5;

    public UpgCtileentityTankClay() {
        super();
        setTank(new UpgCTank(Capacity.SMALL_TANK));
        setCanBeDouble(true);

    }

    public void updateEntity() {
        super.updateEntity();
        if (getBlockMetadata() < 2) {
            if (getFluid() != null) {
                this.drain(ForgeDirection.UNKNOWN, amountLost, true);

                if (this.getTank().isToHot()) {
                    if (progress <= 0) {
                        int newMeta;
                        if (getBlockMetadata() == 0) {
                            newMeta = 2;


                        } else newMeta = 3;

                        this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 3);
                    } else {
                        progress--;
                        this.drain(ForgeDirection.UNKNOWN, 2 * amountLost, true);
                    }

                } else if (progress < TOTAL_PROGRESS)
                    progress++;
                else if (progress > TOTAL_PROGRESS)
                    progress = TOTAL_PROGRESS;

            }

        }
    }

    @Override
    protected boolean tileEntityInstanceOf(TileEntity tileEntity) {
        int myMeta = this.getBlockMetadata();
        int otherMeta = tileEntity.getBlockMetadata();

        boolean result = (myMeta < 2 && otherMeta < 2) || (myMeta >= 2 && otherMeta >= 2);

        return tileEntity instanceof UpgCtileentityTankClay && result;
    }
}
