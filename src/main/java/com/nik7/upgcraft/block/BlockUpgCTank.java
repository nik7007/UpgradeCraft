package com.nik7.upgcraft.block;

import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public abstract class BlockUpgCTank extends BlockUpgC implements ITileEntityProvider {

    protected int capacity;
    protected int flammability = 0;
    protected int fireSpreadSpeed = 0;
    private int oldFlammability = 0;

    public BlockUpgCTank(Material material) {
        super(material);
        this.setTickRandomly(false);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

        UpgCtileentityTank entity = (UpgCtileentityTank) world.getTileEntity(x, y, z);
        ItemStack equippedItemStack = player.getCurrentEquippedItem();

        if (equippedItemStack != null) {
            if (FluidContainerRegistry.isContainer(equippedItemStack))    // react to registered fluid containers
            {
                handleContainerClick(world, x, y, z, player, entity, equippedItemStack);

                return true;
            }
        }

        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    private void handleContainerClick(World world, int x, int y, int z, EntityPlayer player, UpgCtileentityTank entity, ItemStack equippedItemStack) {

        if (FluidContainerRegistry.isBucket(equippedItemStack)) {
            if (FluidContainerRegistry.isEmptyContainer(equippedItemStack)) {
                fillBucketFromTank(world, x, y, z, player, entity, equippedItemStack);
            } else {
                drainBucketIntoTank(player, entity, equippedItemStack);

            }
        }

    }

    private void fillBucketFromTank(World world, int x, int y, int z, EntityPlayer player, UpgCtileentityTank entity, ItemStack equippedItemStack) {

        FluidStack fluid = entity.getTank().getFluid();

        if (fluid == null)
            return;

        if (fluid.amount < FluidContainerRegistry.BUCKET_VOLUME)
            return;

        FluidStack oneBucketOfFluid = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
        ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(oneBucketOfFluid, FluidContainerRegistry.EMPTY_BUCKET);

        if (filledBucket != null && entity.drain(null, oneBucketOfFluid, false).amount == FluidContainerRegistry.BUCKET_VOLUME) {

            entity.drain(null, oneBucketOfFluid, true);

            if (!player.capabilities.isCreativeMode) {
                if (equippedItemStack.stackSize == 1) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, filledBucket);
                    return;
                }

                if (!player.inventory.addItemStackToInventory(filledBucket)) {
                    equippedItemStack.stackSize--;
                    world.spawnEntityInWorld(new EntityItem(world, x + 0.5D, y + 1.5D, z + 0.5D, filledBucket));
                } else if (player instanceof EntityPlayerMP) {
                    equippedItemStack.stackSize--;
                    ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
                }
            }

        }

    }

    private void drainBucketIntoTank(EntityPlayer player, UpgCtileentityTank entity, ItemStack equippedItemStack) {

        UpgCTank tank = entity.getTank();

        if ((tank.getFluidAmount() == 0 || tank.getFluid().isFluidEqual(equippedItemStack)) && tank.getCapacity() - tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME) {
            FluidStack fluidFromBucket = FluidContainerRegistry.getFluidForFilledItem(equippedItemStack);

            if (entity == null) {
                LogHelper.fatal("tile is null");
                return;
            }

            if (fluidFromBucket == null) {
                LogHelper.fatal("fluidFromBucket is null");
                return;
            }


            if (entity.fill(null, fluidFromBucket, true) == FluidContainerRegistry.BUCKET_VOLUME) {
                // don't consume the filled bucket in creative mode
                if (!player.capabilities.isCreativeMode) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
                }
            }
        }

    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        boolean canBurn = flammability > 0;
        UpgCtileentityTank entity = (UpgCtileentityTank) world.getTileEntity(x, y, z);
        UpgCTank tank = entity.getTank();
        boolean toHot = tank.isToHot();

        if (toHot && canBurn) {
            //LogHelper.info("Liquid is to HOT!!!");
            if (oldFlammability == 0)
                oldFlammability = flammability;
            flammability = 75;

            if (!setInFire(world, x, y, z))
                setInFireNeighbors(world, x, y, z);

        }

        if ((!toHot || !canBurn) && oldFlammability > 0)
            flammability = oldFlammability;

        //LogHelper.info((new Integer(flammability)).toString());

    }

    private boolean setInFire(World world, int x, int y, int z) {

        if (world.isAirBlock(x, y + 1, z)) {
            world.setBlock(x, y + 1, z, Blocks.fire);
            return true;
        } else if (world.isAirBlock(x, y - 1, z)) {
            world.setBlock(x, y - 1, z, Blocks.fire);
            return true;
        } else if (world.isAirBlock(x, y, z + 1)) {
            world.setBlock(x, y, z + 1, Blocks.fire);
            return true;
        } else if (world.isAirBlock(x, y, z - 1)) {
            world.setBlock(x, y, z - 1, Blocks.fire);
            return true;
        } else if (world.isAirBlock(x + 1, y, z)) {
            world.setBlock(x + 1, y, z, Blocks.fire);
            return true;
        } else if (world.isAirBlock(x - 1, y, z)) {
            world.setBlock(x - 1, y, z, Blocks.fire);
            return true;
        }

        return false;
    }

    private boolean setInFireNeighbors(World world, int x, int y, int z) {

        if (world.getBlock(x, y + 1, z).isFlammable(world, x, y + 1, z, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x, y + 1, z);

        } else if (world.getBlock(x, y, z + 1).isFlammable(world, x, y, z + 1, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x, y, z + 1);

        } else if (world.getBlock(x, y, z - 1).isFlammable(world, x, y, z - 1, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x, y, z - 1);

        } else if (world.getBlock(x + 1, y, z).isFlammable(world, x + 1, y, z, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x + 1, y, z);

        } else if (world.getBlock(x - 1, y, z).isFlammable(world, x - 1, y, z, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x - 1, y, z);

        } else if (world.getBlock(x, y - 1, z).isFlammable(world, x, y - 1, z, ForgeDirection.UNKNOWN)) {
            return setInFire(world, x, y - 1, z);

        }

        return false;
    }


    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
        UpgCtileentityTank entity = (UpgCtileentityTank) world.getTileEntity(x, y, z);
        float maxCapacity = entity.getTank().getCapacity();
        float fluidAmount;

        if (entity.getTank().getFluid() != null) {
            fluidAmount = entity.getTank().getFluid().amount;
        } else {
            fluidAmount = 0;
        }

        return (int) Math.ceil((fluidAmount / maxCapacity) * 15.0f);
    }
}
