package com.nik7.upgcraft.tileentities;


import com.nik7.upgcraft.block.BlockUpgCClayFluidTank;
import com.nik7.upgcraft.network.NetworkHandler;
import com.nik7.upgcraft.network.UpdateRequestMessage;
import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.tank.UpgCFluidTank;
import com.nik7.upgcraft.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class UpgCtileentityClayFluidTank extends UpgCtileentityFluidTank {

    private static final int TOTAL_PROGRESS = 200;
    private static final int AMOUNT_LOST = 5;
    private BlockPos hotBlockPos = null;
    public boolean isCooking = false;
    private int progress = TOTAL_PROGRESS;

    private int tickCont = 0;

    public UpgCtileentityClayFluidTank() {
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

    @Override
    public void update() {
        super.update();

        if (!worldObj.isRemote)
            if (getBlockMetadata() < 2) {
                this.drain(EnumFacing.DOWN, AMOUNT_LOST, true);
                tickCont %= 20;

                if (isFluidHot()) {
                    progress--;
                    if (tickCont == 10) {
                        this.drain(EnumFacing.DOWN, 20 * AMOUNT_LOST, true);
                        if (progress < TOTAL_PROGRESS)
                            progress++;
                    }
                }

                foundHotFluid(tickCont);

                if (isCooking) {
                    progress--;
                }

                tickCont++;

                if (progress <= 0) {
                    progress = 0;

                    Block block = blockType;
                    if (block instanceof BlockUpgCClayFluidTank) {
                        NetworkHandler.getInstance().sendToAllAround(new UpdateRequestMessage(pos), new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 16));
                        ((BlockUpgCClayFluidTank) block).hardenedClayTank(worldObj, pos, worldObj.getBlockState(pos));
                    }

                    markDirty();
                    if (isDouble()) {
                        separateTanks();
                        findAdjTank();
                        markDirty();
                    } else {
                        findAdjTank();
                        markDirty();
                    }
                }
            }
    }

    private boolean isFluidHot() {
        return ((UpgCFluidTank) tank).isFluidHot();
    }

    public boolean isCooking() {
        return isFluidHot() || isCooking;
    }

    private void foundHotFluid(int tick) {
        if (hotBlockPos == null && tick >= 0) {
            int choice = tick % 6;
            BlockPos toCheck;

            switch (choice) {

                case 0:
                    toCheck = pos.up();
                    break;
                case 1:
                    toCheck = pos.down();
                    break;
                case 2:
                    toCheck = pos.east();
                    break;
                case 3:
                    toCheck = pos.west();
                    break;
                case 4:
                    toCheck = pos.north();
                    break;
                case 5:
                    toCheck = pos.south();
                    break;
                default:
                    toCheck = pos.north();
                    break;
            }

            if (isBlockFluidHot(toCheck)) {
                hotBlockPos = toCheck;
                isCooking = true;
                updateModBlock();
            } else isCooking = false;
        } else if (hotBlockPos != null) {

            if (isBlockFluidHot(hotBlockPos))
                isCooking = true;
            else {
                hotBlockPos = null;
                isCooking = false;
            }
        }

    }

    private boolean isBlockFluidHot(BlockPos pos) {

        Block block = WorldHelper.getBlock(worldObj, pos);
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

        return result && tileEntity instanceof UpgCtileentityClayFluidTank;
    }
}
