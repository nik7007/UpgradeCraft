package com.nik7.upgcraft.block;

import com.nik7.upgcraft.config.ConfigurableObject;
import com.nik7.upgcraft.config.SystemConfig;
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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Random;

public abstract class BlockUpgCTank extends BlockUpgC implements ITileEntityProvider, ConfigurableObject {

    protected int capacity;
    protected boolean haveSubBlocks = false;

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

    protected void handleContainerClick(World world, int x, int y, int z, EntityPlayer player, UpgCtileentityTank entity, ItemStack equippedItemStack) {

        if (FluidContainerRegistry.isBucket(equippedItemStack)) {
            if (FluidContainerRegistry.isEmptyContainer(equippedItemStack)) {
                fillBucketFromTank(world, x, y, z, player, entity, equippedItemStack);
            } else {
                drainBucketIntoTank(player, entity, equippedItemStack);

            }
        }

    }

    protected void fillBucketFromTank(World world, int x, int y, int z, EntityPlayer player, UpgCtileentityTank entity, ItemStack equippedItemStack) {

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

    protected void drainBucketIntoTank(EntityPlayer player, UpgCtileentityTank entity, ItemStack equippedItemStack) {

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

    protected void spawnParticle(World world, int x, int y, int z, Random random, String... particles) {

        float f = (float) x + 0.5F;
        float f1 = (float) y + random.nextFloat();
        float f2 = (float) z + 0.5F;
        float f3 = 0.52F;
        float f4 = random.nextFloat() * 0.6F - 0.3F;

        for (String p : particles) {


            world.spawnParticle(p, (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            world.spawnParticle(p, (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            world.spawnParticle(p, (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            world.spawnParticle(p, (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);

            world.spawnParticle(p, x + 0.5D - random.nextDouble(), (double) y + 1, z + 0.5D - random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
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
    @SuppressWarnings({"unchecked", "rawtypes"})
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

    @Override
    public void appliedConfig(SystemConfig.ConfigValue... values) {

        if (values.length >= 1) {
            for (SystemConfig.ConfigValue c : values) {

                if (c.configName.equals("basicTankCapacity")) {

                    this.capacity = new Integer(c.value);

                }

            }

        }

    }

}
