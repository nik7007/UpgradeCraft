package com.nik7.upgcraft.block;

import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.util.LogHelper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockUpgCTank extends BlockUpgC implements ITileEntityProvider {

    protected int capacity;


    public BlockUpgCTank(Material material) {
        super(material);

    }


    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new UpgCtileentityTank(new UpgCTank(capacity));
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

        if (filledBucket != null && entity.drain(null, oneBucketOfFluid, true).amount == FluidContainerRegistry.BUCKET_VOLUME) {
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
}
