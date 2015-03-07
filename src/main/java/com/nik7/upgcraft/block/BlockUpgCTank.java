package com.nik7.upgcraft.block;

import com.nik7.upgcraft.tank.UpgCTank;
import com.nik7.upgcraft.tileentities.UpgCtileentityTank;
import com.nik7.upgcraft.util.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Random;

public abstract class BlockUpgCTank extends BlockUpgC implements ITileEntityProvider {

    protected int capacity;
    protected int flammability = 0;
    protected int fireSpreadSpeed = 0;
    protected boolean haveSubBlocks = false;
    private int oldFlammability = 0;

    public BlockUpgCTank(Material material) {
        super(material);
        this.setTickRandomly(false);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {

        UpgCtileentityTank entity = (UpgCtileentityTank) world.getTileEntity(x, y, z);
        ItemStack equippedItemStack = player.getCurrentEquippedItem();

        if (equippedItemStack != null) {
            if (FluidContainerRegistry.isContainer(equippedItemStack))    // react to registered fluid containers
            {
                if (!world.isRemote) {
                    handleContainerClick(world, x, y, z, player, entity, equippedItemStack);
                }

                return true;
            }
        }


        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
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

        FluidStack fluid = entity.getFluid();

        if (fluid == null)
            return;

        if (fluid.amount < FluidContainerRegistry.BUCKET_VOLUME)
            return;

        FluidStack oneBucketOfFluid = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
        ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(oneBucketOfFluid, FluidContainerRegistry.EMPTY_BUCKET);

        if (filledBucket != null && entity.drain(null, oneBucketOfFluid, false) != null) {

            if (entity.drain(null, oneBucketOfFluid, false).amount == FluidContainerRegistry.BUCKET_VOLUME) {

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

    }

    private void drainBucketIntoTank(EntityPlayer player, UpgCtileentityTank entity, ItemStack equippedItemStack) {

        if (entity == null) {
            LogHelper.fatal("tile is null");
            return;
        }

        UpgCTank tank = entity.getTank();

        if ((tank.getFluidAmount() == 0 || tank.getFluid().isFluidEqual(equippedItemStack)) && tank.getCapacity() - tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME) {
            FluidStack fluidFromBucket = FluidContainerRegistry.getFluidForFilledItem(equippedItemStack);

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

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (this.flammability > 0) {
            UpgCtileentityTank entity = (UpgCtileentityTank) world.getTileEntity(x, y, z);

            if (entity.getTank().isToHot()) {
                float f = (float) x + 0.5F;
                float f1 = (float) y + random.nextFloat();
                float f2 = (float) z + 0.5F;
                float f3 = 0.52F;
                float f4 = random.nextFloat() * 0.6F - 0.3F;

                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);

                world.spawnParticle("smoke", x + 0.5D - random.nextDouble(), (double) y + 1, z + 0.5D - random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        boolean canBurn = flammability > 0;
        UpgCtileentityTank entity = (UpgCtileentityTank) world.getTileEntity(x, y, z);
        boolean toHot = entity.getTank().isToHot();

        if (toHot && canBurn) {

            if (oldFlammability == 0)
                oldFlammability = flammability;
            flammability = 75;

            if (!setInFire(world, x, y, z))
                setInFireNeighbors(world, x, y, z);

        }

        if ((!toHot || !canBurn) && oldFlammability > 0)
            flammability = oldFlammability;

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
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);
        UpgCtileentityTank tileEntity = (UpgCtileentityTank) world.getTileEntity(x, y, z);

        if (tileEntity != null) {
            tileEntity.updateContainingBlockInfo();
        }
    }


    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
        UpgCtileentityTank entity = (UpgCtileentityTank) world.getTileEntity(x, y, z);
        float maxCapacity = entity.getCapacity();

        float fluidAmount;
        if (!entity.isEmpty()) {
            fluidAmount = entity.getFluid().amount;
        } else fluidAmount = 0;

        int result = (int) Math.ceil((fluidAmount / maxCapacity) * 15.0f);

        //In this way it returns the maximum redstone strength (15) only if it's completely full
        if (result == 15 && fluidAmount < maxCapacity) {
            result--;
        }

        return result;
    }

    @Override
    public int damageDropped(int metadata) {

        return metadata;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {

        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {

        if (haveSubBlocks) {
            subItems.add(new ItemStack(this, 1, 0));
            subItems.add(new ItemStack(this, 1, 1));
        } else {
            super.getSubBlocks(item, tab, subItems);
        }

    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 1) {
            return ((UpgCtileentityTank) world.getTileEntity(x, y, z)).getFluidLightLevel();
        } else
            return 0;
    }

    public int getCapacity() {
        return capacity;
    }

}
