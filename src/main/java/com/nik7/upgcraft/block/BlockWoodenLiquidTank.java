package com.nik7.upgcraft.block;

import com.nik7.upgcraft.reference.Capacity;
import com.nik7.upgcraft.reference.Names;
import com.nik7.upgcraft.tileentities.WoodenLiquidTankEntity;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

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
        return !(world.getBlock(x, y + 1, z) == this && world.getBlock(x, y - 1, z) == this);

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

    @Override
    public int fill(FluidStack resource, boolean doFill) {

        int canFill = Math.min(resource.amount, this.capacity);

        if (resource == null)
            return 0;

        if (!doFill) {
            if (fluid == null)
                return canFill;

            if (!fluid.isFluidEqual(resource))
                return 0;

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

        tile = new WoodenLiquidTankEntity(this);

        return tile;
    }


    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

        ItemStack equippedItemStack = player.getCurrentEquippedItem();

        if (equippedItemStack != null) {
            if (FluidContainerRegistry.isContainer(equippedItemStack))    // react to registered fluid containers
            {
                handleContainerClick(world, x, y, z, player, equippedItemStack);

                return true;
            }
        }

        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    private void handleContainerClick(World world, int x, int y, int z, EntityPlayer player, ItemStack equippedItemStack) {

        if (FluidContainerRegistry.isBucket(equippedItemStack)) {
            if (FluidContainerRegistry.isEmptyContainer(equippedItemStack)) {
                fillBucketFromTank(world, x, y, z, player, equippedItemStack);
            } else {
                drainBucketIntoTank(player, equippedItemStack);

            }
        }

    }

    private void fillBucketFromTank(World world, int x, int y, int z, EntityPlayer player, ItemStack equippedItemStack) {
        if (fluid == null)
            return;

        if (fluid.amount < FluidContainerRegistry.BUCKET_VOLUME)
            return;

        FluidStack oneBucketOfFluid = new FluidStack(this.getFluid(), FluidContainerRegistry.BUCKET_VOLUME);
        ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(oneBucketOfFluid, FluidContainerRegistry.EMPTY_BUCKET);

        if (filledBucket != null && tile.drain(null, oneBucketOfFluid, true).amount == FluidContainerRegistry.BUCKET_VOLUME) {
            // add filled bucket to player inventory or drop it to the ground if the inventory is full
            if (!player.inventory.addItemStackToInventory(filledBucket)) {
                world.spawnEntityInWorld(new EntityItem(world, x + 0.5D, y + 1.5D, z + 0.5D, filledBucket));
            } else if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
            }
        }

        if (--equippedItemStack.stackSize <= 0) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
        }
    }


    private void drainBucketIntoTank(EntityPlayer player, ItemStack equippedItemStack) {

        if ((this.getFluidAmount() == 0 || this.getFluid().isFluidEqual(equippedItemStack)) && this.getCapacity() - this.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME) {
            FluidStack fluidFromBucket = FluidContainerRegistry.getFluidForFilledItem(equippedItemStack);

            if (tile == null) {
                LogHelper.fatal("tile is null");
                return;
            }

            if (fluidFromBucket == null) {
                LogHelper.fatal("fluidFromBucket is null");
                return;
            }


            if (tile.fill(null, fluidFromBucket, true) == FluidContainerRegistry.BUCKET_VOLUME) {
                // don't consume the filled bucket in creative mode
                if (!player.capabilities.isCreativeMode) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
                }
            }
        }

    }

}




